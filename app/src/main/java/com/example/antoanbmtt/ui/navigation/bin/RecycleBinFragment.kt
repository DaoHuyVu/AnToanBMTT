package com.example.antoanbmtt.ui.navigation.bin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.antoanbmtt.adapter.TempDeleteResourceAdapter
import com.example.antoanbmtt.databinding.FragmentRecycleBinBinding
import com.example.antoanbmtt.fragment.TempDeleteResourceBottomSheetFragment
import com.example.antoanbmtt.helper.Util
import com.example.antoanbmtt.helper.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class RecycleBinFragment : Fragment() {
    private var _binding : FragmentRecycleBinBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RecycleBinViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecycleBinBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        binding.apply {
            refreshLayout.setOnRefreshListener {
                viewModel.getResources()
                refreshLayout.isRefreshing = false
            }
            val adapter = TempDeleteResourceAdapter(
                {   uri ->
                    val tempFile = File(requireContext().cacheDir,uri)
                    if(!tempFile.exists()){
                        viewModel.getResourceContent(uri)
                    }
                    else{
                        Util.openCacheFile(requireContext(),uri, Util.getMimeType(uri)!!)
                    }
                },
                { id,isTempDelete ->
                   TempDeleteResourceBottomSheetFragment(
                        {
                            findNavController().navigate(RecycleBinFragmentDirections.actionRecycleBinFragmentToResourceDetailsFragment(id))
                        },
                        {
                            viewModel.updateTempDelete(id,!isTempDelete)
                        },
                        {
                            // MUST IMPLEMENT
                        }
                    ).show(
                        requireActivity().supportFragmentManager,"TAG"
                    )
                }
            )
            recycleView.adapter = adapter
            viewModel.recycleBinUiState.observe(viewLifecycleOwner){
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
                it.content?.let{ content ->
                    val file = File(requireContext().cacheDir,it.uri!!)
                    Util.writeToFile(content,file)
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