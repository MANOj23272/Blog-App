package com.springboot.blog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService{

	private CommentRepository commentRepository;
	private PostRepository postRepository;
	private ModelMapper mapper;
	public CommentServiceImpl(CommentRepository commentRepository ,PostRepository postRepository,ModelMapper mapper) {
		super();
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.mapper= mapper;
	}

	@Override
	public CommentDto createComment(long postId, CommentDto commentDto) {
		
		Comment comment = mapToEntity(commentDto);
		Post post= postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

		//set post to comment entity
		comment.setPost(post);
		
		// save 
		Comment newComment = commentRepository.save(comment);
		return mapToDTO(newComment);	
	}

	private CommentDto mapToDTO(Comment comment) {
		CommentDto commentDto = mapper.map(comment, CommentDto.class);
		
//		CommentDto commentDto = new  CommentDto();
//		commentDto.setId(comment.getId());
//		commentDto.setName(comment.getName());
//		commentDto.setEmail(comment.getEmail());
//		commentDto.setBody(comment.getBody());
		return commentDto;
		
	}
	
private Comment mapToEntity(CommentDto commentDto) {
		Comment comment = mapper.map(commentDto, Comment.class);
//		Comment comment = new  Comment();
//		comment.setId(commentDto.getId());
//		comment.setName(commentDto.getName());
//		comment.setEmail(commentDto.getEmail());
//		comment.setBody(commentDto.getBody());
		return comment;
		
	}

@Override
public List<CommentDto> getCommentsByPostId(long postId) {
	//retrive comments by post id
			List<Comment> comments= commentRepository.findByPostId(postId);
			//convert list of comments entity to list of comment dto
			return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
		
	}

	@Override
	public CommentDto getCommentById(long postId, long commentId) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
		
		if(!comment.getPost().getId().equals(post.getId())) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "comments does not exist in the post");
		}
		
		return mapToDTO(comment);
	}

	@Override
	public CommentDto updateComment(long postId, long commentId, CommentDto commentRequest) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
		
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
		if(!comment.getPost().getId().equals(post.getId())) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "comments does not belongs to post");
		}
		comment.setName(commentRequest.getName());
		comment.setEmail(commentRequest.getEmail());
		comment.setBody(commentRequest.getBody());
		
		Comment updateComment = commentRepository.save(comment);
		return mapToDTO(updateComment);
	}

	@Override
	public void deleteComment(Long postId, long commentId) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
		
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
		if(!comment.getPost().getId().equals(post.getId())) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "comments does not belongs to post");
		}
		commentRepository.delete(comment);
	}
}
