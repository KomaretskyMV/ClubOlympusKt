package com.example.android.clubolympuskt

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.android.clubolympuskt.data.ClubOlympusContract.MemberEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
//    private lateinit var floatingActionButton: FloatingActionButton
    lateinit var dataTextView : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataTextView = findViewById(R.id.dataTextView)

        val floatingActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddMemberActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        displayData()
    }

    private fun displayData() {
        val projection = arrayOf(
            MemberEntry._ID,
            MemberEntry.COLUMN_FIRST_NAME,
            MemberEntry.COLUMN_LAST_NAME,
            MemberEntry.COLUMN_GENDER,
            MemberEntry.COLUMN_SPORT
        )
        val cursor = contentResolver.query(
            MemberEntry.CONTENT_URI, projection,
            null, null, null
        )
        dataTextView.text = "All members\n\n"
        dataTextView.append(
            MemberEntry._ID + " " +
            MemberEntry.COLUMN_FIRST_NAME + " " +
            MemberEntry.COLUMN_LAST_NAME + " " +
            MemberEntry.COLUMN_GENDER + " " +
            MemberEntry.COLUMN_SPORT
        )
        val idIndex = cursor!!.getColumnIndex(MemberEntry._ID)
        val idFirstName = cursor.getColumnIndex(MemberEntry.COLUMN_FIRST_NAME)
        val idLastName = cursor.getColumnIndex(MemberEntry.COLUMN_LAST_NAME)
        val idGender = cursor.getColumnIndex(MemberEntry.COLUMN_GENDER)
        val idSport = cursor.getColumnIndex(MemberEntry.COLUMN_SPORT)

        while (cursor.moveToNext()) {
            val currentId = cursor.getInt(idIndex)
            val currentFirstName = cursor.getString(idFirstName)
            val currentLastName = cursor.getString(idLastName)
            val currentGender = cursor.getInt(idGender)
            val currentSport = cursor.getString(idSport)

            dataTextView.append(
                "\n" + currentId + " " + currentFirstName + " "
                 + currentLastName + " " + currentGender + " " + currentSport
            )
        }
        cursor.close()
    }
}