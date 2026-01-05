package ru.shaxowskiy.notificationservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.generics.TelegramClient
import java.util.logging.Logger

@Component
class BotConfig(
    @Value("\${bot.token}") private val token: String
) : LongPollingSingleThreadUpdateConsumer {

    private val telegramClient: TelegramClient = OkHttpTelegramClient(token)


    private val logger = Logger.getLogger(this::class.java.name)

    override fun consume(update: Update?) {
        if (update?.hasMessage() == true && update.message.hasText()) {
            val text = update.message.text;
            val chatId = update.message.chatId
            logger.info("This message is $text")
            val message = SendMessage
                .builder()
                .chatId(chatId)
                .text(text)
                .build()

            try {
                telegramClient.execute(message)
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }

        }
    }

}
