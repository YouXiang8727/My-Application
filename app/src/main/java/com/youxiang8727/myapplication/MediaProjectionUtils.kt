package com.youxiang8727.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build.VERSION.SDK_INT
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import android.view.Display
import androidx.annotation.MainThread
import androidx.core.hardware.display.DisplayManagerCompat
import com.elvishew.xlog.XLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MediaProjectionUtils private constructor(
    val context: Context,
    resultCode: Int,
    data: Intent
){
    companion object {
        private var INSTANCE: MediaProjectionUtils? = null

        fun init(
            context: Context,
            resultCode: Int,
            data: Intent
        ) {
            if (INSTANCE == null) {
                INSTANCE = MediaProjectionUtils(
                    context,
                    resultCode,
                    data
                )
            }
        }

        fun getInstance(): MediaProjectionUtils = synchronized(this) {
            INSTANCE ?: throw IllegalStateException("MediaProjectionUtils not initialized")
        }
    }

    private val defaultDisplay =
        DisplayManagerCompat.getInstance(context).getDisplay(Display.DEFAULT_DISPLAY)

    private val displayContext = context.createDisplayContext(defaultDisplay!!)

    private val width = displayContext.resources.displayMetrics.widthPixels

    private val height = displayContext.resources.displayMetrics.heightPixels

    private val dpi = displayContext.resources.displayMetrics.densityDpi

    private val mediaProjectionManager = context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

    private val imageReader by lazy {
        ImageReader.newInstance(
            width, height, PixelFormat.RGBA_8888, 2
        )
    }

    private val mediaProjection = mediaProjectionManager.getMediaProjection(
        resultCode,
        data
    ).also {
        it.registerCallback(object : MediaProjection.Callback() {}, null)
    }

    private val virtualDisplay = mediaProjection
        .createVirtualDisplay(
            "screen-shot",
            width,
            height,
            dpi,
            16,
            imageReader.surface,
            null,
            null
        )

    fun getScreenshot(
        saveImage: Boolean = false
    ): Bitmap? {
        return runBlocking(Dispatchers.Main.immediate) {
            val image = imageReader.acquireLatestImage() ?: return@runBlocking null
            val width = image.width
            val height = image.height
            val planes = image.planes
            val buffer = planes[0].buffer
            val pixelStride = planes[0].pixelStride
            val rowStride = planes[0].rowStride
            val rowPadding = rowStride - pixelStride * width
            var bitmap = Bitmap.createBitmap(
                width + rowPadding / pixelStride,
                height,
                Bitmap.Config.ARGB_8888
            )
            bitmap.copyPixelsFromBuffer(buffer)
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height)
            if (saveImage && SDK_INT >= 30) {
                val storageManager: StorageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
                val storageVolume: StorageVolume = storageManager.storageVolumes[0]
                val file = File("${storageVolume.directory?.path}${File.separator}Download${File.separator}${System.currentTimeMillis()}.jpg")
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                val byteArray = bos.toByteArray()
                val fos = FileOutputStream(file)
                fos.write(byteArray)
                fos.close()
                bos.close()
            }
            image.close()
            return@runBlocking bitmap
        }
    }

    fun onDestroy() {
        virtualDisplay.release()
        mediaProjection.stop()
    }
}