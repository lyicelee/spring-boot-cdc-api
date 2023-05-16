package com.orizonsh.cdc.api.bean.table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public final class CategoryDataBean extends TableDataBeanBase {

	/** ID */
	@JsonProperty(value = "id")
	private Long id;

	/** カテゴリ名 */
	@JsonProperty(value = "category_name")
	private String categoryName;

}
