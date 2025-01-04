package com.example.antoanbmtt.helper

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class Util{
    companion object{
        fun getPartFromUri(uri : Uri, contentResolver: ContentResolver) : MultipartBody.Part{
            val inputStream = contentResolver.openInputStream(uri)
            val byteArray = inputStream?.readBytes()
            val mimeType = MediaType.parse(contentResolver.getType(uri)!!)!!
            val body = RequestBody.create(mimeType,byteArray!!)
            val fileName = getFileNameFromContentUri(contentResolver,uri)
            val part = MultipartBody.Part.createFormData("fileContent","$fileName",body)
            inputStream.close()
            return part
        }
        private fun getFileNameFromContentUri(contentResolver: ContentResolver, uri: Uri): String? {
            var fileName: String? = null
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && it.moveToFirst()) {
                    fileName = it.getString(nameIndex)
                }
            }
            return fileName
        }

        @SuppressLint("QueryPermissionsNeeded")
        fun openCacheFile(context: Context, fileName: String, mimeType: String) {
            val file = File(context.cacheDir, fileName) // Locate the file in the cache directory

            if (!file.exists()) {
                println("File does not exist: ${file.absolutePath}")
                return
            }
            val fileUri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(fileUri, mimeType)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                println("No app available to open the file.")
            }
        }
        fun getMimeType(fileName: String): String? {
            val extension = fileName.substringAfterLast('.', "")
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
        }
        fun writeToFile(body: ResponseBody, file : File){
            return try {
                val fos = FileOutputStream(file);
                fos.write(body.bytes())
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        fun downloadFile(data : ByteArray,fileName : String,context : Context){
            val downloadFolder = File(Environment.DIRECTORY_DOWNLOADS + "/MEGA Storage Downloads")
            if(!downloadFolder.exists()){
                downloadFolder.mkdirs()
            }
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,fileName)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/MEGA Storage Downloads");

            val uri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"),contentValues)
            context.contentResolver.openOutputStream(uri!!).use { outputStream ->
                outputStream?.write(data)
            }
        }
        fun downloadFromCacheFile(cacheFile : File,fileName: String,context: Context){
            val downloadFolder = File(Environment.DIRECTORY_DOWNLOADS + "/MEGA Storage Downloads")
            if(!downloadFolder.exists()){
                downloadFolder.mkdirs()
            }
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,fileName)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/MEGA Storage Downloads");

            val uri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"),contentValues)
            FileInputStream(cacheFile).use { inputStream ->
                context.contentResolver.openOutputStream(
                    uri!!
                ).use { outputStream ->
                    if (outputStream != null) {
                        val buffer = ByteArray(4096)
                        var bytesRead: Int

                        while ((inputStream.read(buffer).also { bytesRead = it }) != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                        }
                    }
                }
            }
        }
    }
}