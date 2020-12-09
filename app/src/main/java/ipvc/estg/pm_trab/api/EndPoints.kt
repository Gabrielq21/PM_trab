package ipvc.estg.pm_trab.api

import android.text.Editable
import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @FormUrlEncoded
    @POST("/myslim/api/login/enter")
    fun postTest(@Field("username") username: String, @Field("password") password: String): Call<LoginOutputPost>
    @FormUrlEncoded
    @POST("myslim/api/login/create")
    fun postcreate(@Field("username") username: String, @Field("password") password: String): Call<LoginOutputPost>
    @FormUrlEncoded
    @POST("myslim/api/ticket/create")
    fun create(@Field("username") username: String, @Field("tipo") tipo: String, @Field("texto") texto: Editable, @Field("lat") lat: String, @Field("lon") lon: String, @Field("foto") foto: String): Call<TicketOutputPost>
    @GET("/myslim/api/markers")
    fun getNotas(): Call<List<Nota>>
    @GET("/myslim/api/select/{id}")
    fun getNota(@Path("id") id: Int): Call<List<Nota>>
    @FormUrlEncoded
    @POST("myslim/api/ticket/update")
    fun updateTicket(@Field("id") id: Int?, @Field("tipo") tipo: String, @Field("texto") texto: String, @Field("lat") lat: String, @Field("lon") lon: String): Call<TicketOutputPost>
    @GET("myslim/api/ticket/remove/{id}")
    fun removeTicket(@Path("id") id: Int?): Call<TicketOutputPost>

}