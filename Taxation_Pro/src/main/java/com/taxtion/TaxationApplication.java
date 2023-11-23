package com.taxtion;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class TaxationApplication {



//	@Bean
//	public LocalDateTimeSerializer localDateTimeDeserializer() {
//		return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//	}
//
//	@Bean
//	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
//		return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer());
//	}

	@Bean
	public Client buildClient(){
		ApplicationContext context =new ClassPathXmlApplicationContext("config.xml");
		BcosSDK bcosSDK = context.getBean(BcosSDK.class);
		return bcosSDK.getClient(1);
	}


	public static void main(String[] args) {
		SpringApplication.run(TaxationApplication.class,args);
	}

}
