package com.inventoryManagementSystem.backend.controller;

import com.inventoryManagementSystem.backend.entity.PurchaseOrder;
import com.inventoryManagementSystem.backend.entity.Review;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/review")
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping
    public ResponseEntity<CommonResponse<List<Review>>> getAllReviews() {
        return new ResponseEntity<>(reviewService.getAllReviews(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommonResponse<String>> addReview(@RequestBody String review) {
        return new ResponseEntity<>(reviewService.addReview(review), HttpStatus.OK);
    }
}
