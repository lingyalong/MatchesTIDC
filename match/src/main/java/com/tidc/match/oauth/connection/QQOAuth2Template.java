package com.tidc.match.oauth.connection;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * @ClassNmae QQOAuth2Template
 * @Description TODO
 * @Author 14631
 **/
public class QQOAuth2Template extends OAuth2Template {
	Logger logger = LoggerFactory.getLogger(getClass());
	public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
		super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
//		因为exchangeForAccess方法会判断这个值之后才会带上ClientID和client_secret参数去发起访问
		setUseParametersForClientAuthentication(true);
	}
	//	这个方法就是添加可以解析Token的格式
//	这个方法就没办法登录的原因不能解析qq传回来的text/html的返回格式
//加入一种读取text/html的返回结果的方法
	@Override
	protected RestTemplate createRestTemplate() {
		RestTemplate restTemplate = super.createRestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
		return restTemplate;
	}
	//	这个方法就是为了获取并且拆解Token
//	因为spring认为返回的Token的形式是Json所以默认按Json的方式去获取Token并且封装成一个Map
//	但是QQ的Token返回形式是一个字符串所以重写一个方法 去重写获得Token
	//把响应的实体的格式按照qq的标准做一个自定义
	@Override
	protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
//		这个是响应实体 加了泛型就是意味这个响应实体是用String接收 发起请求获取Token
		ResponseEntity<String> result = getRestTemplate().postForEntity(accessTokenUrl,parameters,String.class);
		//分割这个响应实体 Token
		String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(String.valueOf(result),"&");
		//取出里面的内容
		String accessToken = StringUtils.substringAfterLast(items[0],"=");
		logger.info("获取到的Token:"+accessToken);
		//到期时间 是一个秒数为单位的
		Long expiresIn = new Long(StringUtils.substringAfterLast(items[1],"="));
		logger.info("获取到的Token的刷新时间(秒):"+expiresIn);
		//刷新Token的
		String refreshToken = StringUtils.substringAfterLast(items[2],"=");
		logger.info("刷新的Token refreshToken:"+refreshToken);
		return new AccessGrant(accessToken,null,refreshToken,expiresIn);
	}

}
