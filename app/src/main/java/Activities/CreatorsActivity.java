package Activities;

import android.os.Bundle;

import com.example.pacman.R;

import Activities.ActivityWithBackGroundMusic;

public class CreatorsActivity extends ActivityWithBackGroundMusic {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Solo mostramos los creditos
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creators);
    }
}
