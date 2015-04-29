package co.herod.contacts;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Matthew Herod
 */
public class ContactActivity extends AppCompatActivity {

    private static final int SELECTOR_IMAGE = 10; // Used to identify image selection intent

    private final Uri contactDirUri = ContactProviderContract.Contact.DIR_URI; // Default Contacts Directory
    private Uri contactUri = null; // Contact url

    private ContentValues contactContentValues = new ContentValues(); // Used to store pending content values

    private boolean unsavedChanges = false; // flag indicating unsaved changes

    private EditText mNameEditText; // reference to contactNameEditText field
    private EditText mEmailEditText; // reference to contactEmailEditText field
    private EditText mTelEditText; // reference to contactTelEditText field

    private ImageView mImageView; // reference to contactImageView field

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mNameEditText = (EditText) findViewById(R.id.contactNameEditText);
        mEmailEditText = (EditText) findViewById(R.id.contactEmailEditText);
        mTelEditText = (EditText) findViewById(R.id.contactTelEditText);
        mImageView = (ImageView) findViewById(R.id.contactImageView);

        Uri dataUri = getIntent().getData();
        if (dataUri != null) { // if data supplied
            contactUri = dataUri;
        }
        if (contactUri != null) { // if starting activity from existing contact uri
            Cursor c = getContentResolver()
                    .query(contactUri, ContactProviderContract.Contact.KEYS, null, null, null);
            // retrieve data and import to form
            importContactFromCursor(c);
        }

        mNameEditText.addTextChangedListener(fieldTextWatcher); // add EditText watchers
        mEmailEditText.addTextChangedListener(fieldTextWatcher);
        mTelEditText.addTextChangedListener(fieldTextWatcher);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // setup image picker
                actionImagePicker();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (unsavedChanges) { // toast when exiting with unsaved changes
            Toast.makeText(this, getString(R.string.msg_discard_without_save), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // back attempt
            if (unsavedChanges) {
                blockBackButton();
                return true; // block when unsaved
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * catches results from intents providing result; the image selection
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECTOR_IMAGE && resultCode == RESULT_OK) {
            Uri imageSelectionUri = data.getData();
            mImageView.setImageURI(imageSelectionUri);
            contactContentValues.put(ContactProviderContract.Contact.KEY_IMGURI,
                    imageSelectionUri.toString());
            unsavedChanges = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }

    /**
     * Toolbar/menu selections
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (unsavedChanges) {
                    blockBackButton();
                    return true; // block up when unsaved
                }
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_contact_save:
                actionSave();
                finish();
                return true;
            case R.id.action_contact_delete:
                actionDelete();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Load previous cursor data into form fields
     *
     * @param cursor
     */
    private void importContactFromCursor(Cursor cursor) {
        cursor.moveToFirst();

        String nameString = cursor.getString(
                cursor.getColumnIndex(ContactProviderContract.Contact.KEY_NAME));
        String emailString = cursor.getString(
                cursor.getColumnIndex(ContactProviderContract.Contact.KEY_EMAIL));
        String telString = cursor.getString(
                cursor.getColumnIndex(ContactProviderContract.Contact.KEY_TEL));
        String imgUriString = cursor.getString(
                cursor.getColumnIndex(ContactProviderContract.Contact.KEY_IMGURI));

        mNameEditText.setText(nameString);
        mEmailEditText.setText(emailString);
        mTelEditText.setText(telString);

        if (imgUriString != null) {
            mImageView.setImageURI(Uri.parse(imgUriString));
        }
    }

    /**
     * Update content values with latest values from EditText fields
     */
    public void exportContentValues() {
        contactContentValues.put(ContactProviderContract.Contact.KEY_NAME,
                mNameEditText.getText().toString());
        contactContentValues.put(ContactProviderContract.Contact.KEY_EMAIL,
                mEmailEditText.getText().toString());
        contactContentValues.put(ContactProviderContract.Contact.KEY_TEL,
                mTelEditText.getText().toString());
    }

    /**
     * Open image picker
     */
    private void actionImagePicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECTOR_IMAGE);
    }

    /**
     * Save contact
     */
    private void actionSave() {
        if (!unsavedChanges) {
            Toast.makeText(this, getString(R.string.msg_no_unsaved), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        exportContentValues();
        ContentResolver contentResolver = getContentResolver();
        if (contactUri == null) {
            contactUri = contentResolver.insert(contactDirUri, contactContentValues);
        } else {
            contentResolver.update(contactUri, contactContentValues, null, null);
        }
        unsavedChanges = false;
        Toast.makeText(this, getString(R.string.msg_contact_saved), Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * Delete contact
     */
    private void actionDelete() {
        if (contactUri != null) { // delete uri, otherwise skip and exit
            getContentResolver().delete(contactUri, null, null);
            Toast.makeText(this, getString(R.string.msg_contact_deleted), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * AlertDialog indicating unsaved changes
     */
    private void blockBackButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You have unsaved changes! Would you like to exit without saving?");
        builder.setCancelable(true);
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // nothing
            }
        });
        builder.create().show();
    }

    /**
     * Used to watch for changes to EditText fields
     */
    public TextWatcher fieldTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            unsavedChanges = true;
        }
    };

}
