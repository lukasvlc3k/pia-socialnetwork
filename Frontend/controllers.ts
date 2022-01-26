import {
    AuthControllerApi,
    ChatControllerApi,
    Configuration,
    FriendsControllerApi,
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

export const authController = new AuthControllerApi(defaultConfig, apiBasePath, axios);
export const publicController = new PublicControllerApi(
    defaultConfig,
    apiBasePath,
    axios
);
export const postController = new PostsControllerApi(defaultConfig, apiBasePath, axios);
export const userController = new UsersControllerApi(defaultConfig, apiBasePath, axios);
export const friendController = new FriendsControllerApi(
    defaultConfig,
    apiBasePath,
    axios
);
export const chatController = new ChatControllerApi(defaultConfig, apiBasePath, axios);
