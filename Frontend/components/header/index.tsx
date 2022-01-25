import React, { useContext } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faDoorOpen, faHome, faUsers } from '@fortawesome/free-solid-svg-icons';
import Button from 'react-bootstrap/Button';

import { useRouter } from 'next/router';
import styles from '../../styles/main-layout.module.scss';
import Link from 'next/link';
import { LoggedUserContext } from '../../contexts/LoggedUserContext';
import Auth from '../common/Auth';
import { RoleNameEnum } from '../../api';

export default function Header() {
    const router = useRouter();
    const { loggedUser, logout } = useContext(LoggedUserContext);

    return (
        <>
            <div className={styles.header}>
                <div className={styles.logo}>
                    <Link href="/app"> PIA NETWORK</Link>
                </div>

                <div>
                    <Button
                        title={'Domů'}
                        onClick={async () => {
                            await router.push('/app');
                        }}
                        className={'me-2'}
                    >
                        <FontAwesomeIcon icon={faHome} className={'me-3'} />
                        Domů
                    </Button>

                    <Button
                        title={'Přátelé'}
                        onClick={async () => {
                            await router.push('/app/friends');
                        }}
                    >
                        <FontAwesomeIcon icon={faUsers} className={'me-3'} />
                        Správa přátel
                    </Button>
                </div>
                <div className={styles.user}>
                    <div>
                        <small>Přihlášený uživatel:</small>
                        <br />
                        <b>{loggedUser?.name}</b>
                    </div>
                    <Button
                        title={'Odhlásit se'}
                        onClick={async () => {
                            await logout();
                        }}
                    >
                        <FontAwesomeIcon icon={faDoorOpen} />
                        Odhlásit se
                    </Button>
                </div>
            </div>
        </>
    );
}
