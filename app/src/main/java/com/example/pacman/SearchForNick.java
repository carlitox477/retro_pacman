package com.example.pacman;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SearchForNick extends DialogFragment {
    private Button btnSearch;
    private EditText etNicknameToSearch;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstance) {
        super.onCreateView(inflater,container,savedInstance);
        View view=inflater.inflate(R.layout.fragment_search_nickname,container,false);
        btnSearch=(Button) view.findViewById(R.id.btn_search);
        etNicknameToSearch=(EditText) view.findViewById(R.id.et_nickname_for_search);

        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getContext(),"Write a nickname before search",Toast.LENGTH_SHORT).show();
            }
        });

        etNicknameToSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==0){
                    btnSearch.getBackground().setTint(getResources().getColor(R.color.white));
                    btnSearch.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            Toast.makeText(getContext(),"Write a nickname before search",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    btnSearch.getBackground().setTint(getResources().getColor(R.color.text));
                    btnSearch.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            String nickname=etNicknameToSearch.getText().toString();

                            //Query

                            if(nickname==null || nickname==""){
                                //Error message
                                Toast.makeText(getContext(),"There are no score for "+nickname+". Try another nickname",Toast.LENGTH_SHORT).show();
                            }else {

                            }
                        }
                    });
                }

            }
        });
    return view;
    }
}
