package com.skillbox.cryptobot.repository;

import com.skillbox.cryptobot.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {

    Subscriber findByTelegramId(Long telegramId);
}
