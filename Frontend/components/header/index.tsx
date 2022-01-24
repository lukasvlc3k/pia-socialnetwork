import React, { useEffect, useRef, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUsers } from '@fortawesome/free-solid-svg-icons';
import Button from 'react-bootstrap/Button';

import { useRouter } from 'next/router';
import styles from '../../styles/main-layout.module.css';
import Link from 'next/link';

export default function Header() {
    const router = useRouter();

    return (
        <>
            <div className={styles.logo}>
                <Link href="/app"> PIA NETWORK</Link>
            </div>
            <div className={styles.header}>
                <div>
                    <Button
                        title={'Přátelé'}
                        onClick={async () => {
                            await router.push('/app/friends');
                        }}
                    >
                        <FontAwesomeIcon icon={faUsers} />
                    </Button>
                </div>
            </div>
            <div className={styles.user}>
                Přihlášený uživatel:
                <br />
                Karel Novák
            </div>
        </>
    );
}
