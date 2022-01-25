import React from 'react';
import {
    FriendRequestDto,
    FriendRequestResolveResolveTypeEnum,
    UserBlockDto,
} from '../../api';
import styles from '../../styles/friends.module.scss';
import Button from 'react-bootstrap/Button';

type FriendRequestRowProps = {
    userBlock: UserBlockDto;
    onUnblock: () => Promise<boolean>;
};
export default function UserBlockRow(props: FriendRequestRowProps) {
    return (
        <div className={styles.rowWrapper}>
            <span title={props.userBlock.user?.email}>{props.userBlock.user?.name}</span>
            <div>
                <Button
                    variant={'success'}
                    onClick={() => {
                        props.onUnblock();
                    }}
                >
                    Odblokovat
                </Button>
            </div>
        </div>
    );
}
