package com.sundeep.demo.currconv.unit.test

import com.sundeep.demo.currconv.FakeRepository
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

    private var repository = FakeRepository()

    @Before
    fun setup() {
        viewModel = MainViewModel(repository)
    }

    @Test
    fun `getAllCurrencies should update allCurrencies`() {
        val currencies = viewModel.allCurrencies.value
        assert(currencies.size == 2)
        assert(currencies[0] == FakeRepository.CURRENCY0)
    }

    @Test
    fun `updateCurrency should update curCurrency`() {
        assert(viewModel.allCurrencies.value.size == 2)
        viewModel.updateCurrency(viewModel.allCurrencies.value[0])
        assert(viewModel.curCurrency.value == viewModel.allCurrencies.value[0])
        assert(repository.savedDefaultCurrency == viewModel.allCurrencies.value[0])
        viewModel.updateCurrency(viewModel.allCurrencies.value[1])
        assert(viewModel.curCurrency.value == viewModel.allCurrencies.value[1])
        assert(repository.savedDefaultCurrency == viewModel.allCurrencies.value[1])
    }

    @Test
    fun `getConversions should update conversions`() {
        viewModel.updateCurrency(viewModel.allCurrencies.value[0])
        var conversions = viewModel.conversions.value
        assert(conversions.size == 1)
        assert(conversions[0].toCurrency == FakeRepository.CURRENCY1)
        viewModel.updateCurrency(viewModel.allCurrencies.value[1])
        conversions = viewModel.conversions.value
        assert(conversions.size == 1)
        assert(conversions[0].toCurrency == FakeRepository.CURRENCY0)
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