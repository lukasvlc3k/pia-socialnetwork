import React, { useContext } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHome, faUsers } from '@fortawesome/free-solid-svg-icons';
import Button from 'react-bootstrap/Button';

import { useRouter } from 'next/router';
import styles from '../../styles/main-layout.module.css';
import Link from 'next/link';
import { LoggedUserContext } from '../../contexts/LoggedUserContext';
import Auth from '../common/Auth';
import { RoleNameEnum } from '../../api';

export default function Header() {
    const router = useRouter();
    const { loggedUser } = useContext(LoggedUserContext);

    return (
        <>
            <div className={styles.logo}>
                <Link href="/app"> PIA NETWORK</Link>
            </div>
            <div className={styles.header}>
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
            </div>
            <div className={styles.user}>
                Přihlášený uživatel:
                <br />
                {loggedUser?.name}
            </div>
        </>
    );
}
