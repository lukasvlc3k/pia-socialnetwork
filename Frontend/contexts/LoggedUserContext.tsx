import React, { createContext, useState } from 'react';
import { UserDto } from '../api';

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
