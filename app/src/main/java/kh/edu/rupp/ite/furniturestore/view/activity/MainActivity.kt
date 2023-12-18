package kh.edu.rupp.ite.furniturestore.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kh.edu.rupp.ite.furniturestore.R
import kh.edu.rupp.ite.furniturestore.databinding.ActivityMainBinding
import kh.edu.rupp.ite.furniturestore.displayFragment.DisplayFragmentActivity
import kh.edu.rupp.ite.furniturestore.utility.AppPreference
import kh.edu.rupp.ite.furniturestore.view.activity.auth.SignInActivity
import kh.edu.rupp.ite.furniturestore.view.fragments.FavoriteFragment
import kh.edu.rupp.ite.furniturestore.view.fragments.HomeFragment
import kh.edu.rupp.ite.furniturestore.view.fragments.SearchFragment
import kh.edu.rupp.ite.furniturestore.view.fragments.ShoppingCartFragment

class MainActivity : AppCompatActivity() {
    private lateinit var displayFragmentActivity: DisplayFragmentActivity

    private lateinit var activityMainBinding: ActivityMainBinding

    private var homeFragment = HomeFragment()
    private var searchFragment = SearchFragment()
    private var favoriteFragment = FavoriteFragment()
    private var shoppingCartFragment = ShoppingCartFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent

        if (intent != null && intent.action == Intent.ACTION_VIEW) {
            val data = intent.data

            // TODO: Handle the case when user sign in with google
            if (data != null && data.scheme == getString(R.string.app_scheme)) {
                // TODO: Save token to shared preference
                val token = data.getQueryParameter("token")
                AppPreference.get(this).setToken(token!!)
            }
        }

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        displayFragmentActivity = DisplayFragmentActivity(supportFragmentManager)

        //click on title of app go back to home fragment and set menu home active
        activityMainBinding.titleTxt.setOnClickListener {
            displayFragmentActivity.displayFragment(homeFragment)
            activityMainBinding.bottomNavigationView.selectedItemId = R.id.mnuHome
        }

        //display home fragment when starting app
        displayFragmentActivity.displayFragment(homeFragment)

        val signInScreen = Intent(this, SignInActivity::class.java)
        val token = AppPreference.get(this).getToken();

        //action on bottom nav_bar when user click menu
        activityMainBinding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mnuHome -> displayFragmentActivity.displayFragment(homeFragment)
                R.id.mnuFav -> {
                    if (token != null) {
                        displayFragmentActivity.displayFragment(favoriteFragment)
                    } else startActivity(signInScreen)
                }

                R.id.mnuSearch -> displayFragmentActivity.displayFragment(searchFragment)
                R.id.mnuCart -> {
                    if (token != null) {
                        displayFragmentActivity.displayFragment(shoppingCartFragment)
                    } else startActivity(signInScreen)
                }

                else -> {
                    if (token != null) {
                        val profileActivity = Intent(this, ProfileActivity::class.java)
                        startActivity(profileActivity)
                    } else startActivity(signInScreen)
                }
            }
            true
        }
    }
}