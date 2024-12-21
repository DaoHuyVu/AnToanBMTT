package com.example.antoanbmtt.ui.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.antoanbmtt.LoginActivity
import com.example.antoanbmtt.R
import com.example.antoanbmtt.databinding.FragmentLoginBinding
import com.example.antoanbmtt.helper.EditTextWatcher
import com.example.antoanbmtt.helper.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var userScreenEntryPoint: UserScreenEntryPoint? = null
    private val viewModel by viewModels<LoginViewModel>()
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
                viewModel.login()
            }
            signUp.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            }
            emailEditText.addTextChangedListener(EditTextWatcher{
                viewModel.userNameChange(it)
            })
            passwordEditText.addTextChangedListener(EditTextWatcher{
                viewModel.passwordChange(it)
            })
            viewModel.loginState.observe(viewLifecycleOwner){ uiState ->
                if(uiState.isLoading){
                    progressBar.visibility = View.VISIBLE
                    loginBtn.isEnabled = false
                }
                else{
                    progressBar.visibility = View.GONE
                    loginBtn.isEnabled = true
                }
                if(uiState.emailFieldEmpty){
                    emailTextInput.isHelperTextEnabled = true
                    emailTextInput.helperText = getString(R.string.username_text_input_helper_text)
                }
                else{
                    emailTextInput.isHelperTextEnabled = false
                }
                if(uiState.passwordFieldEmpty){
                    passwordTextInput.isHelperTextEnabled = true
                    passwordTextInput.helperText = getString(R.string.password_text_input_helper_text)
                }
                else{
                    passwordTextInput.isHelperTextEnabled = false
                }
                uiState.message?.let{
                    showToast(it)
                    viewModel.messageShown()
                }
                if(uiState.isLoginSuccessfully){
                    userScreenEntryPoint!!.toUserScreen()
                }
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
