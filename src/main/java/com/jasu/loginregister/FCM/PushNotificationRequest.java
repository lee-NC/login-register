package com.jasu.loginregister.FCM;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PushNotificationRequest {
    private String title;
    private String message;
    private String topic;
    private String token;
}
