package com.xh.redis.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xh.redis.util.RedisUtil;

/**
* <p>Title: </p>
* <p>Description: </p>
* 
* @author H.Yang
* @QQ 1033542070
* @date 2018年2月24日
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-redis.xml")
public class RedisTest {

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

		boolean result = RedisUtil.set("name2", "我是一只111小鸟");
		System.out.println(result);
	}

}
