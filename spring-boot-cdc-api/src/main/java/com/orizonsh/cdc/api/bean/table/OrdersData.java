package com.orizonsh.cdc.api.bean.table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OrdersData extends TableDataBase {

	/** ID */
	@JsonProperty(value = "id")
	private Long id;

	/** 商品ID */
	@JsonProperty(value = "product_id")
	private Long productId;

	/** 顧客名 */
	@JsonProperty(value = "customer_name")
	private String customerName;

	/** オーダー金額 */
	@JsonProperty(value = "order_total")
	private Double orderTotal;

}
