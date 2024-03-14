package com.auth0.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.management.ManagementException
import com.auth0.android.management.UsersAPIClient
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.auth0.sample.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var account: Auth0
    private lateinit var binding: ActivityMainBinding

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the account object with the Auth0 application details
        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )

        // Bind the button click with the login action
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonLogin.setOnClickListener { loginWithBrowser() }
    }
    private fun loginWithBrowser() {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    val credentials = WebAuthProvider.login(account)
                        .withScheme(getString(R.string.com_auth0_scheme))
                        .withScope("openid profile email read:current_user update:current_user_metadata")
                        .withAudience("https://${getString(R.string.com_auth0_domain)}/api/v2/")
                        .await(this@MainActivity)
                    println(credentials)
                } catch (e: AuthenticationException) {
                    e.printStackTrace()
                }
            }
        }
    }
}