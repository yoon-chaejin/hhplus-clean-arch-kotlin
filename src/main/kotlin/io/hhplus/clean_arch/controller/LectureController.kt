package io.hhplus.clean_arch.controller

import io.hhplus.clean_arch.common.TimestampFactory
import io.hhplus.clean_arch.domain.LectureService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/lectures")
class LectureController(
    @Autowired val lectureService: LectureService
) {
    @GetMapping("/available")
    fun getAvailableLectures(@RequestBody @Valid request: GetAvailableLecturesRequest): List<LectureResponse> {
        return lectureService.getAvailableLectures(
            TimestampFactory.from(LocalDate.parse(request.date, DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay()),
            TimestampFactory.from(LocalDate.parse(request.date, DateTimeFormatter.ofPattern("yyyyMMdd")).plusDays(1).atStartOfDay()),
            TimestampFactory.now()
        ).map { it.toLectureResponse() }
    }

    @GetMapping("/registered")
    fun getRegisteredLectures(@RequestBody @Valid request: GetRegisteredLecturesRequest): List<LectureResponse> {
        return lectureService.getRegisteredLectures(request.userId).map { it.toLectureResponse() }
    }

    @PostMapping("/{lectureId}/registration")
    fun registerForLecture(@PathVariable lectureId: Long, @RequestBody request: RegisterForLectureRequest): LectureResponse {
        return lectureService.register(lectureId, request.userId, request.requestedAt).toLectureResponse()
    }
}