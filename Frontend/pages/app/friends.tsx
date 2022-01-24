import React, { useRef } from 'react';
import Head from 'next/head';
import styles from '../../styles/friends-layout.module.css';
import Posts from '../../components/posts';
import Header from '../../components/header';
import UsersSearch from '../../components/users-search';
import FriendRequests from '../../components/friend-requests';

export default function Friends() {
    return (
        <div className={styles.layoutwrapper}>
            <Head>
                <title>Friends | PIA Network</title>
                <meta
                    name="description"
                    content="Social network as a semestral work for KIV/PIA"
                />
            </Head>

            <div className={styles.search}>
                <UsersSearch />
            </div>
            <div className={styles.requests}>
                <FriendRequests />
            </div>
            <div className={styles.blocations}>Blocations</div>
            <Header />
        </div>
    );
}
