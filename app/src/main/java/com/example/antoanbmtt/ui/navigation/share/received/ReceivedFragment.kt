package com.example.antoanbmtt.ui.navigation.share.received

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.antoanbmtt.adapter.ReceivedResourceAdapter
import com.example.antoanbmtt.databinding.FragmentReceivedBinding
import com.example.antoanbmtt.fragment.ReceivedResourceBottomSheetFragment
import com.example.antoanbmtt.helper.Util
import com.example.antoanbmtt.helper.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ReceivedFragment : Fragment() {
    private var _binding : FragmentReceivedBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ReceivedViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReceivedBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getResources()
            binding.refreshLayout.isRefreshing = false
        }
        val adapter = ReceivedResourceAdapter(
            { uri ->
                val tempFile = File(requireContext().cacheDir,uri)
                if(!tempFile.exists()){
                    viewModel.getResourceContent(uri)
                }
                else{
                    Util.openCacheFile(requireContext(),uri, Util.getMimeType(uri)!!)
                }
            },
            { uri,fileName ->
                ReceivedResourceBottomSheetFragment{
                    val tempFile = File(requireContext().cacheDir,uri)
                    if(!tempFile.exists()){
                        viewModel.downloadContent(uri,fileName)
                    }
                    else{
                        Util.downloadFromCacheFile(tempFile,fileName,requireContext())
                        Toast.makeText(requireContext(),"Download successfully", Toast.LENGTH_SHORT).show()
                    }
                }.show(requireActivity().supportFragmentManager,"TAG")
            }
        )
        binding.recycleView.adapter = adapter
        viewModel.receivedUiState.observe(viewLifecycleOwner){
            if(it.isLoading){
                binding.progressBar.visibility = View.VISIBLE
            }
            else{
                binding.progressBar.visibility = View.GONE
            }
            it.errorMessage?.let{ message ->
                showToast(message)
                viewModel.messageShown()
            }
            adapter.submitList(it.receivedResources)
        }
        viewModel.resourceContent.observe(viewLifecycleOwner){
            it.content?.let{ content ->
                val file = File(requireContext().cacheDir,it.uri!!)
                Util.writeToFile(content,file)
                Util.openCacheFile(requireContext(),it.uri, Util.getMimeType(it.uri)!!)
                viewModel.resetContent()
            }
        }
        viewModel.downloadContent.observe(viewLifecycleOwner){
            it.downloadContent?.let{ content ->
                Util.downloadFile(content.bytes(),it.fileName!!,requireContext())
                Toast.makeText(requireContext(),"Download successfully", Toast.LENGTH_SHORT).show()
                viewModel.resetDownloadContent()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}