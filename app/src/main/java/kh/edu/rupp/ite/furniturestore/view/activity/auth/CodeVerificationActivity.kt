package kh.edu.rupp.ite.furniturestore.view.activity.auth

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import kh.edu.rupp.ite.furniturestore.R
import kh.edu.rupp.ite.furniturestore.databinding.ActivityCodeVerificationBinding
import kh.edu.rupp.ite.furniturestore.model.api.model.Status
import kh.edu.rupp.ite.furniturestore.view.activity.BaseActivity
import kh.edu.rupp.ite.furniturestore.view.activity.MainActivity
import kh.edu.rupp.ite.furniturestore.view.activity.validation.AuthValidation
import kh.edu.rupp.ite.furniturestore.viewmodel.AuthViewModel

/**
 * Activity for verifying a user's email with a code.
 */
class CodeVerificationActivity :
    BaseActivity<ActivityCodeVerificationBinding>(ActivityCodeVerificationBinding::inflate) {

    private val authViewModel: AuthViewModel by viewModels()
    private val codeInput: EditText by lazy { binding.codeVerifyInput }
    private val verifyBtn: Button by lazy { binding.verifyBtn }
    private val errMsg: TextView by lazy { binding.errorMsg }
    private val resendCodeContent: TextView by lazy { binding.resendCodeContent }
    private val resendCodeBtn: TextView by lazy { binding.resendCodeBtn }

    private lateinit var countdownTimer: CountDownTimer

    companion object {
        private const val EMAIL_EXTRA = "email"
    }

    override fun initActions() {
        // Set up text input validation
        AuthValidation().handleOnChangeEditText(codeInput)

        navigationBetweenEditTexts(codeInput, null) {
            verifyEmail()
        }
        prevBack(binding.backBtn)
    }

    override fun setupListeners() {
        verifyBtn.setOnClickListener {
            verifyEmail()
        }

        codeInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Check if the length of the text is 6 characters
                if (s?.length == 6) {
                    // Call handleValidation() when the length is 6
                    verifyEmail()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used
            }
        })
    }

    override fun setupObservers() {
        authViewModel.validationVerify.observe(this) {
            // Handle the validation response
            handleValidationResponse(it)
        }

        // Observe the authentication response
        authViewModel.resAuth.observe(this) {
            when (it.status) {
                Status.Processing -> {
                    // Handle the processing status, e.g., show loading indicator
                    errMsg.visibility = View.GONE
                    disableVerifyButton()
                }

                Status.Success -> {
                    // Handle the success status, e.g., navigate to the main activity
                    val mainActivityIntent = Intent(this, MainActivity::class.java)
                    mainActivityIntent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(mainActivityIntent)
                }

                Status.Failed -> {
                    // Handle the failure status, e.g., show error message
                    it.data?.let { m ->
                        handleInvalidValidation(m.message)
                    }
                }

                else -> {
                    // Handle any other unknown status
                    errMsg.visibility = View.VISIBLE
                    errMsg.text = "Something went wrong"
                    verifyBtn.isEnabled = true
                    verifyBtn.setTextColor(Color.WHITE)
                    verifyBtn.setBackgroundResource(R.drawable.custom_style_btn)
                }
            }
        }
    }

    private fun verifyEmail() {
        authViewModel.verifyEmail(
            intent.getStringExtra(EMAIL_EXTRA).toString(),
            codeInput.text.toString()
        )
    }

    /**
     * Handle the validation response from the API.
     */
    private fun handleValidationResponse(validateRes: Pair<Boolean, String>) {
        val (isValid, errorMessages) = validateRes

        if (isValid) {
            // If validation is successful, proceed to handle the authentication response
            enableVerifyButton()
        } else {
            // If validation fails, show error messages and Enable the verification button
            handleInvalidValidation(errorMessages)
        }
    }

    /**
     * Handle the case when validation is unsuccessful.
     */
    private fun handleInvalidValidation(errorMessages: String) {
        // Enable the verification button and set its appearance
        enableVerifyButton()

        // Highlight the input field with an error
        codeInput.backgroundTintList = ColorStateList.valueOf(Color.RED)
        codeInput.error = errorMessages
    }

    /**
     * Disable the verification button and set its appearance to indicate a disabled state.
     */
    private fun disableVerifyButton() {
        verifyBtn.isEnabled = false
        verifyBtn.setTextColor(Color.BLACK)
        verifyBtn.setBackgroundResource(R.drawable.disable_btn)
        codeInput.backgroundTintList = null
    }

    /**
     * Enable the verification button and set its appearance to indicate an enabled state.
     */
    private fun enableVerifyButton() {
        verifyBtn.isEnabled = true
        verifyBtn.setTextColor(Color.WHITE)
        verifyBtn.setBackgroundResource(R.drawable.custom_style_btn)
    }

    override fun onDestroy() {
        super.onDestroy()
        authViewModel.resAuth.removeObservers(this)
        authViewModel.validationVerify.removeObservers(this)
    }
}
