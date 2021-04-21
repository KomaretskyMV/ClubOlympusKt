package com.example.android.clubolympuskt

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView

import com.example.android.clubolympuskt.data.ClubOlympusContract.MemberEntry

class MemberCursorAdapter(context: Context?, cursor: Cursor?, autoRequery: Boolean) :
        CursorAdapter(context, cursor, autoRequery) {
    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(R.layout.member_item, parent, false)
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        val firstNameTextView = view!!.findViewById<TextView>(R.id.firstNameTextView)
        val lastNameTextView = view.findViewById<TextView>(R.id.lastNameTextText)
        val sportTextView = view.findViewById<TextView>(R.id.sportTextView)

        val firstName = cursor!!.getString(cursor.getColumnIndexOrThrow(MemberEntry.COLUMN_FIRST_NAME))
        val lastName = cursor!!.getString(cursor.getColumnIndexOrThrow(MemberEntry.COLUMN_LAST_NAME))
        val sport = cursor!!.getString(cursor.getColumnIndexOrThrow(MemberEntry.COLUMN_SPORT))

        firstNameTextView.text = firstName
        lastNameTextView.text = lastName
        sportTextView.text = sport
    }
}