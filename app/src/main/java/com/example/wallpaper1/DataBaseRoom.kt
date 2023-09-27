package com.example.wallpaper1

import androidx.room.*
import android.content.Context

@Entity(tableName = "categories")
data class CategoryDBEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id:Int,
    @ColumnInfo(name = "category")
    val category:String
)

@Entity(
    tableName = "images",
    foreignKeys = [
        ForeignKey(
            entity = CategoryDBEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class ImageDBEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id:Int,
    @ColumnInfo(name = "categoryId")
    val categoryId:Int,
    @ColumnInfo(name = "url")
    val url:String,
    @ColumnInfo(name = "saved")
    val saved:Boolean,
    @ColumnInfo(name = "favorite")
    val favorite:Boolean
)



@Dao
interface DaoDataBase{
    @Query("SELECT * FROM categories")
    fun getCategories():List<CategoryDBEntity>

    @Query("SELECT url FROM images WHERE categoryId = :categoryId")
    fun getUrls(categoryId: Int):List<String>

    @Query("SELECT * FROM images WHERE url = :url")
    fun getUrl(url: String):List<ImageDBEntity>

    @Query("SELECT url FROM images WHERE saved = 1")
    fun getSaved():List<String>

    @Query("UPDATE images SET saved = 1 WHERE id = :id")
    fun addImageToSaved(id: Int):Int

    @Query("UPDATE images SET saved = 0 WHERE id = :id")
    fun deleteImageFromSaved(id: Int):Int

    @Query("SELECT url FROM images WHERE favorite = 1")
    fun getFavorite():List<String>

    @Query("UPDATE images SET favorite = 1 WHERE id = :id")
    fun addImageToFavorite(id: Int):Int

    @Query("UPDATE images SET favorite = 0 WHERE id = :id")
    fun deleteImageFromFavorite(id: Int):Int

    @Insert
    fun insertCategory(category: CategoryDBEntity):Long

    @Insert
    fun insertUrl(url: ImageDBEntity):Long

    @Query("DELETE FROM categories")
    fun deleteAll()
}

@Database(entities = [CategoryDBEntity::class, ImageDBEntity::class], version = 3)
abstract class DataBaseRoom: RoomDatabase(){
    abstract fun dao(): DaoDataBase

    companion object{
        private const val DATABASE_NAME = "Images.db"

        fun getInstance(appContext: Context): DataBaseRoom {
            return Room.databaseBuilder(appContext, DataBaseRoom::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}