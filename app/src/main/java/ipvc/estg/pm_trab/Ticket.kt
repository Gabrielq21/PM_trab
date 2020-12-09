package ipvc.estg.pm_trab

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.pm_trab.api.EndPoints
import ipvc.estg.pm_trab.api.ServiceBuilder
import ipvc.estg.pm_trab.api.Problema
import ipvc.estg.pm_trab.api.TicketOutputPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private lateinit var edittituloView: EditText
private lateinit var editlatView: TextView
private lateinit var editlonView: TextView
private lateinit var edittipoView: TextView


private lateinit var tituloView: TextView
private lateinit var latView: TextView
private lateinit var lonView: TextView
private lateinit var tipoView: TextView

class Ticket : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val user = sharedPref.getString(getString(R.string.automatic_login_username), null)!!
        var tipo = String()
        val intent = intent
        val id = intent.getStringExtra(EXTRA_MSG)
        val lat = intent.getStringExtra(EXTRA_LAT)
        val lon = intent.getStringExtra(EXTRA_LON)
        val call_id = id?.toInt()
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getMarker(call_id!!)
        call.enqueue(object : Callback<List<Problema>> {
            override fun onResponse(call: Call<List<Problema>>, response: Response<List<Problema>>) {
                if (response.isSuccessful) {
                    val c = response.body()!!
                    for (note in c) {
                        if (!note.username.equals(user)) {
                            setContentView(R.layout.marker_not_owner)
                            tituloView = findViewById(R.id.ticket)
                            latView = findViewById(R.id.lat)
                            lonView = findViewById(R.id.lon)
                            tipoView = findViewById(R.id.tipo)
                            tituloView.setText(note.texto)
                            latView.setText(note.lat)
                            lonView.setText(note.lon)
                            tipoView.setText(note.tipo)


                        }else{
                            setContentView(R.layout.marker_onwer)
                            edittituloView = findViewById(R.id.edit_ticket)
                            editlatView = findViewById(R.id.edit_lat)
                            editlonView = findViewById(R.id.edit_lon)
                            edittipoView = findViewById(R.id.edit_tipo)
                            edittituloView.setText(note.texto)
                            editlatView.setText(note.lat)
                            editlonView.setText(note.lon)
                            edittipoView.setText(note.tipo)

                            val local = findViewById<FloatingActionButton>(R.id.btn_local)
                            local.setOnClickListener {
                                editlatView.setText(lat)
                                editlonView.setText(lon)
                            }
                            val type = findViewById<FloatingActionButton>(R.id.btn_type)
                            type.setOnClickListener {
                                edittipoView.setText(tipo)
                            }

                            val types = resources.getStringArray(R.array.Types)
                            val spinner = findViewById<Spinner>(R.id.spinner1)
                            if (spinner != null) {
                                val adapter = ArrayAdapter(
                                    this@Ticket,
                                    android.R.layout.simple_spinner_item,
                                    types
                                )
                                spinner.adapter = adapter

                                spinner.onItemSelectedListener = object :
                                    AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(parent: AdapterView<*>,view: View, position: Int, id: Long) {
                                        tipo = types[position]
                                    }
                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                    }
                                }
                            }

                            val save = findViewById<Button>(R.id.button_save)
                            save.setOnClickListener {
                                val callid = id?.toInt()
                                val calltipo = edittipoView.getText().toString()
                                val calltexto = edittituloView.getText().toString()
                                val calllat = editlatView.getText().toString()
                                val calllon = editlonView.getText().toString()
                                val request =
                                    ServiceBuilder.buildService(EndPoints::class.java)
                                val call = request.updateMarker(
                                    callid,
                                    calltipo,
                                    calltexto,
                                    calllat,
                                    calllon
                                )
                                call.enqueue(object : Callback<TicketOutputPost> {
                                    override fun onResponse(call: Call<TicketOutputPost>, response: Response<TicketOutputPost>) {
                                        if (response.isSuccessful){
                                            val c = response.body()!!
                                            if (c.success) {
                                                Toast.makeText(this@Ticket,"Sucesso", Toast.LENGTH_SHORT).show()
                                                val intent = Intent(this@Ticket, MapActivity::class.java)
                                                startActivity(intent)
                                            } else Toast.makeText(this@Ticket, "Falha", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                    override fun onFailure(call: Call<TicketOutputPost>, t: Throwable) {
                                        Toast.makeText(this@Ticket, "${t.message}", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }


                            val remove = findViewById<Button>(R.id.button_remove)
                            remove.setOnClickListener {
                                val callid = id?.toInt()
                                val request = ServiceBuilder.buildService(EndPoints::class.java)
                                val call = request.removeMarker(callid)
                                call.enqueue(object : Callback<TicketOutputPost> {
                                    override fun onResponse(call: Call<TicketOutputPost>, response: Response<TicketOutputPost>) {
                                        if (response.isSuccessful) {
                                            val c = response.body()!!
                                            if (c.success) {
                                                Toast.makeText(this@Ticket,"Sucesso", Toast.LENGTH_SHORT).show()
                                                val intent = Intent(this@Ticket, MapActivity::class.java)
                                                startActivity(intent)
                                            } else Toast.makeText(this@Ticket, "Falha", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                    override fun onFailure(call: Call<TicketOutputPost>, t: Throwable) {
                                        Toast.makeText(this@Ticket, "${t.message}", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }
                        }

                    }
                }
            }
            override fun onFailure(call: Call<List<Problema>>, t: Throwable) {
                Toast.makeText(this@Ticket, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    companion object {
        const val EXTRA_MSG = "com.example.android.wordlistsql.MSG"
        const val EXTRA_LAT = "com.example.android.wordlistsql.LAT"
        const val EXTRA_LON = "com.example.android.wordlistsql.LON"
    }

}