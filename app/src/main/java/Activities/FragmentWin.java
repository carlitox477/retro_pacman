package Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pacman.R;

public class FragmentWin extends DialogFragment {
    private TextView scoreTv;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstance,int score) {
        super.onCreateView(inflater, container, savedInstance);
        TextView scoreTv;
        View view;

        view = inflater.inflate(R.layout.fragment_win, container, false);
        scoreTv=(TextView)view.findViewById(R.id.tv_score_value);
        scoreTv.setText(score+"");
        return view;
    }
}
