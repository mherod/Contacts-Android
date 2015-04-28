package co.herod.contacts;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew Herod
 */
public class ContactTable {

    public static final String TABLE = "contacts";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TEL = "tel";
    public static final String COLUMN_EMAIL = "email";

    public static final String SQL_CREATE = "CREATE TABLE " +
            TABLE + "(" +
            COLUMN_ID + " integer primary key autoincrement not null, " +
            COLUMN_NAME + " text, " +
            COLUMN_TEL + " text, " +
            COLUMN_EMAIL + " text" +
            ");";

    private static final String[] COLUMNS = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_TEL,
            COLUMN_EMAIL
    };

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public ContactTable(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void openWritable() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void open() {
        db = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long updateContact(Contact contact) {
        return db.replace(TABLE, null, contact.exportContentValues());
    }

    public void deleteContact(long contactId) {
        db.delete(TABLE, COLUMN_ID + "=?", new String[]{String.valueOf(contactId)});
    }

    public Contact getContact(long id) {
        Cursor cursor = db.query(
                TABLE,
                COLUMNS,
                COLUMN_ID + "=?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();

        return new Contact(cursor);
    }

    public List<Contact> getContactList() {
        Cursor cursor = db.query(
                TABLE,
                COLUMNS,
                null,
                null,
                null,
                null,
                null
        );

        List<Contact> contactList = new ArrayList<Contact>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            contactList.add(new Contact(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return contactList;
    }

}
