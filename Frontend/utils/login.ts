import { authController } from '../controllers';
import { LoginResponse, UserDto } from '../api';

export async function login(email: string, password: string): Promise<UserDto | null> {
    try {
        const loginRes = await authController.loginUser({ email, password });

        const data = loginRes.data;

        if (data && data.token) {
            localStorage.setItem('token', JSON.stringify(data));
            return data.user ?? null;
        }
    } catch (e) {
        if (e instanceof Error) {
            if (e.message.includes('status code 401')) {
                return null;
            }
        }
    }

    return null;
}
export async function getTokenData(): Promise<LoginResponse | null> {
    const dataJSON = localStorage.getItem('token');

    if (!dataJSON) {
        // no token found
        return null;
    }

    return JSON.parse(dataJSON);
}
export async function getToken(): Promise<string> {
    const data = await getTokenData();
    if (!data) {
        return '';
    }

    return data.token ?? '';
}
