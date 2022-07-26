package org.feature.fox.coffee_counter.data.repository

import androidx.lifecycle.LiveData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.data.local.database.dao.UserDao
import org.feature.fox.coffee_counter.data.local.database.tables.Funding
import org.feature.fox.coffee_counter.data.local.database.tables.Image
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.local.database.tables.User
import org.feature.fox.coffee_counter.data.models.body.FundingBody
import org.feature.fox.coffee_counter.data.models.body.LoginBody
import org.feature.fox.coffee_counter.data.models.body.PurchaseBody
import org.feature.fox.coffee_counter.data.models.body.UserBody
import org.feature.fox.coffee_counter.data.models.response.LoginResponse
import org.feature.fox.coffee_counter.data.models.response.TransactionResponse
import org.feature.fox.coffee_counter.data.models.response.UserIdResponse
import org.feature.fox.coffee_counter.data.models.response.UserResponse
import org.feature.fox.coffee_counter.di.services.network.ApiService
import org.feature.fox.coffee_counter.util.Resource
import java.io.InputStream
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val apiService: ApiService,
) : UserRepositoryInt {
    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun insertFunding(funding: Funding) {
        userDao.insertFunding(funding)
    }

    override suspend fun insertPurchase(purchase: Purchase) {
        userDao.insertPurchase(purchase)
    }

    override suspend fun insertImage(image: Image) {
        userDao.insertImage(image)
    }

    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    override suspend fun deleteFunding(funding: Funding) {
        userDao.deleteFunding(funding)
    }

    override suspend fun deletePurchase(purchase: Purchase) {
        userDao.deletePurchase(purchase)
    }

    override suspend fun deleteImage(image: Image) {
        userDao.deleteImage(image)
    }

    override suspend fun getUserByIdDb(id: String): User {
        return userDao.getUserById(id)
    }

    override suspend fun getFundingListOfUser(id: String): List<Funding> {
        return userDao.getFundingListOfUser(id)
    }

    override suspend fun getPurchaseListOfUser(id: String): List<Purchase> {
        return userDao.getPurchaseListOfUser(id)
    }

    override suspend fun getImageByIdFromUser(id: String): Image? {
        return userDao.getImageById(id)
    }

    override fun observeTotalBalanceOfUser(id: String): LiveData<Double> {
        return userDao.observeTotalBalanceOfUser(id)
    }

    override suspend fun login(id: String, password: String): Boolean {
        return userDao.login(id, password)
    }

    override fun observeAllUsers(): LiveData<List<User>> {
        return userDao.observeAllUsers()
    }

    override suspend fun postLogin(loginBody: LoginBody): Resource<LoginResponse> {
        return try {
            val response = apiService.postLogin(loginBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun getUsers(): Resource<List<UserResponse>> {
        return try {
            val response = apiService.getUsers()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun getUserById(id: String): Resource<UserIdResponse> {
        return try {
            val response = apiService.getUserById(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun updateUser(id: String, userBody: UserBody): Resource<String> {
        return try {
            val response = apiService.updateUser(id, userBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun signUp(userBody: UserBody): Resource<String> {
        return try {
            val response = apiService.signUp(userBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun deleteUser(id: String): Resource<String> {
        return try {
            val response = apiService.deleteUser(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            println(e)
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun getTransactions(id: String): Resource<List<TransactionResponse>> {
        return try {
            val response = apiService.getTransactions(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun purchaseItem(id: String, purchaseBody: PurchaseBody): Resource<String> {
        return try {
            val response = apiService.purchaseItem(id, purchaseBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun getImage(id: String): Resource<Image> {
        return try {
            val response = apiService.getImage(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    val image = Image(
                        id,
                        it.encodedImage,
                        it.timestamp
                    )
                    return@let Resource.success(image)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun uploadImage(id: String, inputStream: InputStream): Resource<String> {
        return try {
            val image = MultipartBody.Part.createFormData(
                "pic", "myPic", RequestBody.create(
                    "image/*".toMediaTypeOrNull(),
                    inputStream.readBytes()
                )
            )
            val response = apiService.postImage(id, image)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun getImageTimestamp(id: String): Resource<Long> {
        return try {
            val response = apiService.getImageTimestamp(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun adminSignUp(userBody: UserBody): Resource<String> {
        return try {
            val response = apiService.adminSignUp(userBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun updateAdmin(id: String, userBody: UserBody): Resource<String> {
        return try {
            val response = apiService.updateAdmin(id, userBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun addFunding(id: String, fundingBody: FundingBody): Resource<String> {
        return try {
            val response = apiService.addFunding(id, fundingBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }
}
