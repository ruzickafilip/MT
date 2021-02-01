import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cz.utb.mtsp.Show

const val DATABASENAME = "SHOW"
const val TABLENAME = "Show"
const val COL_ID = "id"
const val COL_NAME = "name"
const val COL_LANGUAGE = "language"
const val COL_OFFICIALSITE = "officialSite"
const val COL_PREMIERED = "premiered"
const val COL_RATING = "rating"
const val COL_SUMMARY = "summary"
const val COL_DATE = "summary"

class Database(var context: Context) : SQLiteOpenHelper(context, DATABASENAME, null,
    1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLENAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,$COL_NAME VARCHAR(256),$COL_LANGUAGE VARCHAR(256),$COL_OFFICIALSITE VARCHAR(256),$COL_PREMIERED VARCHAR(256),$COL_RATING VARCHAR(256),$COL_SUMMARY VARCHAR(256),$COL_DATE VARCHAR(256))"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertData(show: Show) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_NAME, show.name)
        contentValues.put(COL_LANGUAGE, show.language)
        contentValues.put(COL_OFFICIALSITE, show.officialSite)
        contentValues.put(COL_PREMIERED, show.premiered)
        contentValues.put(COL_RATING, show.rating)
        contentValues.put(COL_SUMMARY, show.summary)
        contentValues.put(COL_DATE, show.date)
        database.insert(TABLENAME, null, contentValues)
    }
    fun readData(): MutableList<Show> {
        val list: MutableList<Show> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val show = Show(result.getString(result.getColumnIndex(COL_ID)).toInt(),
                                                result.getString(result.getColumnIndex(COL_LANGUAGE)),
                                                result.getString(result.getColumnIndex(COL_NAME)),
                                                result.getString(result.getColumnIndex(COL_OFFICIALSITE)),
                                                result.getString(result.getColumnIndex(COL_PREMIERED)),
                                                result.getString(result.getColumnIndex(COL_RATING)),
                                                result.getString(result.getColumnIndex(COL_SUMMARY)),
                                                result.getString(result.getColumnIndex(COL_DATE)))
                list.add(show)
            }
            while (result.moveToNext())
        }
        return list
    }

    fun deleteData() {
        val db = this.writableDatabase
        db.delete(TABLENAME, null, null)
    }
}