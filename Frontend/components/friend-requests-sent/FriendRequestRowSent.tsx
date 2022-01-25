import React from 'react';
import { FriendRequestDto, FriendRequestResolveResolveTypeEnum } from '../../api';
import styles from '../../styles/friends.module.scss';
import Button from 'react-bootstrap/Button';

type FriendRequestRowProps = {
    friendRequest: FriendRequestDto;
};
export default function FriendRequestRowSent(props: FriendRequestRowProps) {
    return (
        <div className={styles.rowWrapper}>
            <span title={props.friendRequest.userTo?.email}>
                {props.friendRequest.userTo?.name}
            </span>
            <span>
                <i>Čeká na vyřízení příjemcem</i>
            </span>
        </div>
    );
}
