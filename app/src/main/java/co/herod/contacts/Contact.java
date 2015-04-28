package co.herod.contacts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Matthew Herod on 11/04/15.
 */
public class Contact implements Serializable {

    private int id;
    private String name = null;
    private String email = null;
    private String tel = null;
    private String imgUriString = null;

    private boolean editedName = false;
    private boolean editedEmail = false;
    private boolean editedTel = false;
    private boolean editedImgUri = false;

    public Contact() {

    }

    public Contact(String name, String email, String tel) {
        this.name = name;
        this.email = email;
        this.tel = tel;
        editedName = true;
        editedEmail = true;
        editedTel = true;
    }

    public Contact(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(ContactTable.COLUMN_ID));
        this.name = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_NAME));
        this.email = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_EMAIL));
        this.tel = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_TEL));
    }

    public int getId() {
        return id;
    }

    public CharSequence getName() {
        return name;
    }

    public CharSequence getEmail() {
        return email;
    }

    public CharSequence getTel() {
        return tel;
    } // TODO: validation

    public void setName(String name) {
        this.editedName = !this.name.equals(name);
        this.name = name;
    }

    public void setEmail(String email) {
        this.editedEmail = !this.email.equals(email);
        this.email = email;
    }

    public void setTel(String tel) {
        this.editedTel = !this.tel.equals(tel);
        this.tel = tel;
    }

    public void setImgUri(Uri uri) {
        setImgUriString(uri.toString());
    }

    public void setImgUriString(String imgUriString) {
        this.editedImgUri = !this.imgUriString.equals(imgUriString);
        this.imgUriString = imgUriString;
    }

    public ContentValues exportContentValues() {
        ContentValues contentValues = new ContentValues();
        /* if (id > -1) {
            // without an assigned id we expect the database will assign one
            contentValues.put(ContactTable.COLUMN_ID, id);
        } */
        if (editedName) {
            contentValues.put(ContactTable.COLUMN_NAME, name);
        }
        if (editedEmail) {
            contentValues.put(ContactTable.COLUMN_EMAIL, email);
        }
        if (editedTel) {
            contentValues.put(ContactTable.COLUMN_TEL, tel);
        }
        if (editedImgUri) {
            contentValues.put(ContactTable.COLUMN_IMGURI, imgUriString);
        }
        return contentValues;
    }

    public void resetEditFlags() {
        editedName = false;
        editedEmail = false;
        editedTel = false;
        editedImgUri = false;
    }

}
