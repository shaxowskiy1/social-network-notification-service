package ru.shaxowskiy.notificationservice

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication
import ru.shaxowskiy.notificationservice.service.TelegramBotService

@SpringBootApplication
class NotificationServiceApplication(
    private val telegramBotService: TelegramBotService,
    @Value("\${bot.token}") private val token: String
) {

    @Bean
    fun telegramBotsApplication() = TelegramBotsLongPollingApplication()

    @EventListener(ContextRefreshedEvent::class)
    fun init() {
        telegramBotsApplication().registerBot(token, telegramBotService)
    }
}

fun main(args: Array<String>) {
    runApplication<NotificationServiceApplication>(*args)
}
