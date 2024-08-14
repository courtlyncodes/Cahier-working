package com.example.cahier.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Note::class, StylusDrawing::class], version = 3)
@TypeConverters(Converters::class) /* Used a type converter to convert the Path object to a string and back*/

abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun drawingDao(): DrawingDao

    companion object {
        @Volatile
        private var Instance: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            return Instance ?: synchronized(this) {
                Instance ?: Room.databaseBuilder(
                    context,
                    NoteDatabase::class.java,
                    "note_database"
                )

                    .createFromAsset("database/notes.db") /* This file is in the assets folder. Created from the terminal */
                    .addMigrations(MIGRATION_2_3) //** Had to migrate from version 2 to 3 to get the foreign key working */
                    .build()
                    .also {
                        Instance = it
                    }
            }
        }

        // Migration from version 2 to 3
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE stylus_drawings (drawingId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, noteId INTEGER NOT NULL, stylusState TEXT NOT NULL, FOREIGN KEY(noteId) REFERENCES notes(id) ON DELETE CASCADE)")
            }
    }

}
}

