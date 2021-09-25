package com.jasu.loginregister.FCM;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PushNotificationService {

    private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    private FCMService fcmService;

    private FirebaseApp firebaseApp;

    public PushNotificationService(FCMService fcmService) {
        this.fcmService = fcmService;
    }


    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void subscribeToTopic(SubscriptionOneRequest subscriptionOneRequest) {
        try {
            FirebaseMessaging.getInstance(firebaseApp).subscribeToTopic(subscriptionOneRequest.getTokens(),
                    subscriptionOneRequest.getTopicName());
        } catch (FirebaseMessagingException e) {
            log.error("Firebase subscribe to topic fail", e);
        }
    }

    public void unsubscribeFromTopic(SubscriptionOneRequest subscriptionOneRequest) {
        try {
            FirebaseMessaging.getInstance(firebaseApp).unsubscribeFromTopic(subscriptionOneRequest.getTokens(),
                    subscriptionOneRequest.getTopicName());
        } catch (FirebaseMessagingException e) {
            log.error("Firebase unsubscribe from topic fail", e);
        }
    }

    public String sendPnsToDevice(PushOneNotificationRequest pushOneNotificationRequest) {
        Message message = Message.builder()
                .setToken(pushOneNotificationRequest.getTarget())
                .setNotification(new Notification(pushOneNotificationRequest.getTitle(), pushOneNotificationRequest.getBody()))
                .putData("content", pushOneNotificationRequest.getTitle())
                .putData("body", pushOneNotificationRequest.getBody())
                .build();

        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Fail to send firebase notification", e);
        }

        return response;
    }

    public String sendPnsToTopic(PushOneNotificationRequest pushOneNotificationRequest) {
        Message message = Message.builder()
                .setTopic(pushOneNotificationRequest.getTarget())
                .setNotification(new Notification(pushOneNotificationRequest.getTitle(), pushOneNotificationRequest.getBody()))
                .putData("content", pushOneNotificationRequest.getTitle())
                .putData("body", pushOneNotificationRequest.getBody())
                .build();

        String response = null;
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Fail to send firebase notification", e);
        }

        return response;
    }

}
