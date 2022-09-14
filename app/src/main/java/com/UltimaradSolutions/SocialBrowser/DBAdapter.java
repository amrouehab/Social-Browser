// ------------------------------------ DBADapter.java ---------------------------------------------

// TODO: Change the package to match your project.
package com.UltimaradSolutions.SocialBrowser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.
public class DBAdapter {

	/////////////////////////////////////////////////////////////////////
	//	Constants & Data
	/////////////////////////////////////////////////////////////////////
	// For logging:
	private static final String TAG = "DBAdapter";
	
	// DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;

    /*
	 * CHANGE 1:
	 */
	// TODO: Setup your fields here:
	public static final String KEY_PAGENAME = "PageName";
	public static final String KEY_PAGEURL = "PageUrl";
    public static final String KEY_Time = "Time";




    // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
	public static final int COL_PAGEName = 1;
	public static final int COL_PageUrl = 2;
    public static final int COL_Time = 3;




    public static final String[] ALL_KEYS = new String[] {KEY_ROWID,KEY_PAGENAME, KEY_PAGEURL,KEY_Time};
	
	// DB info: it's name, and the table we are using (just one).
    public String DATABASE_NAME = "MyDb";
	public String DATABASE_TABLE = "Tabs";
	// Track DB version if a new version of your app changes the format.
	public static final int DATABASE_VERSION = 2;
    private  String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "
			/*
			 * CHANGE 2:
			 */
                    // TODO: Place your fields here!
                    // + KEY_{...} + " {type} not null"
                    //	- Key is the column name you created above.
                    //	- {type} is one of: text, integer, real, blob
                    //		(http://www.sqlite.org/datatype3.html)
                    //  - "not null" means it is a required field (must be given a value).
                    // NOTE: All must be comma separated (end of line!) Last one must have NO comma!!
                    + KEY_PAGENAME + " string not null, "
                    + KEY_PAGEURL + " string not null, "
                    + KEY_Time + " string "


                    // Rest  of creation:
                    + ");";

	private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase myDataBase;


    /////////////////////////////////////////////////////////////////////
	//	Public methods:
	/////////////////////////////////////////////////////////////////////

    public DBAdapter(Context ctx,String DbName,String DbTableName,String SQlCreation) {
        DATABASE_NAME=DbName;
        DATABASE_TABLE=DbTableName;
        DATABASE_CREATE_SQL=SQlCreation;
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context,DATABASE_TABLE,DATABASE_NAME
        ,DATABASE_CREATE_SQL);
    }

    public DBAdapter(Context context) {
        this.context = context;
        myDBHelper = new DatabaseHelper(context, DATABASE_TABLE, DATABASE_NAME, DATABASE_CREATE_SQL);

    }

    // Open the database connection.
    public DBAdapter open() {
        myDataBase = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    public boolean checkExistance(String tabName,String tabUrl,String Date) {
        boolean exist=false;
        Cursor c=null;
        SQLiteDatabase db ;

        String where = KEY_PAGENAME + "='" + tabName+"'";
        try {
            db =myDBHelper.getReadableDatabase();
            c = db.query(DATABASE_TABLE, ALL_KEYS, where,
                    null, null, null, null, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        if (Date.equals(c.getString(COL_Time))&&tabName.equals(c.getString(COL_PAGEName))) {
                            exist = true;
                            break;
                        } else {
                            exist = false;
                        }

                    } while (c.moveToNext());
                    c.close();
                }

            }
        }catch (Exception e){
            e.getMessage();

        }
        c.close();

        return exist;

    }

	// Add a new set of values to the database.
	public long insertRow(String TabUrl, String TabName, String date) {
        myDataBase = myDBHelper.getWritableDatabase();
		// Create row's data:
		ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PAGEURL,TabUrl);
		initialValues.put(KEY_PAGENAME, TabName);
        initialValues.put(KEY_Time,date);

            return myDataBase.insert(DATABASE_TABLE, null, initialValues);

        }
    public long inseserttoDb(ContentValues initialValues) {
        long a=-1;
        myDataBase = myDBHelper.getWritableDatabase();
        try {

            if (!checkExistance(initialValues.getAsString(KEY_PAGENAME),initialValues.getAsString(KEY_PAGEURL),initialValues.getAsString(KEY_Time)))

                a = myDataBase.insert(DATABASE_TABLE, null, initialValues);

            else a=-1;
        }catch (Exception e){
            e.getMessage();
        }

        //will return -1 if the row is exist will return the  number of the inserted row  if thr row is nt exist
        return a;

    }

    // Delete a row from the database, by rowId (primary key)
	public boolean deleteRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		return myDataBase.delete(DATABASE_TABLE, where, null) != 0;
	}
    //get a row from the database, by rowId (primary key)
     Cursor getRowByID(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
         Cursor c = 	myDataBase.query(true, DATABASE_TABLE, ALL_KEYS,
                 where, null, null, null, null, null);
         if (c != null) {
             c.moveToFirst();
         }
         return c;

    }
	public void deleteAll() {
        myDataBase.delete(DATABASE_TABLE, null, null);

    }
	
	// Return all data in the database.
	public Cursor getAllRows() {
        Cursor c = null;
        try {
            myDataBase = myDBHelper.getReadableDatabase();
            c = myDataBase.query(true, DATABASE_TABLE, ALL_KEYS,
                    null, null, null, null, null, null);

            if (c != null) {
                c.moveToFirst();
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return c;
    }


	// Get a specific row by url
	 Cursor getRow(String TabUrl) {
        String where = KEY_PAGEURL + "='" + TabUrl+"'";
		Cursor c = 	myDataBase.query(true, DATABASE_TABLE, ALL_KEYS,
						where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

    public Cursor getRowByName(String TabName) {
        String where = KEY_PAGENAME + "='" + TabName+"'";
        Cursor c = 	myDataBase.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    /////////////////////////////////////////////////////////////////////
	//	Private Helper Classes:
	/////////////////////////////////////////////////////////////////////

    ///////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        private final String DataTable;
        private final String DATABASE_CREATE_SQL;

        DatabaseHelper(Context context, String DATABASE_TABLE, String DATABASE_NAME, String DATABASE_CREATE_SQL) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.DATABASE_CREATE_SQL=DATABASE_CREATE_SQL;
            this.DataTable=DATABASE_TABLE;

        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DataTable);

            // Recreate new database:
            onCreate(_db);
        }
    }


}
