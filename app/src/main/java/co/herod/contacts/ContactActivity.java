package co.herod.contacts;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ContactActivity extends ActionBarActivity {

    private ContactTable contactTable;

    private Contact mContact;

    private TextView mNameTextView;
    private TextView mEmailTextView;
    private TextView mTelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mNameTextView = (TextView) findViewById(R.id.contactNameTextView);
        mEmailTextView = (TextView) findViewById(R.id.contactEmailTextView);
        mTelTextView = (TextView) findViewById(R.id.contactTelTextView);

        contactTable = new ContactTable(this);
        contactTable.open();

        Bundle extras = getIntent().getExtras();
        int contactId = extras.getInt("contactid");

        mContact = contactTable.getContact(contactId);

        mNameTextView.setText(mContact.getName());
        mEmailTextView.setText(mContact.getEmail());
        mTelTextView.setText(mContact.getTel());

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
