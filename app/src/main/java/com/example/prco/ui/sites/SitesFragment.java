package com.example.prco.ui.sites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prco.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class SitesFragment extends Fragment {

    private SitesViewModel sitesViewModel;
    private FirebaseFirestore mFirestore;
    private RecyclerView mSiteData;
    private FirestoreRecyclerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sitesViewModel =
                ViewModelProviders.of(this).get(SitesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sites, container, false);

        mFirestore = FirebaseFirestore.getInstance();
        mSiteData = root.findViewById(R.id.recycler_view);

        Query query = mFirestore.collection("sites").orderBy("site_name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Sites> options = new FirestoreRecyclerOptions.Builder<Sites>()
                .setQuery(query, Sites.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Sites, SitesViewHolder>(options) {
            @NonNull
            @Override
            public SitesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sites_list_item, parent, false);
                return new SitesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull SitesViewHolder sitesViewHolder, int i, @NonNull Sites sites) {
                sitesViewHolder.list_name.setText(sites.getSite_name());
                sitesViewHolder.list_desc.setText(sites.getSite_desc());
                sitesViewHolder.direction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri maps = Uri.parse("google.navigation:q=" + sites.getSite_locationLat() + "," + sites.getSite_locationLong());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, maps);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }
                    }
                });
                sitesViewHolder.website.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = sites.getSite_url();

                        if (url.equals("")) {
                            Toast toast = Toast.makeText(getActivity(), "No website has been added for this site! Please choose a different one.", Toast.LENGTH_LONG);
                            toast.show();
                        } else if (url.startsWith("www.")){
                            Toast toast = Toast.makeText(getActivity(), "The website URL for this site is invalid. Please contact the app developer.", Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                            CustomTabsIntent customTabsIntent = builder.build();
                            customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
                        }
                    }
                });
            }
        };

        mSiteData.setHasFixedSize(true);
        mSiteData.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSiteData.setAdapter(adapter);

        return root;
    }


    private class SitesViewHolder extends RecyclerView.ViewHolder {

        private TextView list_name;
        private TextView list_desc;
        private ImageView direction;
        private ImageView website;

        public SitesViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.txtSiteListName);
            list_desc = itemView.findViewById(R.id.txtSiteDesc);
            direction = itemView.findViewById(R.id.imgDirection);
            website = itemView.findViewById(R.id.imgWebsite);

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}