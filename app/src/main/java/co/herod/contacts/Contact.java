package co.herod.contacts;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by Matthew Herod on 11/04/15.
 */
public class Contact implements Serializable {

    private int id;
    private String name;
    private String email;
    private String tel;

    public Contact(Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndex(ContactTable.COLUMN_ID));
        name = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_NAME));
        email = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_EMAIL));
        tel = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_TEL));
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTel() {
        return tel;
    }

    public ContentValues exportContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactTable.COLUMN_ID, id);
        contentValues.put(ContactTable.COLUMN_NAME, name);
        contentValues.put(ContactTable.COLUMN_EMAIL, email);
        contentValues.put(ContactTable.COLUMN_TEL, tel);
        return contentValues;
    }

}
