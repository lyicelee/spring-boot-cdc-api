package com.orizonsh.cdc.api.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.orizonsh.cdc.api.exception.CDCApiException;

@Component
public final class ApplicationContextUtils implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		this.applicationContext = applicationContext;
	}

	public <T> T getBean(Class<T> targetClass) throws CDCApiException {
		try {
			return targetClass.cast(applicationContext.getBean(Class.forName(targetClass.getName())));
		} catch (Exception e) {
			throw new CDCApiException(e.getMessage(), e);
		}
	}
}
