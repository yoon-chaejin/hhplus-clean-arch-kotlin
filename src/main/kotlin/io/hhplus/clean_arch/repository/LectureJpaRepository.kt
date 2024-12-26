package io.hhplus.clean_arch.repository

import io.hhplus.clean_arch.domain.Lecture
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface LectureJpaRepository : JpaRepository<Lecture, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from Lecture l where l.id = :id")
    fun findByIdOrNullForUpdate(@Param("id") id: Long): Lecture?

    fun findByRegistrationsUserId(@Param("userId") userId: Long): List<Lecture>
}