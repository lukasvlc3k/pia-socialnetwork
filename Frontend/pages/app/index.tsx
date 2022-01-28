import React, { useContext } from 'react';
import Head from 'next/head';
import styles from '../../styles/main-layout.module.scss';
import Posts from '../../components/posts';
import Header from '../../components/header';
import Friends from '../../components/friends';
import { ComponentAuth } from '../../types/auth';
import { UserDtoRolesEnum } from '../../api';
import ChatBox from '../../components/chat';
import { ChatContext } from '../../contexts/ChatContext';

export default function MainPage() {
    const { chatWith } = useContext(ChatContext);

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

            {chatWith && <ChatBox />}
        </div>
    );
}

MainPage.auth = new ComponentAuth(UserDtoRolesEnum.User);
