package io.hhplus.clean_arch.domain

import io.hhplus.clean_arch.common.CustomException
import io.hhplus.clean_arch.common.CustomExceptionType
import jakarta.persistence.*
import java.sql.Timestamp
import java.time.Instant

@Entity
data class Lecture(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val title: String,
    val registrationCount: Int = 0,
    val lectureBy: Int,
    val lectureAt: Timestamp,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lecture", cascade = [CascadeType.PERSIST, CascadeType.MERGE] )
    val registrations: List<LectureRegistration> = emptyList(),
) {
    init {
        require(registrationCount in 0..MAX_REGISTRATION_COUNT) {
            throw CustomException(CustomExceptionType.LECTURE_REGISTRATION_UNAVAILABLE)
        }
    }

    companion object {
        const val MAX_REGISTRATION_COUNT = 30
    }

    fun isAvailable(now: Timestamp = Timestamp.from(Instant.now())): Boolean {
        return now.before(this.lectureAt) && this.registrationCount < MAX_REGISTRATION_COUNT
    }

    fun register(userId: Long, at: Timestamp): Lecture {
        if (!isAvailable(at)) { throw CustomException(CustomExceptionType.LECTURE_REGISTRATION_UNAVAILABLE) }

        return Lecture(
            id = this.id,
            title = this.title,
            registrationCount = this.registrationCount + 1,
            lectureBy = this.lectureBy,
            lectureAt = this.lectureAt,

            registrations = this.registrations + LectureRegistration(
                lecture = this,
                userId = userId,
                registeredAt = at
            )
        )
    }
}