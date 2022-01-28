import { ChatMessageDto, UserDtoRolesEnum } from '../../api';
import React, { useContext } from 'react';
import Auth from '../common/Auth';
import { LoggedUserContext } from '../../contexts/LoggedUserContext';
import styles from '../../styles/chat.module.scss';

interface ChatMessageProps {
    chatMessage: ChatMessageDto;
}
export default function ChatMessageBubble(props: ChatMessageProps) {
    const { loggedUser } = useContext(LoggedUserContext);

    return (
        <div
            className={styles.message}
            style={{
                backgroundColor:
                    props.chatMessage.userFrom?.id === loggedUser?.id
                        ? '#d0dff3'
                        : 'whitesmoke',
                alignSelf:
                    props.chatMessage.userFrom?.id === loggedUser?.id
                        ? 'flex-end'
                        : 'flex-start',
                borderColor:
                    props.chatMessage.userFrom?.id === loggedUser?.id
                        ? '#76a2dc'
                        : 'gainsboro',
            }}
        >
            {props.chatMessage.message}
        </div>
    );
}
