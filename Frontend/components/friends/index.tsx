import React, { useEffect, useState } from 'react';
import { friendController } from '../../controllers';
import { UserDto } from '../../api';
import FriendRow from './FriendRow';

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

    return (
        <div className={'container'}>
            <h2>Přátelé</h2>

            {friends?.map((f) => (
                <FriendRow friend={f} key={f.id} />
            ))}
        </div>
    );
}
