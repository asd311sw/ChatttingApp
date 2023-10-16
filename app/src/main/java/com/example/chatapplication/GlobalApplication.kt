package com.example.chatapplication

import android.app.Application
import com.google.firebase.BuildConfig
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.common.KakaoSdk
class GlobalApplication:Application() {

    override fun onCreate() {
        super.onCreate()


        KakaoSdk.init(applicationContext, "b2e2433a0a4c22219f142a3a2c654346")

    }
}