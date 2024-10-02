package com.github.uber_eat_cloneapi1.controller.feedback;

import com.github.uber_eat_cloneapi1.dto.request.FeedbackDTO;
import com.github.uber_eat_cloneapi1.dto.request.ReplyDTO;
import com.github.uber_eat_cloneapi1.dto.request.ResponseDTO;
import org.springframework.web.bind.annotation.*;


@RestController
public class FeedbackController {

    @PostMapping("/feedback/restaurant/{restaurantId}")
    public String submitRestaurantFeedback(@PathVariable String restaurantId, @RequestBody FeedbackDTO feedbackDTO) {
        return "Feedback submitted for restaurant with ID: " + restaurantId;
    }

    @PostMapping("/feedback/order/{orderId}")
    public String submitOrderFeedback(@PathVariable String orderId, @RequestBody FeedbackDTO feedbackDTO) {
        return "Feedback submitted for order with ID: " + orderId;
    }


    @PostMapping("/feedback/driver/{driverId}")
    public String submitDriverFeedback(@PathVariable String driverId, @RequestBody FeedbackDTO feedbackDTO) {
        return "Feedback submitted for driver with ID: " + driverId;
    }

    @PostMapping("/feedback/{feedbackId}/respond")
    public String respondToFeedback(@PathVariable String feedbackId, @RequestBody ResponseDTO responseDTO) {
        return "Response sent to feedback with ID: " + feedbackId;
    }

    @PostMapping("/feedback/{feedbackId}/flag")
    public String flagFeedbackForReview(@PathVariable String feedbackId) {
        return "Feedback with ID: " + feedbackId + " has been flagged for review.";
    }

    @GetMapping("/feedback/flagged")
    public String getFlaggedFeedbackForReview() {
        return "List of flagged feedback entries for review.";
    }

    @PostMapping("/feedback/{feedbackId}/resolve-flag")
    public String resolveFlaggedFeedback(@PathVariable String feedbackId) {
        return "Flag for feedback with ID: " + feedbackId + " has been resolved.";
    }

    @GetMapping("/feedback/statistics/restaurant/{restaurantId}")
    public String getRestaurantFeedbackStatistics(@PathVariable String restaurantId) {
        return "Feedback statistics for restaurant with ID: " + restaurantId + ": Average rating: 4.5, Total feedback: 250.";
    }


    @GetMapping("/feedback/statistics/driver/{driverId}")
    public String getDriverFeedbackStatistics(@PathVariable String driverId) {
        return "Feedback statistics for driver with ID: " + driverId + ": Average rating: 4.8, Total feedback: 120.";
    }

    @PostMapping("/feedback/{feedbackId}/reply")
    public String replyToFeedback(@PathVariable String feedbackId, @RequestBody ReplyDTO replyDTO) {
        return "Customer service reply sent to feedback with ID: " + feedbackId;
    }

    @GetMapping("/feedback/restaurant/{restaurantId}/recent")
    public String getRecentFeedbackForRestaurant(@PathVariable String restaurantId) {
        return "Most recent feedback for restaurant with ID: " + restaurantId;
    }

    @GetMapping("/feedback/driver/{driverId}/recent")
    public String getRecentFeedbackForDriver(@PathVariable String driverId) {
        return "Most recent feedback for driver with ID: " + driverId;
    }

    @GetMapping("/feedback/search")
    public String searchFeedbackByKeyword(@RequestParam String keyword) {
        return "Feedback entries mentioning keyword: " + keyword;
    }

    @GetMapping("/feedback/time-period")
    public String getFeedbackForTimePeriod(@RequestParam String startDate, @RequestParam String endDate) {
        return "Feedback entries from " + startDate + " to " + endDate;
    }

    @GetMapping("/feedback/sentiment/restaurant/{restaurantId}")
    public String getRestaurantFeedbackSentiment(@PathVariable String restaurantId) {
        return "Sentiment analysis for restaurant with ID: " + restaurantId + ": 70% positive, 20% neutral, 10% negative.";
    }

    @GetMapping("/feedback/restaurant/{restaurantId}/common-complaints")
    public String getCommonComplaintsForRestaurant(@PathVariable String restaurantId) {
        return "Most common complaints for restaurant with ID: " + restaurantId + ": Late delivery, wrong items.";
    }



    @GetMapping("/feedback/driver/{driverId}/common-compliments")
    public String getCommonComplimentsForDriver(@PathVariable String driverId) {
        return "Most common compliments for driver with ID: " + driverId + ": Fast delivery, polite.";
    }


    @PostMapping("/feedback/{feedbackId}/mark-helpful")
    public String markFeedbackAsHelpful(@PathVariable String feedbackId) {
        return "Feedback with ID: " + feedbackId + " marked as helpful.";
    }

    @GetMapping("/feedback/helpful")
    public String getHelpfulFeedback() {
        return "List of feedback entries marked as helpful by users.";
    }


}
