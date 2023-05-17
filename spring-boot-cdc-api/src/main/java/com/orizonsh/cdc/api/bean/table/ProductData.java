package com.orizonsh.cdc.api.bean.table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ProductData extends TableDataBase {

	private static final long serialVersionUID = -4849520910531171118L;

	/** ID */
	@JsonProperty(value = "id")
	private Long id;

	/** 商品名 */
	@JsonProperty(value = "product_name")
	private String productName;
}
