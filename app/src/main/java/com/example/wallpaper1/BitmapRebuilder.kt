package com.example.wallpaper1

import android.graphics.Bitmap

class BitmapRebuild(private val bitmap: Bitmap) {
    fun getCrop(height: Int, width: Int):Bitmap{
        val newHeight: Int
        val newWidth: Int
        when(listOf(height > bitmap.height , width > bitmap.width)){
            listOf(false,false) -> {
                newHeight = bitmap.height
                newWidth = bitmap.width
            }
            listOf(false,true) -> {
                newHeight = (height * (bitmap.width.toFloat()/width)).toInt()
                newWidth = bitmap.width
            }
            listOf(true,false) -> {
                newHeight = bitmap.height
                newWidth = (width * (bitmap.height.toFloat()/height)).toInt()
            }
            listOf(true,true) -> {
                if(height*bitmap.width > width*bitmap.height){
                    newHeight = bitmap.height
                    newWidth = (width * (bitmap.height.toFloat()/height)).toInt()
                }else{
                    newHeight = (height * (bitmap.width.toFloat()/width)).toInt()
                    newWidth = bitmap.width
                }
            }
            else -> throw Exception("WTF on BitmapRebuild")
        }
        val bmUpRightPartial = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
        val pixels = IntArray(newHeight * newWidth)
        bitmap.getPixels(pixels, 0, newWidth, (bitmap.width-newWidth)/2, (bitmap.height-newHeight)/2, newWidth, newHeight)
        bmUpRightPartial.setPixels(pixels, 0, newWidth, 0, 0, newWidth, newHeight)
        return bmUpRightPartial
    }
}