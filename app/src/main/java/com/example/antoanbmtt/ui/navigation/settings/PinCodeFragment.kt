package com.example.antoanbmtt.ui.navigation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.antoanbmtt.databinding.FragmentPinCodeBinding
import com.example.antoanbmtt.helper.PINStore
import com.example.antoanbmtt.repository.UserDataStore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PinCodeFragment : Fragment() {
    private var _binding : FragmentPinCodeBinding? = null
    private  val binding get() = _binding!!
    @Inject lateinit var userDataStore: UserDataStore
    @Inject lateinit var pinStore : PINStore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinCodeBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        binding.apply {
            confirmPin.setOnEditorActionListener { _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if(!confirmPin.text.isNullOrEmpty() && confirmPin.text.toString() == enterPin.text.toString()){
                        warning.visibility = View.GONE
                        userDataStore.setPinEnable(true)
                        pinStore.storePin(enterPin.text!!.toString())
                        findNavController().navigate(PinCodeFragmentDirections.actionPinCodeFragmentToSettingsFragment())
                    } else{
                        warning.visibility = View.VISIBLE
                    }
                    true
                }
                else
                    false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}