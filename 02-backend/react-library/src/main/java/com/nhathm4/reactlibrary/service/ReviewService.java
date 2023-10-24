package com.nhathm4.reactlibrary.service;

import com.nhathm4.reactlibrary.dao.BookRepository;
import com.nhathm4.reactlibrary.dao.ReviewRepository;
import com.nhathm4.reactlibrary.entity.Review;
import com.nhathm4.reactlibrary.requestmodels.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@Transactional
public class ReviewService {

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    public void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception{
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, reviewRequest.getBookId());

        if (validateReview != null){
            throw new Exception("It was exists Review");
        }
        Review review = new Review();
        review.setBookId(reviewRequest.getBookId());

        review.setDate(Date.valueOf(LocalDate.now()));
        review.setRating(reviewRequest.getRating());
        review.setUserEmail(userEmail);

        if (reviewRequest.getReviewDescription().isPresent()){
            review.setReviewDecription(reviewRequest.getReviewDescription().map(
                    Object::toString
            ).orElse(null));
        }
        reviewRepository.save(review);
    }

    public boolean userReviewListed(String userEmail, Long bookId){
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, bookId);
        if (validateReview != null){
            return true;
        }
        return false;
    }
}
