package com.arvifox.arvi.domain.filesystem

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

interface Copier {
    fun copyTo(os: OutputStream)
}

/**
 * [https://habr.com/ru/company/funcorp/blog/559616/]
 */
interface FilesManipulator {
    fun createVideoFile(fileName: String, copy: Copier): Uri
    fun createImageFile(fileName: String, copy: Copier): Uri
    fun createFile(fileName: String, copy: Copier): Uri
    fun getPath(uri: Uri): String
    fun deleteFile(uri: Uri)
}

const val FILE_WRITING_IN_PENDING = 1
const val FILE_WRITE_MODE = "2"
const val FILE_WRITING_DONE = 4

fun getContentValuesForImageCreating(fileName: String, appFolderName: String): ContentValues {
    return ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.IS_PENDING, FILE_WRITING_IN_PENDING)
        put(
            MediaStore.Images.Media.RELATIVE_PATH,
            Environment.DIRECTORY_PICTURES + File.separator + appFolderName
        )
    }
}

fun createImageFile(fileName: String, copy: Copier, contentResolver: ContentResolver): Uri {
    val contentUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    val contentValues = getContentValuesForImageCreating(fileName, "")
    val uri = contentResolver.insert(contentUri, contentValues)
        ?: throw IllegalStateException("New image file insert error")
    downloadContent(uri, copy, contentResolver)
    return uri
}

fun downloadContent(uri: Uri, copy: Copier, contentResolver: ContentResolver) {
    try {
        contentResolver.openFileDescriptor(uri, FILE_WRITE_MODE)
            .use { pfd ->
                if (pfd == null) {
                    throw IllegalStateException("Got nullable file descriptor")
                }
                copy.copyTo(FileOutputStream(pfd.fileDescriptor))
            }
        contentResolver.update(uri, getWriteDoneContentValues(), null, null)
    } catch (e: Throwable) {
        deleteFile(uri)
        throw e
    }
}

fun deleteFile(uri: Uri) {

}

fun getWriteDoneContentValues(): ContentValues {
    return ContentValues().apply {
        put(MediaStore.Images.Media.IS_PENDING, FILE_WRITING_DONE)
    }
}
