package kh.edu.rupp.ite.furniturestore.ui.activity.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kh.edu.rupp.ite.furniturestore.R
import kh.edu.rupp.ite.furniturestore.ui.activity.validation.AuthValidation


class SignUpActivity: AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var cfPassword: EditText

    private var isAllFieldsChecked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //start create sign up screen
        setContentView(R.layout.activity_sign_up)

        //class method SignInScreenDisplay
        initSignInScreenDisplay()

        //class method handleSignUpProcessing
        handleSignUpProcessing()
    }

    //handle Sign In Process
    private fun handleSignUpProcessing(){
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)

        //get value of editText of SignUpActivity
        username = findViewById(R.id.usernameInput)
        email = findViewById(R.id.emInput)
        password = findViewById(R.id.pwInput)
        cfPassword = findViewById(R.id.cfPwInput)


        //call dynamic handleOnChangeEditText from AuthValidation Class
        AuthValidation().handleOnChangeEditText(username)
        AuthValidation().handleOnChangeEditText(email)
        AuthValidation().handleOnChangeEditText(password)
        AuthValidation().handleOnChangeEditText(cfPassword)

        // event Processing of Sign In Button
        signUpBtn.setOnClickListener {
            //class method checkAllFields from AuthValidation Class
            isAllFieldsChecked = AuthValidation().checkAllFields(username);
            isAllFieldsChecked = AuthValidation().checkAllFields(email);
            isAllFieldsChecked = AuthValidation().passwordChecked(password, cfPassword)

            //validation checked is true go
            if (isAllFieldsChecked){

                Toast.makeText(this, "done", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun initSignInScreenDisplay(){
        //get value from Sign In Button
        val signInBtn = findViewById<TextView>(R.id.signInBtn)
        //defined activity SignIn we're want to route
        val signInScreen  = Intent(this, SignInActivity::class.java)

        //action when user click sign in button it will go to sign in screen
        signInBtn.setOnClickListener {
            startActivity(signInScreen)
        }
    }


}