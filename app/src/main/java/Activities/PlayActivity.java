package Activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
    private GameView gameView; //no lose in references?
    private static Semaphore CHANGE_LIFES_MUTEX=new Semaphore(0,true);
    private static Semaphore CHANGE_SCORE_MUTEX=new Semaphore(0,true);
    private static Semaphore CHANGE_DIRECTION_MUTEX=new Semaphore(0,true);
    private static Semaphore WIN_LOSE_THREAD_MUTEX=new Semaphore(0,true);
    private Thread changeScoreThread, changeDirectionThread,changeLifesThread, winLoseThread;
    private String playerNickNameString;
    private DBManager scoreManagerDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Modified code
        setContentView(R.layout.activity_game);
        //we get text view that we will use
        playerNickname=(TextView) this.findViewById(R.id.tv_player);
        scoreTv=(TextView) this.findViewById(R.id.tv_current_score);
        maxScore=(TextView) this.findViewById(R.id.tv_current_max_score);
        gameSurfaceView= (GameView) this.findViewById(R.id.game_view);

        //set text view initial values
        playerNickNameString=getIntent().getExtras().getString("playerNickname");
        playerNickname.setText(playerNickNameString);
        scoreTv.setText("0");

        //this.scoreManagerDB=new DBManager(this);
        this.maxScore.setText("To modify");
        this.gameView=new GameView(gameSurfaceView.getContext());
        this.gameView.setSemaphores(CHANGE_SCORE_MUTEX,CHANGE_DIRECTION_MUTEX, WIN_LOSE_THREAD_MUTEX);
        this.gameSurfaceView.getHolder().addCallback(this.gameView);
    }

    protected void onResume(){
        super.onResume();
        //mediaPlayer=MediaPlayer.create(this, R.raw.pacman_siren);
        //mediaPlayer.setVolume(50,50);
        //mediaPlayer.setLooping(true);
        //mediaPlayer.start();
        this.initChangerThreads();
    }

    public void updateScoreTv(int score){
        this.scoreTv.setText(""+score);
    }

    protected void onPause(){
        super.onPause();
        //in order to stop the threads
        CHANGE_SCORE_MUTEX.release();
        CHANGE_DIRECTION_MUTEX.release();
        WIN_LOSE_THREAD_MUTEX.release();
    }

    public void saveScore(int score){
        //We try to save the score, if there is a previous register we write only if this score
        //is better that the one before
        Score scoreToSave;

        scoreToSave=new Score(this.playerNickname.toString(), score);
        Log.i("SCORE DB",this.playerNickNameString+" " + score);
        //if(this.scoreManagerDB.saveScore(scoreToSave)!=-1){
        //    this.scoreManagerDB.updateScore(scoreToSave);
        //}
    }

    private void initChangerThreads() {
        this.changeScoreThread = new Thread(new Runnable() {
            public void run() {
                while (gameView.isDrawing()) {
                    //Log.i("Score ",""+gameManager.getScore());
                    try {
                        CHANGE_SCORE_MUTEX.acquire();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateScoreTv(gameView.getGameManager().getScore());
                            }
                        });
                    }catch (Exception e){}
                }
                Log.i("Score Thread","ended");
            }
        });

        this.winLoseThread =new Thread(new Runnable(){
            @Override
            public void run() {
                while (gameView.isDrawing()) {
                    //Log.i("Checker ",""+gameManager.getScore());
                    try {
                         WIN_LOSE_THREAD_MUTEX.acquire();
                        Log.i("Fragment","try to create fragment (STATE "+gameView.getWinLoseKey()+")");
                        if(gameView.getWinLoseKey()=='W'){
                            FragmentWin fragmentWin=new FragmentWin();
                            Bundle bundle = new Bundle();
                            bundle.putString("Score", (String)scoreTv.getText());
                            saveScore(gameView.getGameManager().getScore());
                            fragmentWin.setArguments(bundle);
                            fragmentWin.show(getSupportFragmentManager(),"Fragment Win");
                        }else if(gameView.getWinLoseKey()=='L'){
                            /*
                            Just for demostration
                            FragmentWin fragmentWin=new FragmentWin();
                            Bundle bundle = new Bundle();
                            bundle.putString("Score", (String)scoreTv.getText());
                            saveScore(gameView.getGameManager().getScore());
                            fragmentWin.setArguments(bundle);
                            fragmentWin.show(getSupportFragmentManager(),"Fragment Win");
                            */
                            Log.i("Lose","create fragment");
                            FragmentLose fragmentLose=new FragmentLose();
                            Bundle bundle = new Bundle();
                            bundle.putString("Score", (String)scoreTv.getText());
                            saveScore(gameView.getGameManager().getScore());
                            fragmentLose.setArguments(bundle);
                            fragmentLose.show(getSupportFragmentManager(),"Fragment Lose");

                        }
                    }catch (Exception e){}
                }

            }
        });
        this.changeScoreThread.start();
        this.winLoseThread.start();
    }

}
