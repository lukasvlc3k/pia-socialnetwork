import { AppProps } from 'next/app';
import { UserDtoRolesEnum } from '../api';

export type SecuredComponent = AppProps['Component'] & {
    auth: ComponentAuth;
};

export class ComponentAuth {
    minRole: UserDtoRolesEnum | null;

    constructor(minRole: UserDtoRolesEnum | null = null) {
        this.minRole = minRole;
    }
}
