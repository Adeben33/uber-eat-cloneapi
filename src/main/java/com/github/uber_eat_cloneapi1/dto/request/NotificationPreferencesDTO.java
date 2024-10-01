package com.github.uber_eat_cloneapi1.dto.request;

import lombok.Data;

@Data
public class NotificationPreferencesDTO {
    private Boolean emailNotifications;
    private Boolean smsNotifications;
    private Boolean pushNotifications;
}
