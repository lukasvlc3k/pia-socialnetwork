import {
    AuthControllerApi,
    Configuration,
    PostsControllerApi,
    PublicControllerApi,
    UsersControllerApi,
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
export const postController = new PostsControllerApi(defaultConfig, apiBasePath, axios);
export const userController = new UsersControllerApi(defaultConfig, apiBasePath, axios);
