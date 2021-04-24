package com.example.android.clubolympuskt

import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.example.android.clubolympuskt.data.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        private const val MEMBER_LOADER = 123
    }

    lateinit var memberCursorAdapter : MemberCursorAdapter

    lateinit var dataListView : ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataListView = findViewById(R.id.dataListView)

        val floatingActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddMemberActivity::class.java)
            startActivity(intent)
        }

        memberCursorAdapter = MemberCursorAdapter(this, null, false)
        dataListView.adapter = memberCursorAdapter
        dataListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            intent = Intent(this@MainActivity, AddMemberActivity::class.java)
            val currentMemberUri = ContentUris.withAppendedId(CONTENT_URI, id)
            intent.data = currentMemberUri
            startActivity(intent)
        }

        LoaderManager.getInstance(this).initLoader(MEMBER_LOADER, null, this)
//        supportLoaderManager.initLoader(MEMBER_LOADER, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val projection = arrayOf(
                _ID,
                COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME,
                COLUMN_SPORT
        )
        return CursorLoader(this,
                CONTENT_URI, projection,
                null, null, null
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        memberCursorAdapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        memberCursorAdapter.swapCursor(null)
    }
}