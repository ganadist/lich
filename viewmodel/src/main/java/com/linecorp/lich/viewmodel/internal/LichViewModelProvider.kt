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
package com.linecorp.lich.viewmodel.internal

import android.content.Context
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.linecorp.lich.viewmodel.AbstractViewModel
import com.linecorp.lich.viewmodel.ViewModelFactory
import java.util.ServiceLoader

interface LichViewModelProvider {

    val loadPriority: Int

    @MainThread
    fun <T : AbstractViewModel> getViewModel(
        context: Context,
        viewModelStoreOwner: ViewModelStoreOwner,
        bridgeViewModelFactory: ViewModelProvider.Factory,
        factory: ViewModelFactory<T>
    ): T

    fun getManager(applicationContext: Context): Any
}

internal val lichViewModelProvider: LichViewModelProvider =
    Sequence {
        ServiceLoader.load(
            LichViewModelProvider::class.java,
            LichViewModelProvider::class.java.classLoader
        ).iterator()
    }.maxByOrNull { it.loadPriority } ?: throw Error("Failed to load LichViewModelProvider.")

fun getViewModelManager(applicationContext: Context): Any =
    lichViewModelProvider.getManager(applicationContext)
