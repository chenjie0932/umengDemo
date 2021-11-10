package com.example.umengdemo.push

import android.content.Context
import com.umeng.message.entity.UMessage
import org.json.JSONObject

/**
@author chenjie
@description:
@date : 2021/11/8 1:25 上午
 */
interface IPushMessageHandle {
    fun openActivity(context: Context, msg: JSONObject){

    }
    fun dealWithCustomMessage(context: Context, msg: JSONObject){

    }
    fun dealWithNotificationMessage(context: Context, msg: JSONObject){

    }
   fun  registerOnSuccess(deviceToken: String){

   }
}