package com.example.wallpaper1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.wallpaper1.pages.*

class MainActivity : AppCompatActivity(), FragmentEvents {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.initDB(this)
        setContentView(R.layout.activity_main)
        findViewById<ImageView>(R.id.imageView).setOnClickListener{
            viewModel.settingsOn()
            supportFragmentManager.beginTransaction()
                .add(R.id.container, Settings.newInstance())
                .addToBackStack(null)
                .commit()
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, Categories.newInstance())
                .commitNow()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.name.observe(this) { name ->
            findViewById<TextView>(R.id.textView2)?.text = name
        }
        viewModel.onSettings.observe(this) {
            findViewById<ImageView>(R.id.imageView)?.visibility = if (it) INVISIBLE else VISIBLE
        }
    }

    override fun openCategoryImages(id: Int) {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, CategoryImages.newInstance(id), "CategoryImages")
            .addToBackStack(null)
            .commit()
    }

    override fun openSpecialImages(key: String) {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, SpecialImages.newInstance(key), "SpecialImages")
            .addToBackStack(null)
            .commit()
    }

    override fun openImage(url:String) {
        if(viewModel.getFragmentNum() == 1){
            supportFragmentManager.beginTransaction()
                .add(R.id.container, Image.newInstance(url), "Image")
                .addToBackStack(null)
                .commit()
        }
    }
}

interface FragmentEvents{
    fun openCategoryImages(id: Int)
    fun openSpecialImages(key: String)
    fun openImage(url:String)
}