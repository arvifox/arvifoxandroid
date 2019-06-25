package com.arvifox.arvi.utils

import android.content.Context
import com.google.gson.GsonBuilder
import java.io.*
import java.lang.reflect.Type

object AssetUtils {

    /**
     * Load asset file
     *
     * @param context  Context
     * @param fileName File to load
     * @return File input stream
     * @throws IOException IOException
     */
    @Throws(IOException::class)
    fun loadFromAssets(context: Context, fileName: String): InputStream {
        return context.resources.assets.open(fileName)
    }


    /**
     * Load asset file content as string
     *
     * @param context  Context
     * @param fileName File to load
     * @return File input stream
     * @throws IOException IOException
     */
    @Throws(IOException::class)
    fun loadStringFromAssets(context: Context, fileName: String): String {
        val `is` = loadFromAssets(context, fileName)
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String? = reader.readLine()
        while (line != null) {
            sb.append(line).append("\n")
            line = reader.readLine()
        }
        reader.close()
        `is`.close()
        return sb.toString()
    }

    /**
     * Load bean from json file using gson
     *
     * @param context     Context
     * @param fileName    Json file
     * @param targetClass Target class (bean)
     * @param <T>         Target class (return type)
     * @return Parsed bean instance
     * @throws Exception Exception
    </T> */
    @Throws(Exception::class)
    fun <T> loadGsonFromAssets(context: Context, fileName: String,
                               targetClass: Class<T>): T {
        var dataInputStream: InputStream? = null
        val gson = GsonBuilder().create()

        try {
            dataInputStream = loadFromAssets(context, fileName)
            return gson.fromJson(InputStreamReader(dataInputStream), targetClass)
        } catch (e: Exception) {

            throw e
        } finally {
            if (dataInputStream != null) {
                try {
                    dataInputStream.close()
                } catch (e: IOException) {

                }

            }
        }
    }

    @Throws(Exception::class)
    fun <T> loadGsonFromAssets(context: Context, fileName: String,
                               targetClass: Type): T {
        var dataInputStream: InputStream? = null
        val gson = GsonBuilder().create()

        try {
            dataInputStream = loadFromAssets(context, fileName)
            return gson.fromJson(InputStreamReader(dataInputStream), targetClass)
        } catch (e: Exception) {

            throw e
        } finally {
            if (dataInputStream != null) {
                try {
                    dataInputStream.close()
                } catch (e: IOException) {

                }

            }
        }
    }

    /**
     * load file from assets
     *
     * @param context   context
     * @param assetPath asset path
     * @param fileName  file name
     * @return file
     */
    fun loadFileFromAssets(context: Context, assetPath: String, fileName: String): File? {
        val dataInputStream: InputStream

        try {
            dataInputStream = loadFromAssets(context, assetPath)
            return FileUtils.createFile(context, dataInputStream, fileName)
        } catch (e: Exception) {

        }

        return null
    }
}
