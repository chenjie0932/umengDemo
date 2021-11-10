package com.example.umengdemo.push

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.umeng.message.PushAgent

/**
@author chenjie
@description:
@date : 2021/11/8 1:43 上午
 */
open class SimpleLifecycleCallbacks:Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {

    }

    override fun onActivityStarted(p0: Activity) {
    }

    override fun onActivityResumed(p0: Activity) {
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }
}