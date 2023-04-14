package com.springboot.blog.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.springboot.blog.dto.PostDto;

public interface PostService {

	PostDto createPost(PostDto postDto);
	List<PostDto> getAllposts();
	PostDto getPostById(Long id);
	
	PostDto updatePost(PostDto postDto, Long id);
	
	void deletePostById(long id);
}
