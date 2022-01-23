import axiosLib from 'axios';
import Router from 'next/router';
import { StatusCodes } from 'http-status-codes';
import { getToken } from './login';
import { apiBasePath } from '../consts/general';

export const axios = axiosLib.create({
    baseURL: apiBasePath,
    timeout: 60000,
});

// Add a request interceptor
axios.interceptors.request.use(async function (config) {
    const token = await getToken();
    if (token && config?.headers) {
        config.headers.Authorization = 'Bearer ' + token;
    }
    return config;
});

// Add a response interceptor
axios.interceptors.response.use(
    (response) => response,
    async function (error) {
        const status = error?.response?.status;
        if (status === StatusCodes.UNAUTHORIZED) {
            console.log('Unauthorized, redirecting...', Router.asPath);
            if (Router.basePath !== '') {
                await Router.replace({
                    pathname: '/',
                    query: {
                        backUrl: encodeURIComponent(Router.asPath),
                    },
                });
            }
        } else if (status === StatusCodes.FORBIDDEN) {
            console.log('Forbidden:', error?.response?.config?.url);
        }

        throw status;
    }
);
