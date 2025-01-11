package io.github.aidenkoog.android.mvvm_template

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.github.aidenkoog.android.mvvm_template.domain.GetQuotesUseCase
import io.github.aidenkoog.android.mvvm_template.domain.GetRandomQuoteUseCase
import io.github.aidenkoog.android.mvvm_template.domain.model.Quote
import io.github.aidenkoog.android.mvvm_template.ui.viewmodel.QuoteViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class QuoteViewModelUnitTest {

    @RelaxedMockK
    private lateinit var getQuotesUseCase: GetQuotesUseCase

    @RelaxedMockK
    private lateinit var getRandomQuoteUseCase: GetRandomQuoteUseCase

    private lateinit var quoteViewModel: QuoteViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        quoteViewModel = QuoteViewModel(getQuotesUseCase, getRandomQuoteUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when randomQuoteUseCase return a quote set on the livedata`() = runTest {
        //Given
        val quote = Quote("Test", "AidenKooG")
        coEvery { getRandomQuoteUseCase() } returns quote

        //When
        quoteViewModel.randomQuote()

        //Then
        assert(quoteViewModel.quoteModel.value == quote)
    }

    @Test
    fun `if randomQuoteUseCase return null keep the last value`() = runTest {
        //Given
        val quote = Quote("AidenKooG", "AidenKooG")
        quoteViewModel.quoteModel.value = quote
        coEvery { getRandomQuoteUseCase() } returns null

        //When
        quoteViewModel.randomQuote()

        //Then
        assert(quoteViewModel.quoteModel.value == quote)
    }

    @Test
    fun `when viewmodel is created at the first time, get all quotes and set the first value`() =
        runTest {
            //Given
            val quote = listOf(Quote("Test", "AidenKooG"), Quote("Dame un like", "Test AidenKooG "))
            coEvery { getQuotesUseCase() } returns quote

            //When
            quoteViewModel.onCreate()

            //Then
            assert(quoteViewModel.quoteModel.value == quote.first())
        }
}