package com.todolist.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author 김융훈
 * 다목적 공통적으로 사용할 맵.
 */
public class CommonMap extends HashMap<String, Object>{
	
	private static final long serialVersionUID = 1L;

	@Getter
	private int page;
	@Getter
	private int size;
	
	public CommonMap(){}

	public CommonMap(String key, Object value) {
		super.put(key, value);
	}
	
	public CommonMap(Map<String, Object> map){
		putAll(map);
	}
	/*
	 * Pagiation 처리
	 */
	public CommonMap putPage(int page , int size){
		this.page = page;
		this.size = size;
		super.put("page", page);
		super.put("size", size);
		super.put("offset" , (size * (page - 1)));
		return this;
	}

	public static CommonMap create(String key , Object value){
		return new CommonMap(key , value);
	}

	public static CommonMap create(int page , int size){
		CommonMap map = new CommonMap();
		map.putPage(page,size);
		return map;
	}


	public String getString(Object key){
		return super.get(key) instanceof String ? (String) super.get(key) : null;
	}

	public Integer getInteger(Object key){
		return super.get(key) instanceof Integer ? (Integer) super.get(key) : null;
	}
	public Boolean getBoolean(Object key){
		return super.get(key) instanceof Boolean ? (Boolean) super.get(key) : null;
	}

	@Override
	public Object get(Object key) {
		return super.get(key);
	}

	@Override
	public CommonMap put(String key, Object value) {
		super.put(key, value);
		return this;
	}
	
	public Map<String, Object> toMap(){
		return this;
	}

	public CommonMap toLowerCaseMap(){
		CommonMap newMap = new CommonMap();
		this.keySet().forEach(c-> newMap.put(c.toLowerCase() , this.get(c)));
		return newMap;
	}

}
