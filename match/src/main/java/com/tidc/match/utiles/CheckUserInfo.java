package com.tidc.match.utiles;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassNmae CheckUserInfo
 * @Description TODO
 * @Author 14631
 **/
public  class CheckUserInfo {
	public static Object getUserInfo(String accessToken){
		RestTemplate restTemplate = new RestTemplate();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor("TIDC", "computer"));
		String url = "http://localhost:5461/oauth/check_token?token="+accessToken;
		String rest = restTemplate.getForObject(url, String.class);
		JSONObject jsonobject = JSONObject.fromObject(rest);
		Object student = jsonobject.get("student");
		return student;
	}
}
