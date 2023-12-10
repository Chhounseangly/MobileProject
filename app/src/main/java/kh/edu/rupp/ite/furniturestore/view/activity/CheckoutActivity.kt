package kh.edu.rupp.ite.furniturestore.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kh.edu.rupp.ite.furniturestore.R
import kh.edu.rupp.ite.furniturestore.model.api.model.PaymentModel
import kh.edu.rupp.ite.furniturestore.model.api.model.ShoppingCart
import kh.edu.rupp.ite.furniturestore.model.api.model.Status
import kh.edu.rupp.ite.furniturestore.viewmodel.PaymentViewModel
import kh.edu.rupp.ite.furniturestore.viewmodel.ShoppingCartViewModel


class CheckoutActivity() : AppCompatActivity() {

    private lateinit var  shoppingCartViewModel: ShoppingCartViewModel
    private lateinit  var paymentViewModel: PaymentViewModel

    private lateinit var paymentBtn: Button

    private lateinit var shippingTxt: TextView
    private lateinit var totalPrice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        paymentBtn = findViewById(R.id.paymentBtn)

        shoppingCartViewModel = ViewModelProvider(this)[ShoppingCartViewModel::class.java]
        paymentViewModel = ViewModelProvider(this)[PaymentViewModel::class.java]

        shoppingCartViewModel.shoppingCartItems.observe(this){
            it.data?.let { data->
                shoppingCartViewModel.calculateTotalPrice(data)
                displayUi(data)
                paymentBtn.setOnClickListener {
                    shoppingCartViewModel.payment(data)
                    shoppingCartViewModel.responseMessage.observe(this){ res ->
                        when(res.status){
                            Status.Processing->{

                            }
                            Status.Success -> {
                                //navigate to payment Success Activity
                                val paymentSuccessActivity = Intent(this, PaymentSuccessActivity::class.java)
                                startActivity(paymentSuccessActivity)
                            }
                            Status.Failed -> {

                            }
                        }
                    }
                }
            }
        }
        shoppingCartViewModel.loadProductsCartData()
        prevBack()
    }

    fun displayUi(shoppingCart: List<ShoppingCart>){
        totalPrice = findViewById(R.id.totalPrice)
        shoppingCartViewModel.totalPrice.observe(this) {
            totalPrice.text = "$ " + it.toString()
        }
    }

    private fun prevBack() {
        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}