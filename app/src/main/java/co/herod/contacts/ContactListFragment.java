package co.herod.contacts;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class ContactListFragment extends Fragment {

    private ListView contactListView;

    private SimpleCursorAdapter contactListAdapter;

    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] bindFields = {ContactProviderContract.Contact.KEY_NAME};
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
                Uri uri = ContentUris.withAppendedId(ContactProviderContract.Contact.DIR_URI, id);
                Intent intent = new Intent(getActivity(), ContactActivity.class);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Cursor contactListCursor = getActivity()
                .getContentResolver()
                .query(ContactProviderContract.Contact.DIR_URI,
                        ContactProviderContract.Contact.KEYS,
                        null, null, null);

        contactListAdapter.swapCursor(contactListCursor);
    }

}
