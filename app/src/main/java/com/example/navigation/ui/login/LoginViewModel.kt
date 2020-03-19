package com.example.navigation.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.navigation.R

class LoginViewModel : ViewModel() {

    sealed class AuthenticationState {
        object UnAuthenticated : AuthenticationState()
        object Authenticated : AuthenticationState()
        class InvalidAuthentication(val fields: List<Pair<String, Int>>) : AuthenticationState()
    }

    val authenticationStateEvent = MutableLiveData<AuthenticationState>()
    var userName : String = ""

    init {

        refuseAuthentication()
    }

    fun refuseAuthentication() {

        authenticationStateEvent.value = AuthenticationState.UnAuthenticated
    }

    fun authentication(userName: String, password: String) {
        if (isInvalidForm(userName, password)) {

            this.userName = userName
            authenticationStateEvent.value = AuthenticationState.Authenticated
        }
    }

    private fun isInvalidForm(userName: String, password: String): Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()

        if (userName.isEmpty()) {
            invalidFields.add(INPUT_USERNAME)
        }

        if (password.isEmpty()) {
            invalidFields.add(INPUT_PASSWORD)
        }

        if (invalidFields.isNotEmpty()) {

            authenticationStateEvent.value =
                AuthenticationState.InvalidAuthentication(invalidFields)

            return false
        }
        return true
    }


    companion object {

        val INPUT_USERNAME = "INPUT_USERNAME" to R.string.login_input_layout_error_invalid_username
        val INPUT_PASSWORD = "INPUT_PASSWORD" to R.string.login_input_layout_error_invalid_password
    }


}
