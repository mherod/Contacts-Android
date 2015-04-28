package co.herod.contacts;

/**
 * Created by Matthew Herod
 */
public class ContactTable {

    public static final String TABLE = "contacts";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TEL = "tel";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_IMGURI = "img_uri";

    public static final String SQL_CREATE = "CREATE TABLE " +
            TABLE + "(" +
            COLUMN_ID + " integer primary key autoincrement not null, " +
            COLUMN_NAME + " text, " +
            COLUMN_TEL + " text, " +
            COLUMN_EMAIL + " text, " +
            COLUMN_IMGURI + " text" +
            ");";

    public static final String[] COLUMNS = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_TEL,
            COLUMN_EMAIL,
            COLUMN_IMGURI
    };

}
