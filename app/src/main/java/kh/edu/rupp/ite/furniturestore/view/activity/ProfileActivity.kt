package kh.edu.rupp.ite.furniturestore.view.activity

import android.content.Intent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kh.edu.rupp.ite.furniturestore.R
import kh.edu.rupp.ite.furniturestore.databinding.ActivityProfileBinding
import kh.edu.rupp.ite.furniturestore.model.api.model.Status
import kh.edu.rupp.ite.furniturestore.model.api.model.User
import kh.edu.rupp.ite.furniturestore.view.activity.auth.ChangePasswordActivity
import kh.edu.rupp.ite.furniturestore.viewmodel.AuthViewModel

class ProfileActivity : BaseActivity<ActivityProfileBinding>(ActivityProfileBinding::inflate) {

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var editProfileBtn: Button
    private lateinit var changePwBtn: Button
    private lateinit var logoutBtn: Button
    private lateinit var profile: ImageView
    private lateinit var username: TextView

    override fun bindUi() {
        editProfileBtn = binding.editAvatarBtn
        changePwBtn = binding.changePwBtn
        username = binding.username
        profile = binding.profile
        logoutBtn = binding.logoutBtn
    }

    override fun initFields() {

    }

    override fun initActions() {
        authViewModel.loadProfile()
        // Set up back button navigation
        prevBack(binding.backBtn)
    }

    override fun setupListeners() {
        val intentChangePasswordActivity = Intent(this, ChangePasswordActivity::class.java)
        changePwBtn.setOnClickListener {
            startActivity(intentChangePasswordActivity)
        }

        logoutBtn.setOnClickListener {
            logOut()
        }

        //route to edit profile activity screen
        editProfileBtn.setOnClickListener {
            val intentEditProfileActivity = Intent(this, EditProfileActivity::class.java)
            startActivity(intentEditProfileActivity)
        }
    }

    override fun setupObservers() {
        authViewModel.userData.observe(this) {
            when (it.status) {
                Status.Success -> {
                    it.data?.let { data ->
                        displayUi(data)
                    }
                }

                else -> {

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        authViewModel.loadProfile()
    }

    private fun displayUi(data: User) {
        Picasso.get()
            .load(data.avatar)
            .placeholder(R.drawable.loading) // Add a placeholder image
            .error(R.drawable.ic_error) // Add an error image
            .into(profile)
        username.text = data.name
    }

    private fun logOut() {
        authViewModel.logout()
        authViewModel.resMsg.observe(this) {
            when (it.status) {
                Status.Success -> {
                    val mainActivityIntent = Intent(this, MainActivity::class.java)
                    mainActivityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(mainActivityIntent)
                }

                else -> {

                }
            }
        }
    }
}
