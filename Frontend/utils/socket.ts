import SockJS from 'sockjs-client';
import { SocketMessage, SocketMessageType } from '../types/socket';
import ReconnectingWebSocket from 'reconnecting-websocket';
import { useEffect, useMemo, useState } from 'react';
import { getToken } from './login';
import { BASE_PATH } from '../api/base';
import { UserDto } from '../api';

const URL = BASE_PATH + '/public/sockets';

export function useSocket(
    loggedUser: UserDto | null,
    onMessage?: (message: SocketMessage) => void,
    onFocus?: () => void
) {
    const socket = useMemo(() => {
        console.log('Creating socket instance');
        return new ReconnectingWebSocket(URL, [], {
            WebSocket: SockJS,
        });
    }, []);

    const [isAuthorized, setIsAuthorized] = useState(false);

    useEffect(() => {
        if (!loggedUser) {
            return;
        }

        const onOpen = async () => {
            console.log('Open socket connection');
            setIsAuthorized(false);
            socket.send(
                new SocketMessage(
                    SocketMessageType.AUTHORIZATION,
                    await getToken()
                ).toString()
            );
        };
        const onMessageSocket = async (e: any) => {
            let data = e;
            if (e.data) {
                data = JSON.parse(e.data);
            }
            const message: SocketMessage = data;
            switch (message.type) {
                case SocketMessageType.AUTHORIZATION:
                    console.log('Authorized status:', message.content);
                    if (message.content === 'SUCCESS') {
                        setIsAuthorized(true);
                    } else {
                        socket.reconnect();
                    }
                    return;
            }

            onMessage?.(message);
        };

        const onClose = async () => {
            console.log('Socket disconnected');
            setIsAuthorized(false);
        };

        const onError = (error: any) => {
            console.log('Socket error', error);
        };

        const onFocusSocket = () => {
            console.log('Socket focus, state:', socket.readyState);
            if (socket.readyState !== ReconnectingWebSocket.OPEN) {
                socket.reconnect();
                return;
            }
            onFocus?.();
        };

        // @ts-ignore
        socket.addEventListener('open', onOpen);
        // @ts-ignore
        socket.addEventListener('message', onMessageSocket);
        // @ts-ignore
        socket.addEventListener('close', onClose);
        socket.addEventListener('error', onError);

        window.addEventListener('focus', onFocusSocket);

        return () => {
            // @ts-ignore
            socket.removeEventListener('open', onOpen);
            // @ts-ignore
            socket.removeEventListener('message', onMessageSocket);
            // @ts-ignore
            socket.removeEventListener('close', onClose);
            socket.removeEventListener('error', onError);

            window.removeEventListener('focus', onFocusSocket);
        };
    }, [onFocus, onMessage, socket, loggedUser]);

    useEffect(() => {
        socket.reconnect();
    }, [loggedUser]);

    return socket;
}
