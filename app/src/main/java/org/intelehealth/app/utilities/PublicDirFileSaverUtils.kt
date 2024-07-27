package org.intelehealth.app.utilities

import android.R.attr.mimeType
import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import org.intelehealth.app.app.IntelehealthApplication
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


/**
 * Created By Tanvir Hasan on 7/25/24 10:24 AM
 * Email: tanvirhasan553@gmail.com
 */
class PublicDirFileSaverUtils {
    companion object {
        @JvmStatic
        fun savePdf(
            fileName: String,
            filePath: String,
            environment: String
        ): Uri {

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, environment)
            }
            val inputSteam = File(filePath).inputStream()
            val resolver: ContentResolver = IntelehealthApplication.getAppContext().contentResolver
            var stream: OutputStream? = null
            var uri: Uri? = null

            try {
                val contentUri = MediaStore.Files.getContentUri("external")
                uri = resolver.insert(contentUri, contentValues)
                val pfd: ParcelFileDescriptor
                try {
                    checkNotNull(uri)
                    pfd = IntelehealthApplication.getAppContext().contentResolver.openFileDescriptor(uri, "w")!!
                    val out = FileOutputStream(pfd.fileDescriptor)

                    val buf = ByteArray(4 * 1024)
                    var len: Int
                    while ((inputSteam.read(buf).also { len = it }) > 0) {
                        out.write(buf, 0, len)
                    }
                    out.close()
                    inputSteam.close()
                    pfd.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                contentValues.clear()
                contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
                if (uri != null) {
                    IntelehealthApplication.getAppContext().contentResolver.update(uri, contentValues, null, null)
                }
                stream = resolver.openOutputStream(uri!!)
                if (stream == null) {
                    throw IOException("Failed to get output stream.")
                }
                return uri
            } catch (e: IOException) {
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(uri!!, null, null)
                throw e
            } finally {
                stream?.close()
            }
        }
    }
}