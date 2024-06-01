package com.photogallery.photogallery;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PhotogalleryApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotogalleryApplication.class, args);
		System.out.println("Photo Gallery App started v 1.0.0 ......");
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
