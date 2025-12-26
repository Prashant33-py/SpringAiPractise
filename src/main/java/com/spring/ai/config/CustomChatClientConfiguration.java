package com.spring.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomChatClientConfiguration {

    @Bean
    @Qualifier("openAiChatClient")
    public ChatClient openAiChatClient(@Qualifier("openAiChatModel") ChatModel openAiChatModel){
        return ChatClient.builder(openAiChatModel)
                .build();
    }

    @Bean
    @Qualifier("anthropicChatClient")
    public ChatClient anthropicChatClient(@Qualifier("anthropicChatModel") ChatModel anthropicChatModel){
        return ChatClient.builder(anthropicChatModel)
                .build();
    }

    @Bean
    @Qualifier("mistralChatClient")
    public ChatClient mistraChatClient(@Qualifier("mistralAiChatModel") ChatModel mistralAiChatModel){
        return ChatClient.builder(mistralAiChatModel)
                .build();
    }

}
