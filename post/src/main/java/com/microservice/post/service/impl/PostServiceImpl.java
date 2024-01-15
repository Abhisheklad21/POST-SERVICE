package com.microservice.post.service.impl;

import com.microservice.post.config.RestTemplateConfig;
import com.microservice.post.entity.Post;

import com.microservice.post.exception.ResourceNotFoundException;
import com.microservice.post.payload.Comment;
import com.microservice.post.payload.PostDto;
import com.microservice.post.repository.PostRepository;
import com.microservice.post.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private RestTemplateConfig restTemplate;

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = mapToEntity(postDto);
        String postId = UUID.randomUUID().toString();
        post.setId(postId);
        Post savedPost = postRepository.save(post);
        return mapToDto(savedPost);

    }

    @Override
    public PostDto getPostByPostId(String postId) {
        Post byId = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post is not found by PostID: " + postId)
        );
        PostDto postDto = mapToDto(byId);
        return postDto;
    }

    @Override
    public PostDto getPostWithComments(String postId) {
        ArrayList<Comment> comments = restTemplate.getRestTemplate().getForObject("http://COMMENT-SERVICE/api/comments/" + postId, ArrayList.class);
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post is not found by PostID: " + postId)

        );
        PostDto postDto = new PostDto();
        postDto = mapToDto(post);
        postDto.setComments(comments);
        System.out.println(postDto);
        return postDto;

    }

    Post mapToEntity(PostDto postDto) {
        Post dto = modelMapper.map(postDto, Post.class);
        return dto;
    }

    PostDto mapToDto(Post post) {
        PostDto postDto = modelMapper.map(post, PostDto.class);
        return postDto;
    }


}
