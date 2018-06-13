package com.xh.redis.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xh.redis.util.RedisJedisPoolUtil;

/**
* <p>Title: </p>
* <p>Description: </p>
* 
* @author H.Yang
* @QQ 1033542070
* @date 2018年2月24日
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-jedispool.xml")
public class RedisJedisPoolTest {

	/**
	 * <p>Title: 添加对象</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年2月24日
	 * 
	 */
	@Test
	public void save() {
		RedisJedisPoolUtil.flushAll();
		boolean result = RedisJedisPoolUtil.save("name", "我是一只111小鸟");
		System.out.println(result);

		String name = (String) RedisJedisPoolUtil.get("name");
		System.out.println(name);

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", 1);
		paramMap.put("name", "常海洋");
		paramMap.put("sex", "男");
		paramMap.put("age", 24);

		result = RedisJedisPoolUtil.save("map", paramMap);
		System.out.println(result);

		paramMap = (Map<String, Object>) RedisJedisPoolUtil.get("map");
		System.out.println(paramMap.toString());

	}

}
