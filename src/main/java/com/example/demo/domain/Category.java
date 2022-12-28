package com.example.demo.domain;

/**
 * カテゴリーを表すドメインクラス.
 * 
 * @author moriharanariki
 *
 */
public class Category {

	/* ID */
	private Integer id;
	/* 親 */
	private Integer parentId;
	/* カテゴリー名 */
	private String categoryName;
	/* 全カテゴリー名 */
	private String nameAll;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getNameAll() {
		return nameAll;
	}

	public void setNameAll(String nameAll) {
		this.nameAll = nameAll;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", parentId=" + parentId + ", categoryName=" + categoryName + ", nameAll="
				+ nameAll + "]";
	}

}
