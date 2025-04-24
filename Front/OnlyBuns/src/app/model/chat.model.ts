import { ChatMessage } from "./chatMessage.model";
import { ChatParticipant } from "./chatParticipant.model";
export interface Chat {
    id: number;
    name: string;
    adminUsername: string;
    participants: ChatParticipant[];
    messages: ChatMessage[];
  }
  