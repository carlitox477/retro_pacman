package Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.pacman.R;

public class LandingActivity extends ActivityWithBackGroundMusic {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        //Mostramos la presentación

        final Context mContext = this;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
            }
        }, 500);
        //Despues la un tiempo mostramos el menu
    }

    public void onClickContinue(View view){
        // Si hacemos click en el landing tambien vamos al menu
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
