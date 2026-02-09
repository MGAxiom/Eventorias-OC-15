package com.example.core_ui.utils

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File

fun photoPickerButtonAction(pickMedia: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>) {
    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
}

fun photoButtonAction(cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>, uri: Uri) {
    cameraLauncher.launch(uri)
}

fun createImageFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(null)
    return File.createTempFile(
        "JPEG_${System.currentTimeMillis()}_",
        ".jpg",
        storageDir
    )
}