package co.herod.contacts;

import android.net.Uri;
import android.provider.BaseColumns;

public final class ContactProviderContract {

    public static final String AUTHORITY = "co.herod.contacts.ContactProvider";

    public static final String DATABASE_NAME = "contacts.db";
    public static final int DATABASE_VERSION = 1;

    private ContactProviderContract() {
    }

    public static final class Contact implements BaseColumns {

        public static final Uri DIR_URI = Uri.parse("content://" + AUTHORITY + "/contacts");

        public static final String TABLE = "contacts";

        public static final String KEY_ID = "_id";
        public static final String KEY_NAME = "name";
        public static final String KEY_TEL = "tel";
        public static final String KEY_EMAIL = "email";
        public static final String KEY_IMGURI = "img_uri";

        public static final String[] COLUMNS = {
                KEY_ID,
                KEY_NAME,
                KEY_TEL,
                KEY_EMAIL,
                KEY_IMGURI
        };

        public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/" +
                "co.herod.contacts.data.text";
        public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/" +
                "co.herod.contacts.data.text";

        public static final String SQL_TABLE_CREATE = "CREATE TABLE " +
                TABLE + "(" +
                KEY_ID + " integer primary key autoincrement not null, " +
                KEY_NAME + " text, " +
                KEY_TEL + " text, " +
                KEY_EMAIL + " text, " +
                KEY_IMGURI + " text" +
                ");";

        public static final String SQL_TABLE_DELETE = "DROP TABLE IF EXISTS " + TABLE;

        private Contact() {
        }
    }
}
