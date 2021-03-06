package com.chahan.blog.controller;

import com.chahan.blog.model.dto.CreateCommentDto;
import com.chahan.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public void createComment(@Valid @RequestBody CreateCommentDto request,
                              @PathVariable Long postId) {
        commentService.createComment(request, postId);
    }

    @PutMapping("/comments/{commentId}")
    public void updateComment(@Valid @RequestBody CreateCommentDto request,
                              @PathVariable Long commentId) {
        commentService.updateComment(request, commentId);
    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

    @PostMapping("/comments/{commentId}/replies")
    public void createCommentReply(@Valid @RequestBody CreateCommentDto request,
                                   @PathVariable Long commentId) {
        commentService.createCommentReply(request, commentId);
    }

    @PostMapping("/comments/{id}/likes")
    public void addLike(@PathVariable Long id) {
        commentService.addLike(id);
    }

    @DeleteMapping("/comments/{id}/likes")
    public void deleteLike(@PathVariable Long id) {
        commentService.deleteLike(id);
    }
}
