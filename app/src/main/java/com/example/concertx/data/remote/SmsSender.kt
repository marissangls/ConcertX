package com.example.concertx.data.remote

import android.content.Context
import android.os.Build
import android.telephony.SmsManager

object SmsSender {

    fun sendOtp(context: Context, phone: String, otp: String): Boolean {
        return try{
            val smsManager = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                context.getSystemService(SmsManager::class.java)
            }else{
                @Suppress("DEPRECATION")
                SmsManager.getDefault()
            }

            val message = "Your OTP code is $otp. It expires in 3 minutes."
            smsManager.sendTextMessage(phone, null, message, null, null)
            true
        }catch(e: Exception){
            false
        }
    }
}