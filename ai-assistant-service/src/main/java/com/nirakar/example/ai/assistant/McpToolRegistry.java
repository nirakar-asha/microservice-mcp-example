package com.nirakar.example.ai.assistant;

import io.modelcontextprotocol.client.McpAsyncClient;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class McpToolRegistry {

    private ToolCallback[] toolCallbacks;

    public McpToolRegistry(List<McpAsyncClient> clients) {
        log.info("MCP Clients: {}", clients);
        AsyncMcpToolCallbackProvider provider = AsyncMcpToolCallbackProvider.builder()
                .mcpClients(clients)
                .build();
        this.toolCallbacks = provider.getToolCallbacks();
    }

    public ToolCallback[] getToolCallbacks() {
        return toolCallbacks;
    }
}
