package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.entity.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseSubscriberService {

    private final SubscriberRepository repository;

    public List<Subscriber> findAll() {
        return repository.findAll();
    }


    public Subscriber save(Subscriber subscriber) {
        return repository.save(subscriber);
    }

    public Subscriber findByTelegramId(Long telegramId) {
        return repository.findByTelegramId(telegramId);
    }

    public Subscriber updatePrice(Long telegramId, Integer price) {
        Subscriber updatedSubscriber = repository.findByTelegramId(telegramId);
        updatedSubscriber.setPrice(price);
        return repository.save(updatedSubscriber);
    }

    public Subscriber updatePointOfNotification(Long telegramId, Long pointOfNotification) {
        Subscriber updatedSubscriber = repository.findByTelegramId(telegramId);
        updatedSubscriber.setPointOfNotification(pointOfNotification);
        return repository.save(updatedSubscriber);
    }
}
