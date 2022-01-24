import React, { useEffect, useState } from 'react';
import { friendController, userController } from '../../controllers';
import { UserDto } from '../../api';
import styles from '../../styles/friends.module.scss';
import { Badge } from 'react-bootstrap';

export default function Friends() {
    const [friends, setFriends] = useState<UserDto[]>([]);

    useEffect(() => {
        loadFriends();
    }, []);

    async function loadFriends() {
        try {
            const res = await friendController.getMyFriends();
            setFriends(res.data);
        } catch (e) {
            console.log(e);
        }
    }

    function FriendRow(props: { friend: UserDto }) {
        return (
            <div title={props.friend.email} className={styles.friendRow}>
                <Badge bg={props.friend.online ? 'success' : 'danger'}> </Badge>{' '}
                <div>{props.friend.name}</div>
            </div>
        );
    }

    return (
        <div className={'container'}>
            <h2>Přátelé</h2>

            {friends?.map((f) => (
                <FriendRow friend={f} key={f.id} />
            ))}
        </div>
    );
}
