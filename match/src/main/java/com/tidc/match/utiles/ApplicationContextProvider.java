package com.tidc.match.utiles;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ClassNmae ApplicationContextProvider
 * @Description TODO
 * @Author 14631
 **/
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	public Object getBean(String name){
		return getApplicationContext().getBean(name);
	}
	public <T> T getBean(Class<T> clazz){
		return getApplicationContext().getBean(clazz);
	}
	public <T> T getBean(String name,Class<T> clazz){
		return getApplicationContext().getBean(name, clazz);
	}
}
