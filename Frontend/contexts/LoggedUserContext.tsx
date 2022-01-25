import React, { createContext, useEffect, useState } from 'react';
import { UserDto } from '../api';
import { userController } from '../controllers';

interface ILoggedUserProvider {
    loggedUser: UserDto | null;
    setLoggedUser: (newLoggedUser: UserDto | null) => void;
}

export const LoggedUserContext = createContext<ILoggedUserProvider>({
    loggedUser: null,
    setLoggedUser: () => {
        return;
    },
});

const LoggedUserProvider = (props: { children: any }) => {
    const [loggedUser, setLoggedUser] = useState<UserDto | null>(null);
    useEffect(() => {
        loadUser();
    }, []);
    async function loadUser() {
        console.log('loading user');
        const res = await userController.getCurrentUser();
        setLoggedUser(res.data);
    }

    return (
        <LoggedUserContext.Provider
            value={{
                loggedUser,
                setLoggedUser,
            }}
        >
            {props.children}
        </LoggedUserContext.Provider>
    );
};

export default LoggedUserProvider;
