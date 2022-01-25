import { authController } from '../controllers';
import { LoginResponse, UserDto } from '../api';

export async function loginUser(email: string, password: string): Promise<boolean> {
    try {
        const loginRes = await authController.loginUser({ email, password });

        const data = loginRes.data;

        if (data && data.token) {
            localStorage.setItem('token', JSON.stringify(data));
            return true;
        }
    } catch (e) {
        if (e instanceof Error) {
            if (e.message.includes('status code 401')) {
                return false;
            }
        }
    }

    return false;
}

export async function logoutUser(): Promise<boolean> {
    localStorage.clear();
    return true;
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
