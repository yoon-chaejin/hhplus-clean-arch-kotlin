package io.hhplus.clean_arch.repository

import io.hhplus.clean_arch.domain.Lecture
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface LectureJpaRepository : JpaRepository<Lecture, Long> {
    fun findByRegistrationsUserId(@Param("userId") userId: Long): List<Lecture>
}