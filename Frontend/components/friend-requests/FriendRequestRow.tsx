import React from 'react';
import { FriendRequestDto, FriendRequestResolveResolveTypeEnum } from '../../api';
import styles from '../../styles/friends.module.scss';
import Button from 'react-bootstrap/Button';

type FriendRequestRowProps = {
    friendRequest: FriendRequestDto;
    onFriendRequestResolve: (
        resolveType: FriendRequestResolveResolveTypeEnum
    ) => Promise<boolean>;
};
export default function FriendRequestRow(props: FriendRequestRowProps) {
    return (
        <div className={styles.rowWrapper}>
            <span title={props.friendRequest.userFrom?.email}>
                {props.friendRequest.userFrom?.name}
            </span>
            <span>
                <Button
                    variant={'success'}
                    onClick={() => {
                        props.onFriendRequestResolve(
                            FriendRequestResolveResolveTypeEnum.Accept
                        );
                    }}
                >
                    Přijmout
                </Button>
                <Button
                    variant={'warning'}
                    onClick={() => {
                        props.onFriendRequestResolve(
                            FriendRequestResolveResolveTypeEnum.Reject
                        );
                    }}
                >
                    Zamítnout
                </Button>
                <Button
                    variant={'danger'}
                    onClick={() => {
                        props.onFriendRequestResolve(
                            FriendRequestResolveResolveTypeEnum.Block
                        );
                    }}
                >
                    Zablokovat
                </Button>
            </span>
        </div>
    );
}
