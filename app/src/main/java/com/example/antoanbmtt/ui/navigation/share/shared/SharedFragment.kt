package com.example.antoanbmtt.ui.navigation.share.shared

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.antoanbmtt.adapter.ResourceAdapter
import com.example.antoanbmtt.databinding.FragmentSharedBinding
import com.example.antoanbmtt.fragment.ResourceDetailsBottomSheetFragment
import com.example.antoanbmtt.helper.Util
import com.example.antoanbmtt.helper.showToast
import com.example.antoanbmtt.ui.navigation.cloud.CloudStorageFragmentDirections
import com.example.antoanbmtt.ui.navigation.share.ShareFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class SharedFragment : Fragment() {
    private var _binding : FragmentSharedBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SharedViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSharedBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getResources()
            binding.refreshLayout.isRefreshing = false
        }
        val adapter = ResourceAdapter(
            { uri ->
                val tempFile = File(requireContext().cacheDir,uri)
                if(!tempFile.exists()){
                    viewModel.getResourceContent(uri)
                }
                else{
                    Util.openCacheFile(requireContext(),uri, Util.getMimeType(uri)!!)
                }
            },
            { id,uri,isFavourite,isTempDelete,fileName ->
                ResourceDetailsBottomSheetFragment(
                    isFavourite,
                    {
                        findNavController().navigate(ShareFragmentDirections.actionShareFragmentToResourceDetailsFragment(id))
                    },
                    {
                        viewModel.updateFavourite(id,!isFavourite)
                    },
                    {
                        viewModel.updateTempDelete(id,!isTempDelete)
                    },
                    {
                        val tempFile = File(requireContext().cacheDir,uri)
                        if(!tempFile.exists()){
                            viewModel.downloadContent(uri,fileName)
                        }
                        else{
                            Util.downloadFromCacheFile(tempFile,fileName,requireContext())
                            Toast.makeText(requireContext(),"Download successfully", Toast.LENGTH_SHORT).show()
                        }
                    },
                    {
                        findNavController().navigate(ShareFragmentDirections.actionShareFragmentToShareDetailsFragment(id))
                    }
                ).show(
                    requireActivity().supportFragmentManager,"TAG"
                )
            }
        )

        binding.recycleView.adapter = adapter
        viewModel.sharedUiState.observe(viewLifecycleOwner){
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
            adapter.submitList(it.resources)
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