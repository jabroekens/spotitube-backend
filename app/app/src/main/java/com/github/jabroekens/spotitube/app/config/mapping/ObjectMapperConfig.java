package com.github.jabroekens.spotitube.app.config.mapping;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.jabroekens.spotitube.model.user.User;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObjectMapperConfig implements ContextResolver<ObjectMapper> {

	private final ObjectMapper objectMapper;

	public ObjectMapperConfig() {
		this.objectMapper = JsonMapper.builder()
		  .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
		  .visibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
		  .addMixIn(User.class, UserMixin.class)
		  .build();
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}

}
