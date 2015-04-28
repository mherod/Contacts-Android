package co.herod.contacts;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


public class ContactActivity extends AppCompatActivity {

    private Uri contactDirUri = ContactProviderContract.Contact.DIR_URI; // Default Contacts Directory
    private Uri contactUri = null;

    private boolean unsavedChanges = false;

    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mTelEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mNameEditText = (EditText) findViewById(R.id.contactNameEditText);
        mEmailEditText = (EditText) findViewById(R.id.contactEmailEditText);
        mTelEditText = (EditText) findViewById(R.id.contactTelEditText);


        Uri dataUri = getIntent().getData();
        if (dataUri != null) {
            contactUri = dataUri;
            Log.d("ContactActivity", "Loading contact uri: " + contactUri);
        }
        if (contactUri != null) {
            Cursor c = getContentResolver()
                    .query(contactUri, ContactProviderContract.Contact.COLUMNS, null, null, null);

            importContactFromCursor(c);
        }

        mNameEditText.addTextChangedListener(fieldTextWatcher);
        mEmailEditText.addTextChangedListener(fieldTextWatcher);
        mTelEditText.addTextChangedListener(fieldTextWatcher);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (unsavedChanges) {
            Toast.makeText(this, getString(R.string.msg_discard_without_save), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (unsavedChanges) {
                blockBackButton();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (unsavedChanges) {
                    blockBackButton();
                    return true;
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

    private void importContactFromCursor(Cursor cursor) {
        cursor.moveToFirst();

        String name = cursor.getString(
                cursor.getColumnIndex(ContactProviderContract.Contact.KEY_NAME));
        String email = cursor.getString(
                cursor.getColumnIndex(ContactProviderContract.Contact.KEY_EMAIL));
        String tel = cursor.getString(
                cursor.getColumnIndex(ContactProviderContract.Contact.KEY_TEL));

        mNameEditText.setText(name);
        mEmailEditText.setText(email);
        mTelEditText.setText(tel);
    }

    public ContentValues exportContentValues(boolean all) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactProviderContract.Contact.KEY_NAME, mNameEditText.getText().toString());
        contentValues.put(ContactProviderContract.Contact.KEY_EMAIL, mEmailEditText.getText().toString());
        contentValues.put(ContactProviderContract.Contact.KEY_TEL, mTelEditText.getText().toString());
        // contentValues.put(ContactTable.KEY_IMGURI, imgUriString);

        return contentValues;
    }

    private void actionSave() {
        if (!unsavedChanges) {
            Toast.makeText(this, getString(R.string.msg_no_unsaved), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        ContentResolver contentResolver = getContentResolver();
        if (contactUri == null) {
            contactUri = contentResolver.insert(contactDirUri, exportContentValues(true));
        } else {
            contentResolver.update(contactUri, exportContentValues(false), null, null);
        }
        unsavedChanges = false;
        Toast.makeText(this, getString(R.string.msg_contact_saved), Toast.LENGTH_SHORT)
                .show();
    }

    private void actionDelete() {
        if (contactUri != null) {
            getContentResolver().delete(contactUri, null, null);
            Toast.makeText(this, getString(R.string.msg_contact_deleted), Toast.LENGTH_SHORT)
                    .show();
        }
    }

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

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

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
