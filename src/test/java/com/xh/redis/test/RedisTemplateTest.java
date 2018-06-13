package com.xh.redis.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xh.redis.util.RedisTemplateUtil;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-redistemplate.xml")
public class RedisTemplateTest {

	/**
	 * <p>Title: 添加对象</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 */
	@Test
	public void save() {
		RedisTemplateUtil.del("name");
		boolean result = RedisTemplateUtil.set("name", "我是一只111小鸟");
		System.out.println(result);
		String name = (String) RedisTemplateUtil.get("name");
		System.out.println(name);
	}
}
