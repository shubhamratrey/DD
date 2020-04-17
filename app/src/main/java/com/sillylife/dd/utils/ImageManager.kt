package com.sillylife.zoopzam.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.os.Build
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.sillylife.dd.DDApplication
import com.sillylife.dd.R
import com.sillylife.dd.utils.CommonUtil
import com.sillylife.dd.utils.svg.SvgSoftwareLayerSetter
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.ExecutionException

object ImageManager {

    private val context = DDApplication.getInstance()

    fun loadImage(imageView: ImageView, imageUrl: String?) {
        val url = imageUrl ?: ""
        load(
                imageView,
                url,
                0,
                getRequestOptions(imageView.drawable),
                null
        )
    }

    fun loadImageCircular(imageView: ImageView, imageUrl: String?) {
        val url = imageUrl ?: ""
        load(
                imageView,
                url,
                0,
                getRequestOptions(imageView.drawable),
                RequestOptions.circleCropTransform()
        )
    }

    fun loadImageBottom(imageView: ImageView, imageUrl: String?, listener: (Boolean) -> Unit) {
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_place_holder_episode)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        listener(true)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        listener(true)
                        return false
                    }

                }).into(imageView)
    }

    fun loadImageCircularBottom(imageView: ImageView, imageUrl: String?, listener: (Boolean) -> Unit) {
        Glide.with(context)
                .setDefaultRequestOptions(RequestOptions.circleCropTransform())
                .load(imageUrl)
                .placeholder(R.drawable.ic_place_holder_episode)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        listener(true)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        listener(true)
                        return false
                    }

                }).into(imageView)
    }

    fun loadImage(imageView: ImageView, resourceId: Int) {
        load(
                imageView,
                null,
                resourceId,
                getRequestOptions(resourceId),
                null
        )
    }

    fun loadImageFile(imageView: ImageView, file: File) {
        Glide.with(context).setDefaultRequestOptions(getRequestOptions(imageView.drawable)).load(file).into(imageView)
    }


    fun loadImageCircular(imageView: ImageView, resourceId: Int) {
        load(
                imageView,
                null,
                resourceId,
                getRequestOptions(resourceId),
                RequestOptions.circleCropTransform()
        )
    }

    fun loadImage(imageView: ImageView, imageUrl: String?, placeHolderId: Int) {
        val url = imageUrl ?: ""
        load(
                imageView,
                url,
                placeHolderId,
                getRequestOptions(placeHolderId),
                null
        )
    }

    fun loadImageCircular(imageView: ImageView, imageUrl: String?, placeHolderId: Int) {
        val url = imageUrl ?: ""
        load(
                imageView,
                url,
                placeHolderId,
                getRequestOptions(placeHolderId),
                RequestOptions.circleCropTransform()
        )
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    fun getBitmapSync(url: String, width: Int, height: Int): Bitmap {
        val stream = ByteArrayOutputStream()
        val bitmap = Glide.with(context)
                .asBitmap()
                .load(url)
                .submit(width, height).get()
        return if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
            Glide.with(context).asBitmap().load(stream.toByteArray()).submit(width, height).get()
        } else {
            bitmap
        }
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    fun getBitmapSync(url: String): Bitmap {
        val stream = ByteArrayOutputStream()
        val bitmap = Glide.with(context)
                .asBitmap()
                .load(url)
                .submit().get()
        return if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
            Glide.with(context).asBitmap().load(stream.toByteArray()).submit().get()
        } else {
            bitmap
        }
    }

    fun loadSVGImage(imageView: ImageView, imageUrl: String?) {
        val url = imageUrl ?: ""
        Glide.with(context).clear(imageView)
        Glide.with(context)
                .setDefaultRequestOptions(getRequestOptions(imageView.drawable))
                .`as`(PictureDrawable::class.java)
                .listener(SvgSoftwareLayerSetter())
                .load(url)
                .into(imageView)
    }

    fun loadSVGImageCircular(imageView: ImageView, imageUrl: String?) {
        val url = imageUrl ?: ""
        Glide.with(context).clear(imageView)
        Glide.with(context)
                .setDefaultRequestOptions(getRequestOptions(imageView.drawable))
                .`as`(PictureDrawable::class.java)
                .listener(SvgSoftwareLayerSetter())
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView)
    }

    fun loadImage(url: String, width: Int, height: Int, bitmapSimpleTarget: SimpleTarget<Bitmap>) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(getDefaultRequestOptions(context).override(width, height))
                .into(bitmapSimpleTarget)
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    fun getBitmapSync(url: String, context: Context): Bitmap {
        val stream = ByteArrayOutputStream()
        val bitmap = Glide.with(context)
                .asBitmap()
                .load(url)
                .submit().get()
        return if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
            Glide.with(context).asBitmap().load(stream.toByteArray()).submit().get()
        } else {
            bitmap
        }
    }

    private fun getDefaultRequestOptions(context: Context): RequestOptions {
        var requestOptions = RequestOptions()
        val progressDrawable = CircularProgressDrawable(context)
        progressDrawable.strokeWidth = 5f
        progressDrawable.centerRadius = 8f
        progressDrawable.start()
        requestOptions = requestOptions.placeholder(progressDrawable)
        //requestOptions = requestOptions.error(R.drawable.ku)
        return requestOptions
    }

    private fun getRequestOptions(drawable: Drawable?): RequestOptions {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.placeholder(drawable)
        requestOptions = requestOptions.error(drawable)
        return requestOptions
    }

    private fun getRequestOptions(placeHolderId: Int): RequestOptions {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.placeholder(placeHolderId)
        requestOptions = requestOptions.error(placeHolderId)
        return requestOptions
    }

    private fun load(
            imageView: ImageView, imageUrl: String?, placeHolderId: Int,
            defaultRequestOptions: RequestOptions, transformationRequestOptions: RequestOptions?
    ) {
        var newImageUrl = imageUrl
        if (isAndroidVersion4() && imageUrl != null) {
            val stringArray = imageUrl.split("https://")
            if (stringArray.size > 2) {
                "http://" + stringArray[2]
            } else if (stringArray.size == 2) {
                "http://" + stringArray[1]
            }
        }
        Glide.with(context).clear(imageView)
        var requestManager = Glide.with(context)
        requestManager = requestManager.setDefaultRequestOptions(defaultRequestOptions)
        var requestBuilder: RequestBuilder<Drawable>? = null
        var signature: ObjectKey? = null
        if (!CommonUtil.textIsEmpty(newImageUrl)) {
            requestBuilder = requestManager.load(newImageUrl)
            signature = ObjectKey(newImageUrl as Any)
        }
        if (placeHolderId > 0) {
            requestBuilder = requestManager.load(placeHolderId)
            signature = ObjectKey(placeHolderId as Any)
        }
        if (requestBuilder != null) {
            if (transformationRequestOptions != null) {
                requestBuilder = requestBuilder.apply(transformationRequestOptions)
            }
            /*requestBuilder.signature(signature!!)*/
            requestBuilder.into(imageView)
        } else {
            Glide.with(context).setDefaultRequestOptions(defaultRequestOptions).load(newImageUrl).into(imageView)
        }

    }

    fun isAndroidVersion4(): Boolean {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            return true
        }
        return false
    }

}