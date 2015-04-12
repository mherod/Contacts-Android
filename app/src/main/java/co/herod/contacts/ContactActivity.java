package co.herod.contacts;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class ContactActivity extends ActionBarActivity {

    private ContactTable contactTable;

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
        int contactId = extras.getInt("contactid");

        if (contactId > -1) {
            mContact = contactTable.getContact(contactId);

            mNameEditText.setText(mContact.getName());
            mEmailEditText.setText(mContact.getEmail());
            mTelEditText.setText(mContact.getTel());
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        contactTable.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
