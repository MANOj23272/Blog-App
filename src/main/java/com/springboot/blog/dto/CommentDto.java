package com.springboot.blog.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

	
	
	private long id;
	@NotEmpty(message="name should not be empty")	
	private String name;
	@NotEmpty(message="name should not be empty")
	@Email
	private String email;
	@NotEmpty
	@Size(min=10,message ="comment body should me minimum 10 characters")
	private String body;
}
