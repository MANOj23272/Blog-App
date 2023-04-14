package com.springboot.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PostDto {

	private Long id;
	private String title;
	private String description;
	private String content;
}
