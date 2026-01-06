package ru.shaxowskiy.notificationservice.listeners

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import ru.shaxowskiy.notificationservice.dto.PostEventDto
import ru.shaxowskiy.notificationservice.repository.UserRepository
import ru.shaxowskiy.notificationservice.service.TelegramBotService

@Component
class PostEventListener(@Autowired var userRepository: UserRepository,
                        var telegramBotService: TelegramBotService) {

    @KafkaListener(topics = ["PUBLISHPOSTEVENT"], groupId = "notification-group")
    fun listen(message: PostEventDto){
        //TODO Multithreading handle events
        println("Получено событие ${message.author_post}")
        val findSubscribersByUsername = userRepository.findSubscribersByUsername(message.author_post)

        findSubscribersByUsername.forEach{ subscriberInfo -> telegramBotService.sendNotificationMessage(subscriberInfo.telegram_chat_id, subscriberInfo.username)}
    }
}
