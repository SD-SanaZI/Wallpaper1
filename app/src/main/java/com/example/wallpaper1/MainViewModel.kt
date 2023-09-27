package com.example.wallpaper1

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.net.URL

class MainViewModel : ViewModel() {
    private var fragmentNum:Int = 0
    private var _onSettings: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val onSettings : LiveData<Boolean> = _onSettings
    private var _name: MutableLiveData<String> = MutableLiveData<String>(getName())
    val name : LiveData<String> = _name
    private var _darkTheme: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val darkTheme : LiveData<Boolean> = _darkTheme

    var database: DBManager? = null

    fun initDB(context: Context){
        database = DBManager(context)
        if (database?.getCategories()?.size == 0) {
            CoroutineScope(Dispatchers.IO).launch {
                database?.downloadDB()
            }
        }
    }

    fun setDarkThemeOn(){
        _darkTheme.value = true
        Log.d("Dark","+")
    }

    fun setDarkThemeOff(){
        _darkTheme.value = false
        Log.d("Dark","-")
    }

    fun getFragmentNum():Int{
        return fragmentNum
    }

    fun onNewFragmentCreate(){
        fragmentNum++
        _name.value = getName()
    }

    fun onNewFragmentDestroy(){
        fragmentNum--
        _name.value = getName()
    }

    fun settingsOn(){
        _onSettings.value = true
        _name.value = getName()
    }

    fun settingsOff(){
        _onSettings.value = false
        _name.value = getName()
    }

    private fun getName(): String{
        return when(_onSettings.value){
            true -> "Settings"
            false -> when(fragmentNum){
                0 -> "Categories"
                1 -> "Images in category"
                2 -> "Image"
                else -> throw Exception("Wrong fragment number $fragmentNum")
            }
            else -> throw Exception("Boolean onSettings is not true or false")
        }
    }

    fun saveImg(url:String?){
        if((database?.getUrlInfo(url ?: "")?.saved == false)){
            CoroutineScope(Dispatchers.IO).launch {
                val byteArrayUrl = URL(url).readBytes()
                val bitmap = BitmapFactory.decodeByteArray(byteArrayUrl, 0, byteArrayUrl.size)
                val name = database?.getUrlName(url ?: "")
                    ?: "${System.currentTimeMillis()}.png"
                ImageManager.download(bitmap, name)
                val id = database?.getUrlInfo(url ?: "")?.id
                if (id != null) database?.setSaved(id, true)
            }
        }
    }

    fun setImageToWallpaper(url:String?, context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            val byteArrayUrl = URL(url).readBytes()
            val bitmap = BitmapFactory.decodeByteArray(
                byteArrayUrl,
                0,
                byteArrayUrl.size
            )
            WallpaperManager
                .getInstance(context)
                .setBitmap(
                    BitmapRebuild(bitmap).getCrop(
                        1980,
                        1080
                    )
                )
        }
    }
}