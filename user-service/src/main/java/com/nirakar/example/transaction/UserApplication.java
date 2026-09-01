package com.nirakar.example.transaction;

import com.nirakar.example.transaction.service.UserService;

import lombok.extern.slf4j.Slf4j;

import com.nirakar.example.transaction.service.TransactionService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Bean
	public ToolCallbackProvider tools(TransactionService transactionService, UserService employeeService) {
		log.info("Registering tools for TransactionService and UserService");
		return MethodToolCallbackProvider.builder().toolObjects(transactionService, employeeService).build();
	}

}
