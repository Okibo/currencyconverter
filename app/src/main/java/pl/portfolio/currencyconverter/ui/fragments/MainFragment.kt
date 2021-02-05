package pl.portfolio.currencyconverter.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import pl.portfolio.currencyconverter.CurrencyConverterActivity
import pl.portfolio.currencyconverter.R
import pl.portfolio.currencyconverter.databinding.FragmentMainBinding
import pl.portfolio.currencyconverter.ui.CurrencyViewModel

private const val TAG = "MainFragment"

class MainFragment : Fragment(R.layout.fragment_main), AdapterView.OnItemSelectedListener {

    lateinit var viewModel: CurrencyViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as CurrencyConverterActivity).viewModel
        binding = FragmentMainBinding.bind(view)

        viewModel.currencies.observe(viewLifecycleOwner, Observer { spinner ->
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
        binding.fromSpinner.onItemSelectedListener = this
        binding.toSpinner.onItemSelectedListener = this

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val symbol = parent?.getItemAtPosition(position).toString()
        if (parent != null) {
            when (parent.id) {
                R.id.from_spinner -> binding.fromEditText.setText(
                    viewModel.currencyResponse?.body()?.rates?.get(symbol).toString(),
                    TextView.BufferType.EDITABLE
                )
                R.id.to_spinner -> binding.toEditText.setText(
                    viewModel.currencyResponse?.body()?.rates?.get(symbol).toString(),
                    TextView.BufferType.EDITABLE
                )
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}