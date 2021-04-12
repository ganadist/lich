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

import androidx.annotation.MainThread
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.linecorp.lich.viewmodel.AbstractViewModel

/**
 * A class that bridges a [ViewModel] of Android Architecture Components and our [AbstractViewModel].
 */
@MainThread
internal class BridgeViewModel(internal val savedStateHandle: SavedStateHandle) : ViewModel() {

    internal var viewModel: AbstractViewModel? = null

    override fun onCleared() {
        viewModel?.clear()
        viewModel = null
    }
}
