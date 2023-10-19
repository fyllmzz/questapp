package com.project.questapp.controllers;

import com.project.questapp.entities.Comment;
import com.project.questapp.entities.Like;
import com.project.questapp.request.CommentCreateRequest;
import com.project.questapp.request.CommentUpdateRequest;
import com.project.questapp.request.LikeCreateRequest;
import com.project.questapp.request.LikeUpdateRequest;
import com.project.questapp.services.LikeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/like")
public class LikeController {
    LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping
    public List<Like> getAllLike(@RequestParam Optional<Long> userId, @RequestParam Optional<Long> postId){
        return likeService.getAllLikeWithParam(userId,postId);
    }

    @GetMapping("/{likeId}")
    public Like getOneLike(@PathVariable Long likeId){
        return likeService.getOneLikeById(likeId);
    }

    //create
    @PostMapping
    public  Like createOneLike(@RequestParam LikeCreateRequest likeCreateRequest ){
        return likeService.createOneLikeById(likeCreateRequest);
    }

    //update
    @PutMapping("/{likeId}")
    public Like updateOneComment(@PathVariable Long likeId, @RequestBody LikeUpdateRequest likeUpdateRequest){
        return likeService.updateOneLikeById(likeId,likeUpdateRequest);
    }

    //delete
    @DeleteMapping("/{likeId}")
    public void deleteOneLike(@PathVariable Long likeId){
        likeService.deleteOneLikeById(likeId);
    }

}
