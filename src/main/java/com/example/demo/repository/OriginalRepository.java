package com.example.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Original;

/**
 * originalsテーブルを操作するリポジトリ.
 * 
 * @author moriharanariki
 *
 */
@Repository
public class OriginalRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	/**
	 * original情報を追加します。
	 * 
	 * @param original オリジナル商品
	 * @return
	 */
	public Original insert(Original original) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(original);
		if(original.getId() == null) {
			String sql = "insert into original(id,name,condition_id,category_name,brand,price,shipping,description)"
					+ "values(:id,:name,:conditionId,:categoryName,:brand,:price,:shipping,:description);";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String[] keyColumnNames = { "id" };
			template.update(sql, param, keyHolder, keyColumnNames);
			original.setId(keyHolder.getKey().intValue());
		} else {
			String sql = "insert into original(id,name,condition_id,category_name,brand,price,shipping,description)"
					+ "values(:id,:name,:conditionId,:categoryName,:brand,:price,:shipping,:description);";
			template.update(sql, param);
		}
		return original;
	}
}
