package com.tidc.match.oauth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @ClassNmae QQImpl
 * @Description TODO
 * @Author 14631
 **/
public class QQImpl  extends AbstractOAuth2ApiBinding implements QQ{
	//	申请QQ登录成功后，分配给应用的appid 这个写成配置文件的形式方便一点 每个第三方的appid都不一样
	private ObjectMapper objectMapper = new ObjectMapper();
	private String appId;
	private String openid;
	private static final String getOpenId = "https://graph.qq.com/oauth2.0/me?access_token=%s";
	private static final String getUserInfo = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";
	public QQImpl(String accessToken,String appId){
//		将Token的发送策略设置为作为参数发起请求拼接到url里
		super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
		RestTemplate restTemplate = getRestTemplate();
//		从配置文件里获取appid
		this.appId = appId;
//		这个写法就是使用accessToken替换掉%s
//		拼接获取openid的url
//		这一步之所以要使用拼接的方式Token是因为这个方法每走完,Token还不是默认加入参数
		String url = String.format(getOpenId,accessToken);
		String result = restTemplate.getForObject(url,String.class);
//		截取openid  得到openid
		this.openid = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
	}

	@Override
	public QQUserInfo getUserInfo() {
		String url = String.format(getUserInfo,appId,openid);
		String result = getRestTemplate().getForObject(url,String.class);
		QQUserInfo qqUserInfo = null;
//		将这个字符串读取成一个userInfo对象
		try {
			qqUserInfo =  objectMapper.readValue(result, QQUserInfo.class);
			qqUserInfo.setOpenid(openid);
			return qqUserInfo;
		} catch (IOException e) {
			throw new RuntimeException("获取用户信息失败",e);
		}
	}
}
