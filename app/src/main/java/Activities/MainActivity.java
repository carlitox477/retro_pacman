package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pacman.R;

public class MainActivity extends ActivityWithBackGroundMusic {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickCreators(View view){
        //View creators info
        Intent intent = new Intent(this, CreatorsActivity.class);
        startActivity(intent);
    }

    public void onClickScores(View view){
        //View scores
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }

    public void onClickPlay(View view){
        //To start to play

        //we sent the backgroundService to stop it when We really start playind

        PreviousPlayFragment fragmentToPlay=new PreviousPlayFragment();
        fragmentToPlay.setMusic(this.mServ);
        fragmentToPlay.show(getSupportFragmentManager(),"ToPlay");
    }

}
