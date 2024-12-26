package io.hhplus.clean_arch.controller

import io.hhplus.clean_arch.domain.Lecture
import java.sql.Timestamp

class LectureResponse(
    val id: Long,
    val title: String,
    val registrationCount: Int,
    val lectureBy: Int,
    val lectureAt: Timestamp,
)

fun Lecture.toLectureResponse(): LectureResponse {
    return LectureResponse(
        id = this.id,
        title = this.title,
        registrationCount = this.registrationCount,
        lectureBy = this.lectureBy,
        lectureAt = this.lectureAt,
    )
}