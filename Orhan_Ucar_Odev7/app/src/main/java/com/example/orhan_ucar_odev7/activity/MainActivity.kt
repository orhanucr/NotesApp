package com.example.orhan_ucar_odev7.activity

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orhan_ucar_odev7.database.DatabaseHelper
import com.example.orhan_ucar_odev7.adapter.NoteAdapter
import com.example.orhan_ucar_odev7.databinding.ActivityMainBinding
import com.example.orhan_ucar_odev7.models.Note

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var noteRecyclerView: RecyclerView
    private lateinit var notes: ArrayList<Note>
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var btnAddNote: ImageView
    private lateinit var inputSearch: EditText
    private lateinit var imageSearch: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        databaseHelper = DatabaseHelper(this)

        noteRecyclerView = binding.notesRecyclerView
        notes = ArrayList()
        noteAdapter = NoteAdapter(notes)

        btnAddNote = binding.imageAddNoteMain
        inputSearch = binding.inputSearch
        imageSearch = binding.imageSearch

        noteRecyclerView.adapter = noteAdapter

        imageSearch.setOnClickListener {
            val searchText = inputSearch.text.toString()
            searchNotes(searchText)
        }

        noteAdapter.setOnItemClickListener(object : NoteAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val note = notes[position]
                startEditNoteActivity(note, position)
            }
        })

        noteRecyclerView.layoutManager = LinearLayoutManager(this)

        btnAddNote.setOnClickListener {
            startEditNoteActivity(null, -1)
        }

        loadNotes()
    }

    private fun loadNotes() {
        val db = databaseHelper.readableDatabase
        val projection = arrayOf(
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_TITLE,
            DatabaseHelper.COLUMN_DETAIL,
            DatabaseHelper.COLUMN_DATE
        )

        val cursor = db.query(
            "notes",
            projection,
            null,
            null,
            null,
            null,
            null
        )

        notes.clear()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE))
            val detail = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DETAIL))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE))
            val note = Note(id, title, detail, date)
            notes.add(note)
        }

        noteAdapter.notifyDataSetChanged()
        cursor.close()
    }

    /*
     private fun deleteNoteFromDatabase(note: Note) {
        val db = databaseHelper.writableDatabase
        val selection = "${DatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(note.id.toString())

        db.delete("notes", selection, selectionArgs)
    }

     */

    private fun searchNotes(searchText: String) {
        val filteredNotes = notes.filter { note ->
            note.title.contains(searchText, ignoreCase = true)
        }

        noteAdapter.setNotes(filteredNotes)
    }

    private fun startEditNoteActivity(note: Note?, position: Int) {
        val intent = Intent(this, CreateNoteActivity::class.java)
        intent.putExtra("note", note)
        intent.putExtra("position", position)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val editedNote = data?.getSerializableExtra("editedNote") as Note
                val position = data.getIntExtra("position", -1)

                if (position == -1) {
                    // Yeni bir not oluşturuldu
                    databaseHelper.insertNoteToDatabase(editedNote)
                    Toast.makeText(this, "Yeni bir not oluşturuldu!", Toast.LENGTH_LONG).show()
                } else {
                    // Varolan bir not güncellendi
                    val existingNote = notes[position]
                    existingNote.title = editedNote.title
                    existingNote.detail = editedNote.detail
                    existingNote.date = editedNote.date
                    val updateSuccessful = databaseHelper.updateNoteInDatabase(existingNote)
                    if (updateSuccessful) {
                        Toast.makeText(this, "Not Güncellendi!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Not Güncellenirken bir hata oluştu.", Toast.LENGTH_LONG).show()
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                val position = data?.getIntExtra("position", -1)
                if (position != -1) {
                    // Silme işlemi gerçekleşmedi, notu sil ve listeyi güncelle
                    val deletedNote = notes[position!!]
                    databaseHelper.deleteNoteFromDatabase(deletedNote)
                    loadNotes()
                    Toast.makeText(this, "Not Başarılı bir şekilde silindi!", Toast.LENGTH_LONG).show()
                }
            }
            loadNotes()
        }
    }

}