export enum SocketMessageType {
    AUTHORIZATION = 'AUTHORIZATION',
    CHAT = 'CHAT',
    USER_JOIN = 'USER_JOIN',
    USER_LEAVE = 'USER_LEAVE',
}

export class SocketMessage {
    type: SocketMessageType;
    content: any;

    constructor(type: SocketMessageType, content: any) {
        this.type = type;
        this.content = content;
    }

    toString(): string {
        return JSON.stringify(this);
    }
}
