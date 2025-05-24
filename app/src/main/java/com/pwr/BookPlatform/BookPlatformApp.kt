package com.pwr.BookPlatform

import android.app.Application
import com.pwr.BookPlatform.data.session.UserSession

class BookPlatformApp : Application() {
    override fun onCreate() {
        super.onCreate()
        UserSession.init(this)
        UserSession.getAuthToken()
    }
}