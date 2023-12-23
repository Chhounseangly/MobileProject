package kh.edu.rupp.ite.furniturestore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kh.edu.rupp.ite.furniturestore.model.api.model.ApiData
import kh.edu.rupp.ite.furniturestore.model.api.model.HistoryModel
import kh.edu.rupp.ite.furniturestore.model.api.model.Status
import kh.edu.rupp.ite.furniturestore.model.api.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel: ViewModel() {

    private val _historyData = MutableLiveData<ApiData<List<HistoryModel>>>()

    val historyData: LiveData<ApiData<List<HistoryModel>>>
        get() = _historyData


    fun loadHistoryData(){
        var apiRes = ApiData<List<HistoryModel>>(Status.Processing, null)
        _historyData.postValue(apiRes)
        viewModelScope.launch(Dispatchers.IO) {
            apiRes = try {
                val response = RetrofitInstance.get().api.loadHistoryPurchase()
                ApiData(Status.Success, response.data)

            }catch (ex:Exception){
                ApiData(Status.Failed, null)
            }
            withContext(Dispatchers.Main.immediate){
                _historyData.postValue(apiRes)
            }
        }

    }
}