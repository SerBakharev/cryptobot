package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.entity.Subscriber;
import com.skillbox.cryptobot.service.DatabaseSubscriberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.UUID;


/**
 * Обработка команды начала работы с ботом
 */
@Service
@AllArgsConstructor
@Slf4j
public class StartCommand implements IBotCommand {

    private final DatabaseSubscriberService databaseSubscriberService;

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Запускает бота";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());

        answer.setText("""
                Привет! Данный бот помогает отслеживать стоимость биткоина.
                Поддерживаемые команды:
                 /get_price - получить стоимость биткоина,
                 /subscribe [целое число без пробелов] - подписаться на стоимость биткоина в USD,
                 /get_subscription - получить текущую подписку,
                 /unsubscribe - отменить подписку на стоимость
                """);
        try {
            absSender.execute(answer);
            Subscriber subscriber = new Subscriber();
            subscriber.setId(UUID.randomUUID());
            subscriber.setTelegramId(message.getChatId());
            subscriber.setPointOfNotification(1L);
            databaseSubscriberService.save(subscriber);

        } catch (TelegramApiException e) {
            log.error("Error occurred in /start command", e);
        }
    }
}