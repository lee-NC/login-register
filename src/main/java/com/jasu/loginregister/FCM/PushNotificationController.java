package com.jasu.loginregister.FCM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class PushNotificationController {


    @Autowired
    private PushNotificationService pushNotificationService;

    @PostMapping("/token")
    @Secured("USER")
    public ResponseEntity sendMultipleNotification(@RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationToToken(request);
        return ResponseEntity.ok("Notification has been sent.");
    }

    @PostMapping("/subscribe")
    public void subscribeToTopic(@RequestBody SubscriptionOneRequest subscriptionOneRequest) {
        pushNotificationService.subscribeToTopic(subscriptionOneRequest);
    }

    @PostMapping("/unsubscribe")
    public void unsubscribeFromTopic(SubscriptionOneRequest subscriptionOneRequest) {
        pushNotificationService.unsubscribeFromTopic(subscriptionOneRequest);
    }

    @PostMapping("/apple_token")
    public String sendPnsToDevice(@RequestBody PushOneNotificationRequest pushOneNotificationRequest) {
        return pushNotificationService.sendPnsToDevice(pushOneNotificationRequest);
    }

    @PostMapping("/topic")
    public String sendPnsToTopic(@RequestBody PushOneNotificationRequest pushOneNotificationRequest) {
        return pushNotificationService.sendPnsToTopic(pushOneNotificationRequest);
    }
}
