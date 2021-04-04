package com.mw.portfolio.config.security;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;

@Configuration
@EnableRedisWebSession
@EnableRedisRepositories
public class RedisConfig {

  @Bean
  public ReactiveRedisTemplate<Object, Object> reactiveRedisTemplate(RedisSerializer<Object> redisJsonSerializer,
                                                                     RedisSerializer<Object> redisStringSerializer,
                                                                     ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
    val serializationContext = RedisSerializationContext
        .newSerializationContext()
        .key(redisStringSerializer)
        .value(redisJsonSerializer)
        .hashKey(redisStringSerializer)
        .hashValue(redisJsonSerializer)
        .build();

    return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext);
  }

  @Bean
  public ReactiveStringRedisTemplate reactiveStringRedisTemplate(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
    return new ReactiveStringRedisTemplate(reactiveRedisConnectionFactory);
  }

  @Bean
  public RedisSerializer<Object> redisJsonSerializer() {
    return new GenericJackson2JsonRedisSerializer();
  }

  @Bean
  public StringRedisSerializer redisStringSerializer() {
    return new StringRedisSerializer();
  }

  @Bean
  public ConfigureRedisAction configureRedisAction() {
    return ConfigureRedisAction.NO_OP;
  }
}
