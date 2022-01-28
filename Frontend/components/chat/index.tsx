import React, {KeyboardEventHandler, useContext, useEffect, useRef, useState} from 'react';
import styles from '../../styles/chat.module.scss';
import { LoggedUserContext } from '../../contexts/LoggedUserContext';
import Button from 'react-bootstrap/Button';
import { ChatContext } from '../../contexts/ChatContext';
import { SocketContext, SocketEvents } from '../../contexts/SocketContext';
import { SocketMessage, SocketMessageType } from '../../types/socket';
import { ChatMessage } from '../../types/chat';
import { ChatMessageDto } from '../../api';
import { Form } from 'react-bootstrap';
import ChatMessageBubble from './ChatMessageBubble';
import { ShowToast } from '../../utils/alerts';

export default function ChatBox() {
    const { loggedUser } = useContext(LoggedUserContext);
    const { chatWith, setChatWith, chatMessages, setChatMessages } =
        useContext(ChatContext);
    const { socketBus, socket } = useContext(SocketContext);

    const [newMessage, setNewMessage] = useState('');
    const messagesEndRef = useRef(null);

    useEffect(() => {
        socketBus.on(SocketEvents.MESSAGE_RECEIVED, onMessageReceived);

        return () => {
            socketBus.off(SocketEvents.MESSAGE_RECEIVED, onMessageReceived);
        };
    }, [chatWith]);

    useEffect(() => {
        scrollToBottom();
    }, [chatMessages]);

    function onMessageReceived(data: SocketMessage) {
        if (!loggedUser || !chatWith) {
            return;
        }

        if (data.type === SocketMessageType.CHAT) {
            const chatMessage: ChatMessage = data.content as ChatMessage;
            if (
                chatMessage.userToId !== chatWith?.id &&
                chatMessage.userFromId !== chatWith?.id
            ) {
                // not for opened chat
                return;
            }

            const userFrom =
                chatMessage.userFromId === chatWith.id ? chatWith : loggedUser;
            const userTo = chatMessage.userToId === chatWith.id ? chatWith : loggedUser;
            const chatMessageDto: ChatMessageDto = {
                message: chatMessage.message,
                timestamp: chatMessage.timestamp,
                userFrom: userFrom,
                userTo: userTo,
            };

            setChatMessages((oldValue) => [...(oldValue ?? []), chatMessageDto]);
            scrollToBottom();
        }
    }

    async function sendMessage() {
        if (!socket || !loggedUser?.id || !chatWith?.id) {
            return;
        }

        if (!newMessage || newMessage.length === 0) {
            ShowToast('Nelze poslat prázdnou zprávu', 'error', 3000, 'bottom');
            return;
        }
        if (newMessage.length > 200) {
            ShowToast('Maximální délka zprávy je 200 znaků', 'error', 3000, 'bottom');
            return;
        }

        const chatMessage: ChatMessage = {
            message: newMessage,
            timestamp: new Date().getTime(),
            userFromId: loggedUser.id,
            userToId: chatWith.id,
        };

        const str = new SocketMessage(SocketMessageType.CHAT, chatMessage).toString();
        console.log(str);

        socket.send(str);
        setNewMessage('');
    }

    const scrollToBottom = () => {
        // @ts-ignore
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    const handleKeyDown: KeyboardEventHandler<HTMLInputElement> = (e) => {
        if (e.key.toLowerCase() === 'enter') {
            e.preventDefault();
            sendMessage();
        }
    };

    return (
        <div className={styles.wrapper}>
            <div className={styles.header}>
                <span>{chatWith?.name}</span>
                <Button
                    onClick={() => {
                        setChatWith(null);
                    }}
                >
                    X
                </Button>
            </div>
            <div className={styles.body}>
                {chatMessages?.map((m) => (
                    <ChatMessageBubble key={m.timestamp} chatMessage={m} />
                ))}

                <div ref={messagesEndRef} />
            </div>
            <div className={styles.footer}>
                <Form.Control
                    as="textarea"
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    className={styles.text}
                    required={true}
                    onKeyDown={handleKeyDown}
                />
                <Button
                    style={{ height: '100%' }}
                    className={styles.button}
                    onClick={async () => {
                        await sendMessage();
                    }}
                >
                    Odeslat
                </Button>
            </div>
        </div>
    );
}
