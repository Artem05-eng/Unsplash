package com.example.unsplash.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.unsplash.R
import com.example.unsplash.network.Networking
import com.example.unsplash.utils.toast
import kotlinx.android.synthetic.main.login_fragment.*
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse


class LoginFragment : Fragment(R.layout.login_fragment) {

    private val viewModel: LoginViewModel by viewModels()
    private val args: LoginFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTH_REQUEST_CODE && data != null) {
            val tokenExchangeRequest = AuthorizationResponse.fromIntent(data)
                ?.createTokenExchangeRequest()
            val exception = AuthorizationException.fromIntent(data)
            when {
                tokenExchangeRequest != null && exception == null ->
                    viewModel.onAuthCodeReceived(tokenExchangeRequest)
                exception != null -> viewModel.onAuthCodeFailed(exception)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun bindViewModel() {
        loginButton.setOnClickListener { viewModel.openLoginPage() }
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::updateIsLoading)
        viewModel.openAuthPageLiveData.observe(viewLifecycleOwner, ::openAuthPage)
        viewModel.toastLiveData.observe(viewLifecycleOwner, ::toast)
        viewModel.authSuccessLiveData.observe(viewLifecycleOwner) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
        }
    }

    private fun updateIsLoading(isLoading: Boolean) {
        //loginButton.isVisible = !isLoading
        //loginProgress.isVisible = isLoading
    }

    private fun openAuthPage(intent: Intent) {
        startActivityForResult(intent, AUTH_REQUEST_CODE)
    }

    override fun onResume() {
        super.onResume()
        val flag = args.flag
        if (flag) {
            Networking.accessToken = ""
            val result = requireContext().cacheDir.deleteRecursively()
            if (result) {
                Toast.makeText(requireContext(), "Logout success!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val AUTH_REQUEST_CODE = 342
    }
}