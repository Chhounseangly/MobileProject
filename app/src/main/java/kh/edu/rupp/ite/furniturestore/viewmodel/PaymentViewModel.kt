package kh.edu.rupp.ite.furniturestore.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kh.edu.rupp.ite.furniturestore.model.api.model.ApiData
import kh.edu.rupp.ite.furniturestore.model.api.model.ObjectPayment
import kh.edu.rupp.ite.furniturestore.model.api.model.PaymentModel
import kh.edu.rupp.ite.furniturestore.model.api.model.ResMessage
import kh.edu.rupp.ite.furniturestore.model.api.model.Status
import kh.edu.rupp.ite.furniturestore.model.api.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentViewModel: ViewModel() {

    // LiveData to hold the response message from the payment API
    private val _resMessage = MutableLiveData<ApiData<ResMessage>>()

    val resMessage: LiveData<ApiData<ResMessage>>
        get() = _resMessage

    // Function to initiate the payment process
    fun payment(data: List<ObjectPayment>) {
        // Convert data to a list of PaymentModel
        val list = mutableListOf<PaymentModel>()
        for (i in data) {
            list.add(PaymentModel(i.product_id, i.shopping_card_id))
        }

        // Initial status while processing payment
        var apiData = ApiData<ResMessage>(Status.Processing, null)
        _resMessage.postValue(apiData)

        // Processing payment in the background
        viewModelScope.launch(Dispatchers.IO) {
            apiData = try {
                // Make a payment request to the API
                RetrofitInstance.get().api.postPayment(list)
                ApiData(Status.Success, null)
            } catch (ex: Exception) {
                // Handle exceptions and set status to failed
                ex.printStackTrace()
                Log.e("failed", "${ex.message}")
                ApiData(Status.Failed, null)
            }

            // Process outside the background (update LiveData)
            withContext(Dispatchers.Main.immediate) {
                _resMessage.postValue(apiData)
            }
        }
    }
}
