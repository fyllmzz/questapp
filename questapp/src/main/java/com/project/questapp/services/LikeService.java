package com.project.questapp.services;

import com.project.questapp.entities.Comment;
import com.project.questapp.entities.Like;
import com.project.questapp.entities.Post;
import com.project.questapp.entities.User;
import com.project.questapp.repos.LikeRepository;
import com.project.questapp.request.CommentCreateRequest;
import com.project.questapp.request.CommentUpdateRequest;
import com.project.questapp.request.LikeCreateRequest;
import com.project.questapp.request.LikeUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    LikeRepository likeRepository;
    UserService userService;
    PostService postService;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public List<Like> getAllLikeWithParam(Optional<Long> userId, Optional<Long> postId) {
        if (userId.isPresent() && postId.isPresent()){
            return likeRepository.findByUserIdAndPostId(userId.get(),postId.get());
        }else if(userId.isPresent()){
            return likeRepository.findByUserId(userId.get());
        } else  if(postId.isPresent()){
            return likeRepository.findByPostId(postId.get());
        } else
            return likeRepository.findAll();
    }
    public Like getOneLikeById(Long liketId) {
        return likeRepository.findById(liketId).orElse(null);
    }

    public Like createOneLikeById(LikeCreateRequest likeCreateRequest) {
        User user=  userService.getOneUserById(likeCreateRequest.getUserId());
        Post post=postService.getOnePostById(likeCreateRequest.getPostId());

        if(user == null || post == null){
            return null;
        }
        Like toSave=new Like();
        toSave.setId(likeCreateRequest.getId());
        toSave.setPost(post);
        toSave.setUser(user);
        return likeRepository.save(toSave);

    }
    public Like updateOneLikeById(Long likeId, LikeUpdateRequest likeUpdateRequest) {
        Optional<Like> like=likeRepository.findById(likeId);
        if(like.isPresent()){
           // List likeToUpdate=like.get();
           // likeToUpdate.setText(likeToUpdate.getText());

          //  return likeRepository.save(likeToUpdate);
            return null;
        }else
            return null;
    }
    public void deleteOneLikeById(Long likeId){
        likeRepository.deleteById(likeId);
    }
}
