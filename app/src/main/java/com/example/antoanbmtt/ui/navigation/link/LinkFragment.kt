package com.example.antoanbmtt.ui.navigation.link

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.antoanbmtt.R
import com.example.antoanbmtt.databinding.FragmentLinkBinding
import com.example.antoanbmtt.helper.EditTextWatcher
import com.example.antoanbmtt.helper.Util
import com.example.antoanbmtt.helper.showToast
import com.example.antoanbmtt.helper.toByteRepresentation
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class LinkFragment : Fragment() {
    private var _binding : FragmentLinkBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LinkViewModel> ()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLinkBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        binding.apply {
            editText.addTextChangedListener(EditTextWatcher{
                viewModel.uriChange(it)
            })
            passwordEditText.addTextChangedListener(EditTextWatcher{
                viewModel.passwordChange(it)
            })
            getLink.setOnClickListener {
                viewModel.getSharedItem()
            }
        }
        viewModel.linkUiState.observe(viewLifecycleOwner){
            it.resource?.let{ resource ->
                binding.apply {
                    resourceInfo.visibility = View.VISIBLE
                    name.text = resource.name
                    capacity.text = resource.capacity.toByteRepresentation()
                    openFile.setOnClickListener {
                        viewModel.getSharedContent()
                    }
                    download.setOnClickListener {
                        val tempFile = File(requireContext().cacheDir,viewModel.inputUri.value!!)
                        if(!tempFile.exists()){
                            viewModel.downloadContent()
                        }
                        else{
                            Util.downloadFromCacheFile(tempFile,resource.name,requireContext())
                            Toast.makeText(requireContext(),"Download successfully",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                viewModel.resetInfo()
            }
            binding.progressBar.visibility = if(it.isLoading) View.VISIBLE else View.GONE
            it.resourceContent?.let { content ->
                val file = File(requireContext().cacheDir,viewModel.inputUri.value!!)
                Util.writeToFile(content,file)
                Util.openCacheFile(requireContext(),viewModel.inputUri.value!!,Util.getMimeType(viewModel.inputUri.value!!)!!)
                viewModel.resetContent()
            }
            it.downloadContent?.let{ content ->
                Util.downloadFile(content.bytes(),it.resource!!.name,requireContext())
                Toast.makeText(requireContext(),"Download successfully",Toast.LENGTH_SHORT).show()
                viewModel.resetDownloadContent()
            }
            it.errorMessage?.let{ message ->
                showToast(message)
                viewModel.messageShown()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}