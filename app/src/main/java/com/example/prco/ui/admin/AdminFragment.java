package com.example.prco.ui.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.prco.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AdminFragment extends Fragment implements View.OnClickListener {

    private AdminViewModel adminViewModel;

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mBtnLogin;
    private Button mBtnLogout;
    private Button mBtnAddSite;
    private TextView mLoginText;
    private EditText mSiteName;
    private EditText mSiteDesc;
    private EditText mSiteLat;
    private EditText mSiteLong;
    private EditText mSiteUrl;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        adminViewModel =
                ViewModelProviders.of(this).get(AdminViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin, container, false);

        mEmailField = root.findViewById(R.id.txtEmail);
        mPasswordField = root.findViewById(R.id.txtPassword);
        mLoginText = root.findViewById(R.id.txtLoginText);
        mSiteName = root.findViewById(R.id.txtSiteName);
        mSiteDesc = root.findViewById(R.id.txtSiteDesc);
        mSiteLat = root.findViewById(R.id.txtSiteLat);
        mSiteLong = root.findViewById(R.id.txtSiteLong);
        mSiteUrl = root.findViewById(R.id.txtSiteUrl);

        mBtnLogin = root.findViewById(R.id.btnLogin);
        mBtnLogout = root.findViewById(R.id.btnLogout);
        mBtnAddSite = root.findViewById(R.id.btnAddSite);

        mBtnLogin.setOnClickListener(this);
        mBtnLogout.setOnClickListener(this);
        mBtnAddSite.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        return root;


    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn(String email, String password){
        Log.d(TAG, "Sign in: " + email);
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Sign in success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    Log.w(TAG, "Sign in failed", task.getException());
                    Toast.makeText(getActivity(), "Authentication failed!", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private boolean validateDataEntry() {
        boolean valid = true;

        if (TextUtils.isEmpty(mSiteName.getText().toString())) {
            mSiteName.setError("Site name is required!");
            valid = false;
        } else if (TextUtils.isEmpty(mSiteDesc.getText().toString())) {
            mSiteDesc.setError("Please enter a site description!");
            valid = false;
        } else if (TextUtils.isEmpty(mSiteLat.getText().toString())) {
            mSiteLat.setError("Latitude co-ordinates are required!");
            valid = false;
        } else if (TextUtils.isEmpty(mSiteLong.getText().toString())) {
            mSiteLong.setError("Longitude co-ordinates are required!");
            valid = false;
        } else {
            mSiteName.setError(null);
            mSiteDesc.setError(null);
            mSiteLat.setError(null);
            mSiteLong.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            mEmailField.setVisibility(View.GONE);
            mPasswordField.setVisibility(View.GONE);
            mBtnLogin.setVisibility(View.GONE);
            mLoginText.setVisibility(View.GONE);
            mBtnLogout.setVisibility(View.VISIBLE);
            mBtnAddSite.setVisibility(View.VISIBLE);
            mSiteName.setVisibility(View.VISIBLE);
            mSiteDesc.setVisibility(View.VISIBLE);
            mSiteLat.setVisibility(View.VISIBLE);
            mSiteLong.setVisibility(View.VISIBLE);
            mSiteUrl.setVisibility(View.VISIBLE);
        } else {
            mEmailField.setVisibility(View.VISIBLE);
            mPasswordField.setVisibility(View.VISIBLE);
            mBtnLogin.setVisibility(View.VISIBLE);
            mLoginText.setVisibility(View.VISIBLE);
            mBtnLogout.setVisibility(View.GONE);
            mBtnAddSite.setVisibility(View.GONE);
            mSiteName.setVisibility(View.GONE);
            mSiteDesc.setVisibility(View.GONE);
            mSiteLat.setVisibility(View.GONE);
            mSiteLong.setVisibility(View.GONE);
            mSiteUrl.setVisibility(View.GONE);
        }
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnLogin) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.btnLogout) {
            signOut();
        } else if (i == R.id.btnAddSite) {
            addNewSite();
        }
    }

    public void addNewSite() {
        if (!validateDataEntry()) {
            return;
        }

        if (mSiteUrl.getText().toString().startsWith("www.")) {
            String urlEdit = "https://" + mSiteUrl.getText().toString();

            Map<String, Object> site = new HashMap<>();
            site.put("site_name", mSiteName.getText().toString());
            site.put("site_desc", mSiteDesc.getText().toString());
            site.put("site_locationLat", Double.parseDouble(mSiteLat.getText().toString()));
            site.put("site_locationLong", Double.parseDouble(mSiteLong.getText().toString()));
            site.put("site_url", urlEdit);

            mFirestore.collection("sites")
                    .add(site)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Document added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document!", e);
                        }
                    });
        } else {
            String urlEdit = mSiteUrl.getText().toString();

            Map<String, Object> site = new HashMap<>();
            site.put("site_name", mSiteName.getText().toString());
            site.put("site_desc", mSiteDesc.getText().toString());
            site.put("site_locationLat", Double.parseDouble(mSiteLat.getText().toString()));
            site.put("site_locationLong", Double.parseDouble(mSiteLong.getText().toString()));
            site.put("site_url", urlEdit);

            mFirestore.collection("sites")
                    .add(site)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Document added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document!", e);
                        }
                    });
        }

        clearForm();

    }

    private void clearForm() {
        if (mSiteName.getText() != null) {
            mSiteName.setText("");
        }
        if (mSiteDesc.getText() != null) {
            mSiteDesc.setText("");
        }
        if (mSiteLat.getText() != null) {
            mSiteLat.setText("");
        }
        if (mSiteLong.getText() != null) {
            mSiteLong.setText("");
        }
        if (mSiteUrl.getText() != null) {
            mSiteUrl.setText("");
        }
    }
}
