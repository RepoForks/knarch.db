package co.touchlab.notepad.db

import co.touchlab.multiplatform.architecture.db.ContentValues
import co.touchlab.multiplatform.architecture.db.sqlite.*
import co.touchlab.notepad.db.NoteColumns.Companion.NOTES_TABLE_NAME

class NoteDbHelper {
    var noteUpdate: (()->Unit)? = null
    private var helper:SQLiteOpenHelper
    init {
        helper = createOpenHelper("holla", initCallback(), null)
    }

    private fun initCallback(): PlatformSQLiteOpenHelperCallback {
        return object : PlatformSQLiteOpenHelperCallback(3) {
            override fun onCreate(db: SQLiteDatabase) {
                db.execSQL(
                        "CREATE TABLE " + NOTES_TABLE_NAME + " ("
                                + NoteColumns._ID + " INTEGER PRIMARY KEY,"
                                + NoteColumns.TITLE + " TEXT,"
                                + NoteColumns.NOTE + " TEXT,"
                                + NoteColumns.HI_BLOB + " BLOB,"
                                + NoteColumns.CREATED_DATE + " INTEGER,"
                                + NoteColumns.MODIFIED_DATE + " INTEGER"
                                + ");")
                println("Create table success!!!!!")
            }

            override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                db.execSQL("DROP TABLE IF EXISTS notes")
                onCreate(db)
            }
        }
    }

    private fun insertNote(note: Note) {
        val cv = ContentValues()
        cv.put(NoteColumns.TITLE, note.title)
        cv.put(NoteColumns.NOTE, note.note)
        cv.put(NoteColumns.CREATED_DATE, note.created)
        cv.put(NoteColumns.MODIFIED_DATE, note.modified)
        if (note.hiBlob == null)
            cv.putNull(NoteColumns.HI_BLOB)
        else
            cv.put(NoteColumns.HI_BLOB, note.hiBlob)

        helper.getWritableDatabase().insert(
                table = NoteColumns.NOTES_TABLE_NAME,
                values = cv
        )
    }

    fun deleteNotes(){
        helper.getWritableDatabase().delete(
                NoteColumns.NOTES_TABLE_NAME,null, null
        )
    }


    fun getNotes(): Array<Note> {
        val db = helper.getWritableDatabase()
        val cursor = db.query(
                table = NoteColumns.NOTES_TABLE_NAME,
                orderBy = "${NoteColumns.MODIFIED_DATE} desc"
        )

        cursor.moveToFirst()

        val notes = Array(cursor.getCount()
        ) { i ->

            val blobIndex = cursor.getColumnIndex(NoteColumns.HI_BLOB)
            val note = Note(
                    cursor.getString(cursor.getColumnIndex(NoteColumns.TITLE)),
                    cursor.getString(cursor.getColumnIndex(NoteColumns.NOTE)),
                    cursor.getLong(cursor.getColumnIndex(NoteColumns.CREATED_DATE)),
                    cursor.getLong(cursor.getColumnIndex(NoteColumns.MODIFIED_DATE)),
                    if (cursor.isNull(blobIndex))
                        null
                    else
                        cursor.getBlob(blobIndex)

            )

            cursor.moveToNext()
            note
        }

        return notes
    }

    fun insertNotes(note: Array<Note>) {
        val db = helper.getWritableDatabase()
        db.withTransaction {
            for (s in note) {
                insertNote(s)
            }
        }
    }
}

interface NoteUpdate {
    fun changes(notes: Array<Note>)
}
