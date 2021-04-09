package com.example.android.clubolympuskt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.app.NavUtils

class AddMemberActivity : AppCompatActivity() {
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var groupEditText: EditText
    private lateinit var genderSpinner: Spinner
    private var gender : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_member)

        firstNameEditText = findViewById(R.id.fistNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        groupEditText = findViewById(R.id.groupEditText)
        genderSpinner = findViewById(R.id.genderSpinner)

//        val spinnerArrayList = arrayListOf("Unknown", "Male", "Female")
//        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerArrayList)
        val spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_gender,
                                                            android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = spinnerAdapter
        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selectedGender = parent!!.getItemAtPosition(position)
                when (selectedGender) {
                    "Male" -> gender = 1
                    "Female" -> gender = 2
                    else -> gender = 0
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                gender = 0
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_member_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_member -> return true
            R.id.delete_member -> return true
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true}
        }
        return super.onOptionsItemSelected(item)
    }
}