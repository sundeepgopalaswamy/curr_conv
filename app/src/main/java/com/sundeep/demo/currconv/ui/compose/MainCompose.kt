package com.sundeep.demo.currconv.ui.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
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
import java.math.RoundingMode
import java.text.DecimalFormat

class MainCompose {
    private lateinit var mainViewModel: MainViewModel
    private val decimalFormat = DecimalFormat("#0.0###")

    @Composable
    fun MainScreen(modifier: Modifier, viewModel: MainViewModel, windowSize: WindowSizeClass) {
        mainViewModel = viewModel
        decimalFormat.roundingMode = RoundingMode.HALF_UP
        if (viewModel.dataloaded.collectAsState().value) {
            Timber.i("Data available for rendering")
            val fromCurrencyModel by viewModel.fromCurrency.collectAsState()
            val toCurrencyModel by viewModel.toCurrency.collectAsState()
            val fromCurrencyValue by viewModel.fromCurrencyAmount.collectAsState()
            val toCurrencyValue by viewModel.toCurrencyAmount.collectAsState()
            fromCurrencyModel?.let { nonNullFromCurrencyModel ->
                toCurrencyModel?.let { nonNullToCurrencyModel ->
                    val dropDownCurrencies =
                        viewModel.allCurrencies.collectAsState().value.filter {
                            it.abb != nonNullFromCurrencyModel.abb
                                    && it.abb != nonNullToCurrencyModel.abb
                        }
                    when (windowSize.widthSizeClass) {
                        WindowWidthSizeClass.Compact -> {
                            MainScreenRenderPortrait(
                                modifier = modifier,
                                currencies = dropDownCurrencies,
                                fromCurrencyModel = nonNullFromCurrencyModel,
                                toCurrencyModel = nonNullToCurrencyModel,
                                fromCurrencyValue = fromCurrencyValue,
                                toCurrencyValue = toCurrencyValue,
                                conversions = viewModel.conversions.collectAsState().value
                            )
                        }

                        WindowWidthSizeClass.Expanded -> {
                            MainScreenRenderLandscape(
                                modifier = modifier,
                                currencies = dropDownCurrencies,
                                fromCurrencyModel = nonNullFromCurrencyModel,
                                toCurrencyModel = nonNullToCurrencyModel,
                                fromCurrencyValue = fromCurrencyValue,
                                toCurrencyValue = toCurrencyValue,
                                conversions = viewModel.conversions.collectAsState().value
                            )
                        }
                    }
                }
            }
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
            Row(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = stringResource(id = R.string.loading),
                    style = MaterialTheme.typography.titleLarge
                )
                CircularProgressIndicator(modifier = Modifier.padding(8.dp))
            }

        }
    }

    @Composable
    fun MainScreenRenderPortrait(
        modifier: Modifier,
        currencies: List<CurrencyModel>,
        fromCurrencyModel: CurrencyModel,
        toCurrencyModel: CurrencyModel,
        fromCurrencyValue: Double,
        toCurrencyValue: Double,
        conversions: List<ConversionPairModel>
    ) {
        Column(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(horizontal = 8.dp)
        ) {
            Heading(title = R.string.convert)
            ConvertCurrencyPortrait(
                currencies,
                fromCurrencyModel,
                OnFromCurrencyChangedListener(),
                fromCurrencyValue,
                OnFromCurrencyValueChangedListener()
            )
            Spacer(modifier = Modifier.height(16.dp))
            ConvertCurrencyPortrait(
                currencies,
                toCurrencyModel,
                OnToCurrencyChangedListener(),
                toCurrencyValue,
                OnToCurrencyValueChangedListener()
            )
            Heading(title = R.string.others)
            ConversionList(
                modifier = Modifier, conversions = conversions
            )
        }
    }

    @Composable
    fun MainScreenRenderLandscape(
        modifier: Modifier,
        currencies: List<CurrencyModel>,
        fromCurrencyModel: CurrencyModel,
        toCurrencyModel: CurrencyModel,
        fromCurrencyValue: Double,
        toCurrencyValue: Double,
        conversions: List<ConversionPairModel>
    ) {
        Row(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(240.dp)
            ) {
                Heading(title = R.string.convert)
                ConvertCurrencyLandscape(
                    currencies,
                    fromCurrencyModel,
                    OnFromCurrencyChangedListener(),
                    fromCurrencyValue,
                    OnFromCurrencyValueChangedListener()
                )
                Spacer(modifier = Modifier.height(16.dp))
                ConvertCurrencyLandscape(
                    currencies,
                    toCurrencyModel,
                    OnToCurrencyChangedListener(),
                    toCurrencyValue,
                    OnToCurrencyValueChangedListener()
                )
            }
            Column(modifier = Modifier.fillMaxHeight()) {
                Heading(title = R.string.others)
                ConversionListLandscape(
                    modifier = Modifier, conversions = conversions
                )
            }
        }
    }

    @Composable
    private fun Heading(@StringRes title: Int) {
        Text(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .paddingFromBaseline(top = 40.dp, bottom = 16.dp),
            text = stringResource(id = title),
            style = MaterialTheme.typography.titleMedium
        )
    }

    @Composable
    private fun ConvertCurrencyPortrait(
        currencies: List<CurrencyModel>,
        curCurrency: CurrencyModel,
        currencyChangedListener: OnCurrencyChangedListener,
        currencyValue: Double,
        currencyValueChangedListener: OnCurrencyValueChangedListener
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .height(72.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CurrDropDown(
                modifier = Modifier.weight(64f),
                currencies = currencies,
                curCurrency = curCurrency,
                currencyChangedListener = currencyChangedListener
            )
            CurrValueTextField(
                modifier = Modifier
                    .weight(36f)
                    .fillMaxHeight(),
                value = currencyValue,
                onValueChangedListener = currencyValueChangedListener
            )
        }
    }

    @Composable
    private fun ConvertCurrencyLandscape(
        currencies: List<CurrencyModel>,
        curCurrency: CurrencyModel,
        currencyChangedListener: OnCurrencyChangedListener,
        currencyValue: Double,
        currencyValueChangedListener: OnCurrencyValueChangedListener
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CurrDropDown(
                modifier = Modifier.height(72.dp),
                currencies = currencies,
                curCurrency = curCurrency,
                currencyChangedListener = currencyChangedListener
            )
            CurrValueTextField(
                modifier = Modifier.height(48.dp),
                value = currencyValue,
                onValueChangedListener = currencyValueChangedListener
            )
        }
    }

    @Composable
    private fun CurrValueTextField(
        modifier: Modifier,
        value: Double,
        onValueChangedListener: OnCurrencyValueChangedListener
    ) {
        TextField(
            modifier = modifier,
            value = decimalFormat.format(value),
            onValueChange = { newValue ->
                if (Regex("^[0-9]*\\.?[0-9]*$").matches(newValue)) {
                    onValueChangedListener.onCurrencyValueChanged(newValue.toDouble())
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = MaterialTheme.typography.bodyLarge
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CurrDropDown(
        modifier: Modifier = Modifier,
        currencies: List<CurrencyModel>,
        curCurrency: CurrencyModel,
        currencyChangedListener: OnCurrencyChangedListener
    ) {
        Timber.i("Rendering of dropdown called with ${currencies.size} currencies with ${curCurrency.name}")
        var expanded by remember { mutableStateOf(false) }
        var selectedOption by remember { mutableStateOf(curCurrency) }

        ExposedDropdownMenuBox(modifier = modifier,
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }) {
            // Create the TextField for the dropdown button
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer),
                value = selectedOption.name,
                onValueChange = { },
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                leadingIcon = {
                    Image(
                        painter = rememberAsyncImagePainter(selectedOption.flagUrl),
                        contentDescription = "Flag",
                        modifier = Modifier
                            .width(80.dp)
                            .height(48.dp)
                            .padding(horizontal = 8.dp)
                    )
                },
                maxLines = 2,
                minLines = 2
            )

            // Create the dropdown menu
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                currencies.forEach { currency ->
                    DropdownMenuItem(text = { GetCurrencyView(currency = currency) }, onClick = {
                        selectedOption = currency
                        currencyChangedListener.onCurrencyChanged(currency)
                        expanded = false
                    }, contentPadding = PaddingValues(vertical = 6.dp, horizontal = 8.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun GetCurrencyView(currency: CurrencyModel) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(currency.flagUrl),
                contentDescription = stringResource(id = R.string.flag),
                modifier = Modifier
                    .height(32.dp)
                    .width(48.dp),
            )
            Text(
                text = currency.name, style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    @Composable
    fun ConversionList(modifier: Modifier, conversions: List<ConversionPairModel>) {
        Timber.i("Conversion List rendering called with ${conversions.size} items")
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(conversions) { conversion ->
                ConversionItem(conversion)
            }
        }
    }

    @Composable
    fun ConversionListLandscape(modifier: Modifier, conversions: List<ConversionPairModel>) {
        Timber.i("Conversion List rendering called with ${conversions.size} items")
        LazyHorizontalGrid(
            modifier = modifier,
            rows = GridCells.Adaptive(48.dp),
            contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(conversions) { conversion ->
                ConversionItem(conversion)
            }
        }
    }

    @Composable
    fun ConversionItem(conversion: ConversionPairModel) {
        Surface(
            shape = MaterialTheme.shapes.small, color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Row(
                modifier = Modifier.padding(end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(conversion.toCurrency.flagUrl),
                    contentDescription = "Flag",
                    modifier = Modifier
                        .height(48.dp)
                        .width(72.dp),
                    contentScale = ContentScale.FillBounds
                )
                Column {
                    Text(
                        text = conversion.toCurrency.abb,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.bodyMedium,
                        text = decimalFormat.format(conversion.rate)
                    )
                }
            }
        }

    }

    @Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO)
    @Composable
    fun MainPreviewDefault() {
        CurrencyConverterTheme {
            val currencies = getSampleCurrencies()
            MainScreenRenderPortrait(
                modifier = Modifier,
                currencies = currencies,
                fromCurrencyModel = currencies[0],
                toCurrencyModel = currencies[1],
                fromCurrencyValue = 1234.5678,
                toCurrencyValue = 1234.5678,
                conversions = getSampleConversions()
            )
        }
    }

    @Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
    @Composable
    fun MainPreviewNight() {
        CurrencyConverterTheme {
            val currencies = getSampleCurrencies()
            MainScreenRenderPortrait(
                modifier = Modifier,
                currencies = currencies,
                fromCurrencyModel = currencies[0],
                toCurrencyModel = currencies[1],
                fromCurrencyValue = 1234.5678,
                toCurrencyValue = 1234.5678,
                conversions = getSampleConversions()
            )
        }
    }

    @Preview(showBackground = true, widthDp = 600, heightDp = 400)
    @Composable
    fun MainPreviewLandscape() {
        CurrencyConverterTheme {
            val currencies = getSampleCurrencies()
            MainScreenRenderLandscape(
                modifier = Modifier,
                currencies = currencies,
                fromCurrencyModel = currencies[0],
                toCurrencyModel = currencies[1],
                fromCurrencyValue = 1234.5678,
                toCurrencyValue = 1234.5678,
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

    @Preview(showBackground = true)
    @Composable
    fun LoadingViewPreview() {
        CurrencyConverterTheme {
            ShowLoadingScreen(modifier = Modifier)
        }
    }

    private fun getSampleCurrencies(): List<CurrencyModel> {
        return listOf(
            CurrencyModel(
                "US Dollar",
                "USD",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/usa.jpg"
            ),
            CurrencyModel(
                "New Zealand Dollar",
                "NZD",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/euro.png"
            ),
            CurrencyModel(
                "Euro",
                "EUR",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/euro.png"
            ),
            CurrencyModel(
                "British Pound",
                "GBP",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/uk.jpg"
            ),
            CurrencyModel(
                "Indian Rupee",
                "INR",
                "https://raw.githubusercontent.com/sundeepgopalaswamy/curr_conv/refs/heads/main/resources/india.png"
            ),
        )
    }

    private fun getSampleConversions(): List<ConversionPairModel> {
        val currencies = getSampleCurrencies()
        return listOf(
            ConversionPairModel(currencies[1], 1.766735),
            ConversionPairModel(currencies[2], 0.9009032),
            ConversionPairModel(currencies[3], 0.761165),
            ConversionPairModel(currencies[4], 84.009151),
        )
    }

    sealed interface OnCurrencyChangedListener {
        fun onCurrencyChanged(newCurrencyModel: CurrencyModel)
    }

    inner class OnFromCurrencyChangedListener : OnCurrencyChangedListener {
        override fun onCurrencyChanged(newCurrencyModel: CurrencyModel) {
            mainViewModel.updateFromCurrency(currencyModel = newCurrencyModel)
        }
    }

    inner class OnToCurrencyChangedListener : OnCurrencyChangedListener {
        override fun onCurrencyChanged(newCurrencyModel: CurrencyModel) {
            mainViewModel.updateToCurrency(currencyModel = newCurrencyModel)
        }
    }

    sealed interface OnCurrencyValueChangedListener {
        fun onCurrencyValueChanged(newValue: Double)
    }

    inner class OnFromCurrencyValueChangedListener : OnCurrencyValueChangedListener {
        override fun onCurrencyValueChanged(newValue: Double) {
            mainViewModel.updateFromCurrencyAmount(newValue)
        }
    }

    inner class OnToCurrencyValueChangedListener : OnCurrencyValueChangedListener {
        override fun onCurrencyValueChanged(newValue: Double) {
            mainViewModel.updateToCurrencyAmount(newValue)
        }
    }
}