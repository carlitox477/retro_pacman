package com.example.pacman;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
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

public class PreviousPlayFragment extends DialogFragment {
    private Button btnPlay;
    private EditText nickname;

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstance){
        super.onCreateView(inflater,container,savedInstance);
        View view=inflater.inflate(R.layout.layout_to_play,container,false);

        btnPlay=(Button) view.findViewById(R.id.btn_play);
        nickname=(EditText) view.findViewById(R.id.et_nickname);

        btnPlay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getContext(),"Write a nickname before start to play",Toast.LENGTH_SHORT).show();
            }
        });

        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==0){
                    btnPlay.getBackground().setTint(getResources().getColor(R.color.white));
                    btnPlay.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            Toast.makeText(getContext(),"Write a nickname before start to play",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{

                    btnPlay.getBackground().setTint(getResources().getColor(R.color.text));
                    btnPlay.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            //create score for test
                            Score score=new Score(nickname.getText().toString(),Math.random()*1000);
                            String scoreAsString=String.valueOf(score.getScore());
                            int indexPoint=scoreAsString.indexOf(".");
                            String integerScore=scoreAsString.substring(0,indexPoint);

                            //We register the score in the DB

                            Toast.makeText(getContext(),"Register "+score.getNickname()+" with a "+integerScore+" score",Toast.LENGTH_SHORT).show();

                            //Finish test code
                            Intent intent = new Intent(getActivity(), PlayActivity.class);
                            startActivity(intent);

                        }
                    });

                }

            }
        });

        return view;
    }


}
