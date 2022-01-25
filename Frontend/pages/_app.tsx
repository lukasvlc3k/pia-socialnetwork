import React from 'react';

//import 'bootstrap/dist/css/bootstrap.css';

import '../styles/bootstrap.scss';
import '../styles/globals.css';

import type { AppProps } from 'next/app';
import LoggedUserProvider from '../contexts/LoggedUserContext';
import SocketProvider from '../contexts/SocketContext';

function MyApp({ Component, pageProps }: AppProps) {
    return (
        <LoggedUserProvider>
            <SocketProvider>
                <Component {...pageProps} />
            </SocketProvider>
        </LoggedUserProvider>
    );
}

export default MyApp;
