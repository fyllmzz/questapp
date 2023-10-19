package com.project.questapp.services;

import com.project.questapp.entities.Post;
import com.project.questapp.entities.User;
import com.project.questapp.repos.PostRepository;
import com.project.questapp.request.PostCreateRequest;
import com.project.questapp.request.PostUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    PostRepository postRepository;

    UserService userService;

    //Generate constructor
    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService=userService;
    }

    public List<Post> getAllPosts(Optional<Long> userId) {
        if(userId.isPresent())//ispresent ifadesi, userıd parmetresi bana geldiyse demek.
        {
            return postRepository.findByUserId(userId);
        }else {
            return postRepository.findAll();
        }
    }

    public Post getOnePostById(Long postId) {
        return  postRepository.findById(postId).orElse(null);
    }

    public Post createOnePost(PostCreateRequest newPostCreateRequest) {

       User user=  userService.getOneUserById(newPostCreateRequest.getUserId());
       if(user == null){
           return null;
       }
       Post toSave=new Post();
       toSave.setId(newPostCreateRequest.getId());
       toSave.setText(newPostCreateRequest.getText());
       toSave.setTitle(newPostCreateRequest.getTitle());
       toSave.setUser(user);
        return postRepository.save(toSave);
    }

    public Post updateOnePostById(Long postId, PostUpdateRequest postUpdateRequest) {
       Optional<Post> post=postRepository.findById(postId);
       if (post.isPresent()){
           Post toUpdate=post.get();
           toUpdate.setText(postUpdateRequest.getText());
           toUpdate.setTitle(postUpdateRequest.getTitle());
           postRepository.save(toUpdate);
           return toUpdate;
       }
           return null;

    }

    public void deleteOnePostById(Long postId) {
       postRepository.deleteById(postId);
    }
}
