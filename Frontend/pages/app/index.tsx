import React, { useRef } from 'react';
import Head from 'next/head';
import styles from '../../styles/main-layout.module.css';
import Posts from '../../components/posts';
import Header from '../../components/header';
import Friends from '../../components/friends';

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

            <Header />

            <Posts />
            <div className={styles.friends}>
                <Friends />
            </div>
        </div>
    );
}
