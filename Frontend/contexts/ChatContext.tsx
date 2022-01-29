import React, {createContext, useContext, useEffect, useState} from 'react';
import {ChatMessageDto, UserDto} from '../api';
import {chatController, userController} from '../controllers';
import moment, {Moment} from 'moment';
import {getTokenData, loginUser, logoutUser} from '../utils/login';
import {useRouter} from 'next/router';
import {LoggedUserContext} from "./LoggedUserContext";

interface IChatProvider {
    chatWith: UserDto | null;
    setChatWith: React.Dispatch<React.SetStateAction<UserDto | null>>;

    chatMessages: ChatMessageDto[] | null;
    setChatMessages: React.Dispatch<React.SetStateAction<ChatMessageDto[] | null>>;
}

export const ChatContext = createContext<IChatProvider>({
    chatWith: null,
    setChatWith: () => {
        return;
    },
    chatMessages: null,
    setChatMessages: () => {
        return;
    },
});

const ChatProvider = (props: { children: React.ReactNode }) => {
    const [chatWith, setChatWith] = useState<UserDto | null>(null);
    const [chatMessages, setChatMessages] = useState<ChatMessageDto[] | null>(null);
    const {loggedUser} = useContext(LoggedUserContext);

    useEffect(() => {
        loadData();
    }, [chatWith]);

    async function loadData() {
        if (!chatWith?.id || !loggedUser?.id) {
            return;
        }

        const res = await chatController.getChatMessages(loggedUser.id, chatWith.id, 10);
        setChatMessages(res?.data.reverse() ?? []);
    }

    return (
        <ChatContext.Provider
            value={{
                chatWith,
                setChatWith,
                chatMessages,
                setChatMessages,
            }}
        >
            {props.children}
        </ChatContext.Provider>
    );
};

export default ChatProvider;
