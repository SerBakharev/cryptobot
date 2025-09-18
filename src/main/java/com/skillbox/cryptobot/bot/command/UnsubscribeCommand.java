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

/**
 * Обработка команды отмены подписки на курс валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class UnsubscribeCommand implements IBotCommand {

    private final DatabaseSubscriberService databaseSubscriberService;


    @Override
    public String getCommandIdentifier() {
        return "unsubscribe";
    }

    @Override
    public String getDescription() {
        return "Отменяет подписку пользователя";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        Subscriber currentSubscriber = databaseSubscriberService.findByTelegramId(message.getChatId());
        try {
            if(currentSubscriber.getPrice() == null) {
                answer.setText("Активные подписки отсутствуют");
                absSender.execute(answer);
            } else {
                databaseSubscriberService.updatePrice(message.getChatId(), null);
                answer.setText("Подписка отменена");
                absSender.execute(answer);
            }

        } catch (Exception e) {
            log.error("Error occurred in /unsubscribe command", e);
        }

    }
}