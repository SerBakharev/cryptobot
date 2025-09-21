package com.skillbox.cryptobot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "subscribers")
public class Subscriber {

    @Id
    private UUID id;

    private Long telegramId;

    private Integer price;

    private Long pointOfNotification;
}
