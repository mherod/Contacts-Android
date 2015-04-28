package co.herod.contacts;

import android.content.DialogInterface;
import android.os.Bundle;
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

    private ContactTable contactTable;

    private long contactId;
    private boolean unsavedChanges = false;

    private Contact mContact;

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

        contactTable = new ContactTable(this);
        contactTable.open();

        Bundle extras = getIntent().getExtras();
        contactId = extras.getInt("contactid");

        if (contactId > -1) {
            mContact = contactTable.getContact(contactId);

            mNameEditText.setText(mContact.getName());
            mEmailEditText.setText(mContact.getEmail());
            mTelEditText.setText(mContact.getTel());
        } else {
            mContact = new Contact();
        }

        EditText[] editTexts = new EditText[] {mNameEditText, mEmailEditText, mTelEditText};
        for (EditText editText : editTexts) {
            editText.addTextChangedListener(editTextWatcher);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (unsavedChanges) {
            Toast.makeText(this, getString(R.string.msg_discard_without_save), Toast.LENGTH_SHORT)
                .show();
        }
        contactTable.close();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // catches back button press
            if (unsavedChanges) {
                blockBackButton();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_contact_save) {
            actionSave();
            return true;
        }
        if (id == R.id.action_contact_delete) {
            actionDelete();
            return true;
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void actionSave() {
        if (!unsavedChanges) {
            Toast.makeText(this, getString(R.string.msg_no_unsaved), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (mContact != null) {
            contactId = contactTable.updateContact(mContact);
            unsavedChanges = false;
            Toast.makeText(this, getString(R.string.msg_contact_saved), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
    }

    private void actionDelete() {
        if (contactId > -1) {
            contactTable.deleteContact(contactId);
            Toast.makeText(this, getString(R.string.msg_contact_deleted), Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(this, getString(R.string.msg_discard_without_save), Toast.LENGTH_SHORT)
                    .show();
        }
        finish();
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

    private TextWatcher editTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            unsavedChanges = true;
            Log.d("ContactActivity", "Marked unsaved changes!");
        }
    };

}
