package pl.portfolio.currencyconverter.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.portfolio.currencyconverter.repository.CurrencyRepository

class CurrencyViewModelProviderFactory( val app: Application,
val currencyRepository: CurrencyRepository ): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrencyViewModel(app, currencyRepository) as T
    }
}