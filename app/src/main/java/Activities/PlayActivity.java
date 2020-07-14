package Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacman.DBManager;
import com.example.pacman.GameView;
import com.example.pacman.R;
import com.example.pacman.Score;

public class PlayActivity extends AppCompatActivity {
    private TextView playerNickname;
    TextView score;
    private TextView maxScore;
    private SurfaceView gameSurfaceView;
    private GameView gameView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Modified code
        setContentView(R.layout.activity_game);
        //we get text view that we will use
        playerNickname=(TextView) this.findViewById(R.id.tv_player);
        score=(TextView) this.findViewById(R.id.tv_current_score);
        maxScore=(TextView) this.findViewById(R.id.tv_current_max_score);
        gameSurfaceView= (GameView) this.findViewById(R.id.game_view);

        //set text view initial values
        playerNickname.setText(getIntent().getExtras().getString("playerNickname"));
        score.setText("0");

        maxScore.setText("To modify");

        this.gameView=new GameView(gameSurfaceView.getContext());
        this.gameSurfaceView.getHolder().addCallback(this.gameView);
    }

    protected void onResume(){
        super.onResume();
        this.gameView.resume();
    }
    protected void onPause(){
        super.onPause();
        this.gameView.pause();
    }

    public void onLose(double score){
        //We try to save the score, if there is a previous register we write only if this score
        //is better that the one before
        DBManager manager;
        long raw;
        Score scoreToSave;
        manager=new DBManager(this);

        scoreToSave=new Score(this.playerNickname.toString(), score);
        if(manager.saveScore(scoreToSave)==-1){
            //if i couldn't save the score
            if(manager.updateScore(scoreToSave)!=-1){
                //if my new score is better than the one previous
            }else{
                //if my new score is worse or equal than the one previous
            }
        }
    }
}
