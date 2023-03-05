package com.echo.country.handler;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/4/2 20:03
 */
public class MessageConverter extends MappingJackson2HttpMessageConverter {

	public MessageConverter() {

		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
		mediaTypes.add(MediaType.APPLICATION_JSON);
		mediaTypes.add(MediaType.TEXT_XML);
		setSupportedMediaTypes(mediaTypes);

	}
}
