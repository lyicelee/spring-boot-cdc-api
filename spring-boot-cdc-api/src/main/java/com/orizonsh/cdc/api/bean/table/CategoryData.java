package com.orizonsh.cdc.api.bean.table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public final class CategoryData extends TableDataBase {

	private static final long serialVersionUID = -4455746951105616445L;

	/** ID */
	@JsonProperty(value = "id")
	private Long id;

	/** カテゴリ名 */
	@JsonProperty(value = "category_name")
	private String categoryName;

}
