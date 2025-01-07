package com.example.antoanbmtt.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.antoanbmtt.R
import com.example.antoanbmtt.databinding.FragmentSignUpBinding
import com.example.antoanbmtt.helper.EditTextWatcher
import com.example.antoanbmtt.helper.showToast
import com.example.antoanbmtt.ui.login.LoginFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding : FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SignUpViewModel> ()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            signUpBtn.setOnClickListener{
                viewModel.signUp()
            }
            emailEditText.addTextChangedListener(EditTextWatcher{
                viewModel.emailChange(it)
            })
            passwordEditText.addTextChangedListener(EditTextWatcher{
                viewModel.passwordChange(it)
            })
            confirmPasswordEditText.addTextChangedListener(EditTextWatcher{
                viewModel.confirmPasswordChange(it)
            })
            userNameEditText.addTextChangedListener(EditTextWatcher{
                viewModel.userNameChange(it)
            })
            viewModel.signUpUiState.observe(viewLifecycleOwner){ uiState ->
                if(uiState.isSignUpSuccessfully){
                    showToast("Sign up successfully\n Verify your email to activate the account")
                    viewModel.messageShown()
                }
                if(uiState.isLoading){
                    progressBar.visibility = View.VISIBLE
                    signUpBtn.isEnabled = false
                }
                else{
                    progressBar.visibility = View.GONE
                    signUpBtn.isEnabled = true
                }
                if(uiState.emailFieldEmpty){
                    emailTextInput.isHelperTextEnabled = true
                    emailTextInput.helperText = getString(R.string.email_text_input_helper_text)
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
                if(uiState.userNameFieldEmpty){
                    userNameTextInput.isHelperTextEnabled = true
                    userNameTextInput.helperText = getString(R.string.username_text_input_helper_text)
                }
                else{
                    userNameTextInput.isHelperTextEnabled = false
                }
                if(uiState.confirmPasswordFieldEmpty || uiState.confirmPasswordNotMatch){
                    if(uiState.confirmPasswordFieldEmpty){
                        confirmTextInput.isHelperTextEnabled = true
                        confirmTextInput.helperText = getString(R.string.confirm_password_text_input_helper_text)
                    }
                    else{
                        confirmTextInput.isHelperTextEnabled = true
                        confirmTextInput.helperText = getString(R.string.confirm_password_not_match)
                    }
                }
                else{
                    confirmTextInput.isHelperTextEnabled = false
                }
                uiState.message?.let{
                    showToast(it)
                    viewModel.messageShown()
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}