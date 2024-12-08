package com.inventoryManagementSystem.backend.service;


import com.inventoryManagementSystem.backend.entity.Review;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.entry.ResponseEnum;
import com.inventoryManagementSystem.backend.entry.StatusEntry;
import com.inventoryManagementSystem.backend.repository.ReviewRepository;
import com.inventoryManagementSystem.backend.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    public CommonResponse<List<Review>> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll(); // No sorting applied
        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY), reviews);
    }

    public CommonResponse<String> addReview(String message) {
        Review review = new Review();

        reviewRepository.save(review);
        return Utility.getResponse(new StatusEntry(ResponseEnum.INSERTED_SUCCESSFULLY), "Added successfully");
    }
}
