package com.example.unsplash.data

import android.net.Uri
import com.example.unsplash.network.Networking
import net.openid.appauth.*
import timber.log.Timber

class AuthRepository {
    fun getAuthRequest(): AuthorizationRequest {
        val serviceConfiguration = AuthorizationServiceConfiguration(
            Uri.parse(AuthObject.AUTH_URI),
            Uri.parse(AuthObject.TOKEN_URI)
        )

        val redirectUri = Uri.parse(AuthObject.CALLBACK_URL)

        return AuthorizationRequest.Builder(
            serviceConfiguration,
            AuthObject.CLIENT_ID,
            AuthObject.RESPONSE_TYPE,
            redirectUri
        )
            .setScope(AuthObject.SCOPE)
            .setPrompt("login")
            .setCodeVerifier(null)
            .build()
    }

    fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
        onComplete: () -> Unit,
        onError: () -> Unit
    ) {
        authService.performTokenRequest(tokenRequest, getClientAuthentication()) { response, ex ->
            when {
                response != null -> {
                    val accessToken = response.accessToken ?: ""
                    Timber.d("token = $accessToken")
                    Networking.accessToken = accessToken
                    onComplete()
                }
                else -> onError()
            }
        }
    }

    private fun getClientAuthentication(): ClientAuthentication {
        return ClientSecretPost(AuthObject.CLIENT_SECRET)
    }
}