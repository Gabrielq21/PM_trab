package ipvc.estg.pm_trab.api

data class LoginOutputPost(
    val success: Boolean,
    val username: String,
    val msg : String
)

data class TicketOutputPost(
    val success: Boolean
)
data class Marker(
    val id: Int,
    val texto: String,
    val foto: String,
    val user_id: Int,
    val tipo: String,
    val lat : String,
    val lon : String,
    val username : String

)
