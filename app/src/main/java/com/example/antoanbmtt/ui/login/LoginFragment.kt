package com.example.antoanbmtt.ui.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.dataStore
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.antoanbmtt.LoginActivity
import com.example.antoanbmtt.R
import com.example.antoanbmtt.databinding.FragmentLoginBinding
import com.example.antoanbmtt.helper.BiometricsSecurity
import com.example.antoanbmtt.helper.EditTextWatcher
import com.example.antoanbmtt.helper.showToast
import com.example.antoanbmtt.repository.UserDataStore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var userScreenEntryPoint: UserScreenEntryPoint? = null
    private val viewModel by viewModels<LoginViewModel>()
    @Inject lateinit var userDataStore: UserDataStore
    @Inject lateinit var biometricsSecurity: BiometricsSecurity
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
            fingerprint.setOnClickListener {
                biometricsSecurity.authenticate(this@LoginFragment){
                    viewModel.loginBiometrics()
                }
            }
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
                if(uiState.isLoginSuccessfully){
                    userScreenEntryPoint!!.toUserScreen()
                }
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
            }
            changeToBiometricAuth.text = "Change to ${userDataStore.getUserNameBiometrics()}"
            changeAccount.setOnClickListener {
                changeToBiometricAuth.visibility = View.VISIBLE
                changeAccount.visibility = View.GONE
                innerLayout.visibility = View.GONE
                emailTextInput.visibility = View.VISIBLE
            }
            changeToBiometricAuth.setOnClickListener {
                changeToBiometricAuth.visibility = View.GONE
                changeAccount.visibility = View.VISIBLE
                innerLayout.visibility = View.VISIBLE
                emailTextInput.visibility = View.GONE
            }
            if(viewModel.loginState.value?.isBiometricsAuthenticated == true){
                userNameBiometrics.text = userDataStore.getUserNameBiometrics()
                if(viewModel.loginState.value?.usingBiometricsAuth == true){
                    changeAccount.visibility = View.VISIBLE
                    changeToBiometricAuth.visibility = View.GONE
                }
                else{
                    changeAccount.visibility = View.GONE
                    changeToBiometricAuth.visibility = View.VISIBLE
                }
            }
            else{
                innerLayout.visibility = View.GONE
                emailTextInput.visibility = View.VISIBLE
                changeAccount.visibility = View.GONE
                changeToBiometricAuth.visibility = View.GONE
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
