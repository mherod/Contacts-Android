package co.herod.contacts;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Matthew Herod
 */
public class ContactProvider extends ContentProvider {

    private DatabaseHelper dbHelper = null;

    private static final UriMatcher uriMatcher;

    public static final int CONTACTS = 1;
    public static final int CONTACTS_ID = 2;

    static { // define contact uri matchers
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ContactProviderContract.AUTHORITY, "contacts", CONTACTS);
        uriMatcher.addURI(ContactProviderContract.AUTHORITY, "contacts/#", CONTACTS_ID);
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
                return ContactProviderContract.Contact.CONTENT_TYPE_MULTIPLE;
            case CONTACTS_ID:
                return ContactProviderContract.Contact.CONTENT_TYPE_SINGLE;
            default:
                throw new IllegalArgumentException("unsupported " + uri);
        }
    }

    /**
     * Retrieve existing records
     *
     * @param uri
     * @param projection
     * @param selection clause to match existing record
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                return db.query(
                        ContactProviderContract.Contact.TABLE,
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
                        ContactProviderContract.Contact.TABLE,
                        projection,
                        ContactProviderContract.Contact.KEY_ID + "=?",
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

    /**
     * Insert record
     *
     * @param uri the uri of the directory of the resource to insert
     * @param values the content values to insert
     * @return the Uri of the newly inserted content
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        switch (uriMatcher.match(uri)) {
            case CONTACTS: // match dir
            case CONTACTS_ID: // match id
                id = db.insert(ContactProviderContract.Contact.TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("unsupported " + uri);
        }
        db.close();
        return ContentUris.withAppendedId(ContactProviderContract.Contact.DIR_URI, id);
        // return uri to inserted resource
    }

    /**
     * Update record
     *
     * @param uri the uri to update
     * @param values the content values to update
     * @param selection clause to match existing record
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int i;
        switch (uriMatcher.match(uri)) {
            case CONTACTS_ID:
                i = db.update(ContactProviderContract.Contact.TABLE,
                        values, ContactProviderContract.Contact.KEY_ID + "=?",
                        new String[]{uri.getLastPathSegment()});
                break;
            default:
                throw new IllegalArgumentException("unsupported " + uri);
        }
        db.close();
        return i;
    }

    /**
     * Delete record
     *
     * @param uri the uri to delete
     * @param selection clause to match existing record
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int i;
        switch (uriMatcher.match(uri)) {
            case CONTACTS_ID:
                i = db.delete(
                        ContactProviderContract.Contact.TABLE,
                        ContactProviderContract.Contact.KEY_ID + "=?",
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
