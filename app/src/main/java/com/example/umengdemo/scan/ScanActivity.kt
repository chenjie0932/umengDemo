package com.example.umengdemo.scan

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.wifi.ScanResult
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.umengdemo.R
import com.huawei.hms.hmsscankit.RemoteView
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions

class ScanActivity : AppCompatActivity() {
    private val TAG = "ScanActivity"
    companion object {
        const val REQUEST_CODE_SELECT_PHOTO = 100
    }
    private lateinit var remoteView: RemoteView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        val container = findViewById<FrameLayout>(R.id.container)
        val flush_btn = findViewById<ImageView>(R.id.flush_btn)
        val select_photo = findViewById<ImageView>(R.id.select_photo)

        // 设置扫码识别区域，您可以按照需求调整参数。
        val dm = getResources().getDisplayMetrics()
        val density = dm.density
        var mScreenWidth = getResources().getDisplayMetrics().widthPixels
        var mScreenHeight = getResources().getDisplayMetrics().heightPixels
        // 当前Demo扫码框的宽高是300dp。
        val SCAN_FRAME_SIZE = 300
        val scanFrameSize = (SCAN_FRAME_SIZE * density).toInt()
        val rect = Rect()
        rect.left = (mScreenWidth / 2 - scanFrameSize / 2).toInt()
        rect.right = (mScreenWidth / 2 + scanFrameSize / 2).toInt()
        rect.top = (mScreenHeight / 2 - scanFrameSize / 2).toInt()
        rect.bottom = (mScreenHeight / 2 + scanFrameSize / 2).toInt()
        // 初始化RemoteView，并通过如下方法设置参数:setContext()（必选）传入context、setBoundingBox()设置扫描区域、setFormat()设置识别码制式，设置完毕调用build()方法完成创建。通过setContinuouslyScan（可选）方法设置非连续扫码模式。
        remoteView =  RemoteView.Builder().setContext(this).setBoundingBox(rect).setContinuouslyScan(false).setFormat(HmsScan.QRCODE_SCAN_TYPE, HmsScan.DATAMATRIX_SCAN_TYPE).build()
        // 将自定义view加载到activity的frameLayout中。
        remoteView.onCreate(savedInstanceState)
        val params = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        container.addView(remoteView, params)

        remoteView.setOnLightVisibleCallback { visible ->
            flush_btn.visibility = if (visible) View.VISIBLE else View.GONE
        }

        remoteView.setOnResultCallback { results ->
            if (results != null && results.isNotEmpty() && results[0] != null && !TextUtils.isEmpty(
                    results[0].originalValue
                )
            ) {
                showResult(results[0])
            }
        }


        flush_btn.setOnClickListener {
            if (remoteView.lightStatus) {
                remoteView.switchLight()
                flush_btn.setImageResource(R.drawable.scan_flashlight_off)
            } else {
                remoteView.switchLight()
                flush_btn.setImageResource(R.drawable.scan_flashlight_on)
            }
        }

        select_photo.setOnClickListener {
            val pickIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(pickIntent, REQUEST_CODE_SELECT_PHOTO)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_SELECT_PHOTO && data != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
            val results = ScanUtil.decodeWithBitmap(
                this,
                bitmap,
                HmsScanAnalyzerOptions.Creator().setPhotoMode(true).create()
            )
            if (results != null && results.isNotEmpty() && results[0] != null && !TextUtils.isEmpty(
                    results[0].originalValue
                )
            ) {
                showResult(results[0])
            }
        }
    }

    private fun showResult(hmsScan: HmsScan) {
        Log.i(TAG,"scan_type:" + hmsScan.scanType + ",scan_reuslt：" + hmsScan.originalValue)
        if (hmsScan.getScanTypeForm() ==  HmsScan.SMS_FORM) {
            // 解析成SMS的结构化数据
            val smsContent = hmsScan.getSmsContent()
            val content = smsContent.getMsgContent()
            val phoneNumber = smsContent.getDestPhoneNumber()
        } else if (hmsScan.getScanTypeForm() == HmsScan.WIFI_CONNECT_INFO_FORM){
            // 解析成Wi-Fi的结构化数据
            val wifiConnectionInfo = hmsScan.getWiFiConnectionInfo()
            val password = wifiConnectionInfo.getPassword()
            val ssidNumber = wifiConnectionInfo.getSsidNumber()
            val cipherMode = wifiConnectionInfo.getCipherMode()

        }
        finish()
    }


    override fun onStart() {
        super.onStart()
        remoteView.onStart()
    }

    override fun onResume() {
        super.onResume()
        remoteView.onResume()
    }

    override fun onPause() {
        super.onPause()
        remoteView.onPause()
    }

    override fun onStop() {
        super.onStop()
        remoteView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        remoteView.onDestroy()
    }

}