package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.service.DatabaseSubscriberService;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@AllArgsConstructor
@Slf4j
public class SubscribeCommand implements IBotCommand {

    private final CryptoCurrencyService service;
    private final DatabaseSubscriberService databaseSubscriberService;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        SendMessage secondAnswer = new SendMessage();
        answer.setChatId(message.getChatId());
        secondAnswer.setChatId(message.getChatId());
        Integer requestedPrice = Integer.parseInt(arguments[0]);
        NumberFormat formatterRU = NumberFormat.getNumberInstance(new Locale("ru", "RU"));
        String formattedNumberRU = formatterRU.format(requestedPrice);
        try {
            answer.setText("Текущая цена биткоина " + TextUtil.toString(service.getBitcoinPrice()) + " USD");
            absSender.execute(answer);
            secondAnswer.setText("Новая подписка создана на стоимость " + formattedNumberRU + " USD");
            absSender.execute(secondAnswer);
            databaseSubscriberService.updatePrice(message.getChatId(), requestedPrice);
        } catch (Exception e) {
            log.error("Error occurred in /subscribe command", e);
        }
    }
}