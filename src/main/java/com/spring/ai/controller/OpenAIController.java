package com.spring.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class OpenAIController {

    private final ChatClient chatClient;
    private ChatModel chatModel;
    @Autowired
    @Qualifier("mistralAiEmbeddingModel")
    private EmbeddingModel embeddingModel;
    @Autowired
    private VectorStore vectorStore;

    public OpenAIController(@Qualifier("mistralAiChatModel") ChatModel model, @Qualifier("mistralChatClient") ChatClient builder){
        this.chatModel = model;
        ChatMemory memory = MessageWindowChatMemory.builder()
                .build();
        this.chatClient = builder.mutate()
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

    @GetMapping("/recommend")
    public ResponseEntity<String> recommendMovie(@RequestParam String type, @RequestParam String year, @RequestParam String language){
        String strPrompt = """
                I want to watch {type} movies with great rating.
                The movies has to be released around the year {year} and in {language}. Suggest 3 movies along with its cast and run time.
                """;
        PromptTemplate template = new PromptTemplate(strPrompt);
        Prompt prompt = template.create(Map.of("type", type, "year", year, "language", language));
        String response = chatClient.prompt(prompt)
                .call()
                .content();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/embedding")
    public float[] getEmbedding(@RequestParam String text){
        float[] embeddings = null;
        try {
            embeddings = embeddingModel.embed(text);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return embeddings;
    }

    @PostMapping("/similarity")
    public double getSimilarity(@RequestParam String text1, @RequestParam String text2){
        float[] embedding1 = embeddingModel.embed(text1);
        float[] embedding2 = embeddingModel.embed(text2);

        float dotProduct = 0;
        float magnitude1 = 0;
        float magnitude2 = 0;

        for (int i = 0; i < embedding1.length; i++) {
            dotProduct += embedding1[i] * embedding2[i];
            magnitude1 += Math.pow(embedding1[i], 2);
            magnitude2 += Math.pow(embedding1[i], 2);
        }
        return dotProduct / Math.sqrt(magnitude1) * Math.sqrt(magnitude2);
    }

    @PostMapping("/product-info")
    public List<Document> getProductInfo(@RequestParam String question){
        return vectorStore.similaritySearch(question);
    }
}
