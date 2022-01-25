import React, { useEffect, useRef, useState } from 'react';
import Head from 'next/head';
import styles from '../../styles/friends-layout.module.scss';
import Header from '../../components/header';
import UsersSearch from '../../components/users-search';
import FriendRequests from '../../components/friend-requests';
import { friendController, userController } from '../../controllers';
import { ShowToast } from '../../utils/alerts';
import { UserBlockDto } from '../../api';
import {
    FriendRequestDto,
    FriendRequestResolveResolveTypeEnum,
    RoleNameEnum,
    UserDto,
} from '../../api';
import { ComponentAuth } from '../../types/auth';
import FriendRequestsSent from '../../components/friend-requests-sent';
import UserBlocks from '../../components/user-blocks';
import Button from 'react-bootstrap/Button';
import FriendManagement from '../../components/friends-management';
import { AxiosResponse } from 'axios';

export default function Friends() {
    const [searchFor, setSearchFor] = useState('');
    const [searchResults, setSearchResults] = useState<UserDto[] | null>(null);

    const [friendRequests, setFriendRequest] = useState<FriendRequestDto[] | null>(null);
    const [friendRequestsSent, setFriendRequestsSent] = useState<
        FriendRequestDto[] | null
    >(null);
    const [friends, setFriends] = useState<UserDto[]>([]);

    const [userBlocks, setUserBlocks] = useState<UserBlockDto[] | null>(null);

    useEffect(() => {
        refreshAll();
    }, []);

    async function handleRes(res: AxiosResponse, text: string): Promise<boolean> {
        if (res.data.ok) {
            ShowToast(text);
            await refreshAll();
            return true;
        } else {
            console.log(res);
            return false;
        }
    }
    async function sendFriendRequest(userID: number): Promise<boolean> {
        const res = await friendController.createNewFriendRequest({ userId: userID });
        return handleRes(res, 'Žádost úspěšně odeslána');
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
        return handleRes(res, 'Žádost úspěšně vyřízena');
    }

    async function unblock(blockId: number | undefined): Promise<boolean> {
        if (!blockId) {
            return false;
        }

        const res = await friendController.unblock(blockId.toString());

        return handleRes(res, 'Uživatel úspěšně odblokován');
    }

    async function setAdmin(userId: number | null, isAdmin: boolean): Promise<void> {
        if (!userId) {
            return;
        }

        const res = await userController.setAdmin(userId.toString(), {
            addAdmin: isAdmin,
        });
        handleRes(res, 'Nastaveno');
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
            const resReceived = await friendController.getMyRequests();
            setFriendRequest(resReceived.data);
        } catch (e) {
            console.log(e);
        }

        try {
            const resSent = await friendController.getMyRequestsSent();
            setFriendRequestsSent(resSent.data);
        } catch (e) {
            console.log(e);
        }
    }

    async function loadBlocks() {
        try {
            const resReceived = await friendController.getMyBlockedUsers();
            setUserBlocks(resReceived.data);
        } catch (e) {
            console.log(e);
        }
    }

    async function loadFriends() {
        try {
            const res = await friendController.getMyFriends();
            setFriends(res.data);
        } catch (e) {
            console.log(e);
        }
    }

    async function refreshAll() {
        if (searchFor.length >= 3) {
            await doSearch(searchFor);
        }

        await loadFriendRequests();
        await loadBlocks();
        await loadFriends();
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

            <Header />

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

            <div className={'container'}>
                <Button onClick={refreshAll}>Aktualizovat data</Button>
            </div>
            <FriendRequests
                friendRequests={friendRequests}
                onFriendRequestResolve={resolveFriendRequest}
            />
            <FriendRequestsSent friendRequests={friendRequestsSent} />

            <UserBlocks userBlocks={userBlocks} onUnblock={unblock} />
            <FriendManagement friends={friends} onSetAdmin={setAdmin} />
        </div>
    );
}
Friends.auth = new ComponentAuth(RoleNameEnum.User);
