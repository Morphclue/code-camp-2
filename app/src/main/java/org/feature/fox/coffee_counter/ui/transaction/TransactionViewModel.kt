package org.feature.fox.coffee_counter.ui.transaction

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.feature.fox.coffee_counter.data.repository.ItemRepository
import org.feature.fox.coffee_counter.data.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val itemRepository: ItemRepository
) : ViewModel() {
}
