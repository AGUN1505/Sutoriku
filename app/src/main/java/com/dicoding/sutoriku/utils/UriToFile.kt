package com.dicoding.sutoriku.utils

import android.content.Context
import android.net.Uri
import java.io.*

fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(
        buffer,
        0,
        length
    )
    outputStream.close()
    inputStream.close()
    return myFile
}