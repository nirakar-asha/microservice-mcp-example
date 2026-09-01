export interface ChatMessage {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  timestamp: Date;
  isError?: boolean;
}

export interface ChatRequest {
  message: string;
}

export interface ChatResponse {
  response: string;
}
