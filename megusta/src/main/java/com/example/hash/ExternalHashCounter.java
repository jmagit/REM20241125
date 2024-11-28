package com.example.hash;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class ExternalHashCounter {
	private String key;
	private HashOperations<String, String, String> redis;

	public ExternalHashCounter(@Value("${app.redis.keys.hash}") String key, StringRedisTemplate template) {
		this.key = key;
		redis = template.opsForHash();
	}

	public Map<String, String> get() {
		return redis.entries(key);
	}

	public long get(int id) {
		try {
			return Long.parseLong(redis.get(key, Integer.toString(id)));
		} catch (NumberFormatException ex) {
			reset(id);
			return 0;
		}
	}

	public void set(int id, long value) {
		redis.put(key, Integer.toString(id), Long.toString(value));
	}

	public long increment(int id) {
		return redis.increment(key, Integer.toString(id), 1);
	}

	public long decrement(int id) {
		return redis.increment(key, Integer.toString(id), -1);
	}

	public void reset(int id) {
		redis.put(key, Integer.toString(id), "0");
	}

}
