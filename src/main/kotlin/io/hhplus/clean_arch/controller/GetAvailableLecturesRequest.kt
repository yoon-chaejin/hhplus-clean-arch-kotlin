package io.hhplus.clean_arch.controller

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class GetAvailableLecturesRequest (
    @field:NotNull
    @field:Pattern(regexp = "^(20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$")
    val date: String
)