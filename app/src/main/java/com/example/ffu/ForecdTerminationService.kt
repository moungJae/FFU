package com.example.ffu

import android.app.Service
import android.content.Intent
import android.os.IBinder

// onDestroy 가 정상적으로 실행되도록 구현한 클래스
class ForecdTerminationService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        stopSelf()
    }
}