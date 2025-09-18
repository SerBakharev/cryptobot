package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.entity.Subscriber;
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

@Service
@Slf4j
@AllArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {

    private final DatabaseSubscriberService databaseSubscriberService;

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        Subscriber currentSubscriber = databaseSubscriberService.findByTelegramId(message.getChatId());
        NumberFormat formatterRU = NumberFormat.getNumberInstance(new Locale("ru", "RU"));
        try {
            if(currentSubscriber.getPrice() == null) {
                answer.setText("Активные подписки отсутствуют");
                absSender.execute(answer);
            } else {
                String formattedNumberRU = formatterRU.format(currentSubscriber.getPrice());
                answer.setText("Вы подписаны на стоимость биткоина "  + formattedNumberRU + " USD");
                absSender.execute(answer);
            }

        } catch (Exception e) {
            log.error("Error occurred in /get_subscription command", e);
        }
    }
}