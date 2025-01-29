package com.sundeep.demo.currconv.unit.test

import com.sundeep.demo.currconv.FakeRepository
import com.sundeep.demo.currconv.data.CurrencyRepository
import com.sundeep.demo.currconv.ui.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MainViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: MainViewModel

    private var respository : CurrencyRepository = FakeRepository()

    @Before
    fun setup() {
        viewModel = MainViewModel(respository)
    }

    @Test
    fun `getAllCurrencies should update allCurrencies`() {
        val currencies = viewModel.allCurrencies.value
        assert(currencies.size == 2)
        assert(currencies[0].name == "n1")
        assert(currencies[0].abb == "c1")
        assert(currencies[0].flagUrl == "f1")
    }

    @Test
    fun `updateCurrency should update curCurrencyIndex`() {
        assert(viewModel.allCurrencies.value.size == 2)
        viewModel.updateCurrency(0)
        assert(viewModel.curCurrencyIndex.value == 0)
        viewModel.updateCurrency(1)
        assert(viewModel.curCurrencyIndex.value == 1)
    }

    @Test
    fun `getConversions should update conversions`() {
        viewModel.updateCurrency(0)
        var conversions = viewModel.conversions.value
        assert(conversions.size == 1)
        assert(conversions[0].toCurrency.name == "n2")
        viewModel.updateCurrency(1)
        conversions = viewModel.conversions.value
        assert(conversions.size == 1)
        assert(conversions[0].toCurrency.name == "n1")
    }
}

@ExperimentalCoroutinesApi
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
): TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}