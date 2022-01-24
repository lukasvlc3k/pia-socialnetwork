import React from 'react';

//import 'bootstrap/dist/css/bootstrap.css';

import '../styles/bootstrap.scss';
import '../styles/globals.css';

import type { AppProps } from 'next/app';
import LoggedUserProvider from '../contexts/LoggedUserContext';

function MyApp({ Component, pageProps }: AppProps) {
    return (
        <LoggedUserProvider>
            <Component {...pageProps} />
        </LoggedUserProvider>
    );
}

export default MyApp;
