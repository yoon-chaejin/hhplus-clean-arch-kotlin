package io.hhplus.clean_arch.domain

import io.hhplus.clean_arch.common.TimestampFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = ["classpath:application-test.yml"])
class LectureServiceIntegrationTest(
    @Autowired val sut: LectureService,
) {
    @Test fun `동시에 동일한 특강에 대해 40명이 신청했을 때, 30명만 성공한다` () {
        //given
        val numOfIterations = 40
        val lectureId = 1L
        val now = TimestampFactory.of(2024, 12, 27, 13, 0, 0)

        val executorService = Executors.newFixedThreadPool(numOfIterations)
        val doneSignal = CountDownLatch(numOfIterations)
        val successCount = AtomicInteger(0)
        val failCount = AtomicInteger(0)

        //when
        for (i in 1..numOfIterations) {
            executorService.execute {
                try {
                    sut.register(lectureId, i.toLong(), now)
                    successCount.getAndIncrement()
                } catch(e: Exception) {
                    failCount.getAndIncrement()
                } finally {
                    doneSignal.countDown()
                }
            }
        }

        doneSignal.await()
        executorService.shutdown()

        //then
        assertAll(
            { assertEquals(Lecture.MAX_REGISTRATION_COUNT, successCount.get()) },
            { assertEquals(numOfIterations - Lecture.MAX_REGISTRATION_COUNT, failCount.get()) },
        )
    }

    @Test fun `동일한 유저 정보로 같은 특강을 5번 신청했을 때, 1번만 성공한다` () {
        //given
        val numOfIterations = 5
        val lectureId = 2L
        val userId = 1L
        val now = TimestampFactory.of(2024, 12, 27, 13, 0, 0)

        var successCount = 0
        var failCount = 0

        //when
        for (i in 1..numOfIterations) {
            try {
                sut.register(lectureId, userId, now)
                successCount++
            } catch(e: Exception) {
                failCount++
            }
        }

        //then
        assertAll(
            { assertEquals(1, successCount) },
            { assertEquals(numOfIterations - 1, failCount) },
        )
    }
}