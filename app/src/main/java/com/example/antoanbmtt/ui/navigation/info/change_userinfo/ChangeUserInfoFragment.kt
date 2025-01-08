package com.example.antoanbmtt.ui.navigation.info.change_userinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.antoanbmtt.R
import com.example.antoanbmtt.databinding.FragmentChangeUserInfoBinding
import com.example.antoanbmtt.databinding.FragmentUserInfoBinding
import com.example.antoanbmtt.helper.showToast
import com.example.antoanbmtt.ui.navigation.info.UserInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeUserInfoFragment : Fragment() {
    private var _binding : FragmentChangeUserInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ChangeUserInfoViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeUserInfoBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.message.observe(viewLifecycleOwner){
            it?.let{ message ->
                showToast(message)
                viewModel.messageShown()
            }
        }
        binding.change.setOnClickListener {
            viewModel.changeUserName(
                binding.userEditText.text.toString()
            )
        }
        viewModel.isSuccessful.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(ChangeUserInfoFragmentDirections.actionChangeUserInfoFragmentToUserInfoFragment())
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}