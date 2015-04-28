package co.herod.contacts;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ContactProvider extends ContentProvider {

    private DatabaseHelper dbHelper = null;

    private static final UriMatcher uriMatcher;

    public static final String AUTHORITY = "co.herod.contacts.ContactProvider";

    public static final Uri CONTACT_URI = Uri.parse("content://" + AUTHORITY + "/contacts");

    public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/ContactProvider.data.text";
    public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/ContactProvider.data.text";

    public static final int CONTACTS = 1;
    public static final int CONTACTS_ID = 2;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "contacts", CONTACTS);
        uriMatcher.addURI(AUTHORITY, "contacts/#", CONTACTS_ID);
    }

    public ContactProvider() {
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                return "vnd.android.cursor.dir/vnd.example.friends";
            case CONTACTS_ID:
                return "vnd.android.cursor.item/vnd.example.friends";
            default:
                throw new IllegalArgumentException("unsupported " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                return db.query(
                        ContactTable.TABLE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder,
                        null
                );
            case CONTACTS_ID:
                return db.query(
                        ContactTable.TABLE,
                        projection,
                        ContactTable.COLUMN_ID + "=?",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder,
                        null
                );
            default:
                throw new IllegalArgumentException("unsupported " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
            case CONTACTS_ID:
                id = db.insert(ContactTable.TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("unsupported " + uri);
        }
        db.close();
        return ContentUris.withAppendedId(CONTACT_URI, id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int i;
        switch (uriMatcher.match(uri)) {
            case CONTACTS_ID:
                i = db.update(ContactTable.TABLE,
                        values, ContactTable.COLUMN_ID + "=?",
                        new String[]{uri.getLastPathSegment()});
                break;
            default:
                throw new IllegalArgumentException("unsupported " + uri);
        }
        db.close();
        return i;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int i;
        switch (uriMatcher.match(uri)) {
            case CONTACTS_ID:
                i = db.delete(
                        ContactTable.TABLE,
                        ContactTable.COLUMN_ID + "=?",
                        new String[]{uri.getLastPathSegment()}
                );
                break;
            default:
                throw new IllegalArgumentException("unsupported " + uri);
        }
        db.close();
        return i;
    }
}
