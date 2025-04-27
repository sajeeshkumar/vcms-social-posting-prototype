package ca.kodingkrafters.inc.vcms.backend.controller;

import ca.kodingkrafters.inc.vcms.backend.api.PostsApi;
import ca.kodingkrafters.inc.vcms.backend.model.PostRequest;
import ca.kodingkrafters.inc.vcms.backend.service.posts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController implements PostsApi {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Override
    public ResponseEntity postsPost(PostRequest postRequest) {
        postService.createPost(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
