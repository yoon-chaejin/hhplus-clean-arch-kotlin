package io.hhplus.clean_arch.domain

import io.hhplus.clean_arch.common.CustomException
import io.hhplus.clean_arch.common.TimestampFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LectureTest {

    @Test
    fun `현재가 특강일시보다 이후인 경우, false를 반환한다`() {
        //given
        val lectureAt = TimestampFactory.of(2024, 12, 28, 13, 0, 0)
        val now = TimestampFactory.of(2024, 12, 29, 13, 0, 0)
        val sut = Lecture(
            title = "강의1",
            lectureBy = 1,
            lectureAt = lectureAt,
        )

        //when
        val result = sut.isAvailable(now)

        //then
        assertFalse(result)
    }


    @Test
    fun `현재가 특강일시와 동일한 경우, false를 반환한다`() {
        //given
        val lectureAt = TimestampFactory.of(2024, 12, 28, 13, 0, 0)

        val sut = Lecture(
            title = "강의1",
            lectureBy = 1,
            lectureAt = lectureAt,
        )

        //when
        val result = sut.isAvailable(lectureAt)

        //then
        assertFalse(result)
    }

    @Test
    fun `현재가 특강일시보다 이전이고 정원이 다 찬 경우, false를 반환한다`() {
        //given
        val lectureAt = TimestampFactory.of(2024, 12, 28, 13, 0, 0)
        val now = TimestampFactory.of(2024, 12, 27, 13, 0, 0)
        val sut = Lecture(
            title = "강의1",
            registrationCount = Lecture.MAX_REGISTRATION_COUNT,
            lectureBy = 1,
            lectureAt = lectureAt,
        )

        //when
        val result = sut.isAvailable(now)

        //then
        assertFalse(result)
    }

    @Test
    fun `현재가 특강일시보다 이전이고 정원이 다 차지 않은 경우, true를 반환한다`() {
        //given
        val lectureAt = TimestampFactory.of(2024, 12, 28, 13, 0, 0)
        val now = TimestampFactory.of(2024, 12, 27, 13, 0, 0)

        val sut = Lecture(
            title = "강의1",
            registrationCount = Lecture.MAX_REGISTRATION_COUNT - 1,
            lectureBy = 1,
            lectureAt = lectureAt,
        )

        //when
        val result = sut.isAvailable(now)

        //then
        assertTrue(result)
    }

    @Test
    fun `정원이 다 찬 특강을 신청할 경우, LECTURE_REGISTRATION_UNAVAILABLE 오류를 발생시킨다`() {
        //given
        val lectureAt = TimestampFactory.of(2024, 12, 28, 13, 0, 0)
        val now = TimestampFactory.of(2024, 12, 27, 13, 0, 0)

        val sut = Lecture(
            title= "강의1",
            registrationCount = Lecture.MAX_REGISTRATION_COUNT,
            lectureBy = 1,
            lectureAt = lectureAt
        )
        val userId = 1L

        //when

        //then
        assertThrows(CustomException::class.java) {
            sut.register(userId, now)
        }
    }

    @Test
    fun `특강일시가 지난 특강에 신청할 경우, LECTURE_REGISTRATION_UNAVAILABLE 오류를 발생시킨다`() {
        //given
        val lectureAt = TimestampFactory.of(2024, 12, 28, 13, 0, 0)
        val now = TimestampFactory.of(2024, 12, 29, 13, 0, 0)

        val sut = Lecture(
            title= "강의1",
            lectureBy = 1,
            lectureAt = lectureAt
        )
        val userId = 1L

        //when

        //then
        assertThrows(CustomException::class.java) {
            sut.register(userId, now)
        }
    }

    @Test
    fun `특강에 등록할 경우, 특강 등록 수가 1 증가하고, 특강 등록 목록에 해당 userId가 추가된다`() {
        //given
        val lectureAt = TimestampFactory.of(2024, 12, 28, 13, 0, 0)
        val now = TimestampFactory.of(2024, 12, 27, 13, 0, 0)

        val sut = Lecture(
            title= "강의1",
            lectureBy = 1,
            lectureAt = lectureAt
        )
        val userId = 1L

        //when
        val result = sut.register(userId, now)

        //then
        val expected = Lecture(
            id = sut.id,
            title = sut.title,
            registrationCount = sut.registrationCount + 1,
            lectureBy = sut.lectureBy,
            lectureAt = sut.lectureAt,

            registrations = sut.registrations + LectureRegistration(
                lecture = sut,
                userId = userId,
                registeredAt = now
            ),
        )
        assertEquals(expected, result)
    }
}