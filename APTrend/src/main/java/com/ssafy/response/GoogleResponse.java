package com.ssafy.response;

import java.util.Map;

public class GoogleResponse implements OAuth2Response{

	private final Map<String, Object> attribute;

    public GoogleResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();  // Google의 고유 사용자 ID
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("family_name").toString()+attribute.get("given_name").toString();
    }

}
