import env from "@beam-australia/react-env";

export const PasswordSecurityLevels = [35, 60, 100]; // minimum, strong, maximum

export const apiBasePath = env("BACKEND_URL");

export const allowedHtmlTagsInPosts = ['br', 'strong'];
export const maxPostLength = 8000;
export const postsRefreshInterval = 5000; // ms
