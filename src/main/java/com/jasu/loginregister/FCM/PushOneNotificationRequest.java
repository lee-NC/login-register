package com.jasu.loginregister.FCM;

import lombok.Data;

@Data
public class PushOneNotificationRequest {

    private String target;
    private String title;
    private String body;
}
