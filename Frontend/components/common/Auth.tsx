import React, { ReactNode, useContext, useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import { LoggedUserContext } from '../../contexts/LoggedUserContext';
import { SecuredComponent } from '../../types/auth';
import { RoleNameEnum } from '../../api';

export default function Auth({
    minRole,
    children,
}: {
    minRole?: RoleNameEnum | null;
    children: ReactNode;
}) {
    const router = useRouter();
    const { loggedUser, roles, tokenExpiration } = useContext(LoggedUserContext);
    const [clientRender, setClientRender] = useState(false);

    function checkRole(): boolean {
        if (!loggedUser && minRole) {
            // user not logged but role required
            return false;
        }

        if (!minRole) {
            // no role required
            return true;
        }

        return (roles?.includes(minRole) || roles?.includes(RoleNameEnum.Admin)) ?? false;
    }

    useEffect(() => {
        setClientRender(true);
    }, []);

    if (tokenExpiration?.isBefore(new Date())) {
        // token has expired
        // TODO logout
        (async () => {
            await router.push('/');
        })();
    }

    if (clientRender && checkRole()) {
        return <>{children}</>;
    }

    return null;
}
