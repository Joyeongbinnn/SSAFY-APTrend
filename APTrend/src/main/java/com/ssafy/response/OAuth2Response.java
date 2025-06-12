package com.ssafy.response;

public interface OAuth2Response {
	//제공자 ex) naver, google, ...
	String getProvider();
	//제공자가 발급해주는 id
	String getProviderId();
	String getEmail();
	String getName();
}
