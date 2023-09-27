package com.example.wallpaper1.pages

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
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wallpaper1.MainViewModel
import com.example.wallpaper1.databinding.FragmentCategoriesBinding
import com.example.wallpaper1.ui.theme.Wallpaper1Theme

class Settings: Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance() = Settings()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
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
                        modifier = Modifier.verticalScroll(rememberScrollState())
                            .background(MaterialTheme.colors.background),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Row{
                            Box(modifier = Modifier
                                .weight(1f)
                                .height(80.dp)
                                .padding(10.dp)
                                .clip(shape = MaterialTheme.shapes.large)
                                .clickable(onClick = { viewModel.setDarkThemeOff()})
                                .background(MaterialTheme.colors.onSecondary),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    "Light",
                                    textAlign = TextAlign.Center,
                                    fontSize = MaterialTheme.typography.body1.fontSize,
                                    color = MaterialTheme.colors.onPrimary
                                )
                            }
                            Box(modifier = Modifier
                                .weight(1f)
                                .height(80.dp)
                                .padding(10.dp)
                                .clip(shape = MaterialTheme.shapes.large)
                                .clickable(onClick = { viewModel.setDarkThemeOn()})
                                .background(MaterialTheme.colors.onSecondary),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    "Dark",
                                    textAlign = TextAlign.Center,
                                    fontSize = MaterialTheme.typography.body1.fontSize,
                                    color = MaterialTheme.colors.onPrimary
                                )
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

    override fun onDetach() {
        super.onDetach()
        viewModel.settingsOff()
    }
}