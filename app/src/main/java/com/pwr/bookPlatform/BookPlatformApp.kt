package com.pwr.bookPlatform

import android.app.Application
import com.pwr.bookPlatform.data.session.UserSession

class BookPlatformApp : Application() {
    override fun onCreate() {
        super.onCreate()
        UserSession.init(this)
        UserSession.getAuthToken()
    }
}