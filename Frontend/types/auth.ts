import { AppProps } from 'next/app';
import { RoleNameEnum } from '../api';

export type SecuredComponent = AppProps['Component'] & {
    minRole: RoleNameEnum | null;
};

export class ComponentAuth {
    minRole: RoleNameEnum | null;

    constructor(minRole: RoleNameEnum | null = null) {
        this.minRole = minRole;
    }
}
