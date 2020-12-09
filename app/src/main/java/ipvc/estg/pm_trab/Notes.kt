import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.pm_trab.AddNote
import ipvc.estg.pm_trab.R
import ipvc.estg.pm_trab.UpdateNote
import ipvc.estg.pm_trab.adapter.NoteAdapter
import ipvc.estg.pm_trab.entity.Note
import ipvc.estg.pm_trab.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_notes.*


private lateinit var noteViewModel: NoteViewModel
class Notes : AppCompatActivity(), NoteAdapter.OnItemClickListener{

    private val AddNoteRequestCode = 1
    private val UpdateActivityRequestCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NoteAdapter(this,this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, {notes ->
            notes?.let{adapter.setNotes(it)}
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@Notes, AddNote::class.java)
            startActivityForResult(intent, AddNoteRequestCode)
        }

        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback( 0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.deleteNote( adapter.getNoteAt(viewHolder.adapterPosition) )
            }
        }
        val itemTouchHelper = ItemTouchHelper( itemTouchHelperCallback )
        itemTouchHelper.attachToRecyclerView( recyclerview )

    }

    override fun onItemClicked(note: Note) {
        val intent = Intent( this, UpdateNote::class.java)
        intent.putExtra(UpdateNote.EXTRA_ID, note.id)
        intent.putExtra(UpdateNote.EXTRA_TITULO, note.titulo)
        intent.putExtra(UpdateNote.EXTRA_DATE, note.date)
        intent.putExtra(UpdateNote.EXTRA_TEXTO, note.texto)
        startActivityForResult(intent, UpdateActivityRequestCode)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AddNoteRequestCode && resultCode == RESULT_OK) {
            val titulo = data?.getStringExtra(AddNote.EXTRA_TITULO).toString()
            val texto =  data?.getStringExtra(AddNote.EXTRA_TEXTO).toString()
            val date =   data?.getStringExtra(AddNote.EXTRA_DATE).toString()
            val note = Note(titulo = (titulo), texto = (texto), date = (date))
            noteViewModel.insert(note)

            Toast.makeText(applicationContext,"Nota Adicionada", Toast.LENGTH_LONG).show()

        }
        else if(requestCode == AddNoteRequestCode) {
            Toast.makeText(applicationContext,"Campos Incompletos", Toast.LENGTH_LONG).show()
        }
        if (requestCode == UpdateActivityRequestCode && resultCode == RESULT_OK) {
            val id = data?.getIntExtra( UpdateNote.EXTRA_ID, -1 )

            val titulo = data?.getStringExtra( UpdateNote.EXTRA_TITULO ).toString()
            val texto = data?.getStringExtra( UpdateNote.EXTRA_TEXTO ).toString()
            val date = data?.getStringExtra( UpdateNote.EXTRA_DATE ).toString()
            val note = Note(id,titulo,texto,date)

            noteViewModel.updateNote(note)
            Toast.makeText(applicationContext,"Nota Editada", Toast.LENGTH_LONG).show()
        }
        else if(requestCode == UpdateActivityRequestCode) {
            Toast.makeText(applicationContext,"Campos Incompletos", Toast.LENGTH_LONG).show()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater =menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.deleteall -> {
                noteViewModel.deleteAll()
                Toast.makeText(applicationContext,"Todas as notas Apagadas", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }




}


