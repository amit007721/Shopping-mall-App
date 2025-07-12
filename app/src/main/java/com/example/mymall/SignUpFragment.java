package com.example.mymall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;


    private EditText email,fullName,password,confirmPassword;
    private ImageButton closeBtn;
    private Button signUpBtn;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern="[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);
        alreadyHaveAnAccount = view.findViewById(R.id.tv_already_have_an_account);
        email= view.findViewById(R.id.sign_up_email);
        fullName=view.findViewById(R.id.sign_up_fullname);
        password=view.findViewById(R.id.sign_up_password);
        confirmPassword=view.findViewById(R.id.sign_up_confirm_password);
        closeBtn=view.findViewById(R.id.sign_up_close_btn);
        signUpBtn=view.findViewById(R.id.sign_up_btn);
        progressBar=view.findViewById(R.id.sign_up_progressbar);
        firebaseAuth = firebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment() );
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkInputs();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    signUpBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkEmailAndPassword();
        }

        private void checkEmailAndPassword() {
            if (email.getText().toString().matches(emailPattern)){
                if ((password.getText().toString().equals(confirmPassword.getText().toString()))){

                    progressBar.setVisibility(View.VISIBLE);
                    signUpBtn.setEnabled(false);
                    signUpBtn.setTextColor(Color.argb(50,255,255,255));

                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        Map<Object,String> userdata=new HashMap<>();
                                        userdata.put("fullname",fullName.getText().toString());

                                        firebaseFirestore.collection("users")
                                                .add(userdata)
                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                                        if (task.isSuccessful()){

                                                            mainIntent();
                                                        }else {
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            signUpBtn.setEnabled(true);
                                                            signUpBtn.setTextColor(Color.rgb(255,255,255));
                                                            String error =task.getException().getMessage();
                                                            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });


                                    }   else{
                                        progressBar.setVisibility(View.INVISIBLE);
                                        signUpBtn.setEnabled(true);
                                        signUpBtn.setTextColor(Color.rgb(255,255,255));
                                        String error =task.getException().getMessage();
                                        Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {
                    confirmPassword.setError("Password doesn't matched");
                }
            }else {
                email.setError("Invalid Email");
            }
        }
    });

    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    public void checkInputs(){
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(fullName.getText())){
                if (!TextUtils.isEmpty(password.getText()) && password.length()>=6){
                    if (!TextUtils.isEmpty(confirmPassword.getText())){
                        signUpBtn.setEnabled(true);
                        signUpBtn.setTextColor(Color.rgb(255,255,255));
                }else {
                        signUpBtn.setEnabled(false);
                        signUpBtn.setTextColor(Color.argb(50,255,255,255));
                    }
            }else {
                    signUpBtn.setEnabled(false);
                    signUpBtn.setTextColor(Color.argb(50,255,255,255));
                }
        }else {
                signUpBtn.setEnabled(false);
                signUpBtn.setTextColor(Color.argb(50,255,255,255));
            }
    }else{
            signUpBtn.setEnabled(false);
            signUpBtn.setTextColor(Color.argb(50,255,255,255));
        }
        }
    private  void mainIntent(){
        Intent mainIntent = new Intent(getActivity(),MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();

    }

}