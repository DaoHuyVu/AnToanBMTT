package com.example.antoanbmtt.ui.navigation.info.change_email

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.antoanbmtt.R
import com.example.antoanbmtt.databinding.FragmentChangeEmailBinding
import com.example.antoanbmtt.databinding.FragmentChangePasswordBinding
import com.example.antoanbmtt.helper.showToast
import com.example.antoanbmtt.ui.navigation.info.UserInfoFragment.LogoutEntryPoint
import com.example.antoanbmtt.ui.navigation.info.change_password.ChangePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeEmailFragment : Fragment() {
    private var _binding : FragmentChangeEmailBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ChangeEmailViewModel>()
    private lateinit var logoutEntryPoint : LogoutEntryPoint
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeEmailBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context !is LogoutEntryPoint){
            throw RuntimeException("Must implement the interface")
        }
        logoutEntryPoint = context
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
            viewModel.changeEmail(
                binding.emailEditText.text.toString(),
            )
        }
        viewModel.isSuccessful.observe(viewLifecycleOwner){
            if(it){
                AlertDialog.Builder(requireContext())
                    .setTitle("Re-authentication Required")
                    .setMessage("Your email has been successfully changed. Please log in again to continue.")
                    .setPositiveButton("OK") { dialog, _ ->
                        logoutEntryPoint.logout()
                        dialog.dismiss()
                    }
                    .setCancelable(false)
                    .show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}