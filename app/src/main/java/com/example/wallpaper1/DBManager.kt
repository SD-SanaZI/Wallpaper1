package com.example.wallpaper1

import android.content.Context
import android.util.Log

class DBManager(context: Context) {
    private var db:DataBaseRoom = DataBaseRoom.getInstance(context)

    fun downloadDB(){
        val categories = listOf(
            CategoryDBEntity(1,"Fire"),
            CategoryDBEntity(2,"Animal"),
            CategoryDBEntity(3,"Category3"),
            CategoryDBEntity(4,"Category4"),
            CategoryDBEntity(5,"Category5"),
            CategoryDBEntity(6,"Category6"),
            CategoryDBEntity(7,"Category7"),
            CategoryDBEntity(8,"Category8"),
            CategoryDBEntity(9,"Category9"),
            CategoryDBEntity(10,"Category10"),
        )
        val urls1 = listOf(
            "https://cdn.pixabay.com/photo/2016/11/01/20/15/flame-1789451_1280.jpg",
            "https://cdn.pixabay.com/photo/2016/10/30/18/24/heart-1783913_640.jpg",
            "https://cdn.pixabay.com/photo/2019/06/23/14/10/dream-4293806_640.jpg",
            "https://cdn.pixabay.com/photo/2014/06/01/20/41/match-359971_640.jpg",
            "https://cdn.pixabay.com/photo/2018/09/01/22/49/wallpaper-3647834_640.jpg",
            "https://cdn.pixabay.com/photo/2019/05/10/23/54/book-cover-4194807_640.jpg",
            "https://cdn.pixabay.com/photo/2017/03/31/23/27/fire-2192625_640.jpg",
            "https://cdn.pixabay.com/photo/2022/09/26/04/38/blacksmith-7479642_640.jpg",
            "https://cdn.pixabay.com/photo/2023/03/20/20/15/dragon-7865805_640.jpg"
            )
        val urls2 = listOf(
            "https://cdn.pixabay.com/photo/2023/03/20/20/15/dragon-7865805_640.jpg",
            "https://cdn.pixabay.com/photo/2018/04/20/17/18/cat-3336579_640.jpg",
            "https://cdn.pixabay.com/photo/2017/02/07/11/45/eagle-2045655_640.jpg"
        )

        categories.forEach {
            db.dao().insertCategory(it)
            Log.d("categories", it.toString())
        }
        Log.d("0", "0..10")
        urls1.forEach {
            db.dao().insertUrl(ImageDBEntity(0,1, it, saved = false,false))
            Log.d("1", it)
        }
        urls2.forEach {
            db.dao().insertUrl(ImageDBEntity(0,2,it, saved = false,false))
            Log.d("2", it)
        }
        for(i in 3..10){
            urls1.forEach {
                db.dao().insertUrl(ImageDBEntity(0,i,it, saved = false,false))
            }
        }
    }

    fun getCategories():List<CategoryDBEntity>{
        return db.dao().getCategories()
    }

    fun getUrlName(url:String):String{
        val info = db.dao().getUrl(url)
        return if(info.isNotEmpty()) "${info[0].id}_${info[0].categoryId}.png" else throw Error("Image not in the data base")
    }

    fun getUrlInfo(url:String):ImageDBEntity{
        val info = db.dao().getUrl(url)
        return if(info.isNotEmpty()) info[0] else throw Error("Image not in the data base")
    }

    fun getUrls(id: Int):List<String>{
        return  db.dao().getUrls(id)
    }

    fun deleteDB(){
        db.dao().deleteAll()
    }

    fun getSaved(): List<String>{
        return db.dao().getSaved()
    }

    fun setSaved(id: Int, saved:Boolean):Int{
        return if(saved) db.dao().addImageToSaved(id)
        else db.dao().deleteImageFromSaved(id)
    }

    fun getFavorite(): List<String>{
        return db.dao().getFavorite()
    }

    fun setFavorite(id: Int, favorite:Boolean):Int{
        return if(favorite) db.dao().addImageToFavorite(id)
        else db.dao().deleteImageFromFavorite(id)
    }
}