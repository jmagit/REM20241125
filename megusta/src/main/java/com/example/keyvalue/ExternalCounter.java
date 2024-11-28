package com.example.keyvalue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class ExternalCounter {
	private String key;
	private ValueOperations<String, String> redis;

	public ExternalCounter(@Value("${app.redis.keys.counter}") String key, StringRedisTemplate template) {
		this.key = key;
		redis = template.opsForValue();
	}

	public long get() {
		try {
			return Long.parseLong(redis.get(key));
		} catch (NumberFormatException ex) {
			reset();
			return 0;
		}
	}

	public void set(long value) {
		redis.set(key, Long.toString(value));
	}

	public long increment() {
		return redis.increment(key);
	}

	public long decrement() {
		return redis.decrement(key);
	}

	public void reset() {
		redis.set(key, "0");
	}

}
