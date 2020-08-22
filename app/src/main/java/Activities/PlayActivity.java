package Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacman.DBManager;
import Game.GameView;
import com.example.pacman.R;
import com.example.pacman.Score;

import java.util.concurrent.Semaphore;

public class PlayActivity extends AppCompatActivity {
    private TextView playerNickname;
    private TextView scoreTv;
    private TextView maxScore;
    private SurfaceView gameSurfaceView;
    private GameView gameView;
    private Semaphore changeScoreSemaphore, changeDirectionSemaphore;
    private Thread changeScoreThread, changeDirectionThread;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Modified code
        this.changeScoreSemaphore=new Semaphore(0,true);
        this.changeDirectionSemaphore=new Semaphore(0,true);
        setContentView(R.layout.activity_game);
        //we get text view that we will use
        playerNickname=(TextView) this.findViewById(R.id.tv_player);
        scoreTv=(TextView) this.findViewById(R.id.tv_current_score);
        maxScore=(TextView) this.findViewById(R.id.tv_current_max_score);
        gameSurfaceView= (GameView) this.findViewById(R.id.game_view);

        //set text view initial values
        playerNickname.setText(getIntent().getExtras().getString("playerNickname"));
        scoreTv.setText("0");

        maxScore.setText("To modify");

        this.gameView=new GameView(gameSurfaceView.getContext());
        this.gameView.setSemaphores(this.changeScoreSemaphore,this.changeDirectionSemaphore);
        try {
            Thread.sleep(200);
        }catch (Exception e){}
        this.gameSurfaceView.getHolder().addCallback(this.gameView);
    }

    protected void onResume(){
        super.onResume();
        this.gameView.resume();
        this.initChangerThreads();
    }
    protected void onPause(){
        super.onPause();
        this.gameView.pause();
        //in order to stop the threads
        this.changeScoreSemaphore.release();
        this.changeDirectionSemaphore.release();
    }

    public void updateScore(int score){
        this.scoreTv.setText(""+score);
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

    private void initChangerThreads() {
        this.changeScoreThread = new Thread(new Runnable() {
            public void run() {
                while (gameView.isDrawing()) {
                    try {
                        changeScoreSemaphore.acquire();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    int score=gameView.getGameManager().getScore();
                                    scoreTv.setText(""+score);
                                }catch (Exception e){};
                            }
                        });
                    }catch (Exception e){}
                }
            }
        });
        this.changeScoreThread.start();
    }

}
