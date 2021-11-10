package com.example.umengdemo

import android.app.Application
import com.example.umengdemo.push.IPushMessageHandle
import com.example.umengdemo.push.PushInitialization

/**
@author chenjie
@description:
@date : 2021/11/8 1:51 上午
 */
object ComAbility {
   fun init (application:Application, iPushMessageHandle: IPushMessageHandle? = null){
       PushInitialization.init(application ,iPushMessageHandle)
   }
}