package com.yu.yuaicodemother.config;

import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Slf4j
@Configuration
@EnableConfigurationProperties(RedisChatMemoryProperties.class)
public class RedisChatMemoryStoreConfig {

    /**
     * Redis 对话记忆存储默认关闭：
     * 它依赖 RedisJSON 模块（普通 Redis 会报 ERR unknown command JSON.GET）。
     * 待切换到 Redis Stack（或安装 RedisJSON 模块）后，
     * 在配置文件中设置 chat-memory.redis-store.enabled=true 即可启用。
     */
    @Bean
    @ConditionalOnProperty(prefix = "chat-memory", name = "redis-store.enabled", havingValue = "true")
    public RedisChatMemoryStore redisChatMemoryStore(RedisChatMemoryProperties properties) {
        log.info("开始加载 RedisChatMemoryStore 配置...");
        
        try {
            // 关键参数检查
            if (!StringUtils.hasText(properties.getHost())) {
                throw new IllegalArgumentException("配置加载失败: Redis Host 为空");
            }

            String maskedPassword = StringUtils.hasText(properties.getPassword()) ? "******" : "无";
            log.info("Redis 配置参数: Host={}, Port={}, Password={}, TTL={}, Database={}", 
                    properties.getHost(), properties.getPort(), maskedPassword, properties.getTtl(), properties.getDatabase());

            RedisChatMemoryStore store = RedisChatMemoryStore.builder()
                    .host(properties.getHost())
                    .port(properties.getPort())
                    .password(properties.getPassword())
                    .ttl(properties.getTtl())
                    .build();
            
            log.info("RedisChatMemoryStore 初始化成功");
            return store;
            
        } catch (Exception e) {
            log.error("RedisChatMemoryStore 初始化失败: {}", e.getMessage(), e);
            throw new IllegalStateException("无法初始化 RedisChatMemoryStore", e);
        }
    }
}
