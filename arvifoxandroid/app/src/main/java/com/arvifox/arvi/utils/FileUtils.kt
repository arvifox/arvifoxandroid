package com.arvifox.arvi.utils

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.arvifox.arvi.BuildConfig
import com.google.common.base.Charsets
import java.io.*
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object FileUtils {

    /**
     * Checks if external storage is available for read and write
     */
    val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }

    /**
     * Checks if external storage is available to at least read
     */
    val isExternalStorageReadable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
        }

    /**
     * deletes all files from a directory
     *
     * @param dir [File]
     * @return deleted at least one file from dir
     */
    fun deleteFilesFromDir(dir: File): Boolean {
        if (!dir.exists() && !dir.isDirectory) {
            return false
        }
        var success = false
        for (oldFiles in dir.listFiles()) {
            success = success or oldFiles.delete()
        }
        return success
    }

    /**
     * url = file path or whatever suitable URL you want.
     *
     * @param url String
     * @return String
     */
    fun getMimeType(url: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            val mime = MimeTypeMap.getSingleton()
            type = mime.getMimeTypeFromExtension(extension)
        }
        return type
    }

    /**
     * returns the android external files dir of the app.
     *
     * @param ui   Context
     * @param type directory type, see [Environment]
     * @return Files Directory
     */
    fun getExternalFilesDir(ui: Context, type: String): File? {
        return ui.getExternalFilesDir(type)
    }

    /**
     * get uri for app internal file. the file path has to be registered in file provider xml file
     *
     * @param ctx  a Context
     * @param file the File to resolve Uri
     * @return the Uri to file from file provider
     */
    fun getFileProviderUri(ctx: Context, file: File): Uri {
        return FileProvider.getUriForFile(ctx, BuildConfig.APPLICATION_ID + ".file.provider", file)
    }

    /**
     * get uri for app internal file. the file path has to be registered in file provider xml file
     *
     * @param ctx  a Context
     * @param path the file path to resolve Uri
     * @return the Uri to file from file provider
     */
    fun getFileProviderUri(ctx: Context, path: String): Uri {
        return getFileProviderUri(ctx, File(path))
    }

    /**
     * deletes a file in directory type in the external files dir
     *
     * @param ui       Context
     * @param type     directory
     * @param fileName file
     * @return success
     */
    fun deleteExternalStorageFile(ui: Context, type: String, fileName: String): Boolean {
        val path = ui.getExternalFilesDir(type)
        return deleteFile(path.toString() + File.separator + fileName)
    }

    /**
     * Deletes a file by its path
     *
     * @param filePath path to file
     * @return true == delete successfully
     */
    fun deleteFileByUri(filePath: Uri?): Boolean {
        return filePath != null && deleteFile(filePath.path)
    }

    /**
     * Deletes a file by its path
     *
     * @param path path to file
     * @return true == delete successfully
     */
    fun deleteFile(path: String?): Boolean {
        if (path == null) {
            return false
        }

        val file = File(path)
        return deleteFile(file)
    }

    /**
     * Deletes a file
     *
     * @param file File to delete
     * @return true == delete successfully
     */
    fun deleteFile(file: File): Boolean {
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }

    /**
     * resolves the filepath within activity result intent
     *
     * @param resolver ContentResolver for Mediastore searching
     * @param data     Intent
     * @return path for file as String
     */
    fun getFilePathByIntent(resolver: ContentResolver, data: Intent): String? {
        val selectedImage = data.data
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = resolver.query(selectedImage!!, filePathColumn, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()

            return picturePath
        } else {
            return null
        }
    }

    /**
     * Copy a external file
     *
     * @param sourcePath source filepath
     * @param targetPath destination filepath
     */
    fun copyExternalResource(sourcePath: String, targetPath: String) {
        var fis: FileInputStream? = null
        var fos: FileOutputStream? = null
        var srcFileStream: FileChannel? = null
        var dstFileStream: FileChannel? = null

        try {
            // Check write permissions
            val sourceFile = File(sourcePath)
            val destinationFile = File(targetPath)

            if (sourceFile.canRead() && sourceFile.exists()) {
                // Create directories if needed
                val destinationDirectory = destinationFile.parentFile
                destinationDirectory?.mkdirs()
                fis = FileInputStream(sourceFile)
                srcFileStream = fis.channel
                fos = FileOutputStream(destinationFile)
                dstFileStream = fos.channel
                dstFileStream!!.transferFrom(srcFileStream, 0, srcFileStream!!.size())
            } else {
            }
        } catch (e: Exception) {
        } finally {
            if (srcFileStream != null) {
                try {
                    srcFileStream.close()
                } catch (e: IOException) {
                }

            }

            if (fis != null) {
                try {
                    fis.close()
                } catch (e: IOException) {
                }

            }

            if (dstFileStream != null) {
                try {
                    dstFileStream.close()
                } catch (e: IOException) {
                }

            }

            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                }

            }
        }
    }

    /**
     * Compress files
     *
     * @param context             Context
     * @param files               files to compress
     * @param destinationFileName Compressed file
     * @return Compressed file
     */
    fun zipFiles(context: Context, files: List<File>, destinationFileName: String): File? {
        try {
            val externalDir = context.getExternalFilesDir("todo") ?: return null

            val destinationFile = File(externalDir.absolutePath + File.separator + destinationFileName)
            val fos = FileOutputStream(destinationFile)
            val zos = ZipOutputStream(fos)

            //write zip entry for each file in files
            for (file in files) {
                val sourceFileName = file.name
                val fis = FileInputStream(file)

                val zipEntry = ZipEntry(sourceFileName)  //only file name
                zos.putNextEntry(zipEntry)

                val buf = ByteArray(1024)
                var len: Int
                len = fis.read(buf)
                while ((len) > 0) {
                    zos.write(buf, 0, len)
                }
                fis.close()
                zos.closeEntry()
            }
            zos.close()
            return destinationFile

        } catch (e: Exception) {
            return null
        }

    }

    /**
     * Generate an empty zip file including a folder as fallback if no data is available to export
     *
     * @param context             current context
     * @param destinationFileName file name
     * @return empty zip file
     */
    fun getEmptyZip(context: Context, destinationFileName: String): File? {
        try {
            val externalDir = context.getExternalFilesDir("todo") ?: return null

            val destinationFile = File(externalDir.absolutePath + File.separator + destinationFileName)
            val fos = FileOutputStream(destinationFile)
            val zos = ZipOutputStream(fos)

            zos.putNextEntry(ZipEntry("export_data/"))
            zos.closeEntry()
            zos.flush()
            zos.close()

            return destinationFile

        } catch (e: Exception) {
            return null
        }

    }

    /**
     * Get non null value from source
     *
     * @param source Value to persist
     * @return Non null value
     */
    fun parseWritableData(source: Any?): Any {
        return source ?: ""
    }

    /**
     * Copy a private file
     *
     * @param ctx          Context
     * @param srcFileName  source file name
     * @param destFilePath dest file path
     * @return copy was successful
     */
    fun copyPrivateResource(ctx: Context, srcFileName: String, destFilePath: String): Boolean {
        try {
            val fis = ctx.openFileInput(srcFileName)
            val streamReader = InputStreamReader(fis, Charset.forName(Charsets.UTF_8.name()))
            val fos = FileOutputStream(File(destFilePath))
            val streamWriter = OutputStreamWriter(fos,
                    Charset.forName(Charsets.UTF_8.name()).newEncoder())
            try {
                var character: Int
                character = streamReader.read()
                while ((character) != -1) {
                    streamWriter.write(Character.toString(character.toChar()))
                }
                streamReader.close()
                streamWriter.close()
            } catch (e: IOException) {
                return false
            }

        } catch (e: FileNotFoundException) {
            return false
        }

        return true
    }

    /**
     * copy a directory/file from source to target dir
     *
     * @param sourceLocation source directory or file
     * @param targetLocation target directory or file
     * @throws IOException file not found or not readable
     */
    @Throws(IOException::class)
    fun copyDirectory(sourceLocation: File, targetLocation: File) {
        if (sourceLocation.isDirectory) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir()
            }

            val children = sourceLocation.list()
            for (i in 0 until sourceLocation.listFiles().size) {
                copyDirectory(File(sourceLocation, children[i]), File(targetLocation, children[i]))
            }
        } else {
            val `in` = FileInputStream(sourceLocation)
            val out = FileOutputStream(targetLocation)

            // Copy the bits from instream to outstream
            val buf = ByteArray(1024)
            var len: Int
            len = `in`.read(buf)
            while ((len) > 0) {
                out.write(buf, 0, len)
            }
            `in`.close()
            out.close()
        }
    }

    /**
     * create file
     *
     * @param context     context
     * @param inputStream input stream
     * @param path        file path
     * @return file
     * @throws IOException IOException
     */
    @Throws(IOException::class)
    fun createFile(context: Context, inputStream: InputStream, path: String): File {
        val file = File(context.cacheDir, path)

        val outputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var length: Int
        length = inputStream.read(buffer)
        while ((length) > 0) {
            outputStream.write(buffer, 0, length)
        }

        outputStream.close()
        inputStream.close()

        return file
    }
}