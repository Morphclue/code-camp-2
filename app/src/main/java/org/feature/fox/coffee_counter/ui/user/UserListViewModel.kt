package org.feature.fox.coffee_counter.ui.user

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.Funding
import org.feature.fox.coffee_counter.data.local.database.tables.User
import org.feature.fox.coffee_counter.data.models.body.FundingBody
import org.feature.fox.coffee_counter.data.models.body.SendMoneyBody
import org.feature.fox.coffee_counter.data.models.body.UserBody
import org.feature.fox.coffee_counter.data.models.response.UserIdResponse
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.di.services.AppPreference
import org.feature.fox.coffee_counter.util.IToast
import org.feature.fox.coffee_counter.util.UIText
import org.feature.fox.coffee_counter.util.Utils
import java.util.*
import javax.inject.Inject

/**
 * Interface for the [UserListViewModel]
 */
interface IUserListViewModel : IToast {
    val userList: SnapshotStateList<UserIdResponse>
    val filteredUserList: SnapshotStateList<UserIdResponse>
    val scrollState: ScrollState
    val isLoaded: MutableState<Boolean>
    val fundingDialogVisible: MutableState<Boolean>
    val editDialogVisible: MutableState<Boolean>
    val sendMoneyDialogVisible: MutableState<Boolean>
    val funding: MutableState<TextFieldValue>
    val editName: MutableState<TextFieldValue>
    val editId: MutableState<TextFieldValue>
    val editPassword: MutableState<TextFieldValue>
    val isAdminState: MutableState<Boolean>
    val editReEnterPassword: MutableState<TextFieldValue>
    var currentUser: MutableLiveData<UserIdResponse>
    val balance: MutableState<Double>
    val sendAmount: MutableState<TextFieldValue>
    val userIdPictureMap: MutableMap<String, Bitmap?>
    val searchField: MutableState<TextFieldValue>

    suspend fun addFunding()
    suspend fun createUser()
    suspend fun getTotalBalance()
    suspend fun sendMoney()
    fun search()
}

/**
 * ViewModel for the UserList.
 *
 * @property userRepository The user repository.
 * @property preference The app preference.
 */
