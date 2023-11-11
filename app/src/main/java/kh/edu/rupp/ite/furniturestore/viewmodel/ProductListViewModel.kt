package kh.edu.rupp.ite.furniturestore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kh.edu.rupp.ite.furniturestore.model.api.model.ApIData
import kh.edu.rupp.ite.furniturestore.model.api.model.Product
import kh.edu.rupp.ite.furniturestore.model.api.model.Res
import kh.edu.rupp.ite.furniturestore.model.api.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class ProductListViewModel: ViewModel() {
    private val BASE_URL = "http://10.0.2.2:8000/"

    private val _productsData = MutableLiveData<ApIData<List<Product>>>()
    val productsData: LiveData<ApIData<List<Product>>>
        get() = _productsData
    private lateinit var product: List<Product>

    fun loadProductsData(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.loadProductList().enqueue(object : Callback<Res<Product>> {
            override fun onResponse(call: Call<Res<Product>>, response: Response<Res<Product>>) {
                val responseData = response.body()
                if (responseData != null) {
                    val apiData = ApIData(response.code(), responseData.data)
                    _productsData.postValue(apiData)
                } else {
                    println("Response data is null")
                }
            }
            override fun onFailure(call: Call<Res<Product>>, t: Throwable) {
                println("Failure: ${t.message}")
            }
        })
    }




}