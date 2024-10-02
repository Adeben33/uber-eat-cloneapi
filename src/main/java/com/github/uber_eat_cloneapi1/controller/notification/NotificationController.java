package com.github.uber_eat_cloneapi1.controller.notification;


import com.github.uber_eat_cloneapi1.dto.request.NotificationDTO;
import com.github.uber_eat_cloneapi1.dto.request.NotificationPreferencesDTO;
import com.github.uber_eat_cloneapi1.dto.request.PushNotificationDTO;
import org.springframework.web.bind.annotation.*;


@RestController
public class NotificationController {

    @GetMapping("/notifications")
    public String getNotifications() {
        return "List of all notifications for the user.";
    }

    @PostMapping("/notifications/mark-as-read")
    public String markNotificationsAsRead(@RequestBody NotificationDTO notificationDTO) {
        return "Notifications marked as read.";
    }

    @PostMapping("/notifications/send")
    public String sendNotificationToUser(@RequestBody NotificationDTO notificationDTO) {
        return "Notification sent to user with ID: " + notificationDTO.getUserId();
    }

    @PostMapping("/notifications/broadcast")
    public String sendBroadcastNotification() {
        return "Broadcast notification sent to all users.";
    }

    @PostMapping("/notifications/{notificationId}/mark-as-read")
    public String markNotificationAsRead(@PathVariable String notificationId) {
        return "Notification with ID: " + notificationId + " marked as read.";
    }


    @PostMapping("/notifications/mark-all-as-read")
    public String markAllNotificationsAsRead() {
        return "All notifications marked as read.";
    }

    @GetMapping("/notifications/unread")
    public String getUnreadNotifications() {
        return "List of unread notifications for the user.";
    }

    @DeleteMapping("/notifications/{notificationId}")
    public String deleteNotification(@PathVariable String notificationId) {
        return "Notification with ID: " + notificationId + " deleted.";
    }


    @DeleteMapping("/notifications/delete-all")
    public String deleteAllNotifications() {
        return "All notifications deleted for the user.";
    }

    @PutMapping("/notifications/preferences")
    public String updateNotificationPreferences(@RequestBody NotificationPreferencesDTO preferencesDTO) {
        return "Notification preferences updated.";
    }

    @GetMapping("/notifications/preferences")
    public String getNotificationPreferences() {
        return "Notification preferences for the user.";
    }


    @PostMapping("/notifications/push/enable")
    public String enablePushNotifications(@RequestBody PushNotificationDTO pushNotificationDTO) {
        return "Push notifications enabled for device: " + pushNotificationDTO.getDeviceId();
    }


    @PostMapping("/notifications/push/disable")
    public String disablePushNotifications(@RequestBody PushNotificationDTO pushNotificationDTO) {
        return "Push notifications disabled for device: " + pushNotificationDTO.getDeviceId();
    }

    @GetMapping("/notifications/promotions")
    public String getPromotionalNotifications() {
        return "List of promotional notifications for the user.";
    }

    @GetMapping("/notifications/orders")
    public String getOrderRelatedNotifications() {
        return "List of order-related notifications for the user.";
    }

    @GetMapping("/notifications/type/{type}")
    public String getNotificationsByType(@PathVariable String type) {
        return "List of notifications of type: " + type;
    }

    @GetMapping("/notifications/history")
    public String getNotificationHistory() {
        return "Notification history for the user.";
    }

    @PostMapping("/notifications/{notificationId}/resend")
    public String resendNotification(@PathVariable String notificationId) {
        return "Notification with ID: " + notificationId + " resent to the user.";
    }

    @PostMapping("/notifications/clear")
    public String clearAllNotifications() {
        return "All notifications cleared from the user's interface.";
    }

}
