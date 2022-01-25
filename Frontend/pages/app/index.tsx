import React from 'react';
import Head from 'next/head';
import styles from '../../styles/main-layout.module.scss';
import Posts from '../../components/posts';
import Header from '../../components/header';
import Friends from '../../components/friends';
import { ComponentAuth } from '../../types/auth';
import { RoleNameEnum } from '../../api';

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

MainPage.auth = new ComponentAuth(RoleNameEnum.User);
