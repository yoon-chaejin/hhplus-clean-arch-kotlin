package io.hhplus.clean_arch.controller

import io.hhplus.clean_arch.common.TimestampFactory
import io.hhplus.clean_arch.domain.LectureService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/lectures")
class LectureController(
    @Autowired val lectureService: LectureService
) {
    @GetMapping("/available")
    fun getAvailableLectures(): List<LectureResponse> {
        return lectureService.getAvailableLectures(TimestampFactory.now()).map { it.toLectureResponse() }
    }

    @GetMapping("/registered")
    fun getRegisteredLectures(@RequestBody userId: Long): List<LectureResponse> {
        return lectureService.getRegisteredLectures(userId).map { it.toLectureResponse() }
    }
    @PostMapping("/{lectureId}/registration")
    fun registerForLecture(@PathVariable lectureId: Long, @RequestBody request: RegisterForLectureRequest): LectureResponse {
        return lectureService.register(lectureId, request.userId, request.requestedAt).toLectureResponse()
    }
}