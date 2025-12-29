package com.spring.ai.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class VectorConfig {

    @Bean
    public VectorStore getVectorStore(JdbcTemplate jdbcTemplate, @Qualifier("mistralAiEmbeddingModel") EmbeddingModel embeddingModel){
        return PgVectorStore.builder(jdbcTemplate, embeddingModel).build();
    }

}
