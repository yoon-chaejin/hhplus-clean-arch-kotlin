package io.hhplus.clean_arch.common

import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class TimestampFactory {
    companion object {
        private val asiaSeoul = ZoneOffset.of("+09:00")

        fun now(): Timestamp {
            return Timestamp.from(Instant.now())
        }

        fun of(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Timestamp {
            return Timestamp.from(LocalDateTime.of(year, month, day, hour, minute, second).toInstant(asiaSeoul))
        }

        fun from(dateTime: LocalDateTime): Timestamp {
            return Timestamp.from(dateTime.toInstant(asiaSeoul))
        }
    }
}