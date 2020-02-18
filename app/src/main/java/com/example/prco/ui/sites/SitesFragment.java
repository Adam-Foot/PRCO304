package com.example.prco.ui.sites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
        View root = inflater.inflate(R.layout.fragment_tools, container, false);

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

        public SitesViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.txtSiteListName);
            list_desc = itemView.findViewById(R.id.txtSiteDesc);
            direction = itemView.findViewById(R.id.imgDirection);

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