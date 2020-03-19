package com.example.navigation.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.navigation.R
import com.example.navigation.extensions.dismissError
import com.example.navigation.extensions.navigateWithAnimations
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.login_fragment.*


class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.authenticationStateEvent.observe(
            viewLifecycleOwner,
            Observer { authenticationState ->
                when (authenticationState) {

                    is LoginViewModel.AuthenticationState.Authenticated -> {
                        findNavController().popBackStack()
                    }
                    is LoginViewModel.AuthenticationState.InvalidAuthentication -> {

                        val validationFields: Map<String, TextInputLayout> = initValidationFields()
                        authenticationState.fields.forEach { fieldError ->
                            validationFields[fieldError.first]?.error = getString(fieldError.second)
                        }
                    }
                }
            })

        buttonLoginSignIn.setOnClickListener {
            val userName = inputLoginUsername.text.toString()
            val password = inputLoginPassword.text.toString()
            viewModel.authentication(userName, password)
        }
        buttonLoginSignUp.setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_loginFragment_to_navigation)
        }

        onchangeText()


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            cancelAuthentication()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        cancelAuthentication()
        return true
    }


    private fun cancelAuthentication() {
        findNavController().popBackStack(R.id.startFragment, false)
    }

    private fun initValidationFields() = mapOf(
        LoginViewModel.INPUT_USERNAME.first to inputLayoutLoginUsername,
        LoginViewModel.INPUT_PASSWORD.first to inputLayoutLoginPassword
    )

    private fun onchangeText() {
        inputLoginUsername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputLayoutLoginUsername.dismissError()
            }
        })

        inputLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputLayoutLoginPassword.dismissError()
            }
        })
    }

}


