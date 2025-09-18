package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.entity.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseSubscriberService {

    private final SubscriberRepository repository;

    public Subscriber save(Subscriber subscriber) {
        return repository.save(subscriber);
    }
}
