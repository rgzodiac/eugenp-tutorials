package com.baeldung.caching.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/item/{id}")
    public Item getItemById(@PathVariable String id) {
        return itemService.getItemForId(id);
    }
    
	@GetMapping("/item/description/{id}")
	public String getItemDescriptionById(@PathVariable String id) {

		String desc = (String) redisTemplate.opsForHash().get("items", id);

		if (StringUtils.isBlank(desc)) {
			desc = itemService.getItemForId(id).getDescription();
			redisTemplate.opsForHash().put("items", id, desc);
		}

		return desc;
	}
    
	@GetMapping("/item/itemDescription/{id}")
	@Cacheable(value = "items")
	public String getItemDescById(@PathVariable String id) {
		return itemService.getItemForId(id).getDescription();
	}

}