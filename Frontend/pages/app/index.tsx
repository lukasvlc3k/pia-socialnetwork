import React from 'react';
import Head from 'next/head';
import styles from '../../styles/main-layout.module.css';
import Posts from '../../components/posts';

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

            <div className={styles.logo}>PIA NETWORK</div>
            <div className={styles.header}></div>
            <div className={styles.user}>
                Přihlášený uživatel:
                <br />
                Karel Novák
            </div>
            <div className={styles.posts}>
                <div className={styles.postsContainer}>
                    <Posts />
                </div>
            </div>
            <div className={styles.friends}>Friends</div>
        </div>
    );
}
