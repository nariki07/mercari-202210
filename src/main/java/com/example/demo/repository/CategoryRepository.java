package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Category;

/**
 * categoryテーブルを操作するリポジトリ.
 * 
 * @author moriharanariki
 *
 */
@Repository
public class CategoryRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<Category> CATEGORY_ROW_MAPPER = (rs, i) -> {
		Category category = new Category();
		category.setId(rs.getInt("id"));
		category.setParentId(rs.getInt("parent"));
		category.setCategoryName(rs.getString("name"));
		category.setNameAll(rs.getString("name_all"));
		return category;
	};

	public Category insert(Category category) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(category);
		if (category.getId() == null) {
			String sql = "insert into category(parent,name,name_all)" + "values(:parentId,:categoryName,:nameAll);";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String[] keyColumnNames = { "id" };
			template.update(sql, param, keyHolder, keyColumnNames);
			category.setId(keyHolder.getKey().intValue());
		} else {
			String sql = "insert into category(parent,name,name_all)" + "values(:parentId,:categoryName,:nameAll);";
			template.update(sql, param);
		}
		return category;
	}

	public Category findByNameLargeCategory(String categoryName) {
		String sql = "SELECT id,parent,name,name_all FROM category WHERE name = :categoryName AND parent IS NULL AND name_all IS NULL";
		SqlParameterSource param = new MapSqlParameterSource().addValue("categoryName", categoryName);
		List<Category> categoryList = template.query(sql, param, CATEGORY_ROW_MAPPER);
		if (categoryList.size() == 0) {
			return null;
		}
		return categoryList.get(0);
	}

	public Category findByNameMidiumCategory(String categoryName) {
		String sql = "SELECT id,parent,name,name_all FROM category WHERE name = :categoryName AND parent IS NOT NULL AND name_all IS NULL;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("categoryName", categoryName);
		List<Category> categoryList = template.query(sql, param, CATEGORY_ROW_MAPPER);
		if (categoryList.size() == 0) {
			return null;
		}
		return categoryList.get(0);
	}

	public List<Category> allFindByNameMidiumCategory(String categoryName) {
		String sql = "SELECT id,parent,name,name_all FROM category WHERE name = :categoryName AND parent IS NOT NULL AND name_all IS NULL;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("categoryName", categoryName);
		List<Category> categoryList = template.query(sql, param, CATEGORY_ROW_MAPPER);
		if (categoryList.size() == 0) {
			return null;
		}
		return categoryList;
	}

	public Category findByNameSmallCategory(String categoryName) {
		String sql = "SELECT id,parent,name,name_all FROM category WHERE name = :categoryName AND parent IS NOT NULL AND name_all IS NOT NULL;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("categoryName", categoryName);
		List<Category> categoryList = template.query(sql, param, CATEGORY_ROW_MAPPER);
		if (categoryList.size() == 0) {
			return null;
		}
		return categoryList.get(0);
	}

	public Category findByNameAll(String nameAll) {
		String sql = "SELECT id,parent,name,name_all FROM category WHERE name_all = :nameAll AND parent IS NOT NULL AND name_all IS NOT NULL;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("nameAll", nameAll);
		List<Category> categoryList = template.query(sql, param, CATEGORY_ROW_MAPPER);
		if (categoryList.size() == 0) {
			return null;
		}
		return categoryList.get(0);
	}

	public List<Category> allFindByNameSmallCategory(String categoryName) {
		String sql = "SELECT id,parent,name,name_all FROM category WHERE name = :categoryName AND parent IS NOT NULL AND name_all IS NOT NULL;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("categoryName", categoryName);
		List<Category> categoryList = template.query(sql, param, CATEGORY_ROW_MAPPER);
		if (categoryList.size() == 0) {
			return null;
		}
		return categoryList;
	}

}
