package io.hhplus.clean_arch.domain

import io.hhplus.clean_arch.common.TimestampFactory
import io.hhplus.clean_arch.repository.LectureRepositoryStub
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LectureServiceTest {
    private val lectureRepository: LectureRepository = LectureRepositoryStub()
    private val sut = LectureService(lectureRepository)

    @Test fun `신청 가능한 강의 목록을 조회할 경우, 정원이 다 찬 강의나 특강일시가 지난 강의는 조회되지 않는다`() {
        //given
        val from = TimestampFactory.of(2024, 12, 28, 0, 0, 0)
        val to = TimestampFactory.of(2024, 12, 29, 0, 0, 0)
        val now = TimestampFactory.of(2024, 12, 28, 9, 0, 0)

        //when
        val result = sut.getAvailableLectures(from, to, now)

        //then
        assertEquals(1, result.size)
        assertTrue(result.all { it.registrationCount < Lecture.MAX_REGISTRATION_COUNT })
        assertTrue(result.all { it.lectureAt > now })
    }

    @Test fun `신청한 강의 목록을 조회 시, 신청자 목록에 해당 userId가 포함된다`() {
        //given
        val userId = 1L

        //when
        val result = sut.getRegisteredLectures(userId)

        //then
        assertTrue(result.all { it.registrations.any { l -> l.userId == userId } })
    }
 }