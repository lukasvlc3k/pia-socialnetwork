export interface ChatMessage {
    userFromId: number;
    userToId: number;
    timestamp: number;
    message: string;
}
