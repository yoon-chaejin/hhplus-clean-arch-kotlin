package io.hhplus.clean_arch.common

import org.springframework.http.HttpStatus

enum class CustomExceptionType (val status: HttpStatus, val resultCode: String, val resultMessage: String) {
    LECTURE_REGISTRATION_UNAVAILABLE(HttpStatus.INTERNAL_SERVER_ERROR, "1001", "특강 신청이 불가합니다."),
    LECTURE_NOT_FOUND(HttpStatus.BAD_REQUEST, "2001", "강의가 존재하지 않습니다.")
}