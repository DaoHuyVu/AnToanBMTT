package com.example.antoanbmtt.ui.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.antoanbmtt.LoginActivity
import com.example.antoanbmtt.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var userScreenEntryPoint: UserScreenEntryPoint? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is UserScreenEntryPoint){
            userScreenEntryPoint = context as LoginActivity
        }
        else{
            throw RuntimeException("Must implement the UserScreenEntryPoint first")
        }
    }
    override fun onStart() {
        super.onStart()
        binding.apply {
            loginBtn.setOnClickListener{
                userScreenEntryPoint!!.toUserScreen()
            }
            signUp.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        userScreenEntryPoint = null
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    interface UserScreenEntryPoint{
        fun toUserScreen()
    }
}
