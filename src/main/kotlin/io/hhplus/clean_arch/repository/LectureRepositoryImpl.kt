package io.hhplus.clean_arch.repository

import io.hhplus.clean_arch.common.CustomException
import io.hhplus.clean_arch.common.CustomExceptionType
import io.hhplus.clean_arch.domain.Lecture
import io.hhplus.clean_arch.domain.LectureRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class LectureRepositoryImpl(
    @Autowired val lectureJpaRepository: LectureJpaRepository,
): LectureRepository {

    override fun getLectures(): List<Lecture> {
        return lectureJpaRepository.findAll()
    }

    override fun getLectureById(id: Long): Lecture {
        return lectureJpaRepository.findByIdOrNullForUpdate(id)?: throw CustomException(CustomExceptionType.LECTURE_NOT_FOUND)
    }

    override fun getRegisteredLecturesByUserId(userId: Long): List<Lecture> {
        return lectureJpaRepository.findByRegistrationsUserId(userId)
    }

    override fun save(lecture: Lecture): Lecture {
        return lectureJpaRepository.save(lecture)
    }
}