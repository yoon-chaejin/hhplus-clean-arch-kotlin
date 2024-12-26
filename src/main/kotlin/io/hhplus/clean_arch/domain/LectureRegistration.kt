package io.hhplus.clean_arch.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.sql.Timestamp

@Entity
@Table(uniqueConstraints = [
    UniqueConstraint(columnNames = ["lecture_id", "user_id"])
])
data class LectureRegistration(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    val lecture: Lecture,

    val userId: Long,
    val registeredAt: Timestamp,
)