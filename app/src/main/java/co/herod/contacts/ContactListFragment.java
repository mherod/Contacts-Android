package co.herod.contacts;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ContactListFragment extends Fragment {

    public static final String TAG = ContactListFragment.class.getSimpleName();

    public static final int CONTACT_LOADER_ID = 10;

    private ListView contactListView;

    private ContactTable contactTable;

    private ContactListUpdateReceiver contactListUpdateReceiver;

    private ContactListAdapter contactListAdapter;


    private SimpleCursorAdapter adapter;

    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getActivity();

        contactTable = new ContactTable(context);

        contactListUpdateReceiver = new ContactListUpdateReceiver();
        contactListAdapter = new ContactListAdapter(context, contactTable);

        setupCursorAdapter();

        getActivity().getSupportLoaderManager().initLoader(CONTACT_LOADER_ID,
                new Bundle(), contactsLoader);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        contactListView = (ListView) view.findViewById(R.id.contactsListView);

        // contactListView.setHasFixedSize(true);
        // contactListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // contactListView.setAdapter(contactListAdapter);
        contactListView.setAdapter(adapter);

        return view;
    }


    public void onButtonPressed(Uri uri) {
        // if (mListener != null) {
        //     mListener.onFragmentInteraction(uri);
        // }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");

        contactTable.open();
        contactTable.updateContact(new Contact("Matt", "matthew.herod@gmail.com", "123"));
        contactListAdapter.refresh();

        IntentFilter intentFilter = new IntentFilter("co.herod.contacts.LIST_UPDATE");
        intentFilter.addAction("new_contact");

        // Register the broadcast receiver to listen for action

        getActivity().registerReceiver(contactListUpdateReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "onPause");

        contactTable.close();

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

    private void setupCursorAdapter() {
        String[] uiBindFrom = {ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_URI};
        int[] uiBindTo = {R.id.contactNameEditText, R.id.contactPicImageView};
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.contact_list_item,
                null, uiBindFrom, uiBindTo, 0);
    }

    private LoaderManager.LoaderCallbacks<Cursor> contactsLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                // Create and return the actual cursor loader for the contacts data
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    // Define the columns to retrieve
                    String[] projectionFields =  new String[] { ContactsContract.Contacts._ID,
                            ContactsContract.Contacts.DISPLAY_NAME,
                            ContactsContract.Contacts.PHOTO_URI };
                    // Construct the loader
                    CursorLoader cursorLoader = new CursorLoader(getActivity(),
                            ContactsContract.Contacts.CONTENT_URI, // URI
                            projectionFields,  // projection fields
                            null, // the selection criteria
                            null, // the selection args
                            null // the sort order
                    );
                    // Return the loader for use
                    return cursorLoader;
                }

                // When the system finishes retrieving the Cursor through the CursorLoader,
                // a call to the onLoadFinished() method takes place.
                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                    // The swapCursor() method assigns the new Cursor to the adapter
                    adapter.swapCursor(cursor);
                }

                // This method is triggered when the loader is being reset
                // and the loader data is no longer available. Called if the data
                // in the provider changes and the Cursor becomes stale.
                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    // Clear the Cursor we were using with another call to the swapCursor()
                    adapter.swapCursor(null);
                }
            };

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
