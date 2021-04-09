package com.example.android.clubolympuskt.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.widget.Toast
import com.example.android.clubolympuskt.data.ClubOlympusContract.MemberEntry

class OlympusContentProvider : ContentProvider() {

    lateinit var dbOpenHelper : OlympusDbOpenHelper

    private val MEMBERS = 111
    private val MEMBERS_ID = 222

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(ClubOlympusContract.AUTHORITY, ClubOlympusContract.PATH_MEMBERS, MEMBERS)
            addURI(
                ClubOlympusContract.AUTHORITY,
                ClubOlympusContract.PATH_MEMBERS + "/#",
                MEMBERS_ID
            )
        }

    override fun onCreate(): Boolean {
        dbOpenHelper = OlympusDbOpenHelper(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<out String>?, selection: String?,
        selectionArgs: Array<out String>?, sortOrder: String?
    ): Cursor? {
        val db = dbOpenHelper.readableDatabase
        val cursor: Cursor
        val match: Int = sUriMatcher.match(uri)
        var localSelection = selection ?: " "
        var localSelectionArgs = selectionArgs ?: " "

        when (match) {
            MEMBERS -> cursor = db.query(
                MemberEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder
            )
            MEMBERS_ID -> {
                localSelection = "${MemberEntry._ID} =?"
                localSelectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                cursor = db.query(
                    MemberEntry.TABLE_NAME, projection, selection,
                    selectionArgs, null, null, sortOrder
                )
            }
            else -> {
                Toast.makeText(context, "Incorrect URI", Toast.LENGTH_LONG).show()
                throw IllegalArgumentException("Can't query incorrect URI $uri")
            }
        }
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }
}