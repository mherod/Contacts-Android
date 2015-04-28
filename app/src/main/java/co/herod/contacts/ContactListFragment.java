package co.herod.contacts;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class ContactListFragment extends Fragment {

    public static final String TAG = ContactListFragment.class.getSimpleName();

    public static final int CONTACT_LOADER_ID = 10;

    private ListView contactListView;

    private ContactTable contactTable;

    private ContactListUpdateReceiver contactListUpdateReceiver;

    private SimpleCursorAdapter contactListAdapter;

    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactListUpdateReceiver = new ContactListUpdateReceiver();

        String[] bindFields = {ContactTable.COLUMN_NAME};
        int[] bindRes = {R.id.contactNameTextView};

        contactListAdapter = new SimpleCursorAdapter(getActivity(), R.layout.contact_list_item,
                null, bindFields, bindRes, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        contactListView = (ListView) view.findViewById(R.id.contactsListView);
        contactListView.setAdapter(contactListAdapter);
        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contactListView.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), ContactActivity.class);
                intent.setData(ContentUris.withAppendedId(ContactProvider.CONTACT_URI, id));
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");

        Cursor contactListCursor = getActivity()
                .getContentResolver()
                .query(ContactProvider.CONTACT_URI, ContactTable.COLUMNS,
                        null, null, null);

        Log.d("TEEE", "hey " + contactListCursor.getCount());

        contactListAdapter.swapCursor(contactListCursor);

        IntentFilter intentFilter = new IntentFilter("co.herod.contacts.LIST_UPDATE");
        intentFilter.addAction("new_contact");

        // Register the broadcast receiver to listen for action

        getActivity().registerReceiver(contactListUpdateReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "onPause");

        getActivity().unregisterReceiver(contactListUpdateReceiver);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.d(TAG, "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.d(TAG, "onDetach");
    }

    /**
     * Listener for broadcasts to service - only used for stopwatch resets
     */
    public class ContactListUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (action == null) {
                return;
            }

            // Log.d(TAG, "onReceive " + action);

            if (action.equals("new_contact")) {
                Contact newContact = (Contact) intent.getExtras().getSerializable("contact");
            }

        }
    }

}
