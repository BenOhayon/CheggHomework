package homework.chegg.com.chegghomework

import android.app.Application
import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration

class CheggHomeAssignmentApplication : Application() {

    companion object {
        lateinit var context : Context
    }

    override fun onCreate() {
        super.onCreate()

        // Initializing AndroidNetworking library
        AndroidNetworking.initialize(this)

        // Initializing Universal Image Loader library
        val displayImageOptions = DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .build()
        val config = ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(displayImageOptions)
                .build()
        ImageLoader.getInstance().init(config)

        context = this
    }
}