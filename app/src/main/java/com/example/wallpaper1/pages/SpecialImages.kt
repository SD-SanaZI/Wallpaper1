package com.example.wallpaper1.pages

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.wallpaper1.FragmentEvents
import com.example.wallpaper1.MainViewModel
import com.example.wallpaper1.databinding.FragmentCategoriesBinding
import com.example.wallpaper1.ui.theme.Wallpaper1Theme

private const val ARG_PARAM1 = "param1"

class SpecialImages : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private var fragmentEvents: FragmentEvents? = null
    private lateinit var viewModel: MainViewModel
    private var keyString: String? = null

    companion object {
        fun newInstance(id: String) = SpecialImages().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, id)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FragmentEvents)
            fragmentEvents = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            keyString = it.getString(ARG_PARAM1)
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
        val imageUrlList: List<String> = when(keyString){
            "Saved" -> {
                viewModel.database?.getSaved() ?: throw Exception("Nullable data base")
            }
            "Favorite" -> {
                viewModel.database?.getFavorite() ?: throw Exception("Nullable data base")
            }
            else -> throw Exception("Wrong key")
        }
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val darkTheme = remember{ mutableStateOf(viewModel.darkTheme.value) }
                Wallpaper1Theme(darkTheme.value?:false) {
                    Column(modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .background(MaterialTheme.colors.background)
                    ){
                        imageUrlList.chunked(2).forEach { pair ->
                            Row {
                                pair.forEach {
                                    AsyncImage(
                                        model = it,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .clickable(onClick = { fragmentEvents?.openImage(it) })
                                            .width(200.dp)
                                            .height(355.dp)
                                            .padding(10.dp)
                                            .clip(shape = MaterialTheme.shapes.large)
                                            .background(MaterialTheme.colors.onSecondary),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
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

    override fun onDetach() {
        super.onDetach()
        fragmentEvents =  null
    }
}