import React from 'react';
import Head from 'next/head';
import styles from '../../styles/login.module.css';

export default function MainPage() {
    return (
        <div className={styles.layoutwrapper}>
            <Head>
                <title>PIA Network</title>
                <meta
                    name="description"
                    content="Social network as a semestral work for KIV/PIA"
                />
            </Head>

            <div className={styles.header}>PIA NETWORK</div>
            <main className={styles.loginWrapper}>
                <div className={styles.login}>
                    <h1>Hlavní stránka</h1>
                </div>
            </main>
        </div>
    );
}
