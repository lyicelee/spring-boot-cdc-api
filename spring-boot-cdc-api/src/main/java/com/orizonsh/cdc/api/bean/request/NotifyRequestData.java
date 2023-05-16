package com.orizonsh.cdc.api.bean.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotifyRequestData implements Serializable {

	/** データ通知URL */
	@NotNull
	private String notifyURL;

}
