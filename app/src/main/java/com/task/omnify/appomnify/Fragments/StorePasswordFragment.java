package com.task.omnify.appomnify.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.task.omnify.appomnify.Interfaces.StoredOnServerListener;
import com.task.omnify.appomnify.Models.UserDataWithPassword;
import com.task.omnify.appomnify.R;
import com.task.omnify.appomnify.Utils.StoredVariables;

public class StorePasswordFragment extends Fragment implements View.OnClickListener {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("user_data");
    StoredOnServerListener storedOnServerListener;
    EditText etPassword,etConfirmPassword;
    private FirebaseAuth mAuth;

    public StorePasswordFragment() {
        // Required empty public constructor
    }


    public static StorePasswordFragment newInstance() {
        StorePasswordFragment fragment = new StorePasswordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_store_password, container, false);
       view.findViewById(R.id.button_confirm).setOnClickListener(this);
       etPassword= view.findViewById(R.id.field_enter_password);
       etConfirmPassword=view.findViewById(R.id.field_confirm_password);
       return view;

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        storedOnServerListener= (StoredOnServerListener) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onClick(View v) {
        int i=v.getId();
        if(i==R.id.button_confirm){
            if(validatePhoneNumber(etConfirmPassword)&& validatePhoneNumber(etPassword)){
                if(arePasswordEqual(etConfirmPassword.getText().toString(),etPassword.getText().toString())){
                    UserDataWithPassword user = new UserDataWithPassword(
                                                        StoredVariables.getTokenId(),
                                                        mAuth.getCurrentUser().getUid().toString(),
                                                        etConfirmPassword.getText().toString());

                    myRef.child(mAuth.getCurrentUser().getPhoneNumber()).setValue(user);
                    storedOnServerListener.onDataUploaded();
                }
            }
        }

    }


    private boolean arePasswordEqual(String p1,String p2){
        if(p1.equals(p1))
            return true;

            return false;
    }

    private boolean validatePhoneNumber(EditText editText) {
        String phoneNumber = editText.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            editText.setError("Invalid phone number.");
            return false;
        }
        return true;
    }

}
