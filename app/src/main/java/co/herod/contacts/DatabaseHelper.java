package co.herod.contacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Matthew Herod
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = DatabaseHelper.class.getSimpleName();

    public DatabaseHelper(Context context) {
        super(context,
                ContactProviderContract.DATABASE_NAME,
                null,
                ContactProviderContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(ContactProviderContract.Contact.SQL_TABLE_CREATE);
    }

    /**
     * Called when the old database version is lower than that of the current application
     * that is running.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ContactProviderContract.Contact.SQL_TABLE_DELETE);
        onCreate(db);
    }

}
