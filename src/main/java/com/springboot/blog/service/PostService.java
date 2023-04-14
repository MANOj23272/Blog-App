package com.springboot.blog.service;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostResponse;

public interface PostService {

	PostDto createPost(PostDto postDto);
	PostResponse getAllposts(int pageNo, int pageSize,String sortBy,String sortDir);
	PostDto getPostById(Long id);
	
	PostDto updatePost(PostDto postDto, Long id);
	
	void deletePostById(long id);
}
