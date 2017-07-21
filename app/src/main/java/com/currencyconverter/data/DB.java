package com.currencyconverter.data;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DB extends SQLiteOpenHelper {

    private static final String TAG = DB.class.getSimpleName();

    public static final String DB_NAME = "currency_conversion.db";
    public static final int DB_VERSION = 1;

    Context context;
    private SQLiteDatabase mDatabase;

    public DB(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        mDatabase = db;

        db.execSQL(TableConversionRates.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableConversionRates.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Content Provider Method variables
     */
    public static final String METHOD_EXECUTE_SQL = "execute_sql";
    public static final String METHOD_RAW_SQL = "raw_query";
    public static final String METHOD_CREATE_TABLE = "create_table";
    public static final String METHOD_GET_TABLE_NAMES = "get_tables";
    public static final String METHOD_GET_TABLE_COLUMNS = "get_table_columns";
    public static final String METHOD_TABLE_EXISTS = "get_table_exists";

    /**
     * Content Provider Result Value variables
     */
    public static final String VALUE_RESPONSE = "response"; // 0 or 1
    public static final String VALUE_ERROR = "error";
    //public static final String VALUE_RESULT = "result";

    /**
     * Android Default Tables - have to be ignored
     */
    public static final String TABLE_IGNORE_METADATA = "android_metadata";
    public static final String TABLE_IGNORE_SEQUENCE = "sqlite_sequence";

    // Hash Map key name
    public static final String KEY_NAME_PARCEL = "parcel";


    public ArrayList<Cursor> getData(String Query) {

        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"mesage"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }
    }
}
