package io.hhplus.clean_arch.domain

import org.springframework.stereotype.Repository

@Repository
interface LectureRepository {
    fun getLectures(): List<Lecture>

    fun getLectureById(id: Long): Lecture

    fun getRegisteredLecturesByUserId(userId: Long): List<Lecture>

    fun save(lecture: Lecture): Lecture
}