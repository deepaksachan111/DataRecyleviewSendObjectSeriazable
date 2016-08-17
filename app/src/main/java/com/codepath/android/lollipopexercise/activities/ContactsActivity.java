package com.codepath.android.lollipopexercise.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.android.lollipopexercise.R;

import com.codepath.android.lollipopexercise.models.Contact;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;


public class ContactsActivity extends AppCompatActivity {
    private RecyclerView rvContacts;
    private ContactsAdapter mAdapter;
    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        // Find RecyclerView and bind to adapter
        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        // allows for optimizations
        rvContacts.setHasFixedSize(true);

        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(ContactsActivity.this, 2);

        // Unlike ListView, you have to explicitly give a LayoutManager to the RecyclerView to position items on the screen.
        // There are three LayoutManager provided at the moment: GridLayoutManager, StaggeredGridLayoutManager and LinearLayoutManager.
        rvContacts.setLayoutManager(layout);

        // get data
        contacts = Contact.getContacts();

        // Create an adapter
        mAdapter = new ContactsAdapter(ContactsActivity.this, contacts);

        // Bind adapter to list
        rvContacts.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }



   class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.VH> {
        private Activity mContext;
        private List<Contact> mContacts;

        public ContactsAdapter(Activity context, List<Contact> contacts) {
            mContext = context;
            if (contacts == null) {
                throw new IllegalArgumentException("contacts must not be null");
            }
            mContacts = contacts;

        }

        // Inflate the view based on the viewType provided.
        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
            return new VH(itemView, mContext);
        }

        // Display data at the specified position
        @Override
        public void onBindViewHolder(VH holder, int position) {
            Contact contact = mContacts.get(position);
            holder.rootView.setTag(contact);
            holder.tvName.setText(contact.getName());
            Picasso.with(mContext).load(contact.getThumbnailDrawable()).fit().centerCrop().into(holder.ivProfile);
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }

        // Provide a reference to the views for each contact item
        class VH extends RecyclerView.ViewHolder {
            final View rootView;
            final ImageView ivProfile;
            final TextView tvName;
            final View vPalette;

            public VH(View itemView, final Context context) {
                super(itemView);
                rootView = itemView;
                ivProfile = (ImageView)itemView.findViewById(R.id.ivProfile);
                tvName = (TextView)itemView.findViewById(R.id.tvName);
                vPalette = itemView.findViewById(R.id.vPalette);

                // Navigate to contact details activity on click of card view.
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Contact contact = (Contact)v.getTag();
                        if (contact != null) {
                            // Fire an intent when a contact is selected
                            // Pass contact object in the bundle and populate details activity.
                            Intent intent = new Intent(ContactsActivity.this, DetailsActivity.class);


                            intent.putExtra("EXTRA_CONTACT", (Serializable) contact);
                            startActivity(intent);

                        }
                    }
                });
            }
        }
    }

}
