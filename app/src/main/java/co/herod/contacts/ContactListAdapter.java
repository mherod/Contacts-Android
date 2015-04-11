package co.herod.contacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew Herod on 19/03/15.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private static final String TAG = ContactListAdapter.class.getSimpleName();

    private Context mContext;

    private final List<Contact> contactList = new ArrayList<Contact>();

    private final ContactTable contactTable;

    public ContactListAdapter(ContactTable contactTable) {
        this.contactTable = contactTable;
    }

    public void refresh() {
        List<Contact> newContacts = contactTable.getContactList();

        contactList.clear();
        contactList.addAll(newContacts);

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();

        View v = LayoutInflater.from(mContext).inflate(R.layout.contact_list_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, int i) {
        final Contact tweet = contactList.get(i);

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        vh.contactNameTextView.setText(tweet.getName());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected final TextView contactNameTextView;

        public ViewHolder(View v) {
            super(v);
            contactNameTextView = (TextView) v.findViewById(R.id.contact_name_textview);
        }
    }

}