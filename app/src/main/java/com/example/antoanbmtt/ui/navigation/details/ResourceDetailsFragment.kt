package com.example.antoanbmtt.ui.navigation.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.antoanbmtt.databinding.FragmentResourceDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import javax.inject.Inject

@AndroidEntryPoint
class ResourceDetailsFragment : Fragment() {
    private var _binding : FragmentResourceDetailsBinding? = null
    private val binding get() =  _binding!!
    private val resourceArgs by navArgs<ResourceDetailsFragmentArgs>()
    @Inject lateinit var factory : ResourceDetailsViewModel.ResourceDetailsViewModelFactory
    private val viewModel by viewModels<ResourceDetailsViewModel>(
        factoryProducer = {
            ResourceDetailsViewModel.provideItemDetailFactory(factory,resourceArgs.resourceId)
        }
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResourceDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        viewModel.resourceDetailsUiState.observe(viewLifecycleOwner){
            binding.progressBar.visibility = if(it.isLoading) View.VISIBLE else View.GONE
            it.errorMessage?.let { message ->
                Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
                viewModel.messageShown()
            }
            it.resource?.let{ resource ->
                binding.apply {
                    name.text = resource.name
                    lastUpdate.text = resource.lastUpdate
                    uploadTime.text = resource.uploadTime
                    capacity.text = resource.capacity
                    location.text = if(resource.isTempDelete) "Recycle Bin" else "Cloud Storage"
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}