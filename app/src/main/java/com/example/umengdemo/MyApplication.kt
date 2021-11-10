package com.example.umengdemo

import android.app.Application

/**
@author chenjie
@description:
@date : 2021/11/8 12:44 上午
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ComAbility.init(this,null)
    }

}