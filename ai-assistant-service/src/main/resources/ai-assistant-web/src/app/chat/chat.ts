import {
  Component,
  ElementRef,
  ViewChild,
  AfterViewChecked,
  signal,
  inject
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService } from './chat.service';
import { ChatMessage } from './chat.model';
import { MarkdownPipe } from './markdown.pipe';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule, MarkdownPipe],
  templateUrl: './chat.html',
  styleUrl: './chat.scss'
})
export class ChatComponent implements AfterViewChecked {
  @ViewChild('chatContainer') private chatContainer!: ElementRef<HTMLDivElement>;
  @ViewChild('messageInput') private messageInput!: ElementRef<HTMLTextAreaElement>;

  private chatService = inject(ChatService);

  messages = signal<ChatMessage[]>([]);
  userInput = signal<string>('');
  isLoading = signal<boolean>(false);
  errorMessage = signal<string | null>(null);
  lastFailedMessage = signal<string | null>(null);

  suggestions = [
    'Give me the last 3 transaction Id',
    'Give me the last 3 transaction Id for John Smith',
    'Find details for John Smith'
  ];

  recentItems = ['Transaction overview'];
  activeRecent = signal<string>('Transaction overview');

  private shouldScrollToBottom = false;

  ngAfterViewChecked(): void {
    if (this.shouldScrollToBottom) {
      this.scrollToBottom();
      this.shouldScrollToBottom = false;
    }
  }

  sendMessage(textToSend?: string): void {
    const text = (textToSend ?? this.userInput()).trim();
    if (!text || this.isLoading()) {
      return;
    }

    const userMsg: ChatMessage = {
      id: this.generateId(),
      role: 'user',
      content: text,
      timestamp: new Date()
    };

    this.messages.update((msgs) => [...msgs, userMsg]);
    this.userInput.set('');
    this.resetTextareaHeight();
    this.isLoading.set(true);
    this.errorMessage.set(null);
    this.lastFailedMessage.set(null);
    this.shouldScrollToBottom = true;

    this.chatService.sendMessage(text).subscribe({
      next: (responseContent) => {
        const aiMsg: ChatMessage = {
          id: this.generateId(),
          role: 'assistant',
          content: responseContent,
          timestamp: new Date()
        };
        this.messages.update((msgs) => [...msgs, aiMsg]);
        this.isLoading.set(false);
        this.shouldScrollToBottom = true;
      },
      error: (err) => {
        this.isLoading.set(false);
        this.lastFailedMessage.set(text);
        const errDetail =
          err?.error?.message || err?.statusText || 'The assistant could not be reached.';
        this.errorMessage.set(errDetail);

        const errorMsg: ChatMessage = {
          id: this.generateId(),
          role: 'assistant',
          content: `**Error:** ${errDetail}`,
          timestamp: new Date(),
          isError: true
        };
        this.messages.update((msgs) => [...msgs, errorMsg]);
        this.shouldScrollToBottom = true;
      }
    });
  }

  selectSuggestion(suggestion: string): void {
    this.sendMessage(suggestion);
  }

  newConversation(): void {
    this.messages.set([]);
    this.errorMessage.set(null);
    this.lastFailedMessage.set(null);
    this.userInput.set('');
    this.resetTextareaHeight();
    if (this.messageInput?.nativeElement) {
      this.messageInput.nativeElement.focus();
    }
  }

  onKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  onInput(event: Event): void {
    const textarea = event.target as HTMLTextAreaElement;
    this.userInput.set(textarea.value);
    textarea.style.height = 'auto';
    textarea.style.height = `${Math.min(textarea.scrollHeight, 130)}px`;
  }

  private resetTextareaHeight(): void {
    if (this.messageInput?.nativeElement) {
      this.messageInput.nativeElement.style.height = 'auto';
    }
  }

  private scrollToBottom(): void {
    try {
      if (this.chatContainer?.nativeElement) {
        this.chatContainer.nativeElement.scrollTop =
          this.chatContainer.nativeElement.scrollHeight;
      }
    } catch (e) {
      // Ignore scroll error
    }
  }

  private generateId(): string {
    return 'msg_' + Math.random().toString(36).substring(2, 9);
  }
}
