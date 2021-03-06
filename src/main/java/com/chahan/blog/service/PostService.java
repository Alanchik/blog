package com.chahan.blog.service;

import com.chahan.blog.exception.BadRequestApiException;
import com.chahan.blog.mapper.PostMapper;
import com.chahan.blog.model.dto.CreatePostDto;
import com.chahan.blog.model.dto.PostDto;
import com.chahan.blog.model.entity.Blogger;
import com.chahan.blog.model.entity.BloggerDetails;
import com.chahan.blog.model.entity.Post;
import com.chahan.blog.repository.PostRepository;
import com.chahan.blog.util.AuthUtils;
import com.chahan.blog.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.chahan.blog.util.CommonUtils.ERROR_INCORRECT_ID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final BloggerService bloggerService;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final Validator validator;

    public Post getById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new BadRequestApiException(ERROR_INCORRECT_ID));
    }

    public void createPost(CreatePostDto postDto) {
        BloggerDetails blogger = AuthUtils.getCurrentBlogger();
        Post post = new Post();
        post.setAuthor(bloggerService.getBlogger(blogger.getId()));
        post.setDescription(postDto.getDescription());
        post.setPublished(LocalDateTime.now());
        postRepository.save(post);
    }

    public void updatePost(CreatePostDto request, Long postId) {
        Post post = getById(postId);
        validator.validatePostAccess(post);
        post.setDescription(request.getDescription());
        postRepository.save(post);
    }

    public void deletePost(Long postId) {
        Post post = getById(postId);
        validator.validatePostAccess(post);
        postRepository.deleteById(postId);
    }


    public List<PostDto> getBloggersPosts() {
        BloggerDetails blogger = AuthUtils.getCurrentBlogger();
        return postMapper.map(postRepository.getByAuthorId(blogger.getId()));
    }

    public List<PostDto> getSubscriptionsPosts(Pageable pageable) {
        Set<Long> subscriptions = bloggerService.getCurrentSubscriptions();
        List<Post> posts = postRepository.getByAuthorIdInOrderByPublishedDesc(subscriptions, pageable);
        return postMapper.map(posts);
    }

    public void addLike(Long postId) {
        BloggerDetails blogger = AuthUtils.getCurrentBlogger();
        Blogger currentBlogger = bloggerService.getBlogger(blogger.getId());
        Post post = postRepository.getById(postId);
        post.getBloggerLikes().add(currentBlogger);
        postRepository.save(post);
    }

    public void deleteLike(Long postId) {
        validator.validatePostExists(postId);
        BloggerDetails blogger = AuthUtils.getCurrentBlogger();
        Blogger currentBlogger = bloggerService.getBlogger(blogger.getId());
        Post post = postRepository.getById(postId);
        post.getBloggerLikes().remove(currentBlogger);
        postRepository.save(post);
    }
}
