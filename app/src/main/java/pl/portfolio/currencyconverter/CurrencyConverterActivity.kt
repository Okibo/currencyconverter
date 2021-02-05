package pl.portfolio.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import pl.portfolio.currencyconverter.repository.CurrencyRepository
import pl.portfolio.currencyconverter.ui.CurrencyViewModel
import pl.portfolio.currencyconverter.ui.CurrencyViewModelProviderFactory
import pl.portfolio.currencyconverter.ui.fragments.MainFragment
import java.io.IOException

class CurrencyConverterActivity : AppCompatActivity() {

    lateinit var viewModel: CurrencyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter)
        title = "Currency converter"

        val currencyRepository = CurrencyRepository()
        val viewModelProviderFactory = CurrencyViewModelProviderFactory(application, currencyRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(CurrencyViewModel::class.java)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.host_fragment, MainFragment())
        fragmentTransaction.commit()
    }


}