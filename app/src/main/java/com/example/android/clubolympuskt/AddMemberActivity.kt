package com.example.android.clubolympuskt

import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NavUtils
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.example.android.clubolympuskt.data.*

class AddMemberActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var sportEditText: EditText
    private lateinit var genderSpinner: Spinner
    private var gender : Int = 0

    companion object {
        private const val EDIT_MEMBER_LOADER = 111
    }

    //    lateinit var currentMemberUri : Uri
    var currentMemberUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_member)

        val intent = intent
        currentMemberUri = intent.data

        if (currentMemberUri == null) {
            title = "Add a Member"
            invalidateOptionsMenu()
        } else {
            title = "Edit the Member"
            LoaderManager.getInstance(this).initLoader(EDIT_MEMBER_LOADER, null, this)
//            supportLoaderManager.initLoader(EDIT_MEMBER_LOADER, null, this)
        }

        firstNameEditText = findViewById(R.id.fistNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        sportEditText = findViewById(R.id.sportEditText)
        genderSpinner = findViewById(R.id.genderSpinner)

//        val spinnerArrayList = arrayListOf("Unknown", "Male", "Female")
//        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerArrayList)
        val spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_gender,
                android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = spinnerAdapter
        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
                    when (parent!!.getItemAtPosition(position)) {
                        "Male" -> gender = GENDER_MALE
                        "Female" -> gender = GENDER_FEMALE
                        else -> gender = GENDER_UNKNOWN
                    }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                gender = 0
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        super.onPrepareOptionsMenu(menu)

        if (currentMemberUri == null) {
            val menuItem = menu!!.findItem(R.id.delete_member)
            menuItem.isVisible = false
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_member_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_member -> {
                saveMember()
                return true
            }
            R.id.delete_member -> {
                showDeleteMemberDialog()
                return true
            }
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveMember() {
        val firstName = firstNameEditText.text.toString().trim()
        val lastName = lastNameEditText.text.toString().trim()
        val sport = sportEditText.text.toString().trim()

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Input the first name", Toast.LENGTH_LONG).show()
            return
        } else if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Input the last name", Toast.LENGTH_LONG).show()
            return
        } else if (gender == GENDER_UNKNOWN) {
            Toast.makeText(this, "Choose the gender", Toast.LENGTH_LONG).show()
            return
        } else if (TextUtils.isEmpty(sport)) {
            Toast.makeText(this, "Input the sport", Toast.LENGTH_LONG).show()
            return
        }

        val contentValues = ContentValues()
        contentValues.put(COLUMN_FIRST_NAME, firstName)
        contentValues.put(COLUMN_LAST_NAME, lastName)
        contentValues.put(COLUMN_SPORT, sport)
        contentValues.put(COLUMN_GENDER, gender)

        if (currentMemberUri == null) {
            val contentResolver = contentResolver
            val uri = contentResolver.insert(CONTENT_URI, contentValues)

            if (uri == null) {
                Toast.makeText(this, "Insertion of data in the table failed", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Data saved", Toast.LENGTH_LONG).show()
            }
        } else {
            val rowsChanged = contentResolver.update(currentMemberUri!!, contentValues, null, null)

            if (rowsChanged == 0) {
                Toast.makeText(this, "Saving of data in the table failed", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Member updated", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val projection = arrayOf(_ID,
                COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME,
                COLUMN_GENDER,
                COLUMN_SPORT)
        return CursorLoader(this,
                currentMemberUri!!,
                projection,
                null,
                null,
                null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data!!.moveToFirst()) {
            val firstNameColumnIndex = data.getColumnIndex(COLUMN_FIRST_NAME)
            val lastNameColumnIndex = data.getColumnIndex(COLUMN_LAST_NAME)
            val genderColumnIndex = data.getColumnIndex(COLUMN_GENDER)
            val sportColumnIndex = data.getColumnIndex(COLUMN_SPORT)

            val firstName = data.getString(firstNameColumnIndex)
            val lastName = data.getString(lastNameColumnIndex)
            val gender = data.getInt(genderColumnIndex)
            val sport = data.getString(sportColumnIndex)

            firstNameEditText.setText(firstName)
            lastNameEditText.setText(lastName)
            sportEditText.setText(sport)

            when (gender) {
                GENDER_MALE -> genderSpinner.setSelection(1)
                GENDER_FEMALE -> genderSpinner.setSelection(2)
                GENDER_UNKNOWN -> genderSpinner.setSelection(0)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        TODO("Not yet implemented")
    }

    private fun showDeleteMemberDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want delete the member?")
        builder.setPositiveButton("Delete") { dialog, which -> deleteMember() }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog?.dismiss()}
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun deleteMember() {
        if (currentMemberUri != null) {
            val rowsDeleted = contentResolver.delete(currentMemberUri!!, null, null)
            if (rowsDeleted == 0 ) {
                Toast.makeText(this, "Deleting of data from the table failed", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Member is deleted", Toast.LENGTH_LONG).show()
            }
            finish()
        }
    }
}