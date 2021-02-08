package pl.portfolio.currencyconverter.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.portfolio.currencyconverter.CurrencyApplication
import pl.portfolio.currencyconverter.models.CurrencyModel
import pl.portfolio.currencyconverter.repository.CurrencyRepository
import retrofit2.Response
import java.io.IOException

class CurrencyViewModel(app: Application, private val currencyRepository: CurrencyRepository) :
    AndroidViewModel(app) {

    private val _currencySymbols = MutableLiveData<List<String>>()
    var connectionStatus = MutableLiveData<Boolean>()

    fun getCurrencySymbols(): LiveData<List<String>> {
        return _currencySymbols
    }

    var currencyResponse: Response<CurrencyModel>? = null

    init {
        getLatestCurrencyExchange()
    }

    fun getLatestCurrencyExchange() {
        viewModelScope.launch {
            safeLatestCurrencyExchangeCall()
        }
    }

    fun getLatestCurrencyExchangeForBase(base: String) {
        viewModelScope.launch {
            safeLatestCurrencyExchangeForBaseCall(base)
        }
    }

    fun getRate(symbol: String): Double? {
        return currencyResponse?.body()?.rates?.get(symbol)
    }

    private suspend fun safeLatestCurrencyExchangeCall() {
        try {
            if (hasInternetConnection()) {
                connectionStatus.value = true
                currencyResponse = currencyRepository.getLatestCurrencyExchange()
                _currencySymbols.value = currencyResponse?.body()?.rates?.keys?.toList()
            }else{
                connectionStatus.value = false
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> return//TODO()
                else -> return//TODO
            }
        }
    }

    private suspend fun safeLatestCurrencyExchangeForBaseCall(base: String) {
        try {
            val hasConnection = hasInternetConnection()
            connectionStatus.postValue(hasConnection)
            if (hasConnection) {
                currencyResponse = currencyRepository.getLatestCurrencyExchangeForBase(base)
                _currencySymbols.value = currencyResponse?.body()?.rates?.keys?.toList()
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> return//TODO()
                else -> return//TODO
            }
        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<CurrencyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
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