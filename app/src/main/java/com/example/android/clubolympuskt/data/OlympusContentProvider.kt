package com.example.android.clubolympuskt.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
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
            else -> throw IllegalArgumentException("Can't query incorrect URI $uri")
        }
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbOpenHelper.writableDatabase
        val match: Int = sUriMatcher.match(uri)
        var id : Long

        return when (match) {
            MEMBERS -> {
                id = db.insert(MemberEntry.TABLE_NAME, null, values)
                if (id.equals(-1)) {
                    Log.e("insertMethod", "Insertion of data in the table failed for $uri")
                    return null
                }
                ContentUris.withAppendedId(uri, id)
            }
            else -> throw IllegalArgumentException("Insertion of data in the table failed for $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbOpenHelper.writableDatabase
        val match = sUriMatcher.match(uri)
        var localSelection = selection ?: " "
        var localSelectionArgs = selectionArgs ?: " "

        return when (match) {
            MEMBERS -> db.delete(MemberEntry.TABLE_NAME, selection, selectionArgs)
            MEMBERS_ID -> {
                localSelection = "${MemberEntry._ID} =?"
                localSelectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                db.delete(MemberEntry.TABLE_NAME, localSelection, localSelectionArgs)
            }
            else -> throw IllegalArgumentException("Can't delete this URI $uri")
        }
    }

    override fun update(
            uri: Uri, values: ContentValues?, selection: String?,
            selectionArgs: Array<out String>?
    ): Int {
        val db = dbOpenHelper.writableDatabase
        val match = sUriMatcher.match(uri)
        var localSelection = selection ?: " "
        var localSelectionArgs = selectionArgs ?: " "

        return when (match) {
            MEMBERS -> db.update(MemberEntry.TABLE_NAME, values, selection, selectionArgs)
            MEMBERS_ID -> {
                localSelection = "${MemberEntry._ID} =?"
                localSelectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                db.update(MemberEntry.TABLE_NAME, values, localSelection, localSelectionArgs)
            }
            else -> throw IllegalArgumentException("Can't update this URI $uri")
        }
    }

    override fun getType(uri: Uri): String? =
        when (sUriMatcher.match(uri)) {
            MEMBERS -> MemberEntry.CONTENT_MULTIPLE_ITEMS
            MEMBERS_ID -> MemberEntry.CONTENT_SINGLE_ITEM
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

}