package pl.portfolio.currencyconverter.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import pl.portfolio.currencyconverter.CurrencyConverterActivity
import pl.portfolio.currencyconverter.R
import pl.portfolio.currencyconverter.databinding.FragmentMainBinding
import pl.portfolio.currencyconverter.ui.CurrencyViewModel
import pl.portfolio.currencyconverter.util.Constants.Companion.RATE_PRECISION

class MainFragment : Fragment(R.layout.fragment_main), AdapterView.OnItemSelectedListener {

    lateinit var viewModel: CurrencyViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as CurrencyConverterActivity).viewModel
        binding = FragmentMainBinding.bind(view)

        viewModel.getCurrencySymbols().observe(viewLifecycleOwner, { spinner ->
            val spinnerAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                spinner
            )
            spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )

            binding.apply {
                baseSpinner.adapter = spinnerAdapter
                fromSpinner.adapter = spinnerAdapter
                toSpinner.adapter = spinnerAdapter

            }

            spinnerAdapter.notifyDataSetChanged()
        })

        viewModel.connectionStatus.observe(viewLifecycleOwner, {
            if (!it)
                Toast.makeText(context, "Internet connection problem", Toast.LENGTH_LONG).show()
        })

        var editTextWatcher: TextWatcher? = null

        editTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                binding.apply {
                    if (toEditText.hasFocus()) {
                        fromEditText.removeTextChangedListener(editTextWatcher)
                        fromEditText.setText("", TextView.BufferType.EDITABLE)
                    } else if (fromEditText.hasFocus()) {
                        toEditText.removeTextChangedListener(editTextWatcher)
                        toEditText.setText("", TextView.BufferType.EDITABLE)
                    }
                }
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                binding.apply {
                    if (!s.isNullOrEmpty()) {

                        val fromSymbol = fromSpinner.selectedItem.toString()
                        val toSymbol = toSpinner.selectedItem.toString()
                        val fromRate =
                            viewModel.getRate(fromSymbol)!!
                        val toRate =
                            viewModel.getRate(toSymbol)!!

                        if (toEditText.hasFocus()) {
                            val toAmount = toEditText.text.toString().toDouble()
                            val baseAmount = toAmount / toRate
                            fromEditText.setText(
                                (baseAmount * fromRate).format(RATE_PRECISION),
                                TextView.BufferType.EDITABLE
                            )
                        } else if (fromEditText.hasFocus()) {
                            val fromAmount = fromEditText.text.toString().toDouble()
                            val baseAmount = fromAmount / fromRate
                            toEditText.setText(
                                (baseAmount * toRate).format(RATE_PRECISION),
                                TextView.BufferType.EDITABLE
                            )
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                binding.apply {
                    if (toEditText.hasFocus())
                        fromEditText.addTextChangedListener(editTextWatcher)
                    else if (fromEditText.hasFocus())
                        toEditText.addTextChangedListener(editTextWatcher)
                }
            }

        }

        binding.fromEditText.addTextChangedListener(editTextWatcher)
        binding.toEditText.addTextChangedListener(editTextWatcher)

        binding.fromSpinner.onItemSelectedListener = this
        binding.toSpinner.onItemSelectedListener = this

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val symbol = parent?.getItemAtPosition(position).toString()
        if (parent != null) {
            when (parent.id) {
                R.id.base_spinner -> viewModel.getLatestCurrencyExchangeForBase(symbol)
                R.id.from_spinner -> binding.fromEditText.setText(
                    viewModel.getRate(symbol).toString(),
                    TextView.BufferType.EDITABLE
                )
                R.id.to_spinner -> binding.toEditText.setText(
                    viewModel.getRate(symbol).toString(),
                    TextView.BufferType.EDITABLE
                )
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Nothing to do here
    }

    fun Double.format(digits: Int) = "%.${digits}f".format(this)
}