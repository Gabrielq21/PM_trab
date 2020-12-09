package ipvc.estg.pm_trab

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import ipvc.estg.pm_trab.api.EndPoints
import ipvc.estg.pm_trab.api.LoginOutputPost
import ipvc.estg.pm_trab.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {
    private lateinit var usernameEditTextView: EditText
    private lateinit var passwordEditTextView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernameEditTextView = findViewById(R.id.login_username)
        passwordEditTextView = findViewById(R.id.login_password)

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE )

        val automatic_login_check = sharedPref.getBoolean(getString(R.string.automatic_login_check), false)
        Log.d("SP_AutoLoginCheck", "$automatic_login_check")

        if( automatic_login_check ) {
            val intent = Intent(this@MainActivity, MapActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun login(view:View) {

        val username = usernameEditTextView.text.toString()
        val password = passwordEditTextView.text.toString().sha256()

        if ( TextUtils.isEmpty(username) ) {
            Toast.makeText(this, R.string.fieldusernameemptylabel, Toast.LENGTH_LONG).show()
            return
        }
        else if ( TextUtils.isEmpty(password) ) {
            Toast.makeText(this, R.string.fieldpasswordemptylabel, Toast.LENGTH_LONG).show()
            return
        }
        else {
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.LoginEnter(
                username,
                password
            )

            call.enqueue(object : Callback<LoginOutputPost> {
                override fun onResponse(call: Call<LoginOutputPost>, response: Response<LoginOutputPost>)
                {
                    if (response.isSuccessful) {
                        val c: LoginOutputPost = response.body()!!
                        if (c.success) {
                            Toast.makeText(this@MainActivity,R.string.logincorrectlabel, Toast.LENGTH_SHORT).show()
                            val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE )
                            with ( sharedPref.edit() ) {
                                putBoolean(getString(R.string.automatic_login_check), true)
                                putString(getString(R.string.automatic_login_username), username )
                                commit()
                            }
                            val intent = Intent(this@MainActivity, MapActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else Toast.makeText(this@MainActivity,R.string.loginincorrectlabel, Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<LoginOutputPost>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun show_notes(view: View) {
        val intent = Intent( this, Notes::class.java )
        startActivity(intent)
    }
    fun create_user(view: View) {
        val username = usernameEditTextView.text.toString()
        val password = passwordEditTextView.text.toString().sha256()

        if ( TextUtils.isEmpty(username) ) {
            Toast.makeText(this, R.string.fieldusernameemptylabel, Toast.LENGTH_LONG).show()
            return
        }
        else if ( TextUtils.isEmpty(password) ) {
            Toast.makeText(this, R.string.fieldpasswordemptylabel, Toast.LENGTH_LONG).show()
            return
        }




        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.LoginCreate(
            username,
            password
        )
        call.enqueue(object : Callback<LoginOutputPost> {
            override fun onResponse(call: Call<LoginOutputPost>, response: Response<LoginOutputPost>)
            {
                if (response.isSuccessful) {
                    val c: LoginOutputPost = response.body()!!
                    if (c.success) {
                        Toast.makeText(this@MainActivity,R.string.logincorrectlabel, Toast.LENGTH_SHORT).show()
                        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE )
                        with ( sharedPref.edit() ) {
                            putBoolean(getString(R.string.automatic_login_check), true)
                            putString(getString(R.string.automatic_login_username), username )
                            commit()
                        }
                        val intent = Intent(this@MainActivity, MapActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else Toast.makeText(this@MainActivity,R.string.loginincorrectlabel, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<LoginOutputPost>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })


    }
    fun String.sha256(): String {
        return hashString(this, "SHA-256")
    }
    private fun hashString(input:String, algorithm:String): String{
        return MessageDigest
            .getInstance(algorithm)
            .digest(input.toByteArray())
            .fold("",{str,it->str + "%02x".format(it)})
    }
}