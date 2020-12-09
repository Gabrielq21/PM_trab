package ipvc.estg.pm_trab

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class UpdateNote : AppCompatActivity() {

    private lateinit var edittituloView: EditText
    private lateinit var edittextoView: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_change)
        edittituloView = findViewById(R.id.edit_titulo)
        edittextoView = findViewById(R.id.edit_texto)

        val intent = intent
        edittituloView.setText( intent.getStringExtra(EXTRA_TITULO) )
        edittextoView.setText( intent.getStringExtra(EXTRA_TEXTO) )
        val date= intent.getStringExtra(EXTRA_DATE)
        val id = intent.getIntExtra( EXTRA_ID , -1)
        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()

            val titulo = edittituloView.text.toString()
            val texto = edittextoView.text.toString()
            if( id != -1 ) {
                replyIntent.putExtra(EXTRA_ID, id)
            }
            replyIntent.putExtra(EXTRA_TITULO, titulo)
            replyIntent.putExtra(EXTRA_TEXTO, texto)
            replyIntent.putExtra(EXTRA_DATE, date)
            setResult(Activity.RESULT_OK, replyIntent)

            finish()
        }
    }

    companion object {
        const val EXTRA_ID = "com.example.android.wordlistsql.ID"
        const val EXTRA_TITULO = "com.example.android.wordlistsql.TITULO"
        const val EXTRA_TEXTO = "com.example.android.wordlistsql.TEXTO"
        const val EXTRA_DATE = "com.example.android.wordlistsql.DATE"
    }
}