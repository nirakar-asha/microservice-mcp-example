import { Pipe, PipeTransform, inject } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { marked } from 'marked';
import DOMPurify from 'dompurify';

@Pipe({
  name: 'markdown',
  standalone: true
})
export class MarkdownPipe implements PipeTransform {
  private sanitizer = inject(DomSanitizer);

  transform(content: string | null | undefined): SafeHtml {
    if (!content) {
      return '';
    }
    // Parse markdown into HTML synchronously
    const rawHtml = marked.parse(content, {
      gfm: true,
      breaks: true
    }) as string;
    
    // Sanitize to prevent XSS vulnerability
    const sanitizedHtml = DOMPurify.sanitize(rawHtml);
    return this.sanitizer.bypassSecurityTrustHtml(sanitizedHtml);
  }
}
