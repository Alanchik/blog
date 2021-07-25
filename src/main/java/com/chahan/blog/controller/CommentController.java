package com.chahan.blog.controller;

import com.chahan.blog.dto.CreateCommentDto;
import com.chahan.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public void createComment(@Valid @RequestBody CreateCommentDto request
            , @PathVariable Long postId) {
        commentService.createComment(request, postId);
    }

    @PutMapping("/{commentId}")
    public void updateComment(@Valid @RequestBody CreateCommentDto request
            , @PathVariable Long postId, @PathVariable Long commentId) {
        commentService.updateComment(request, postId, commentId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
