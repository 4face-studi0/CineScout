package util.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.UncompletedCoroutinesError
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import util.DispatchersProvider
import kotlin.coroutines.CoroutineContext

/**
 * Implement this for tests that use structured concurrency
 * One should use [coroutinesTest] instead of [runBlockingTest]
 */
interface CoroutinesTest {

    val dispatchers: DispatchersProvider get() =
        TestDispatchersProvider

    @Before
    fun setMain() {
        Dispatchers.setMain(dispatchers.Main)
    }

    @After
    fun resetMain() {
        Dispatchers.resetMain()
    }

    /**
     * @see runBlockingTest with [DispatchersProvider.Main] from [dispatchers] as default [context]
     */
    fun coroutinesTest(
        context: CoroutineContext = dispatchers.Main,
        ignoreUnfinishedJobs: Boolean = false,
        testBody: suspend TestCoroutineScope.() -> Unit
    ) {
        if (ignoreUnfinishedJobs) {
            try {
                runBlockingTest(context, testBody)
            } catch (error: UncompletedCoroutinesError) {
                println("\n!!!\n${error.message}\n")
            }
        } else {
            runBlockingTest(context, testBody)
        }
    }
}
