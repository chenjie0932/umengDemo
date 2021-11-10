package com.example.umengdemo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.umengdemo.scan.ScanActivity
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val scan_btn = findViewById<Button>(R.id.scan_btn)
        val pay_btn = findViewById<Button>(R.id.pay_btn)
        scan_btn.setOnClickListener {
            requestPermissions()
            startActivity(Intent(this, ScanActivity::class.java))
            // CAMERA_REQ_CODE为用户自定义，用于接收权限校验结果的请求码。
          //  ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), CAMERA_REQ_CODE)
            Log.i(TAG, "点击了")
        }
        pay_btn.setOnClickListener {


        }

    }
    /**
     * 请求权限
     */
    @SuppressLint("CheckResult")
    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT <= 23) {
            Log.i(TAG,"old version android sdk=${Build.VERSION.SDK_INT} to old permission")
            return
        }
        val rxPermissions = RxPermissions(this)
        rxPermissions.requestEach(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.CAMERA,
            "android.hardware.camera",
            "android.hardware.camera.autofocus",
            "android.hardware.camera.flash",
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_CONFIGURATION,
            Manifest.permission.ACCOUNT_MANAGER,
            Manifest.permission.CAPTURE_AUDIO_OUTPUT,
            Manifest.permission.INTERNET,
            Manifest.permission.REORDER_TASKS,
            Manifest.permission.BROADCAST_STICKY,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.VIBRATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WAKE_LOCK
        )
            .subscribe { permission: Permission ->
                when {
                    permission.granted -> {
                        // 用户已经同意该权限,
                        Log.i(TAG, "权限申请${permission.name} is granted.")
                    }
                    permission.shouldShowRequestPermissionRationale -> {
                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        Log.i(TAG, "权限申请${permission.name} is denied. More info should be provided.")
                    }
                    else -> { // 用户拒绝了该权限，并且选中『不再询问』
                        Log.i(TAG, "权限申请${permission.name} is denied.")
                    }
                }
            }
    }
}