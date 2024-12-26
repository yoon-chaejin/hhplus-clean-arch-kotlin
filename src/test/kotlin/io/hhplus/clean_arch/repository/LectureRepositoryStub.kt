package io.hhplus.clean_arch.repository

import io.hhplus.clean_arch.common.TimestampFactory
import io.hhplus.clean_arch.domain.Lecture
import io.hhplus.clean_arch.domain.LectureRepository
import java.sql.Timestamp

class LectureRepositoryStub : LectureRepository {
    override fun getLecturesByLectureAtBetween(from: Timestamp, to: Timestamp): List<Lecture> {
        return listOf(
            Lecture(
                id = 1L,
                title = "신청 가능한 강의",
                lectureBy = 1,
                lectureAt = TimestampFactory.of(2024, 12, 28, 13, 0, 0)
            ),
            Lecture(
                id = 2L,
                title = "정원이 다 찬 강의",
                lectureBy = 1,
                lectureAt = TimestampFactory.of(2024, 12, 28, 15, 0, 0),
                registrationCount = 30
            ),
            Lecture(
                id = 3L,
                title = "특강일시가 지난 강의",
                lectureBy = 1,
                lectureAt = TimestampFactory.of(2024, 12, 28, 5, 0, 0)
            )
        )
    }

    override fun getLectureById(id: Long): Lecture {
        return Lecture(
            id = id,
            title = "강의1",
            lectureBy = 1,
            lectureAt = TimestampFactory.of(2024, 12, 28, 13, 0, 0)
        )
    }

    override fun getRegisteredLecturesByUserId(userId: Long): List<Lecture> {
        val lecture = Lecture(
            id = 1L,
            title = "강의1",
            lectureBy = 1,
            lectureAt = TimestampFactory.of(2024, 12, 28, 13, 0, 0),
        )

        val registeredLecture = lecture.register(
            userId = userId,
            at = TimestampFactory.of(2024, 12, 27, 13, 0, 0)
        )

        return listOf(
            registeredLecture
        )
    }

    override fun save(lecture: Lecture): Lecture {
        return lecture
    }
}