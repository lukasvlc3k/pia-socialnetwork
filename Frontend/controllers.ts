import { AuthControllerApi, Configuration } from './api';

const defaultConfig: Configuration = new Configuration({
    basePath: 'http://localhost:8080',
    accessToken: getAccessToken,
});

async function getAccessToken(): Promise<string> {
    return '';
}

export const authController = new AuthControllerApi(defaultConfig);
