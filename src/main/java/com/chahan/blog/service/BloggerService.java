package com.chahan.blog.service;

import com.chahan.blog.exception.BadRequestApiException;
import com.chahan.blog.model.entity.Blogger;
import com.chahan.blog.model.entity.BloggerDetails;
import com.chahan.blog.repository.BloggerRepository;
import com.chahan.blog.util.AuthUtils;
import com.chahan.blog.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static com.chahan.blog.util.CommonUtils.ERROR_INCORRECT_ID;
import static com.chahan.blog.util.CommonUtils.ERROR_USER_FOLLOW_YOURSELF;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class BloggerService {

    private final BloggerRepository bloggerRepository;
    private final Validator validator;

    public void saveNewBlogger(String username, String password) {
        Blogger blogger = new Blogger();
        blogger.setUsername(username);
        blogger.setPassword(password);
        bloggerRepository.save(blogger);
    }

    public Blogger getBlogger(String username) {
        return bloggerRepository.getByUsername(username);
    }

    public Blogger getBlogger(Long id) {
        return bloggerRepository.findById(id)
                .orElseThrow(() -> new BadRequestApiException(ERROR_INCORRECT_ID));
    }

    public void follow(Long bloggerId) {
        validator.validateBloggerExists(bloggerId);
        BloggerDetails blogger = AuthUtils.getCurrentBlogger();
        if (bloggerId.equals(blogger.getId())) {
            throw new BadRequestApiException(ERROR_USER_FOLLOW_YOURSELF);
        }
        Blogger currentBlogger = bloggerRepository.getById(blogger.getId());
        Blogger subscribeBlogger = bloggerRepository.getById(bloggerId);
        currentBlogger.getSubscriptions().add(subscribeBlogger);
        bloggerRepository.save(currentBlogger);

    }

    public void unfollow(Long bloggerId) {
        validator.validateBloggerExists(bloggerId);
        BloggerDetails blogger = AuthUtils.getCurrentBlogger();
        Blogger currentBlogger = bloggerRepository.getById(blogger.getId());
        currentBlogger.getSubscriptions().removeIf(subscription -> subscription.getId().equals(bloggerId));
        bloggerRepository.save(currentBlogger);
    }

    public Set<Long> getCurrentSubscriptions() {
        BloggerDetails blogger = AuthUtils.getCurrentBlogger();
        return getSubscriptions(blogger.getId());
    }

    public Set<Long> getSubscriptions(Long bloggerId) {
        Blogger currentBlogger = bloggerRepository.getById(bloggerId);
        return currentBlogger.getSubscriptions().stream()
                .map(Blogger::getId)
                .collect(toSet());
    }

    public Set<Long> getCurrentSubscribers() {
        BloggerDetails blogger = AuthUtils.getCurrentBlogger();
        Blogger currentBlogger = bloggerRepository.getById(blogger.getId());
        return currentBlogger.getSubscribers().stream()
                .map(Blogger::getId)
                .collect(Collectors.toSet());
    }
}
