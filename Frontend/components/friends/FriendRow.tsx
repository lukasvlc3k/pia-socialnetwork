import { UserDto } from '../../api';
import React, { useContext, useEffect, useState } from 'react';
import { SocketContext, SocketEvents } from '../../contexts/SocketContext';
import styles from '../../styles/friends.module.scss';
import { Badge } from 'react-bootstrap';
import { SocketMessage, SocketMessageType } from '../../types/socket';

interface FriendRowProps {
    friend: UserDto;
}
export default function FriendRow(props: FriendRowProps) {
    const { socketBus } = useContext(SocketContext);
    const [isOnline, setIsOnline] = useState(props.friend.online);

    function onMessageReceived(data: SocketMessage) {
        if (data.type === SocketMessageType.USER_JOIN) {
            const id: number = data.content as number;
            if (id === props.friend.id) {
                setIsOnline(true);
            }
        } else if (data.type === SocketMessageType.USER_LEAVE) {
            const id: number = data.content as number;
            if (id === props.friend.id) {
                setIsOnline(false);
            }
        }
    }

    useEffect(() => {
        socketBus.on(SocketEvents.MESSAGE_RECEIVED, onMessageReceived);

        return () => {
            socketBus.off(SocketEvents.MESSAGE_RECEIVED, onMessageReceived);
        };
    }, []);

    return (
        <div title={props.friend.email} className={styles.friendRow}>
            <Badge bg={isOnline ? 'success' : 'danger'}> </Badge>{' '}
            <div>{props.friend.name}</div>
        </div>
    );
}
