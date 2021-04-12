/*
 * Copyright 2019 LINE Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linecorp.lich.viewmodel.test

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.linecorp.lich.savedstate.createSavedStateHandleForTesting
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame

@RunWith(AndroidJUnit4::class)
class ViewModelMocksTest {

    @After
    fun tearDown() {
        clearAllMockViewModels()
    }

    @Test
    fun simpleMocking() {
        setMockViewModel(ViewModelX) { _, _ ->
            val mockSavedState = createSavedStateHandleForTesting(
                ViewModelXArgs(message = "Mocked X.")
            )
            ViewModelX(mockSavedState)
        }
        setMockViewModel(ViewModelY) { _, _ ->
            val mockSavedState = createSavedStateHandleForTesting(
                ViewModelYArgs(message = "Mocked Y.")
            )
            ViewModelY(mockSavedState)
        }

        val context: Context = ApplicationProvider.getApplicationContext()
        val intent = TestActivity.newIntent(context)
        ActivityScenario.launch<TestActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assertNotSame(activity.viewModelX, activity.testFragment.viewModelX)
                assertSame(activity.viewModelX, activity.testFragment.activityViewModelX)

                assertEquals("Mocked X.", activity.viewModelX.message)
                assertEquals("Mocked X.", activity.testFragment.viewModelX.message)
                assertEquals("Mocked Y.", activity.viewModelY.message)
            }
        }
    }

    @Test
    fun mockingForEachViewModelStoreOwner() {
        setMockViewModel(ViewModelX) { viewModelStoreOwner, savedState ->
            when (viewModelStoreOwner) {
                is TestActivity -> {
                    val mockSavedState = createSavedStateHandleForTesting(
                        ViewModelXArgs(message = "Mocked for TestActivity.")
                    )
                    ViewModelX(mockSavedState)
                }
                else -> createRealViewModel(ViewModelX, savedState)
            }
        }

        val context: Context = ApplicationProvider.getApplicationContext()
        val intent = TestActivity.newIntent(context)
        ActivityScenario.launch<TestActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assertNotSame(activity.viewModelX, activity.testFragment.viewModelX)
                assertSame(activity.viewModelX, activity.testFragment.activityViewModelX)

                assertEquals("Mocked for TestActivity.", activity.viewModelX.message)
                assertEquals(
                    "I am TestFragment.viewModelX.",
                    activity.testFragment.viewModelX.message
                )
                assertEquals("I am TestActivity.viewModelY.", activity.viewModelY.message)
            }
        }
    }

    @Test
    fun clearMock() {
        setMockViewModel(ViewModelX) { _, _ ->
            val mockSavedState = createSavedStateHandleForTesting(
                ViewModelXArgs(message = "Mocked X.")
            )
            ViewModelX(mockSavedState)
        }
        setMockViewModel(ViewModelY) { _, _ ->
            val mockSavedState = createSavedStateHandleForTesting(
                ViewModelYArgs(message = "Mocked Y.")
            )
            ViewModelY(mockSavedState)
        }

        val context: Context = ApplicationProvider.getApplicationContext()
        val intent = TestActivity.newIntent(context)
        ActivityScenario.launch<TestActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assertEquals("Mocked X.", activity.viewModelX.message)
                assertEquals("Mocked X.", activity.testFragment.viewModelX.message)
                assertEquals("Mocked Y.", activity.viewModelY.message)
            }
        }

        clearMockViewModel(ViewModelX)

        ActivityScenario.launch<TestActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assertEquals("I am TestActivity.viewModelX.", activity.viewModelX.message)
                assertEquals(
                    "I am TestFragment.viewModelX.",
                    activity.testFragment.viewModelX.message
                )
                assertEquals("Mocked Y.", activity.viewModelY.message)
            }
        }
    }

    @Test
    fun clearAll() {
        setMockViewModel(ViewModelX) { _, _ ->
            val mockSavedState = createSavedStateHandleForTesting(
                "message" to "Mocked X."
            )
            ViewModelX(mockSavedState)
        }
        setMockViewModel(ViewModelY) { _, _ ->
            val mockSavedState = createSavedStateHandleForTesting(
                "message" to "Mocked Y."
            )
            ViewModelY(mockSavedState)
        }

        val context: Context = ApplicationProvider.getApplicationContext()
        val intent = TestActivity.newIntent(context)
        ActivityScenario.launch<TestActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assertEquals("Mocked X.", activity.viewModelX.message)
                assertEquals("Mocked X.", activity.testFragment.viewModelX.message)
                assertEquals("Mocked Y.", activity.viewModelY.message)
            }
        }

        clearAllMockViewModels()

        ActivityScenario.launch<TestActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assertEquals("I am TestActivity.viewModelX.", activity.viewModelX.message)
                assertEquals(
                    "I am TestFragment.viewModelX.",
                    activity.testFragment.viewModelX.message
                )
                assertEquals("I am TestActivity.viewModelY.", activity.viewModelY.message)
            }
        }
    }
}
