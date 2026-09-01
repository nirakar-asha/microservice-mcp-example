package com.nirakar.example.transaction.service;

import com.nirakar.example.transaction.dto.User;
import dev.toonformat.jtoon.JToon;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Tool(name="fetch_user",description = "fetch user details by name. Use this tool if a user wants to fetch user details by name")
    public String fetchUserTool(String name) {
        log.info("Tool calling :: fetch_user.... {}", name);
        var user = User.getAll().stream().filter(e -> e.name().equalsIgnoreCase(name)).findFirst().orElse(null);
        String jtoonUser = "User not found";
        if(user != null) {
            jtoonUser = JToon.encode(user);
        }
        log.info("Tool calling finished :: fetch_user: {}", jtoonUser);
        return jtoonUser;
    }
}
