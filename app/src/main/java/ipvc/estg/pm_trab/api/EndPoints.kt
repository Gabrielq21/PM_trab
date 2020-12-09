package ipvc.estg.pm_trab.api

import android.text.Editable
import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @FormUrlEncoded
    @POST("/myslim/api/login/enter")
    fun LoginEnter(@Field("username") username: String, @Field("password") password: String): Call<LoginOutputPost>

    @FormUrlEncoded
    @POST("myslim/api/login/create")
    fun LoginCreate(@Field("username") username: String, @Field("password") password: String): Call<LoginOutputPost>

    @FormUrlEncoded
    @POST("myslim/api/ticket/create")
    fun create(@Field("username") username: String, @Field("tipo") tipo: String, @Field("texto") texto: Editable, @Field("lat") lat: String, @Field("lon") lon: String): Call<TicketOutputPost>

    @GET("/myslim/api/markers")
    fun getMarkers(): Call<List<Problema>>

    @GET("/myslim/api/select/{id}")
    fun getMarker(@Path("id") id: Int): Call<List<Problema>>

    @FormUrlEncoded
    @POST("myslim/api/ticket/update")
    fun updateMarker(@Field("id") id: Int?, @Field("tipo") tipo: String, @Field("texto") texto: String, @Field("lat") lat: String, @Field("lon") lon: String): Call<TicketOutputPost>

    @GET("myslim/api/ticket/remove/{id}")
    fun removeMarker(@Path("id") id: Int?): Call<TicketOutputPost>

}