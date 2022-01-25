import React from 'react';
import styles from '../../styles/friends.module.scss';
import { UserBlockDto } from '../../api';
import UserBlockRow from './UserBlockRow';

type FriendRequestProps = {
    userBlocks: UserBlockDto[] | null;
    onUnblock: (blockId: number | undefined) => Promise<boolean>;
};
export default function UserBlocks(props: FriendRequestProps) {
    return (
        <div className={styles.panel}>
            <h2>Zablokovaní uživatelé</h2>

            {props.userBlocks?.map((ub) => (
                <UserBlockRow
                    key={ub.id}
                    userBlock={ub}
                    onUnblock={async () => {
                        return await props.onUnblock(ub.id);
                    }}
                />
            ))}
            {props.userBlocks?.length === 0 && <p>Neblokujete žádné uživatele</p>}
        </div>
    );
}
