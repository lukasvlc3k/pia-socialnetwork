import React, { useState } from 'react';
import styles from '../../styles/friendrequests.module.scss';
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
        <div className={'container'}>
            <h2>Přijaté žádosti o přátelství</h2>

            <div>
                {props.friendRequests?.map((fr) => (
                    <FriendRequestRow
                        key={fr.requestId}
                        friendRequest={fr}
                        onFriendRequestResolve={async (solution) => {
                            return await props.onFriendRequestResolve(
                                fr.requestId,
                                solution
                            );
                        }}
                    />
                ))}
            </div>
        </div>
    );
}
