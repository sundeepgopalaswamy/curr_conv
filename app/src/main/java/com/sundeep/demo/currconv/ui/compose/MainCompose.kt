package com.sundeep.demo.currconv.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sundeep.demo.currconv.R
import com.sundeep.demo.currconv.data.models.ConversionPairModel
import com.sundeep.demo.currconv.data.models.CurrencyModel
import com.sundeep.demo.currconv.ui.theme.CurrencyConverterTheme
import com.sundeep.demo.currconv.ui.viewmodel.MainViewModel
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Currency
import java.util.Locale

class MainCompose {
    private lateinit var mainViewModel: MainViewModel

    @Composable
    fun MainScreen(modifier: Modifier, viewModel: MainViewModel) {
        mainViewModel = viewModel
        if (viewModel.dataloaded.collectAsState().value) {
            Timber.i("Data available for rendering")
            if (viewModel.curCurrencyIndex.collectAsState().value == -1) {
                val currentLocale = Locale.getDefault()
                val currency = Currency.getInstance(currentLocale)
                val currencyCode = currency.currencyCode
                val currencyIndex = viewModel.getCurrencyModelIndex(currencyCode)
                val index = when (currencyIndex) {
                    -1 -> 0
                    else -> {
                        currencyIndex
                    }
                }
                viewModel.updateCurrency(index)
            }
            MainScreenRender(
                modifier = modifier,
                currencies = viewModel.allCurrencies.collectAsState().value,
                curCurrencyIndex = viewModel.curCurrencyIndex.collectAsState().value,
                conversions = viewModel.conversions.collectAsState().value
            )
        } else {
            Timber.i("No data available, so showing loading")
            ShowLoadingScreen(modifier)
        }
    }

    @Composable
    private fun ShowLoadingScreen(modifier: Modifier) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.loading),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }

    @Composable
    fun MainScreenRender(
        modifier: Modifier,
        currencies: List<CurrencyModel>,
        curCurrencyIndex: Int,
        conversions: List<ConversionPairModel>
    ) {
        Column(modifier = modifier.background(color = MaterialTheme.colorScheme.background)) {
            MainCurrDropDown(
                modifier = Modifier,
                currencies = currencies,
                curCurrencyIndex = curCurrencyIndex
            )
            ConversionList(
                modifier = Modifier,
                conversions = conversions
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainCurrDropDown(
        modifier: Modifier = Modifier,
        currencies: List<CurrencyModel>,
        curCurrencyIndex: Int = 0
    ) {
        Timber.i("Rendering of dropdown called with ${currencies.size} currencies with $curCurrencyIndex index")
        if (currencies.isEmpty() || curCurrencyIndex < 0 || curCurrencyIndex >= currencies.size) {
            return
        }
        var expanded by remember { mutableStateOf(false) }
        var selectedOption by remember { mutableStateOf(currencies[curCurrencyIndex]) }

        ExposedDropdownMenuBox(
            modifier = modifier,
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            // Create the TextField for the dropdown button
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer),
                value = selectedOption.name,
                onValueChange = { },
                readOnly = true,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.secondary),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                leadingIcon = {
                    Image(
                        painter = rememberAsyncImagePainter(selectedOption.flagUrl),
                        contentDescription = "Flag",
                        modifier = Modifier
                            .size(32.dp)
                            .padding(end = 4.dp)
                    )
                }
            )

            // Create the dropdown menu
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                currencies.forEachIndexed { index, currency ->
                    DropdownMenuItem(
                        text = { GetCurrencyView(currency = currency) },
                        onClick = {
                            selectedOption = currency
                            onCurrencyChanged(index)
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun GetCurrencyView(currency: CurrencyModel) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(currency.flagUrl),
                contentDescription = stringResource(id = R.string.flag),
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = currency.name,
                style = TextStyle(color = MaterialTheme.colorScheme.secondary)
            )
        }
    }

    @Composable
    fun ConversionList(modifier: Modifier, conversions: List<ConversionPairModel>) {
        Timber.i("Conversion List rendering called with ${conversions.size} items")
        LazyColumn(modifier = modifier) {
            items(conversions) { conversion ->
                ConversionItem(conversion)
            }
        }
    }

    @Composable
    fun ConversionItem(conversion: ConversionPairModel) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(conversion.toCurrency.flagUrl),
                contentDescription = "Flag",
                modifier = Modifier
                    .size(32.dp)
                    .padding(start = 8.dp)
            )
            Text(
                text = conversion.toCurrency.name,
                style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.padding(end = 8.dp),
                textAlign = TextAlign.End,
                style = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                text = BigDecimal(conversion.rate).setScale(4, RoundingMode.HALF_UP).toString()
            )
        }
    }

    private fun onCurrencyChanged(newIndex: Int) {
        mainViewModel.updateCurrency(newIndex)
    }

    @Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO)
    @Composable
    fun MainPreviewDefault() {
        CurrencyConverterTheme {
            MainScreenRender(
                modifier = Modifier,
                currencies = getSampleCurrencies(),
                curCurrencyIndex = 0,
                conversions = getSampleConversions()
            )
        }
    }

    @Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
    @Composable
    fun MainPreviewNight() {
        CurrencyConverterTheme {
            MainScreenRender(
                modifier = Modifier,
                currencies = getSampleCurrencies(),
                curCurrencyIndex = 0,
                conversions = getSampleConversions()
            )
        }
    }

    @Preview(showBackground = true, widthDp = 600, heightDp = 400)
    @Composable
    fun MainPreviewLandscape() {
        CurrencyConverterTheme {
            MainScreenRender(
                modifier = Modifier,
                currencies = getSampleCurrencies(),
                curCurrencyIndex = 0,
                conversions = getSampleConversions()
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun CurrencyViewPreview() {
        CurrencyConverterTheme {
            GetCurrencyView(
                currency = getSampleCurrencies()[0]
            )
        }
    }

    private fun getSampleCurrencies(): List<CurrencyModel> {
        return listOf(
            CurrencyModel(
                "US Dollars",
                "USD",
                "https://www.worldometers.info//img/flags/small/tn_us-flag.gif"
            ),
            CurrencyModel("Euro", "EUR", "https://goo.gl/gEgYUd"),
            CurrencyModel(
                "British Pound",
                "GBP",
                "https://www.worldometers.info//img/flags/small/tn_uk-flag.gif"
            ),
            CurrencyModel(
                "Indian Rupee",
                "INR",
                "https://www.worldometers.info//img/flags/small/tn_in-flag.gif"
            ),
        )
    }

    private fun getSampleConversions(): List<ConversionPairModel> {
        val currencies = getSampleCurrencies()
        return listOf(
            ConversionPairModel(currencies[1], 0.9009032),
            ConversionPairModel(currencies[2], 0.761165),
            ConversionPairModel(currencies[3], 84.009151)
        )
    }
}