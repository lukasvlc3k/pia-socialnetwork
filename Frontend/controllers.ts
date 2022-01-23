import {
    AuthControllerApi,
    Configuration,
    DataControllerApi,
    PublicControllerApi,
} from './api';
import { getToken } from './utils/login';
import { axios } from './utils/axios';
import { apiBasePath } from './consts/general';

const defaultConfig: Configuration = new Configuration({
    basePath: apiBasePath,
    accessToken: getToken,
});

export const authController = new AuthControllerApi(defaultConfig);
export const publicController = new PublicControllerApi(defaultConfig);
