package com.capgemini.processing.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.capgemini.processing.dto.RechargeDto;

@Configuration
public class JsonConfig {
	
	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
		converter.setClassMapper(classMapper());
		return converter;
	}

	@Bean
	public DefaultClassMapper classMapper() {
		DefaultClassMapper classMapper = new DefaultClassMapper();
		classMapper.setTrustedPackages("*");
		Map<String, Class<?>> idClassMapping = new HashMap<>();
		idClassMapping.put("com.capgemini.recharge.dto.RechargeProducerDto", RechargeDto.class);
		classMapper.setIdClassMapping(idClassMapping);
		return classMapper;
	}

}
