package com.skeletonkotlin.e_cigarette.helper.util

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import java.io.*
import java.nio.charset.Charset

object FileUtil {
    fun saveImage(
        bitmap: Bitmap,
        name: String,
        directoryName: String,
        compressFormat: Bitmap.CompressFormat
    ): String {

        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/$directoryName")
        myDir.mkdirs()
        val fname = "/$name"
        val file = File(myDir, fname)

        try {
            file.createNewFile()
            if (file.exists())
                file.delete()
            val out = FileOutputStream(file)
            val bos = BufferedOutputStream(out)
            bitmap.compress(compressFormat, 100, bos)
            out.flush()
            out.close()
            return "$root/$directoryName$fname"

        } catch (e: FileNotFoundException) {
            ("Error saving image file: " + e.message).logD()
            return ""
        } catch (e: IOException) {
            ("Error saving image file: " + e.message).logD()
            return ""
        }
    }

    fun loadJSONFromAsset(context: Context, fileName: String): String? {
        var json: String? = null
        try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = buffer.toString(Charset.defaultCharset())
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

    fun duplicateFile(file: File, destinationPath: String) =
        File(destinationPath).apply {
            if (exists())
                delete()
            createNewFile()

            val inputStream = file.inputStream()
            val outputStream = FileOutputStream(this)
            try {
                inputStream.channel.transferTo(0, inputStream.channel.size(), outputStream.channel)
            } finally {
                inputStream.close()
                outputStream.close()
            }
        }

    fun removeFile(imageFilePath: String): Boolean = try {
        File(imageFilePath).deleteRecursively()
    } catch (e: Exception) {
        false
    }
}

fun String.getFileFromSDCard(): File? {
    val externalStorage = Environment.getExternalStorageDirectory()
    return File(externalStorage, this)
}
