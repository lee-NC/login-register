package com.jasu.loginregister.FCM;

import lombok.Data;

import java.util.List;

@Data
public class SubscriptionOneRequest {

    String topicName;
    List<String> tokens;
}
