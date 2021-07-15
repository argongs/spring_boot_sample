package com.college.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class CollegeDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(CollegeDataApplication.class, args);
	}

	@Bean
	public Docket collegeDataApi () {
		return new Docket (DocumentationType.SWAGGER_2)	
					.select ()
					.paths (PathSelectors.ant ("/data/**"))
					.apis (RequestHandlerSelectors.basePackage ("com.college.data"))
					.build ()
					.apiInfo (apiInfo ());
	}

	private ApiInfo apiInfo () {
		return new ApiInfoBuilder ()
				.title ("College Data API")
				.description ("API for interacting with the college database")
				.version ("0.0.1")
				.build ();
	}
}