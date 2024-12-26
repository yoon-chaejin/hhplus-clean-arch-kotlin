package io.hhplus.clean_arch.controller

import io.hhplus.clean_arch.common.TimestampFactory
import java.sql.Timestamp

data class RegisterForLectureRequest (
    val userId: Long,
) {
    val requestedAt: Timestamp = TimestampFactory.now()
}