package org.feature.fox.coffee_counter.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface IToast {
    val toastChannel: Channel<UIText>
    val toast: Flow<UIText>
}
