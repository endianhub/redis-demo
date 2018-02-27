package com.xh.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xh.redis.dao.BaseDao;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年2月24日
 */
@Service
public class StudentService {

	@Autowired
	private BaseDao<String, Object> dao;

	public boolean addString(String key, Object value) {

		return dao.set(key, value);
	}

}
