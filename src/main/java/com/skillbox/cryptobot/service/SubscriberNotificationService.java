package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.entity.Subscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriberNotificationService {

    private final DatabaseSubscriberService databaseSubscriberService;
    private final CryptoCurrencyService service;
    private final CryptoBot cryptoBot;
    private final CacheManager cacheManager;

    @Value("${telegram.bot.notify.delay.value:0}") // Значение Long из application.yml
    private Long delayValue;

    private int iteration = 0;

    @Scheduled(fixedRateString = "${telegram.bot.task.frequency}")
    public void performNotification() throws IOException, TelegramApiException {
        evictMyCache();
        iteration = iteration + 1;
        log.info("number of iteration " + iteration + " at " + LocalDateTime.now());
        AtomicReference<Double> currentPrice = new AtomicReference<>();
        currentPrice.set(service.getBitcoinPrice());
        List<Long> subscriberIds = new ArrayList<>();
        List<Subscriber> subscribers = databaseSubscriberService.findAll();
        if(!subscribers.isEmpty()) {
            Long timePoint = System.currentTimeMillis();
            for (Subscriber subscriber : subscribers) {
                if(subscriber.getPrice() != null) {
                    Long endPoint = subscriber.getPointOfNotification() + delayValue;
                    Long delta = endPoint - timePoint;
                    log.info("by subscriber " + subscriber.getPrice() + " delta is " + delta);
                    if((delta < 500) && (currentPrice.get() < subscriber.getPrice())) {

                        subscriberIds.add(subscriber.getTelegramId());
                    }
                }
            }
            for (Long subscriberId : subscriberIds) {
                cryptoBot.sendMessageToUser(subscriberId, "Пора покупать, стоимость биткоина " + currentPrice + " USD");
                log.info("notification was sent to " + subscriberId + " at " + LocalDateTime.now());
                databaseSubscriberService.updatePointOfNotification(subscriberId,timePoint);
            }
        }
    }

    @CacheEvict(value = "myCache", allEntries = true, condition = "#result != null")
    public void evictMyCache() {
        // Метод для очистки кэша, который не имеет аргументов, но требует обработки
        Objects.requireNonNull(cacheManager.getCache("myCache")).clear();
    }


}
