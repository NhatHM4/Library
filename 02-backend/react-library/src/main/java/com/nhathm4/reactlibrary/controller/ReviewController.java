package com.nhathm4.reactlibrary.controller;

import com.nhathm4.reactlibrary.requestmodels.ReviewRequest;
import com.nhathm4.reactlibrary.service.ReviewService;
import com.nhathm4.reactlibrary.utils.ExtractJWT;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @PostMapping("/secure")
    public void postReview(@RequestHeader(value = "Authorization") String token,
                           @RequestBody ReviewRequest reviewRequest) throws  Exception{
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        if (userEmail == null){
            throw new Exception(" User email not exist");
        }
        reviewService.postReview(userEmail,reviewRequest);
    }

    @GetMapping("/secure/user/book")
    public Boolean reviewBookUser(@RequestHeader(value = "Authorization") String token,
                                  @RequestParam Long bookId){
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
       return reviewService.userReviewListed(userEmail, bookId);
    }


}
