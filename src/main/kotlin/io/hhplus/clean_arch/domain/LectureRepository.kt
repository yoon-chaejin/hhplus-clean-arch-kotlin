package io.hhplus.clean_arch.domain

import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
interface LectureRepository {
    fun getLecturesByLectureAtBetween(from: Timestamp, to: Timestamp): List<Lecture>

    fun getLectureById(id: Long): Lecture

    fun getRegisteredLecturesByUserId(userId: Long): List<Lecture>

    fun save(lecture: Lecture): Lecture
}