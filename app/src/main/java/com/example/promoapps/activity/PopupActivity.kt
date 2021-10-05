package com.example.promoapps.activity

import android.app.Activity
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.example.promoapps.R
import com.example.promoapps.adapter.Helper
import com.google.zxing.WriterException

class PopupActivity : Activity() {
    private lateinit var imgQR: ImageView

    private lateinit var promoid: String
    private lateinit var userid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_qr_code)

        window.setLayout(970, 1100)

        imgQR = findViewById(R.id.img_qr_code)

        promoid = intent.getStringExtra(Helper.PROMOID).toString()
        userid = intent.getStringExtra(Helper.USER).toString()

        val data = "$promoid*$userid"
        val qrGEncoder = QRGEncoder(data, null, QRGContents.Type.TEXT, 1000)
        try {
            val qrBitmap = qrGEncoder.bitmap
            imgQR.setImageBitmap(qrBitmap)
        } catch (e:WriterException){
            Log.v(TAG, e.toString())
        }
    }
}