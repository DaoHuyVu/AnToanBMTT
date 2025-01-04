package com.example.antoanbmtt.ui.navigation.cloud
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.antoanbmtt.adapter.ResourceAdapter
import com.example.antoanbmtt.databinding.FragmentCloudStorageBinding
import com.example.antoanbmtt.fragment.ResourceDetailsBottomSheetFragment
import com.example.antoanbmtt.helper.Util
import com.example.antoanbmtt.helper.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class CloudStorageFragment : Fragment() {
    private var _binding : FragmentCloudStorageBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CloudStorageViewModel>()
    private lateinit var launcher : ActivityResultLauncher<Array<String>>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCloudStorageBinding.inflate(inflater,container,false)
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
                { id,uri,isFavourite,isTempDelete ->
                    ResourceDetailsBottomSheetFragment(
                        isFavourite,
                        {

                        },
                        {
                            viewModel.updateFavourite(id,!isFavourite)
                        },
                        {
                            viewModel.updateTempDelete(id,!isTempDelete)
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
            viewModel.cloudStorageUiState.observe(viewLifecycleOwner){
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