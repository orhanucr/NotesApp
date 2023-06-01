package com.example.orhan_ucar_odev7.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.orhan_ucar_odev7.databinding.ActivityCreateNoteBinding
import com.example.orhan_ucar_odev7.models.Note
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNoteBinding

    private lateinit var etTitle: EditText
    private lateinit var etDetail: EditText
    private lateinit var tvDate: TextView
    private lateinit var btnDelete: ImageView
    private lateinit var btnDate: Button
    private lateinit var btnSave: ImageView
    private lateinit var btnCancel: ImageView
    private lateinit var calendar: Calendar
    private lateinit var selectedDate: Date
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var currentDate: String

    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        etTitle = binding.etTitle
        etDetail = binding.etDetail
        tvDate = binding.tvDate
        btnDelete = binding.btnDelete
        btnDate = binding.btnDate
        btnSave = binding.btnSave
        btnCancel = binding.btnCancel

        calendar = Calendar.getInstance()
        selectedDate = calendar.time

        dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        currentDate = dateFormat.format(selectedDate)
        tvDate.text = currentDate

        btnDate.setOnClickListener {
            showDatePickerDialog()
        }

        btnSave.setOnClickListener {
            saveNote()
        }

        btnCancel.setOnClickListener {
            cancelEditing()
        }

        btnDelete.setOnClickListener {
            deleteNote()
        }

        val note = intent.getSerializableExtra("note") as Note?
        position = intent.getIntExtra("position", -1)

        note?.let {
            etTitle.setText(note.title)
            etDetail.setText(note.detail)
            tvDate.text = note.date
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.time
                currentDate = dateFormat.format(selectedDate)
                tvDate.text = currentDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun saveNote() {
        val title = etTitle.text.toString().trim()
        val detail = etDetail.text.toString().trim()
        val date = tvDate.text.toString().trim()

        if (title.isEmpty() || detail.isEmpty() || date.isEmpty()) {
            val emptyFields = mutableListOf<String>()
            if (title.isEmpty()) emptyFields.add("Başlık")
            if (detail.isEmpty()) emptyFields.add("Detay")
            if (date.isEmpty()) emptyFields.add("Tarih")

            val errorMessage = "${emptyFields.joinToString(", ")} boş bırakılamaz."
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            return
        }

        val editedNote = Note(0, title, detail, date)

        val resultIntent = Intent()
        resultIntent.putExtra("editedNote", editedNote)
        resultIntent.putExtra("position", position)
        setResult(Activity.RESULT_OK, resultIntent)

        finish()
    }

    private fun cancelEditing() {
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun deleteNote() {
        val resultIntent = Intent()
        resultIntent.putExtra("position", position)
        setResult(Activity.RESULT_CANCELED, resultIntent)
        finish()
    }
}