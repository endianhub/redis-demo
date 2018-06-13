package com.xh.redis.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xh.redis.util.RedisTemplateUtil2;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-redistemplate2.xml")
public class RedisTemplateTest2 {

	/**
	 * <p>Title: 添加对象</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 */
	@Test
	public void save() {
		RedisTemplateUtil2.remove("name");
		boolean result = RedisTemplateUtil2.set("name", "我是一只111小鸟");
		System.out.println(result);
		
		String name = (String) RedisTemplateUtil2.get("name");
		System.out.println(name);
		
		RedisTemplateUtil2.remove("paramMap");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", 1);
		paramMap.put("name", "常海洋");
		paramMap.put("sex", "男");
		paramMap.put("age", 24);
		RedisTemplateUtil2.hmSet("paramMap", "hashKey", paramMap);
		
		paramMap = (Map<String, Object>) RedisTemplateUtil2.hmGet("paramMap", "hashKey");
		System.out.println(paramMap.toString());
	}
}
