package com.tidc.match.oauth.connection;

import com.tidc.match.oauth.api.QQ;
import com.tidc.match.oauth.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * @ClassNmae QQAdapter
 * @Description TODO
 * @Author 14631
 **/
public class QQAdapter implements ApiAdapter<QQ> {
//	测试连通
	@Override
	public boolean test(QQ qq) {
		return true;
	}

	@Override
	public void setConnectionValues(QQ api, ConnectionValues connectionValues) {
		QQUserInfo qqUserInfo = api.getUserInfo();
//			设置用户的名字
		connectionValues.setDisplayName(qqUserInfo.getNickname());
//			设置用户的头像
		connectionValues.setImageUrl(qqUserInfo.getFigureurl_qq_1());
//			这个是个人主页但是qq是没有的
		connectionValues.setProfileUrl(null);
//			qq号 openId
		connectionValues.setProviderUserId(qqUserInfo.getOpenid());
	}

	@Override
	public UserProfile fetchUserProfile(QQ qq) {
		return null;
	}

	@Override
	public void updateStatus(QQ qq, String s) {

	}
}
