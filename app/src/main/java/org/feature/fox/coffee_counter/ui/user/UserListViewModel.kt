package org.feature.fox.coffee_counter.ui.user

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface IUserListViewModel

@HiltViewModel
class UserListViewModel @Inject constructor(

) : ViewModel(), IUserListViewModel

class UserListViewModelPreview : IUserListViewModel
