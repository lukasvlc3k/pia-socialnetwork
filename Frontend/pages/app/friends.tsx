import React, { useEffect, useRef, useState } from 'react';
import Head from 'next/head';
import styles from '../../styles/friends-layout.module.css';
import Posts from '../../components/posts';
import Header from '../../components/header';
import UsersSearch from '../../components/users-search';
import FriendRequests from '../../components/friend-requests';
import { friendController, userController } from '../../controllers';
import { ShowToast } from '../../utils/alerts';
import {
    FriendRequestDto,
    FriendRequestResolveResolveTypeEnum,
    UserDto,
} from '../../api';
import FriendRequestRow from '../../components/friend-requests/FriendRequestRow';

export default function Friends() {
    const [searchFor, setSearchFor] = useState('');
    const [searchResults, setSearchResults] = useState<UserDto[] | null>(null);

    const [friendRequests, setFriendRequest] = useState<FriendRequestDto[] | null>(null);

    useEffect(() => {
        refreshAll();
    }, []);

    async function sendFriendRequest(userID: number): Promise<boolean> {
        const res = await friendController.createNewFriendRequest({ userId: userID });
        if (res.data.ok) {
            ShowToast('Žádost úspěšně odeslána');
            await refreshAll();
            return true;
        } else {
            console.log(res);
            return false;
        }
    }
    async function resolveFriendRequest(
        requestId: number | undefined,
        resolveType: FriendRequestResolveResolveTypeEnum
    ) {
        if (!requestId) {
            return false;
        }

        const res = await friendController.resolveFriendRequest(requestId.toString(), {
            resolveType,
        });
        if (res.data.ok) {
            ShowToast('Žádost úspěšně vyřízena');
            await refreshAll();
            return true;
        } else {
            console.log(res);
            return false;
        }
    }

    async function doSearch(value: string) {
        try {
            const res = await userController.searchRelevantUsers(value);
            setSearchResults(res.data);
        } catch (e) {
            console.log(e);
        }
    }
    async function loadFriendRequests() {
        try {
            const res = await friendController.getMyRequests();
            setFriendRequest(res.data);
        } catch (e) {
            console.log(e);
        }
    }

    async function refreshAll() {
        if (searchFor.length >= 3) {
            await doSearch(searchFor);
        }

        await loadFriendRequests();
    }

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
                <UsersSearch
                    onSendFriendRequest={sendFriendRequest}
                    doSearch={doSearch}
                    searchFor={searchFor}
                    setSearchFor={setSearchFor}
                    searchResults={searchResults}
                    setSearchResults={setSearchResults}
                />
            </div>
            <div className={styles.requests}>
                <FriendRequests
                    friendRequests={friendRequests}
                    onFriendRequestResolve={resolveFriendRequest}
                />
            </div>
            <div className={styles.blocations}>Blocations</div>
            <Header />
        </div>
    );
}
