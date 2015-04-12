package co.herod.contacts;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactListFragment extends Fragment {

    public static final String TAG = ContactListFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    private ContactTable contactTable;

    private ContactListUpdateReceiver contactListUpdateReceiver;

    private ContactListAdapter contactListAdapter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.contact_list_recycler);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(contactListAdapter);

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
