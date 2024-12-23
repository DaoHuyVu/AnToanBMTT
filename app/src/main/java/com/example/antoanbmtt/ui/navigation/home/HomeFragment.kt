package com.example.antoanbmtt.ui.navigation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.antoanbmtt.adapter.ResourceAdapter
import com.example.antoanbmtt.databinding.FragmentHomeBinding
import com.example.antoanbmtt.fragment.ResourceDetailsBottomSheetFragment
import com.example.antoanbmtt.helper.Util
import com.example.antoanbmtt.helper.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var launcher : ActivityResultLauncher<Array<String>>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        launcher = registerForActivityResult(ActivityResultContracts.OpenDocument()){uri ->
            uri?.let{
                val file = Util.getPartFromUri(it,requireActivity().contentResolver)
                viewModel.pushResource(file)
            }
        }
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        binding.apply {
            refreshLayout.setOnRefreshListener {
                viewModel.getResources()
                refreshLayout.isRefreshing = false
            }
            fab.setOnClickListener{
                launcher.launch(arrayOf("*/*"))
            }
            val adapter = ResourceAdapter(
                { uri ->
                    val tempFile = File(requireContext().cacheDir,uri)
                    if(!tempFile.exists()){
                        viewModel.getResourceContent(uri)
                    }
                    else{
                        Util.openCacheFile(requireContext(),uri,Util.getMimeType(uri)!!) }
                },
                { isFavourite ->
                    ResourceDetailsBottomSheetFragment(
                        isFavourite,
                        {
                            
                        },
                        {

                        },
                        {

                        },
                        {

                        },
                        {

                        }
                    ).show(
                        requireActivity().supportFragmentManager,"TAG"
                    )
                }
            )
            recycleView.adapter = adapter
            viewModel.homeUiState.observe(viewLifecycleOwner){
                if(it.isLoading){
                    progressBar.visibility = View.VISIBLE
                }
                else{
                    progressBar.visibility = View.GONE
                }
                it.errorMessage?.let{ message ->
                    showToast(message)
                    viewModel.messageShown()
                }
                adapter.submitList(it.resources)
            }
            viewModel.resourceContent.observe(viewLifecycleOwner){
                it.base64Content?.let{ content ->
                    val file = File(requireContext().cacheDir,it.uri!!)
                    Util.decodeBase64ToFile(content,file)
                    Util.openCacheFile(requireContext(),it.uri,Util.getMimeType(it.uri)!!)
                    viewModel.resetContent()
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}