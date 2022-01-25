import React, { createContext, useContext, useState } from 'react';
import { UserDto } from '../api';
import { useSocket } from '../utils/socket';
import ReconnectingWebSocket from 'reconnecting-websocket';
import Emittery from 'emittery';
import { LoggedUserContext } from './LoggedUserContext';

interface ISocketContext {
    socketBus: Emittery;
    socket?: ReconnectingWebSocket;
}
const socketEventBus = new Emittery();

export const SocketContext = createContext<ISocketContext>({
    socketBus: socketEventBus,
});

export enum SocketEvents {
    MESSAGE_RECEIVED = 'messageReceived',
    SOCKET_FOCUS = 'socketFocus',
}
const SocketProvider = (props: { children: any }) => {
    const { loggedUser } = useContext(LoggedUserContext);

    const socket = useSocket(
        loggedUser,
        (message) => {
            socketEventBus.emit(SocketEvents.MESSAGE_RECEIVED, message);
        },
        () => {
            socketEventBus.emit(SocketEvents.SOCKET_FOCUS);
        }
    );

    return (
        <SocketContext.Provider
            value={{
                socketBus: socketEventBus,
                socket,
            }}
        >
            {props.children}
        </SocketContext.Provider>
    );
};

export default SocketProvider;
