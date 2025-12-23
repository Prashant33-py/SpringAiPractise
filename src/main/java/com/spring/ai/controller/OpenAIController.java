package com.spring.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class OpenAIController {

    private ChatClient chatClient;
    private ChatModel chatModel;

    public OpenAIController(OpenAiChatModel model, ChatClient.Builder builder){
        this.chatModel = model;
        ChatMemory memory = MessageWindowChatMemory.builder()
                .build();
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                .build();

    }

    @GetMapping
    public ResponseEntity<String> sendRequestToAIModel(@RequestParam String requestMessage){
        String response = chatClient.prompt(requestMessage)
                .call()
                .content();
        return ResponseEntity.ok().body(response);
    }

}
