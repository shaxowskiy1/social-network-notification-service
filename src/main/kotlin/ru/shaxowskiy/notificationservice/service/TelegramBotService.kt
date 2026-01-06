package ru.shaxowskiy.notificationservice.service

import io.github.oshai.kotlinlogging.KotlinLogging
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.generics.TelegramClient
import ru.shaxowskiy.notificationservice.repository.UserRepository

@Component
@Slf4j
class TelegramBotService(
    @Value("\${bot.token}") private val token: String,
    private val userRepository : UserRepository
) : LongPollingSingleThreadUpdateConsumer {

    private val log = KotlinLogging.logger("TelegramBotService")
    val greetingMessage = "Привет! Вы подписаны на рассылку об уведомлениях из нашей социальной сети :)"
    private val telegramClient: TelegramClient = OkHttpTelegramClient(token)

    override fun consume(update: Update?) {
        if (update?.hasMessage() == true && update.message.hasText() && update.message.text.equals("/start")) {
            val chatId = update.message.chatId
            val message = SendMessage
                .builder()
                .chatId(chatId)
                .text(greetingMessage)
                .build()

            insertChatId(chatId.toString(), update.message.from.userName)


            try {
                telegramClient.execute(message)
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }
        }
    }

    fun insertChatId(chatId : String, username: String){
        log.info { "Insert entity in db users" }
        //TODO handle exceptions
        userRepository.insert(chatId, username)
    }

    fun sendNotificationMessage(chatId: String, author: String){
        val message = SendMessage
            .builder()
            .chatId(chatId)
            .text("Человек на которого вы подписаны $author опубликовал(а) пост")
            .build()


        try {
            telegramClient.execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

}
