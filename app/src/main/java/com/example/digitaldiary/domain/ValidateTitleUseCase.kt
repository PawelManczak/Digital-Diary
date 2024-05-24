package com.example.digitaldiary.domain

import javax.inject.Inject

class ValidateTitleUseCase @Inject constructor() {

    operator fun invoke(title: String): Boolean {
        return title.isNotBlank()
    }

}