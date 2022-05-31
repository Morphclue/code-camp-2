package org.feature.fox.coffee_counter.di.services.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.feature.fox.coffee_counter.data.models.body.LoginBody
import org.feature.fox.coffee_counter.data.models.body.PurchaseBody
import org.feature.fox.coffee_counter.data.models.body.UserBody
import org.feature.fox.coffee_counter.data.models.response.ItemResponse
import org.feature.fox.coffee_counter.data.models.response.LoginResponse
import org.feature.fox.coffee_counter.data.models.response.UserIdResponse
import org.feature.fox.coffee_counter.data.models.response.UserResponse
import org.feature.fox.coffee_counter.di.module.ApiModule
import org.feature.fox.coffee_counter.util.Constants
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ApiServiceTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private var mockWebServer = MockWebServer()
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        mockWebServer.start()
        apiService = ApiModule.provideApiService(
            ApiModule.provideRetrofit(
                mockWebServer.url("/").toString(),
                ApiModule.provideConverterFactory(),
                ApiModule.provideOkHttpClient(
                    ApiModule.providesLoggingInterceptor()
                )
            )
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `GET Items`() = runBlocking {
        val item1 = ItemResponse("001", "Espresso", 93, 2.0)
        val item2 = ItemResponse("002", "Cappuccino", 18, 3.5)
        val itemList = ArrayList<ItemResponse>(listOf(item1, item2))
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(javaClass.getResource("/json/200-get-items.json")!!.readText())

        mockWebServer.enqueue(response)

        val actualResponse = apiService.getItems()

        assertThat(actualResponse).isNotNull()
        assertThat(actualResponse.code()).isEqualTo(200)
        assertThat(actualResponse.body()?.size).isEqualTo(2)
        assertThat(actualResponse.body()).isEqualTo(itemList)

        val request = mockWebServer.takeRequest()

        assertThat(request.method).isEqualTo("GET")
        assertThat(request.path).isEqualTo(Constants.ITEMS_ENDPOINT)
    }

    @Test
    fun `GET Item by id`() = runBlocking {
        val item = ItemResponse("003", "Latte Macchiato", 5, 3.5)
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(javaClass.getResource("/json/200-get-item-by-id.json")!!.readText())

        mockWebServer.enqueue(response)

        val actualResponse = apiService.getItemById("003")

        assertThat(actualResponse).isNotNull()
        assertThat(actualResponse.code()).isEqualTo(200)
        assertThat(actualResponse.body()).isEqualTo(item)

        val request = mockWebServer.takeRequest()

        assertThat(request.method).isEqualTo("GET")
        assertThat(request.path).isEqualTo(Constants.ITEMS_ENDPOINT + "/003")
    }

    @Test
    fun `GET Item by id with invalid id`() = runBlocking {
        val response = MockResponse()
            .setResponseCode(404)
            .setBody("Item with that ID does not exist")

        mockWebServer.enqueue(response)

        val actualResponse = apiService.getItemById("004")

        assertThat(actualResponse.code()).isEqualTo(404)
        assertThat(actualResponse.body()).isNull()

        val request = mockWebServer.takeRequest()

        assertThat(request.method).isEqualTo("GET")
        assertThat(request.path).isEqualTo(Constants.ITEMS_ENDPOINT + "/004")
    }

    @Test
    fun `GET Users`() = runBlocking {
        val user1 = UserResponse("koffee-admin", "admin")
        val user2 = UserResponse("ad582fa4-d17d-4e2e-9d51-2cae89533ecd", "AndroidB")
        val userList = ArrayList<UserResponse>(listOf(user1, user2))
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(javaClass.getResource("/json/200-get-users.json")!!.readText())

        mockWebServer.enqueue(response)

        val actualResponse = apiService.getUsers()

        assertThat(actualResponse).isNotNull()
        assertThat(actualResponse.code()).isEqualTo(200)
        assertThat(actualResponse.body()?.size).isEqualTo(2)
        assertThat(actualResponse.body()).isEqualTo(userList)

        val request = mockWebServer.takeRequest()

        assertThat(request.method).isEqualTo("GET")
        assertThat(request.path).isEqualTo(Constants.USERS_ENDPOINT)
    }

    @Test
    fun `GET User by id`() = runBlocking {
        val userId = "ad582fa4-d17d-4e2e-9d51-2cae89533ecd"
        val user = UserIdResponse(userId, "AndroidB", 100.0)
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(javaClass.getResource("/json/200-get-user-by-id.json")!!.readText())

        mockWebServer.enqueue(response)

        val actualResponse = apiService.getUserById(userId)

        assertThat(actualResponse).isNotNull()
        assertThat(actualResponse.code()).isEqualTo(200)
        assertThat(actualResponse.body()).isEqualTo(user)

        val request = mockWebServer.takeRequest()

        assertThat(request.method).isEqualTo("GET")
        assertThat(request.path).isEqualTo("${Constants.USERS_ENDPOINT}/$userId")
    }

    @Test
    fun `GET User by id with invalid id`() = runBlocking {
        val wrongUserId = "ad582fa4-d17d-4e2e-9d51-2cae89533ecd123"
        val response = MockResponse()
            .setResponseCode(401)
            .setBody("ID does not match path param")

        mockWebServer.enqueue(response)

        val actualResponse = apiService.getUserById(wrongUserId)

        assertThat(actualResponse.code()).isEqualTo(401)
        assertThat(actualResponse.body()).isNull()

        val request = mockWebServer.takeRequest()

        assertThat(request.method).isEqualTo("GET")
        assertThat(request.path).isEqualTo("${Constants.USERS_ENDPOINT}/$wrongUserId")
    }

    @Test
    fun `PUT update User by id`() = runBlocking {
        val userId = "ad582fa4-d17d-4e2e-9d51-2cae89533ecd"
        val body = UserBody(userId, "foo", "123456789")
        val response = MockResponse()
            .setResponseCode(200)
            .setBody("User.updated.successfully.")

        mockWebServer.enqueue(response)

        val actualResponse = apiService.updateUser(userId, body)

        assertThat(actualResponse).isNotNull()
        assertThat(actualResponse.code()).isEqualTo(200)
        assertThat(actualResponse.body()).isNotNull()

        val request = mockWebServer.takeRequest()

        assertThat(request.method).isEqualTo("PUT")
        assertThat(request.path).isEqualTo("${Constants.USERS_ENDPOINT}/$userId")
    }

    @Test
    fun `DELETE delete User by id`() = runBlocking {
        val userId = "ad582fa4-d17d-4e2e-9d51-2cae89533ecd"
        val response = MockResponse()
            .setResponseCode(200)
            .setBody("User.deleted.successfully.")

        mockWebServer.enqueue(response)

        val actualResponse = apiService.deleteUser(userId)

        assertThat(actualResponse).isNotNull()
        assertThat(actualResponse.code()).isEqualTo(200)
        assertThat(actualResponse.body()).isNotNull()

        val request = mockWebServer.takeRequest()

        assertThat(request.method).isEqualTo("DELETE")
        assertThat(request.path).isEqualTo("${Constants.USERS_ENDPOINT}/$userId")
    }

    @Test
    fun `GET transactions by UserId`() = runBlocking {
        val userId = "ad582fa4-d17d-4e2e-9d51-2cae89533ecd"
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(javaClass.getResource("/json/200-get-transactions.json")!!.readText())

        mockWebServer.enqueue(response)

        val actualResponse = apiService.getTransactions(userId)

        assertThat(actualResponse).isNotNull()
        assertThat(actualResponse.code()).isEqualTo(200)
        assertThat(actualResponse.body()).isNotNull()

        val request = mockWebServer.takeRequest()

        assertThat(request.method).isEqualTo("GET")
        assertThat(request.path).isEqualTo("${Constants.USERS_ENDPOINT}/$userId/transactions")
    }

    @Test
    fun `Post purchase item by id`() = runBlocking {
        val userId = "ad582fa4-d17d-4e2e-9d51-2cae89533ecd"
        val body = PurchaseBody("003", 2)
        val response = MockResponse()
            .setResponseCode(200)
            .setBody("Purchase.processed.successfully.")

        mockWebServer.enqueue(response)

        val actualResponse = apiService.purchaseItem(userId, body)

        assertThat(actualResponse).isNotNull()
        assertThat(actualResponse.code()).isEqualTo(200)
        assertThat(actualResponse.body()).isNotNull()

        val request = mockWebServer.takeRequest()

        assertThat(request.method).isEqualTo("POST")
        assertThat(request.path).isEqualTo("${Constants.USERS_ENDPOINT}/$userId/purchases")
    }

    @Test
    fun `POST login successful`() = runBlocking {
        val login = LoginResponse("abcdef", expiration = 123456789)
        val body = LoginBody("foo", "bar")
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(LoginBody::class.java)
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(javaClass.getResource("/json/200-post-login.json")!!.readText())

        mockWebServer.enqueue(response)

        val actualResponse = apiService.postLogin(body)

        assertThat(actualResponse).isNotNull()
        assertThat(actualResponse.code()).isEqualTo(200)
        assertThat(actualResponse.body()).isEqualTo(login)

        val request = mockWebServer.takeRequest()

        assertThat(request.method).isEqualTo("POST")
        assertThat(request.path).isEqualTo(Constants.LOGIN_ENDPOINT)
        assertThat(adapter.fromJson(request.body.readUtf8())).isEqualTo(body)
    }

    @Test
    fun `POST login invalid body`() = runBlocking {
        val body = LoginBody("", "")
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(LoginBody::class.java)
        val response = MockResponse()
            .setResponseCode(401)
            .setBody("ID or password incorrect")

        mockWebServer.enqueue(response)

        val actualResponse = apiService.postLogin(body)

        assertThat(actualResponse).isNotNull()
        assertThat(actualResponse.code()).isEqualTo(401)
        assertThat(actualResponse.body()).isNull()

        val request = mockWebServer.takeRequest()

        assertThat(request.method).isEqualTo("POST")
        assertThat(request.path).isEqualTo(Constants.LOGIN_ENDPOINT)
        assertThat(adapter.fromJson(request.body.readUtf8())).isEqualTo(body)
    }

    @Test
    fun `POST Users to SignUp`() = runBlocking {
        val body = UserBody("123456789", "foo", "123456789")
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(UserBody::class.java)
        val response = MockResponse()
            .setResponseCode(201)
            .setBody(body.password)

        mockWebServer.enqueue(response)

        val actualResponse = apiService.signUp(body)

        assertThat(actualResponse).isNotNull()
        assertThat(actualResponse.code()).isEqualTo(201)
        assertThat(actualResponse.body()).isEqualTo(response.getBody()?.readUtf8())

        val request = mockWebServer.takeRequest()

        assertThat(request.method).isEqualTo("POST")
        assertThat(request.path).isEqualTo(Constants.USERS_ENDPOINT)
        assertThat(adapter.fromJson(request.body.readUtf8())).isEqualTo(body)
    }
}
