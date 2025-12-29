package com.spring.ai.util;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    public VectorStore vectorStore;

    public DataInitializer(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void initializeData() {
        TextReader reader = new TextReader(new ClassPathResource("product_details.txt"));
        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(50)
                .withMinChunkSizeChars(20)
                .withMinChunkLengthToEmbed(100)
                .withMaxNumChunks(40)
                .withKeepSeparator(false)
                .build();

        vectorStore.add(splitter.split(reader.get()));
    }

}
