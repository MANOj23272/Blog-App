package com.springboot.blog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostResponse;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;


@Service
public class PostServiceImpl implements PostService{
	
	
	private PostRepository postRepository;
	private ModelMapper mapper;
//	/@Autowired
	public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {
		super();
		this.postRepository = postRepository;
		this.mapper= mapper;
	}

	@Override
	public PostDto createPost(PostDto postDto) {
		
		// convert Dto into entity
//		Post post = new Post();
//		post.setTitle(postDto.getTitle());
//		post.setDescription(postDto.getDescription());
//		post.setContent(postDto.getContent());
		
		Post post = mapToEntity(postDto);
		Post newPost = postRepository.save(post);
		
		//convert entity into Dto
		
		PostDto postResponse = mapToDTO(newPost);
//		PostDto postResponse = new PostDto();
//		postResponse.setId(newPost.getId());
//		postResponse.setTitle(newPost.getTitle());
//		postResponse.setDescription(newPost.getDescription());
//		postResponse.setContent(newPost.getContent());
		return postResponse;
	}

	@Override
	public PostResponse getAllposts(int pageNo,int pageSize,String sortBy,String sortDir) {
		
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		
//		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Post> posts = postRepository.findAll(pageable);
		
		List<Post> listOfPosts = posts.getContent();
		List<PostDto> content =  listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(content);
		postResponse.setPageNo(posts.getNumber());
		postResponse.setPageSize(posts.getSize());
		postResponse.setTotalElements(posts.getTotalElements());
		postResponse.setTotalPages(posts.getTotalPages());
		postResponse.setLast(posts.isLast());
		
		return postResponse;
	}
	
	//converted entity to Dto
	private PostDto mapToDTO(Post post) {
		
		PostDto postDto = mapper.map(post, PostDto.class);
//		PostDto postDto = new PostDto();
//		postDto.setId(post.getId());
//		postDto.setDescription(post.getDescription());
//		postDto.setTitle(post.getTitle());
//		postDto.setContent(post.getContent());
		return postDto;
	}

	//converted Dto to entity
	private Post mapToEntity(PostDto postDto) {
		
		Post post = mapper.map(postDto, Post.class);
//		Post post = new Post();
//		post.setTitle(postDto.getTitle());
//		post.setDescription(postDto.getDescription());
//		post.setContent(postDto.getContent());
		return post;
	}

	@Override
	public PostDto getPostById(Long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
		return mapToDTO(post);
		
	}

	@Override
	public PostDto updatePost(PostDto postDto, Long id) {

		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
		post.setTitle(postDto.getTitle());
		post.setDescription(postDto.getDescription());
		post.setContent(postDto.getContent());
		
		Post updatePost = postRepository.save(post);
		return mapToDTO(updatePost);
	}

	@Override
	public void deletePostById(long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
		postRepository.delete(post);
		
	}

}
