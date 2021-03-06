/*
 * Copyright (c) 2018 Touchlab Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.touchlab.multiplatform.architecture.db.sqlite

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import co.touchlab.multiplatform.architecture.db.DatabaseErrorHandler

class PlatformSQLiteOpenHelper(
        val callback:PlatformSQLiteOpenHelperCallback,
        context: Context,
        name:String?,
        version:Int,
        errorHandler: DatabaseErrorHandler?):SQLiteOpenHelper(context,name,null,version, errorHandler){

    override fun onCreate(db: SQLiteDatabase) {
        callback.onCreate(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        callback.onUpgrade(db, oldVersion, newVersion)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        callback.onDowngrade(db, oldVersion, newVersion)
    }

    override fun onOpen(db: SQLiteDatabase) {
        callback.onOpen(db)
    }

    override fun onConfigure(db: SQLiteDatabase) {
        callback.onConfigure(db)
    }
}