package com.arvifox.arvi.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.nio.charset.Charset

object AndroidStorage {

    fun internal(context: Context) {
        val internalDir = context.filesDir
        val internalCacheDir = context.cacheDir
        val f = File(internalDir, "file1.txt")
        writeToFile(f, "text1")

        val tempFile = File.createTempFile("file2.txt", null, internalCacheDir)
        writeToFile(tempFile, "text2")
    }

    fun pictureDirPublic(): File? {
        val f = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "myPicture")
        f.mkdir()
        return f
    }

    /*
    If you want to hide your files from the Media Scanner,
    include an empty file named .nomedia in your external files
    directory (note the dot prefix in the filename). This prevents media scanner
    from reading your media files and providing them to other apps through the MediaStore content provider.
     */

    fun getPrivateAlbumStorageDir(context: Context, albumName: String = "myAlbumPrivate"): File? {
        // Get the directory for the app's private pictures directory.
        val file = File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName)
        if (!file.mkdirs()) {
            Logger.d { "Directory not created" }
        }
        return file
    }

    fun getPathInfo(context: Context): String {
        val sb = StringBuilder()
        sb.append(File("/").freeSpace).append(" < ").append(File("/").totalSpace).append("\n")
        sb.append(Environment.getExternalStorageDirectory().absolutePath).append("\n")
        sb.append(Environment.getExternalStoragePublicDirectory("").absolutePath).append("\n")
        sb.append(pictureDirPublic()?.absolutePath).append("\n")
        val f = File(Environment.getExternalStorageDirectory(), "myfile.fox")
        writeToFile(f, "mytext")
        sb.append(f.absolutePath).append("\n")
        sb.append(getPrivateAlbumStorageDir(context)?.absolutePath).append("\n")
        return sb.toString()
    }

    private fun writeToFile(f: File, c: String) {
        val fos = f.outputStream()
//        val fos = context.openFileOutput("file1.txt", Context.MODE_PRIVATE)
        fos.write(c.toByteArray(Charset.forName("UTF-8")))
        fos.close()
    }

    /* Checks if external storage is available for read and write */
    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /* Checks if external storage is available to at least read */
    private fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }
}