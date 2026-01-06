package ru.shaxowskiy.notificationservice.dto

import java.time.LocalDateTime
import java.util.UUID

class PostEventDto (val uuid: UUID, val author_post: String, val event_ts: LocalDateTime){
    override fun toString(): String {
        return "PostEventDto{" +
                "uuid= " + uuid.toString() +
                ", author_post= " + author_post +
                ", event_ts= " + event_ts
    }
}
