package com.tidc.match.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassNmae DruidConfig
 * @Description TODO
 * @Author 14631
 **/
@Configuration
public class DruidConfig {
	@ConfigurationProperties(prefix = "spring.datasource")
	@Bean
	public DataSource druid(){
		return new DruidDataSource();
	}
	@Bean
	public ServletRegistrationBean servletRegistrationBean(){
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
		Map<String,String> map = new HashMap<>();
		map.put("loginUsername","root");
		map.put("loginPassword","546100");
		servletRegistrationBean.setInitParameters(map);
		return servletRegistrationBean;
	}
	@Bean
	public FilterRegistrationBean registrationBean(){
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
		Map<String,String> map = new HashMap<>();
		//设置静态资源不拦截以及后台访问
		map.put("exclusions","*.js,*.jq,*.css,*.html,/druid/*");
		filterRegistrationBean.setInitParameters(map);
		filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
		return filterRegistrationBean;
	}
}
