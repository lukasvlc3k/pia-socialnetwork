import React, { createContext, useEffect, useState } from 'react';
import { UserDto } from '../api';
import { userController } from '../controllers';
import moment, { Moment } from 'moment';
import { getTokenData, loginUser, logoutUser } from '../utils/login';
import { useRouter } from 'next/router';

interface ILoggedUserProvider {
    loggedUser: UserDto | null;
    setLoggedUser: (newLoggedUser: UserDto | null) => void;

    login: (email: string, password: string) => Promise<boolean>;
    logout: () => Promise<boolean>;

    tokenExpiration?: Moment | null;
    setTokenExpiration?: (newTokenExpiration: Moment | null) => void;

    roles?: string[] | null;
    setRoles?: (newRoles: string[] | null) => void;

    refreshData?: () => Promise<void>;
}

export const LoggedUserContext = createContext<ILoggedUserProvider>({
    loggedUser: null,
    setLoggedUser: () => {
        return;
    },
    login: async (email: string, password: string): Promise<boolean> => {
        return false;
    },
    logout: async (): Promise<boolean> => {
        return false;
    },
});

const LoggedUserProvider = (props: { children: any }) => {
    const [loggedUser, setLoggedUser] = useState<UserDto | null>(null);
    const [roles, setRoles] = useState<string[] | null>(null);
    const [tokenExpiration, setTokenExpiration] = useState<Moment | null>(null);
    const router = useRouter();

    useEffect(() => {
        loadData();
    }, []);

    async function loadUser() {
        const res = await userController.getCurrentUser();

        if (res?.data?.roles) {
            setRoles(res.data.roles);
        }

        setLoggedUser(res.data);
    }

    async function loadData() {
        const tokenData = await getTokenData();
        if (!tokenData) {
            setRoles(null);
            setTokenExpiration(null);
        } else {
            setRoles(tokenData.roles ?? []);
            setTokenExpiration(moment(tokenData.expiration));
        }

        await loadUser();
    }

    async function login(email: string, password: string): Promise<boolean> {
        const res = await loginUser(email, password);
        if (!res) {
            return false;
        }

        await loadData();

        return true;
    }

    async function logout(): Promise<boolean> {
        const res = await logoutUser();

        await router.push('/');
        return res;
    }

    return (
        <LoggedUserContext.Provider
            value={{
                loggedUser,
                setLoggedUser,
                tokenExpiration,
                setTokenExpiration,
                roles,
                setRoles,
                refreshData: loadData,
                login,
                logout,
            }}
        >
            {props.children}
        </LoggedUserContext.Provider>
    );
};

export default LoggedUserProvider;
