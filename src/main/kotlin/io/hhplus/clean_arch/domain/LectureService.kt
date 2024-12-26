package io.hhplus.clean_arch.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp

@Service
class LectureService (
    @Autowired val lectureRepository: LectureRepository
) {
    @Transactional
    fun register(lectureId: Long, userId: Long, at: Timestamp): Lecture {
        val lecture = lectureRepository.getLectureById(lectureId)
        return lectureRepository.save(lecture.register(userId, at))
    }

    fun getAvailableLectures(at: Timestamp): List<Lecture> {
        return lectureRepository.getLectures().filter { it.isAvailable(at) }
    }

    fun getRegisteredLectures(userId: Long): List<Lecture> {
        return lectureRepository.getRegisteredLecturesByUserId(userId)
    }
}