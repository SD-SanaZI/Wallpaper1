package com.example.wallpaper1.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.wallpaper1.MainViewModel
import com.example.wallpaper1.databinding.FragmentCategoriesBinding
import com.example.wallpaper1.ui.theme.Wallpaper1Theme

private const val ARG_PARAM1 = "param1"

class Image : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private var url: String? = null

    companion object {
        fun newInstance(url:String) = Image().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, url)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(ARG_PARAM1)
        }
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        viewModel.onNewFragmentCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val darkTheme = remember{ mutableStateOf(viewModel.darkTheme.value) }
                Wallpaper1Theme(darkTheme.value?:false){
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .background(MaterialTheme.colors.background),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        AsyncImage(
                            model = url,
                            contentDescription = "",
                            modifier = Modifier
                                .width(300.dp)
                                .height(533.dp)
                                .padding(10.dp)
                                .clip(shape = MaterialTheme.shapes.large)
                                .background(MaterialTheme.colors.onSecondary),
                            contentScale = ContentScale.Crop
                        )
                        Row{
                            val areFavorite = remember {
                                mutableStateOf(viewModel.database?.getUrlInfo(url?:"")?.favorite ?: false)
                            }
                            val drawableList = listOf(android.R.drawable.star_big_off,android.R.drawable.star_big_on)
                            val image = remember {
                                mutableStateOf(drawableList[if(areFavorite.value) 1 else 0])
                            }
                            Image(
                                painterResource(image.value),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clickable(
                                        onClick = {
                                            val id = viewModel.database?.getUrlInfo(url ?: "")?.id ?: 0
                                            viewModel.database?.setFavorite(id, !areFavorite.value)
                                            areFavorite.value = !areFavorite.value
                                            image.value = drawableList[if(areFavorite.value) 1 else 0]
                                        }
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .width(240.dp)
                                    .height(80.dp)
                                    .padding(10.dp)
                                    .clip(shape = MaterialTheme.shapes.large)
                                    .clickable(
                                        onClick = {
                                            try {
                                                viewModel.setImageToWallpaper(url,context)
                                                Toast.makeText(
                                                    context,
                                                    "The image is set to wallpaper",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }catch (e:Exception){
                                                Toast.makeText(
                                                    context,
                                                    "Error: $e",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    )
                                    .background(MaterialTheme.colors.onSecondary),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    "Set image",
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colors.onPrimary,
                                )
                            }
                            Image(
                                painterResource(id = android.R.drawable.ic_menu_save),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clickable(
                                        onClick = {
                                            try {
                                                viewModel.saveImg(url)
                                                Toast.makeText(
                                                    context,
                                                    "Image saved",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }catch (e:Exception){
                                                Toast.makeText(
                                                    context,
                                                    "Error: $e",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    )
                            )
                        }
                    }
                }
                viewModel.darkTheme.observe(viewLifecycleOwner) {
                    darkTheme.value = it
                }
            }
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onNewFragmentDestroy()
    }
}