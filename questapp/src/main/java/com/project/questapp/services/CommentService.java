package com.project.questapp.services;

import com.project.questapp.entities.Comment;
import com.project.questapp.entities.Post;
import com.project.questapp.entities.User;
import com.project.questapp.repos.CommentRepository;
import com.project.questapp.repos.UserRepository;
import com.project.questapp.request.CommentCreateRequest;
import com.project.questapp.request.CommentUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    CommentRepository commentRepository;
    UserService userService;
    PostService postService;

    public CommentService(CommentRepository commentRepository, UserService userService, PostService postService) {
        this.commentRepository = commentRepository;
        this.userService=userService;
        this.postService=postService;
    }

    public List<Comment> getAllCommentWithParam(Optional<Long> userId, Optional<Long> postId) {
        if (userId.isPresent() && postId.isPresent()){
            return commentRepository.findByUserIdAndPostId(userId.get(),postId.get());
        }else if(userId.isPresent()){
            return commentRepository.findByUserId(userId.get());
        } else  if(postId.isPresent()){
            return commentRepository.findByPostId(postId.get());
        } else
            return commentRepository.findAll();
    }

    public Comment getOneCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public Comment createOneCommentById(CommentCreateRequest commentCreateRequest) {
        User user=  userService.getOneUserById(commentCreateRequest.getUserId());
        Post post=postService.getOnePostById(commentCreateRequest.getPostId());

        if(user == null || post == null){
            return null;
        }
        Comment toSave=new Comment();
        toSave.setId(commentCreateRequest.getId());
        toSave.setText(commentCreateRequest.getText());
        toSave.setPost(post);
        toSave.setUser(user);
        return commentRepository.save(toSave);

    }

    public void deleteOneCommentById(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Comment updateOneCommentById(Long commentId, CommentUpdateRequest commentUpdateRequest) {
        Optional<Comment> comment=commentRepository.findById(commentId);
        if(comment.isPresent()){
            Comment commenttoUpdate=comment.get();
            commenttoUpdate.setText(commentUpdateRequest.getText());

            return commentRepository.save(commenttoUpdate);
        }else
            return null;
    }
}
