import { UserDto } from '../../api';
import React, { useContext, useEffect, useState } from 'react';
import { SocketContext, SocketEvents } from '../../contexts/SocketContext';
import styles from '../../styles/friends.module.scss';
import { Badge } from 'react-bootstrap';
import { SocketMessage, SocketMessageType } from '../../types/socket';
import Button from 'react-bootstrap/Button';
import { ChatContext } from '../../contexts/ChatContext';

interface FriendRowProps {
    friend: UserDto;
}
export default function FriendRow(props: FriendRowProps) {
    const { socketBus } = useContext(SocketContext);
    const [isOnline, setIsOnline] = useState(props.friend.online);
    const { chatWith, setChatWith } = useContext(ChatContext);

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

    function OnlineDot({ online }: { online: boolean }) {
        return (
            <div
                style={{
                    width: '10px',
                    height: '10px',
                    borderRadius: '60px',
                    backgroundColor: online ? '#1EA896' : '#C03221',
                }}
            />
        );
    }

    useEffect(() => {
        socketBus.on(SocketEvents.MESSAGE_RECEIVED, onMessageReceived);

        return () => {
            socketBus.off(SocketEvents.MESSAGE_RECEIVED, onMessageReceived);
        };
    }, []);

    return (
        <div title={props.friend.email} className={styles.friendRow}>
            <div className={styles.namePart}>
                <OnlineDot online={isOnline ?? false} /> <div>{props.friend.name}</div>
            </div>
            <Button
                style={{ justifySelf: 'flex-end' }}
                onClick={() => {
                    setChatWith(props.friend);
                }}
            >
                chat
            </Button>
        </div>
    );
}
