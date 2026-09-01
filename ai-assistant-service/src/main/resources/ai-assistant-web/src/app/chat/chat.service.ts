import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  /**
   * Sends a user message to the AI agent backend API.
   * GET {apiUrl}/chat?message={message}
   */
  sendMessage(message: string): Observable<string> {
    return this.http.get(`${this.apiUrl}/chat`, {
      params: { message },
      responseType: 'text'
    });
  }
}

