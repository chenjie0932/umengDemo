package com.example.umengdemo.push

import android.app.Activity
import android.app.Application
import android.app.Notification
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.example.umengdemo.help.PushConstants
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import com.umeng.message.UmengMessageHandler
import com.umeng.message.UmengNotificationClickHandler
import com.umeng.message.entity.UMessage
import org.json.JSONObject

/**
@author chenjie
@description:
@date : 2021/11/8 12:47 上午
 */
object PushInitialization {
    private val TAG = "PushInitialization"
    fun init(application: Application, iPushMessageHandle: IPushMessageHandle? = null) {
        initUmengPushSdk(application, iPushMessageHandle)
    }

    // 基础组件包提供的初始化函数，应用配置信息：http://message.umeng.com/list/apps
    // 参数一：上下文context；
    // 参数二：应用申请的Appkey；
    // 参数三：发布渠道名称；
    // 参数四：设备类型，UMConfigure.DEVICE_TYPE_PHONE：手机；UMConfigure.DEVICE_TYPE_BOX：盒子；默认为手机
    // 参数五：Push推送业务的secret，填写Umeng Message Secret对应信息
    fun initUmengPushSdk(application: Application, iPushMessageHandle: IPushMessageHandle? = null) {
        UMConfigure.init(
            application, PushConstants.APP_KEY, PushConstants.CHANNEL,
            UMConfigure.DEVICE_TYPE_PHONE, PushConstants.MESSAGE_SECRET
        )

        //获取推送实例
        val pushAgent = PushAgent.getInstance(application)

        //TODO:需修改为您app/src/main/AndroidManifest.xml中package值
        pushAgent.resourcePackageName = "com.example.umengdemo"

        //注册推送服务，每次调用register方法都会回调该接口
        pushAgent.register(object : IUmengRegisterCallback {
            override fun onSuccess(deviceToken: String) {
                Log.i(TAG, "deviceToken --> $deviceToken")
                iPushMessageHandle?.registerOnSuccess(deviceToken)

            }

            override fun onFailure(p0: String?, p1: String?) {
              //  iPushMessageHandle?.registerOnSuccess(deviceToken)
                Log.e(TAG, "register failure：--> code:$p0,desc:$p1")
            }

        })
        //推送消息处理
        val msgHandler: UmengMessageHandler = object : UmengMessageHandler() {
            //处理通知栏消息
            override fun dealWithNotificationMessage(context: Context, msg: UMessage) {
                super.dealWithNotificationMessage(context, msg)
                Log.i(TAG, "notification receiver:" + msg.raw.toString())
                if (iPushMessageHandle != null) {
                    iPushMessageHandle.dealWithNotificationMessage(context, JSONObject())
                }

            }

            //自定义通知样式，此方法可以修改通知样式等
            override fun getNotification(context: Context, msg: UMessage): Notification {
                return super.getNotification(context, msg)
            }

            //处理透传消息
            override fun dealWithCustomMessage(context: Context, msg: UMessage) {
                super.dealWithCustomMessage(context, msg)
                if (iPushMessageHandle != null) {
                    iPushMessageHandle.dealWithCustomMessage(context, JSONObject())
                }
                Log.i(TAG, "custom receiver:" + msg.raw.toString())
            }
        }
        pushAgent.messageHandler = msgHandler
        //推送消息点击处理

        //推送消息点击处理
        val notificationClickHandler: UmengNotificationClickHandler =
            object : UmengNotificationClickHandler() {
                override fun openActivity(context: Context, msg: UMessage) {
                    super.openActivity(context, msg)
                    if (iPushMessageHandle != null) {
                        iPushMessageHandle.openActivity(context, JSONObject())
                    }
                    Log.i(
                        TAG,
                        "click openActivity: " + msg.raw.toString()
                    )
                }

                override fun launchApp(context: Context, msg: UMessage) {
                    super.launchApp(context, msg)
                    Log.i(
                        TAG,
                        "click launchApp: " + msg.raw.toString()
                    )
                }

                override fun dismissNotification(context: Context, msg: UMessage) {
                    super.dismissNotification(context, msg)
                    Log.i(
                        TAG,
                        "click dismissNotification: " + msg.raw.toString()
                    )
                }
            }
        pushAgent.notificationClickHandler = notificationClickHandler
        application.registerActivityLifecycleCallbacks(object :SimpleLifecycleCallbacks(){
            override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
                super.onActivityPreCreated(activity, savedInstanceState)
                PushAgent.getInstance(activity.application).onAppStart()
            }
        })
    }
}