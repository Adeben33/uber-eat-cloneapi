package com.github.uber_eat_cloneapi1.controller.feedback;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


}
