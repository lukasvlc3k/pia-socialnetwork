import React, { useRef } from 'react';
import Head from 'next/head';
import styles from '../../styles/main-layout.module.css';
import Posts from '../../components/posts';

export default function MainPage() {
    const listInnerRef = useRef();

    const onScroll = () => {
        if (listInnerRef.current) {
            const { scrollTop, scrollHeight, clientHeight } = listInnerRef.current;
            if (scrollTop + clientHeight === scrollHeight) {
                console.log('reached bottom');
            }
        }
    };

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

            <Posts />
            <div className={styles.friends}>Friends</div>
        </div>
    );
}
