package com.microservice.post.service;

import com.microservice.post.payload.PostDto;

public interface PostService {
    PostDto createPost(PostDto postDto);

    PostDto getPostByPostId(String postId);

    PostDto getPostWithComments(String postId);
}
