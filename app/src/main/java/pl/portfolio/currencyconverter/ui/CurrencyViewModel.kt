package pl.portfolio.currencyconverter.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import pl.portfolio.currencyconverter.CurrencyApplication
import pl.portfolio.currencyconverter.api.RetrofitInstance
import pl.portfolio.currencyconverter.models.CurrencyModel
import pl.portfolio.currencyconverter.repository.CurrencyRepository
import pl.portfolio.currencyconverter.util.Resource
import retrofit2.Response
import java.io.IOException

private const val TAG = "CurrencyViewModel"
class CurrencyViewModel(app: Application, val currencyRepository: CurrencyRepository): AndroidViewModel(app) {

    var currencies = MutableLiveData<List<String>>()
    var currencyResponse: Response<CurrencyModel>? = null

    init {
        getLatestCurrencyExchange()
    }

    fun getLatestCurrencyExchange(){
        viewModelScope.launch {
            safeLatestCurrencyExchangeCall()
        }
    }

    private suspend fun safeLatestCurrencyExchangeCall(){
        try {
            if(hasInternetConnection()) {
                currencyResponse = currencyRepository.getLatestCurrencyExchange()
                currencies.value = currencyResponse?.body()?.rates?.keys?.toList()
                Log.e(TAG, "safeLatestCurrencyExchangeCall: ${currencyResponse?.body()?.rates?.keys}", )
            }else{
                //TODO() Toast.makeText("No internet connection"))
            }
        }catch(t: Throwable){
            when(t){
                is IOException -> return//TODO()
                else -> return//TODO
            }
        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<CurrencyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}