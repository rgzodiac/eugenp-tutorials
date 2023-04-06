package com.baeldung.caching.redis;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
@EnableCaching
public class RedisCacheApplication {

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Bean
	CommandLineRunner loadTestData() {
		return args -> {
			itemRepository.findAll()
					.forEach(item -> redisTemplate.opsForHash().put("items", item.getId(), item.getDescription()));
			redisTemplate.expire("items", Duration.ofMinutes(60));
		};
	}
	
    public static void main(String[] args) {
        SpringApplication.run(RedisCacheApplication.class, args);
    }
}