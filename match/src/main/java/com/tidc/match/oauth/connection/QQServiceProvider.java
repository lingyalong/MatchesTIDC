package com.tidc.match.oauth.connection;

import com.tidc.match.oauth.api.QQ;
import com.tidc.match.oauth.api.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * @ClassNmae QQServiceProvider
 * @Description TODO
 * @Author 14631
 **/
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {
	//	将用户引导到哪个地址进行用户认证
	private static final String authorizeUrl = "https://graph.qq.com/oauth2.0/authorize";
	//	使用授权码获取令牌的地址
	private static final String accessTokenUrl = "https://graph.qq.com/oauth2.0/token";
	private String appId;
//因为流程执行到获取到授权码用授权码去获取Token令牌的类我们使用的是spring提供的OAth2Template 这个类解析不了返回来的数据 只能解析Json和Form类性的 但是返回的Token的类型是txt/html 无法解析也就是获取不到Token
//也就成了登录失败 SpringSocial的登录失败处理器会让请求跳转到/sigin地址
	public QQServiceProvider(String appId ,String appSecret) {
		//		传入springSocial提供的OAuth2perations 实现类
		super(new QQOAuth2Template(appId,appSecret,authorizeUrl,accessTokenUrl));
		this.appId = appId;
	}


	@Override
	public QQ getApi(String token) {
		return new QQImpl(token,appId);
	}
}
