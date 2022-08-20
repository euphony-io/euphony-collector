package co.jbear.euphony_hub_collector.ui.components

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.MutableLiveData
import co.jbear.euphony_hub_collector.presentation.collector.TxRxCheckerViewModel
import co.jbear.euphony_hub_collector.ui.theme.EuphonyHubCollectorTheme
import co.jbear.euphony_hub_collector.util.Constants.TAG_BUTTON
import co.jbear.euphony_hub_collector.util.Constants.TAG_INPUT
import co.jbear.euphony_hub_collector.util.Constants.TAG_RESULT_FAIL
import co.jbear.euphony_hub_collector.util.Constants.TAG_RESULT_SUCCESS
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@RunWith(MockitoJUnitRunner::class)
class TxRxCheckerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    private lateinit var viewModel: TxRxCheckerViewModel

    private val isProcessing = MutableLiveData(false)
    private val txCode = MutableLiveData("")
    private val rxCode = MutableLiveData("")

    @Before
    fun setup() {
        `when`(viewModel.isProcessing).thenReturn(isProcessing)
        `when`(viewModel.txCode).thenReturn(txCode)
        `when`(viewModel.rxCode).thenReturn(rxCode)
    }

    @Test
    fun initTxRxCheckerWithRandomLength() {
        val randomLength = 5
        composeTestRule.setContent {
            EuphonyHubCollectorTheme {
                TxRxChecker(viewModel = viewModel, randomLength)
            }
        }

        composeTestRule.onNodeWithTag(TAG_INPUT).assertIsNotEnabled()
        composeTestRule.onNodeWithTag(TAG_RESULT_FAIL).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TAG_RESULT_SUCCESS).assertDoesNotExist()
    }

    @Test
    fun initTxRxCheckerWithoutRandomLength() {
        composeTestRule.setContent {
            EuphonyHubCollectorTheme {
                TxRxChecker(viewModel = viewModel, 0)
            }
        }

        composeTestRule.onNodeWithTag(TAG_INPUT).assertIsEnabled()
        composeTestRule.onNodeWithTag(TAG_RESULT_FAIL).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TAG_RESULT_SUCCESS).assertDoesNotExist()
    }

    @Test
    fun startTxRxCheckerAndReturnSuccess() {
        val randomLength = 5
        composeTestRule.setContent {
            EuphonyHubCollectorTheme {
                TxRxChecker(viewModel = viewModel, randomLength)
            }
        }

        `when`(viewModel.isSuccess()).thenReturn(true)

        composeTestRule.onNodeWithTag(TAG_BUTTON).performClick()
        verify(viewModel).start(anyString(), anyInt())

        txCode.postValue("hello")

        composeTestRule.onNodeWithTag(TAG_RESULT_SUCCESS).assertIsDisplayed()
    }

    @Test
    fun startTxRxCheckerAndReturnFail() {
        val randomLength = 5
        composeTestRule.setContent {
            EuphonyHubCollectorTheme {
                TxRxChecker(viewModel = viewModel, randomLength)
            }
        }

        `when`(viewModel.isSuccess()).thenReturn(false)

        composeTestRule.onNodeWithTag(TAG_BUTTON).performClick()
        verify(viewModel).start(anyString(), anyInt())

        txCode.postValue("hello")

        composeTestRule.onNodeWithTag(TAG_RESULT_FAIL).assertIsDisplayed()
    }
}