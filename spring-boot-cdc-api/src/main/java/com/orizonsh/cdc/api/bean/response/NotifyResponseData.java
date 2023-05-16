package com.orizonsh.cdc.api.bean.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class NotifyResponseData implements Serializable {

	/** エラーコード */
	private String resultCode = "0";
}
