package ipvc.estg.pm_trab

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

class AddNote : AppCompatActivity() {
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    val currentDate = sdf.format(Date())
    private lateinit var edittituloView: EditText
    private lateinit var edittextoView: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_change)
        edittituloView = findViewById(R.id.edit_titulo)
        edittextoView = findViewById(R.id.edit_texto)
        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(edittituloView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else if (TextUtils.isEmpty(edittextoView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val titulo = edittituloView.text.toString()
                val texto = edittextoView.text.toString()
                val date = currentDate.toString()
                replyIntent.putExtra(EXTRA_TITULO, titulo)
                replyIntent.putExtra(EXTRA_TEXTO, texto)
                replyIntent.putExtra(EXTRA_DATE, date)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_TITULO = "com.example.android.wordlistsql.TITULO"
        const val EXTRA_TEXTO = "com.example.android.wordlistsql.TEXTO"
        const val EXTRA_DATE = "com.example.android.wordlistsql.DATE"
    }
}