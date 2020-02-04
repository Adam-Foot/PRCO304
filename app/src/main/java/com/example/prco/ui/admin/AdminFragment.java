package com.example.prco.ui.admin;

import android.content.Context;
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
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.prco.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AdminFragment extends Fragment implements View.OnClickListener {

    private AdminViewModel adminViewModel;

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mBtnLogin;
    private Button mBtnLogout;

    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        adminViewModel =
                ViewModelProviders.of(this).get(AdminViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin, container, false);
        final TextView textView = root.findViewById(R.id.text_admin);

        mEmailField = root.findViewById(R.id.txtEmail);
        mPasswordField = root.findViewById(R.id.txtPassword);
        mBtnLogin = root.findViewById(R.id.btnLogin);
        mBtnLogout = root.findViewById(R.id.btnLogout);

        mBtnLogin.setOnClickListener(this);
        mBtnLogout.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        adminViewModel.getText().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
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

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            mEmailField.setVisibility(View.GONE);
            mPasswordField.setVisibility(View.GONE);
            mBtnLogin.setVisibility(View.GONE);
            mBtnLogout.setVisibility(View.VISIBLE);
        } else {
            mEmailField.setVisibility(View.VISIBLE);
            mPasswordField.setVisibility(View.VISIBLE);
            mBtnLogin.setVisibility(View.VISIBLE);
            mBtnLogout.setVisibility(View.GONE);
        }
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnLogin) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.btnLogout) {
            signOut();
        }
    }
}
