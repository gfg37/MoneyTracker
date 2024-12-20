package com.example.myapp1.serv




import com.example.myapp1.model.TransactionRequest
import com.example.myapp1.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @POST("users/register")
    fun register(@Body user: User): Call<User>

    @POST("users/authenticate")
    fun authenticate(@Body request: AuthenticationRequest): Call<User>

    @POST("/transactions")
    fun createTransaction(@Body transaction: TransactionRequest): Call<Void>

    @GET("/users/by-username/{username}")
    fun getUserByUsername(@Path("username") username: String): Call<User>

    @GET("/transactions/balance/{id}")
    fun getBalance(@Path("id") userId: Long): Call<Double>

}