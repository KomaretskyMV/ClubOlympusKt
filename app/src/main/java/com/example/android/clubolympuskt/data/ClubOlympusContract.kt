package com.example.android.clubolympuskt.data

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

const val DATABASE_VERSION = 1
const val DATABASE_NAME = "olympus"
const val SCHEME = "content://"
const val AUTHORITY = "com.example.android.clubolympuskt"
const val PATH_MEMBERS = "members"
val BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY)

const val TABLE_NAME = "members"

val CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEMBERS)

const val CONTENT_MULTIPLE_ITEMS = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/$AUTHORITY/$PATH_MEMBERS"
const val CONTENT_SINGLE_ITEM = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/$AUTHORITY/$PATH_MEMBERS"

const val _ID = BaseColumns._ID
const val COLUMN_FIRST_NAME = "firstName"
const val COLUMN_LAST_NAME = "lastName"
const val COLUMN_GENDER = "gender"
const val COLUMN_SPORT = "sport"

const val GENDER_UNKNOWN = 0
const val GENDER_MALE = 1
const val GENDER_FEMALE = 2
