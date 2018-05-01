package com.task.omnify.appomnify.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.task.omnify.appomnify.Activities.BaseActivity;
import com.task.omnify.appomnify.Interfaces.PhonePasswordVerificationListener;
import com.task.omnify.appomnify.Models.UserDataWithPassword;
import com.task.omnify.appomnify.R;


public class EnterPasswordFragment extends Fragment implements View.OnClickListener {

    BaseActivity parentActivity;
    EditText etPhoneNo,etPassword;
    String phoneNo;
    PhonePasswordVerificationListener phonePasswordVerificationListener;

    ValueEventListener valueEventListener;
    public EnterPasswordFragment() {

    }


    public static EnterPasswordFragment newInstance(BaseActivity parentActivity) {
        EnterPasswordFragment fragment = new EnterPasswordFragment();
        fragment.parentActivity=parentActivity;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_enter_password, container, false);
        etPhoneNo=view.findViewById(R.id.field_enter_phone);
        etPassword=view.findViewById(R.id.field_password);
        view.findViewById(R.id.button_new_phoneNumber).setOnClickListener(this);
        view.findViewById(R.id.button_confirm).setOnClickListener(this);
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        phonePasswordVerificationListener= (PhonePasswordVerificationListener) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        int i=v.getId();
        if(i==R.id.button_new_phoneNumber){
        phonePasswordVerificationListener.onNewSignUpWithPhonePressed();
        }

        if(i==R.id.button_confirm){
            if(validateEditText(etPhoneNo)&& validateEditText(etPassword)) {
                phoneNo=etPhoneNo.getText().toString();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("user_data/" + phoneNo);
                addValueEventListener(ref);
            }
        }


    }

    private boolean validateEditText(EditText editText) {
        String phoneNumber = editText.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            editText.setError("Invalid phone number.");
            return false;
        }
        return true;
    }
    private boolean arePasswordEqual(String p1,String p2){
        if(p1.equals(p2))
            return true;

        return false;
    }
    public void onCheckServerVerification(UserDataWithPassword userDataWithPassword,String password){
        if(userDataWithPassword==null)
        {
            Toast.makeText(getContext(),"Account Does not Exist!",Toast.LENGTH_LONG).show();

        }

        else if (!TextUtils.isEmpty(userDataWithPassword.password)) {
                if (arePasswordEqual(userDataWithPassword.password, password)) {
                    phonePasswordVerificationListener.onPasswordMatched();
                }
                else
                    Toast.makeText(getContext(),"Incorrect Password!",Toast.LENGTH_LONG).show();
            }

        }

    public void addValueEventListener(DatabaseReference ref){
        valueEventListener=null;
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDataWithPassword userDataWithPassword = dataSnapshot.getValue(UserDataWithPassword.class);
                onCheckServerVerification(userDataWithPassword, etPassword.getText().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ref.addValueEventListener(valueEventListener);

    }


}
