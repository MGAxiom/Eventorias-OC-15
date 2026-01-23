package com.example.eventorias.ui.model

import android.net.Uri
import androidx.annotation.StringRes
import com.example.eventorias.R

sealed interface FormEvent {
    data class TitleChanged(val title: String) : FormEvent
    data class DescriptionChanged(val description: String) : FormEvent
    data class DateChanged(val date: String) : FormEvent
    data class TimeChanged(val time: String) : FormEvent
    data class PhotoUriChanged(val photoUri: Uri?) : FormEvent
    data class LocationChanged(val location: String) : FormEvent
}

sealed class FormError(@StringRes val messageRes: Int) {
    data object TitleError : FormError(R.string.error_title)
    data object PhotoError : FormError(R.string.error_image)
    data object DateError : FormError(R.string.error_date)
    data object TimeError : FormError(R.string.error_time)
}