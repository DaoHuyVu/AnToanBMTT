package com.example.antoanbmtt.ui.navigation.info

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.antoanbmtt.databinding.ChangePasswordLayoutBinding
import com.example.antoanbmtt.databinding.FragmentUserInfoBinding
import com.example.antoanbmtt.helper.showToast
import com.example.antoanbmtt.repository.UserDataStore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserInfoFragment : Fragment() {
    private var _binding : FragmentUserInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<UserInfoViewModel>()
    private lateinit var logoutEntryPoint : LogoutEntryPoint
    @Inject lateinit var userDataStore: UserDataStore
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context !is LogoutEntryPoint){
            throw RuntimeException("Must implement the interface")
        }
        logoutEntryPoint = context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserInfoBinding.inflate(inflater,container,false)

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        binding.apply {
            userName.text = userDataStore.getUserName()
            userEmail.text = userDataStore.getEmail()
            logout.setOnClickListener {
                logoutEntryPoint.logout()
            }
            changeEmail.setOnClickListener {
                findNavController().navigate(UserInfoFragmentDirections.actionUserInfoFragmentToChangeEmailFragment())
            }
            changeName.setOnClickListener {
                findNavController().navigate(UserInfoFragmentDirections.actionUserInfoFragmentToChangeUserInfoFragment())
            }
            changePassword.setOnClickListener {
               findNavController().navigate(UserInfoFragmentDirections.actionUserInfoFragmentToChangePasswordFragment())
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    interface LogoutEntryPoint{
        fun logout()
    }
}