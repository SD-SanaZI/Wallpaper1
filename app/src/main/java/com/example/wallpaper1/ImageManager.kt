package com.example.wallpaper1

import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.FileOutputStream


class ImageManager{
    companion object{
        //Сохранение изображения image на телефоне под именем {name}.png
        fun download(image:Bitmap, name:String){
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+ "/Wallpaper01")
            if(!path.exists()) path.mkdirs()
            val imageFile = File(path, name)
            val out = FileOutputStream(imageFile)
            image.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
        }
    }
}