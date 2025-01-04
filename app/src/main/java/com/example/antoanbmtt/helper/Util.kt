package com.example.antoanbmtt.helper

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import android.util.Log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import android.content.Context
import android.content.Intent
import android.util.Base64
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import java.io.File
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
        fun generatePdfThumbnail(contentResolver: ContentResolver, pdfUri: Uri): MultipartBody.Part? {
            try {
                val fileDescriptor: ParcelFileDescriptor? = contentResolver.openFileDescriptor(pdfUri, "r")
                fileDescriptor?.let {
                    val pdfRenderer = PdfRenderer(it)

                    if (pdfRenderer.pageCount > 0) {
                        val page = pdfRenderer.openPage(0)

                        val width = (page.width * 1.5).toInt() // Scale for better resolution
                        val height = (page.height * 1.5).toInt()
                        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

                        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                        page.close()
                        pdfRenderer.close()

                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos)
                        val byteArr = baos.toByteArray()
                        val requestBody = RequestBody.create(MediaType.parse("image/jpg"),byteArr)
                        val fileName = getFileNameFromContentUri(contentResolver,pdfUri)
                        return MultipartBody.Part.createFormData("thumbnail",fileName,requestBody)
                    }
                    pdfRenderer.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
        private fun getFileNameFromContentUri(contentResolver: ContentResolver, uri: Uri): String? {
            var fileName: String? = null

            // Query the content resolver for the file's metadata
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                // Try to get the column index for the filename
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && it.moveToFirst()) {
                    // Extract the filename from the cursor
                    fileName = it.getString(nameIndex)
                }
            }

            return fileName
        }

        @SuppressLint("QueryPermissionsNeeded")
        fun openCacheFile(context: Context, fileName: String, mimeType: String) {
            val file = File(context.cacheDir, fileName) // Locate the file in the cache directory

            if (!file.exists()) {
                // Handle the case where the file does not exist
                println("File does not exist: ${file.absolutePath}")
                return
            }

            val fileUri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(fileUri, mimeType)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant permission for the receiving app
            }

            // Verify if there's an app to handle the intent
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                println("No app available to open the file.")
            }
        }
        fun getMimeType(fileName: String): String? {
            val extension = fileName.substringAfterLast('.', "") // Extract the file extension
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
    }
}