@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preference: AppPreference,
) : ViewModel(), IUserListViewModel {
    override var userList = mutableStateListOf<UserIdResponse>()
    override val filteredUserList = mutableStateListOf<UserIdResponse>()
    override val scrollState = ScrollState(0)
    override val isLoaded = mutableStateOf(false)
    override val fundingDialogVisible = mutableStateOf(false)
    override val editDialogVisible = mutableStateOf(false)
    override val sendMoneyDialogVisible = mutableStateOf(false)
    override val funding = mutableStateOf(TextFieldValue())
    override val editName = mutableStateOf(TextFieldValue())
    override val editId = mutableStateOf(TextFieldValue())
    override val editPassword = mutableStateOf(TextFieldValue())
    override val isAdminState = mutableStateOf(false)
    override val editReEnterPassword = mutableStateOf(TextFieldValue())
    override var currentUser = MutableLiveData<UserIdResponse>()
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()
    override val balance = mutableStateOf(0.0)
    override val sendAmount = mutableStateOf(TextFieldValue())
    override val userIdPictureMap = mutableMapOf<String, Bitmap?>()
    override val searchField = mutableStateOf(TextFieldValue())

    /**
     * Initializes the view model.
     */
    init {
        viewModelScope.launch {
            isAdminState.value = preference.getTag(BuildConfig.IS_ADMIN, true)
            loadUsers()
            getTotalBalance()
        }
    }

    /**
     * Adds funding to a user.
     */
    override suspend fun addFunding() {
        funding.value = TextFieldValue(funding.value.text.replace(",", "."))
        if (funding.value.text.count { '.' == it } > 1) {
            toastChannel.send(UIText.StringResource(R.string.incorrect_money_format))
            return
        }
        val fundingAmount = funding.value.text.toDouble()

        currentUser.value?.let { userIdResponse ->
            val response = userRepository.addFunding(userIdResponse.id, FundingBody(fundingAmount))
            val transactions = userRepository.getTransactions(userIdResponse.id)
            if (response.data == null || transactions.data == null) {
                toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                    ?: UIText.StringResource(R.string.unknown_error))
                return
            }

            userRepository.insertFundingDb(
                Funding(
                    timestamp = transactions.data.last().timestamp,
                    userId = userIdResponse.id,
                    value = transactions.data.last().value
                )
            )

            removeAndAddUser(userIdResponse, userIdResponse.balance + fundingAmount)
        }

        funding.value = TextFieldValue()
        fundingDialogVisible.value = false
    }

    /**
     * Creates a new user.
     */
    override suspend fun createUser() {
        if (editPassword.value.text != editReEnterPassword.value.text) {
            toastChannel.send(UIText.StringResource(R.string.match_password))
            return
        }

        val response = userRepository.adminSignUp(
            UserBody(
                editId.value.text,
                editName.value.text,
                editPassword.value.text,
                isAdminState.value
            )
        )

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }

        // Assuming that this method only creates new Users and does not update them
        userRepository.insertUserDb(
            User(
                userId = editId.value.text,
                name = editName.value.text,
                isAdmin = isAdminState.value
            )
        )

        toastChannel.send(UIText.StringResource(R.string.created_account))
        editDialogVisible.value = false
    }

    /**
     * Gets the total balance of the logged in user.
     * FIXME: Maybe use "observeTotalBalance" instead of calling this Method after each change
     */
    override suspend fun getTotalBalance() {
        val response = userRepository.getUserById(preference.getTag(BuildConfig.USER_ID))

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        balance.value = response.data.balance
    }

    /**
     * Sends money to a user.
     */
    override suspend fun sendMoney() {
        sendAmount.value = TextFieldValue(sendAmount.value.text.replace(",", "."))
        if (sendAmount.value.text.count { '.' == it } > 1) {
            toastChannel.send(UIText.StringResource(R.string.incorrect_money_format))
            return
        }
        val sendMoneyAmount = sendAmount.value.text.toDouble()

        currentUser.value?.let { userIdResponse ->
            val response = userRepository.sendMoney(
                preference.getTag(BuildConfig.USER_ID),
                SendMoneyBody(sendMoneyAmount, userIdResponse.id)
            )

            if (response.data == null) {
                toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                    ?: UIText.StringResource(R.string.unknown_error))
                return
            }

            removeAndAddUser(userIdResponse, userIdResponse.balance + sendMoneyAmount)
        }
        toastChannel.send(UIText.StringResource(R.string.money_sent_success))
        sendAmount.value = TextFieldValue()
        sendMoneyDialogVisible.value = false
    }

    /**
     * Searches for a user by fuzzy search.
     */
    override fun search() {
        Utils.fuzzySearch(filteredUserList, userList, searchField.value.text)
    }

    /**
     * Removes and adds a user to the user list.
     * @param user The user to be removed and added.
     * @param amount The new balance of the user.
     */
    private fun removeAndAddUser(user: UserIdResponse, amount: Double) {
        val filteredUser = userList.filterIndexed { index, _ ->
            user.id == userList[index].id
        }.first()
        userList.remove(filteredUser)
        userList.add(
            UserIdResponse(
                user.id,
                user.name,
                amount
            )
        )
    }

    /**
     * Loads all users.
     */
    private suspend fun loadUsers() {
        val response = if (isAdminState.value) {
            userRepository.getUsersAsAdmin()
        } else {
            userRepository.getUsers()
        }

        if (response.data == null) {
            return
        }

        response.data.forEach { user ->
            userList.add(UserIdResponse(
                id = user.id,
                name = user.name,
                balance = user.balance ?: 0.0
            ))

            loadProfilePicture(user.id, user.imageTimestamp)
        }

        isLoaded.value = true
        userList = userList.sortedBy { it.name.lowercase(Locale.ROOT) }.toMutableStateList()
        filteredUserList.addAll(userList)
    }

    /**
     * Loads the profile picture of a user.
     * @param id The id of the user.
     * @param imageTimestamp The timestamp of the image.
     */
    private suspend fun loadProfilePicture(id: String, imageTimestamp: Long?) {
        if (imageTimestamp == null) {
            return
        }

        val dbImage = userRepository.getImageByIdDb(id)
        if (dbImage != null) {
            if (dbImage.timestamp >= imageTimestamp) {
                setImage(id, dbImage.encodedImage)
                return
            }
        }

        val response = userRepository.getImage(id)
        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        userRepository.insertImageDb(response.data)
        setImage(id, response.data.encodedImage)
    }

    /**
     * Sets the profile picture of a user.
     * @param id The id of the user.
     * @param encodedImage The encoded image of the user.
     */
    private fun setImage(id: String, encodedImage: String) {
        val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        userIdPictureMap[id] = bitmap
    }
}

/**
 * Preview for the UserList.
 */
class UserListViewModelPreview : IUserListViewModel {
    override val userList = mutableStateListOf<UserIdResponse>()
    override val filteredUserList = mutableStateListOf<UserIdResponse>()
    override val scrollState = ScrollState(0)
    override val isLoaded = mutableStateOf(true)
    override val fundingDialogVisible = mutableStateOf(false)
    override val editDialogVisible = mutableStateOf(true)
    override val sendMoneyDialogVisible = mutableStateOf(false)
    override val funding = mutableStateOf(TextFieldValue())
    override val editName = mutableStateOf(TextFieldValue())
    override val editId = mutableStateOf(TextFieldValue())
    override val editPassword = mutableStateOf(TextFieldValue())
    override val editReEnterPassword = mutableStateOf(TextFieldValue())
    override val isAdminState = mutableStateOf(true)
    override var currentUser = MutableLiveData<UserIdResponse>()
    override val balance = mutableStateOf(13.0)
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()
    override val sendAmount = mutableStateOf(TextFieldValue())
    override val userIdPictureMap = mutableMapOf<String, Bitmap?>()
    override val searchField = mutableStateOf(TextFieldValue())

    override suspend fun addFunding() {
        TODO("Not yet implemented")
    }

    override suspend fun createUser() {
        TODO("Not yet implemented")
    }

    override suspend fun getTotalBalance() {
        TODO("Not yet implemented")
    }

    override suspend fun sendMoney() {
        TODO("Not yet implemented")
    }

    override fun search() {
        TODO("Not yet implemented")
    }
}
