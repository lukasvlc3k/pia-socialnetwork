import React, { useState } from 'react';
import styles from '../../styles/friends.module.scss';
import { Form } from 'react-bootstrap';
import { FriendRequestDto, FriendRequestResolveResolveTypeEnum } from '../../api';
import FriendRequestRowSent from './FriendRequestRowSent';

type FriendRequestProps = {
    friendRequests: FriendRequestDto[] | null;
};
export default function FriendRequestsSent(props: FriendRequestProps) {
    return (
        <div className={styles.panel}>
            <h2>Odeslané žádosti o přátelství</h2>

            {props.friendRequests?.map((fr) => (
                <FriendRequestRowSent key={fr.requestId} friendRequest={fr} />
            ))}
            {props.friendRequests?.length === 0 && (
                <p>Nemáte žádné nevyřízené odeslané žádosti o přátelství</p>
            )}
        </div>
    );
}
