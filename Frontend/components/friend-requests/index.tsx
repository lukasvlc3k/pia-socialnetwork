import React, { useState } from 'react';
import styles from '../../styles/friends.module.scss';
import { Form } from 'react-bootstrap';
import { FriendRequestDto, FriendRequestResolveResolveTypeEnum } from '../../api';
import FriendRequestRow from './FriendRequestRow';

type FriendRequestProps = {
    friendRequests: FriendRequestDto[] | null;

    onFriendRequestResolve: (
        requestId: number | undefined,
        resolveType: FriendRequestResolveResolveTypeEnum
    ) => Promise<boolean>;
};
export default function FriendRequests(props: FriendRequestProps) {
    return (
        <div className={styles.panel}>
            <h2>Přijaté žádosti o přátelství</h2>

            {props.friendRequests?.map((fr) => (
                <FriendRequestRow
                    key={fr.requestId}
                    friendRequest={fr}
                    onFriendRequestResolve={async (solution) => {
                        return await props.onFriendRequestResolve(fr.requestId, solution);
                    }}
                />
            ))}
            {props.friendRequests?.length === 0 && (
                <p>Nemáte žádné nevyřízené žádosti o přátelství</p>
            )}
        </div>
    );
}
