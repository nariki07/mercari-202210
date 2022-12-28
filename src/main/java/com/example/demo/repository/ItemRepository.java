package com.example.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Item;

/**
 * itemテーブルを操作するためのリポジトリ.
 * 
 * @author moriharanariki
 *
 */
@Repository
public class ItemRepository {
	 
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public Item insert(Item item) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(item);
		if(item.getId() == null) {
			String sql = "insert into items(name,condition,category,brand,price,shipping,description)"
					+ "values(:name,:conditionId,:category,:brand,:price,:shipping,:description);";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String[] keyColumnNames = { "id" };
			template.update(sql, param, keyHolder, keyColumnNames);
			item.setId(keyHolder.getKey().intValue());
		} else {
			String sql = "insert into items(name,condition,category,brand,price,shipping,description)"
					+ "values(:name,:conditionId,:category,:brand,:price,:shipping,:description);";
			template.update(sql, param);
		}
		return item;
	}
}
