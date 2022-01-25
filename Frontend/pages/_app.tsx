import React from 'react';

import '../styles/bootstrap.scss';
import '../styles/globals.css';

import type { AppProps } from 'next/app';
import LoggedUserProvider from '../contexts/LoggedUserContext';
import SocketProvider from '../contexts/SocketContext';
import { SecuredComponent } from '../types/auth';
import Auth from '../components/common/Auth';

function MyApp({
    Component,
    pageProps,
}: {
    Component: SecuredComponent;
    pageProps: AppProps['pageProps'];
}) {
    return (
        <LoggedUserProvider>
            {Component.auth ? (
                <SocketProvider>
                    <Auth minRole={Component.auth?.minRole}>
                        <Component {...pageProps} />
                    </Auth>
                </SocketProvider>
            ) : (
                <Component {...pageProps} />
            )}
        </LoggedUserProvider>
    );
}

export default MyApp;
