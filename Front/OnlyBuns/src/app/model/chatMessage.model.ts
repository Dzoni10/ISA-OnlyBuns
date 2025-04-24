export interface ChatMessage {
    id: number;
    chatId: number;
    username: string;
    text: string;
    creationTime: Date; // ISO string format (primer: "2024-11-23T12:34:56")
  }
  