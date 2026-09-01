package com.nirakar.example.ai.assistant.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nirakar.example.ai.assistant.McpToolRegistry;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

// http://localhost:8072/myapp/aiassistant/chat?message=give me last 3 transactions
// http://localhost:8072/myapp/aiassistant/chat?message=give me "John Smith" employee details

@RestController
@Slf4j
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(McpToolRegistry mcpToolRegistry, ChatClient.Builder chatClientBuilder) {
        ToolCallback[] callbacks = mcpToolRegistry.getToolCallbacks();

        this.chatClient = chatClientBuilder
                .defaultTools((Object[]) callbacks)
                .build();

    }

    @GetMapping(value = "/chat")
    public Flux<String> chat(@RequestParam(value="message") String message) {
        log.info("Received message: {}", message);
        return chatClient.prompt().user(message).stream().content();
    }
}

