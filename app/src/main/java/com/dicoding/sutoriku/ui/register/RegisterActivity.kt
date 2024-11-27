package com.dicoding.sutoriku.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.*
import androidx.core.view.*
import androidx.lifecycle.ViewModelProvider
import com.dicoding.sutoriku.R
import com.dicoding.sutoriku.databinding.ActivityRegisterBinding
import com.dicoding.sutoriku.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val registerViewModelFactory: RegisterViewModelFactory = RegisterViewModelFactory.getInstance(application)
        registerViewModel = ViewModelProvider(this, registerViewModelFactory)[RegisterViewModel::class.java]

        registerViewModel.successDialog.observe(this) {
            successDialog(it)
        }

        registerViewModel.errorDialog.observe(this) {
            errorDialog(it)
        }

        registerViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressIndicator.visibility = View.VISIBLE
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            } else {
                binding.progressIndicator.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }

        registerAction()
    }

    private fun errorDialog(errorMessage: String) {
        val builder = AlertDialog.Builder(this)
            .setMessage(errorMessage)
            .setPositiveButton(R.string.confirmation) { dialog, _ ->
                dialog.dismiss()
            }
        val alertDialog = builder.create()
        alertDialog.show()
        val messageView = alertDialog.findViewById<TextView>(android.R.id.message)
        @Suppress("DEPRECATION")
        messageView?.setTextColor(resources.getColor(R.color.black))
        alertDialog.window?.setBackgroundDrawableResource(R.color.white)
    }

    private fun successDialog(message: String) {
        val builder = AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(R.string.confirmation) { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()

        val messageView = alertDialog.findViewById<TextView>(android.R.id.message)
        messageView?.setTextColor(resources.getColor(R.color.black))
        alertDialog.window?.setBackgroundDrawableResource(R.color.white)
    }

    private fun registerAction() {
        binding.buttonRegister.setOnClickListener{
            val userName = binding.edRegisterName.text.toString().trim()
            val userEmail = binding.edRegisterEmail.text.toString().trim()
            val userPassword = binding.edRegisterPassword.text.toString().trim()
            registerViewModel.registerUser(userName, userEmail, userPassword)
        }
    }
}