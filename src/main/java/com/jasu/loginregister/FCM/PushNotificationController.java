package com.jasu.loginregister.FCM;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class PushNotificationController {


    private PushNotificationService pushNotificationService;

    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @PostMapping("/token")
    @Secured("USER")
    public ResponseEntity sendTokenNotification(@RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationToToken(request);
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

    @PostMapping("/subscribe")
    public void subscribeToTopic(@RequestBody SubscriptionRequest subscriptionRequest) {
        pushNotificationService.subscribeToTopic(subscriptionRequest);
    }

    @PostMapping("/unsubscribe")
    public void unsubscribeFromTopic(SubscriptionRequest subscriptionRequest) {
        pushNotificationService.unsubscribeFromTopic(subscriptionRequest);
    }

    @PostMapping("/token")
    public String sendPnsToDevice(@RequestBody NotificationRequest notificationRequest) {
        return pushNotificationService.sendPnsToDevice(notificationRequest);
    }

    @PostMapping("/topic")
    public String sendPnsToTopic(@RequestBody NotificationRequest notificationRequest) {
        return pushNotificationService.sendPnsToTopic(notificationRequest);
    }
}
