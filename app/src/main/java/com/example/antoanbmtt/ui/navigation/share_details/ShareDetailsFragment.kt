package com.example.antoanbmtt.ui.navigation.share_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.antoanbmtt.R
import com.example.antoanbmtt.SERVER_HOST
import com.example.antoanbmtt.databinding.FragmentResourceDetailsBinding
import com.example.antoanbmtt.databinding.FragmentShareDetailsBinding
import com.example.antoanbmtt.helper.EditTextWatcher
import com.example.antoanbmtt.helper.copyToClipboard
import com.example.antoanbmtt.helper.showToast
import com.example.antoanbmtt.ui.navigation.details.ResourceDetailsFragmentArgs
import com.example.antoanbmtt.ui.navigation.details.ResourceDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class ShareDetailsFragment : Fragment() {
    private var _binding : FragmentShareDetailsBinding? = null
    private val binding get() =  _binding!!
    private val resourceArgs by navArgs<ShareDetailsFragmentArgs>()
    @Inject lateinit var factory : ShareDetailsViewModel.SharedDetailsViewModelFactory
    private val viewModel by viewModels<ShareDetailsViewModel>(
        factoryProducer = {
            ShareDetailsViewModel.provideItemDetailFactory(factory,resourceArgs.id)
        }
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShareDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        viewModel.sharedDetailsUiState.value?.apply {
            binding.apply {
                resource?.let{
                    name.text = it.name
                    if(it.isShared){
                        status.text = "Shared"
                        sharedAt.text = it.sharedAt
                        deleteLink.visibility = View.VISIBLE
                        updatePassword.text = "Update password"
                    }
                    else{
                        status.text = "Not shared"
                        sharedAt.text = "No data"
                        deleteLink.visibility = View.GONE
                        updatePassword.text = "Share"
                    }
                    link.text = "${SERVER_HOST}/${it.uri}"
                }
            }

        }
        viewModel.sharedDetailsUiState.observe(viewLifecycleOwner){
            binding.progressBar.visibility = if(it.isLoading) View.VISIBLE else View.GONE
            it.errorMessage?.let{ message ->
                showToast(message)
                viewModel.messageShown()
            }
            binding.apply {
                it.resource?.let{ resource ->
                    if(resource.isShared){
                        status.text = "Shared"
                        sharedAt.text = resource.sharedAt
                        deleteLink.visibility = View.VISIBLE
                        updatePassword.text = "Update password"
                    }
                    else{
                        status.text = "Not shared"
                        sharedAt.text = "No data"
                        deleteLink.visibility = View.GONE
                        updatePassword.text = "Share"
                    }
                }
            }
        }
        binding.apply {
            imageButton.setOnClickListener{
                copyToClipboard(link.text.toString())
            }
            editText.addTextChangedListener(EditTextWatcher{
                viewModel.passwordChange(it)
            })
            updatePassword.setOnClickListener {
                viewModel.updatePassword()
            }
            deleteLink.setOnClickListener {
                viewModel.deleteLink()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}