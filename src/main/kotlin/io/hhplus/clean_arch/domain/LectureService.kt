package io.hhplus.clean_arch.domain

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp

@Service
class LectureService (
    @Autowired val lectureRepository: LectureRepository
) {
    private val log = LoggerFactory.getLogger(LectureService::class.java)

    @Transactional
    fun register(lectureId: Long, userId: Long, at: Timestamp): Lecture {
        val lecture = lectureRepository.getLectureById(lectureId)
        log.info("User Id : $userId")
        return lectureRepository.save(lecture.register(userId, at))
    }

    fun getAvailableLectures(at: Timestamp): List<Lecture> {
        return lectureRepository.getLectures().filter { it.isAvailable(at) }
    }

    fun getRegisteredLectures(userId: Long): List<Lecture> {
        return lectureRepository.getRegisteredLecturesByUserId(userId)
    }
}