package com.ShoppingApp.NotificationService;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    @Observed(
            name = "user.name",
            lowCardinalityKeyValues = {
                    "userType", "userType2"
            }
    )
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        // we can write the logic to send out email notifications

        // we will use the Log
        log.info("Received Notification for Order - {}", orderPlacedEvent.getOrderNumber());
    }
}
