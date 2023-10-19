package com.project.questapp.controllers;

import com.project.questapp.entities.Post;
import com.project.questapp.entities.User;
import com.project.questapp.request.PostCreateRequest;
import com.project.questapp.request.PostUpdateRequest;
import com.project.questapp.services.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //@RequestParams requestin request parçalar içindeki parametreyi alır.
    @GetMapping
    public List<Post> getAllPosts(@RequestParam Optional<Long> userId){
        return postService.getAllPosts(userId);
    }

    //@Pathvariable pathdeki parametreyi alır.
    @GetMapping("/{userId}")
    public Post getOnePost(@PathVariable Long postId){
        return  postService.getOnePostById(postId);
    }

    @PostMapping
    public Post createOnePost(@RequestBody PostCreateRequest newPostRequest){
        return postService.createOnePost(newPostRequest);
    }

    @PutMapping("/{postId}")
    public Post updateOnePost(@PathVariable Long postId,@RequestBody PostUpdateRequest updateRequest){
      return  postService.updateOnePostById(postId, updateRequest);

    }

    @DeleteMapping("{postId}")
    public  void deleteOnePost(@PathVariable Long postId){
        postService.deleteOnePostById(postId);
    }

}
