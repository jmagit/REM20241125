package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.MeGustaService.Pelicula;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RedisConfig {
	@Bean
	RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		var stringSerializer = new StringRedisSerializer();
		var jackson2JsonRedisSerializer = jsonRedisSerializer(Object.class);
		var template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(connectionFactory);
		template.setDefaultSerializer(new JdkSerializationRedisSerializer());
		template.setKeySerializer(stringSerializer);
		template.setHashKeySerializer(stringSerializer);
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		template.setValueSerializer(jackson2JsonRedisSerializer);
		return template;
	}

	@Bean
	ReactiveRedisTemplate<String, Pelicula> peliculaRedisTemplate(ReactiveRedisConnectionFactory factory) {
		return generateReactiveRedisTemplate(factory, Pelicula.class);
	}

	@Bean
	ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
		return generateReactiveRedisTemplate(factory, Object.class);
	}

	private <T> ReactiveRedisTemplate<String, T> generateReactiveRedisTemplate(ReactiveRedisConnectionFactory factory,
			Class<T> type) {
		return new ReactiveRedisTemplate<>(factory, getRedisSerializationContext(type));
	}

	private static <T> Jackson2JsonRedisSerializer<T> jsonRedisSerializer(Class<T> type) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//		if("java.lang.Object".equals(type.getCanonicalName()))
//			mapper.activateDefaultTypingAsProperty(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.EVERYTHING, "_class");
//		else
			mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
		return new Jackson2JsonRedisSerializer<>(mapper, type);
	}

	public static <T> RedisSerializationContext<String, T> getRedisSerializationContext(Class<T> type) {
		return RedisSerializationContext.<String, T>newSerializationContext(new JdkSerializationRedisSerializer())
				.key(StringRedisSerializer.UTF_8).value(jsonRedisSerializer(type)).build();
	}
}
