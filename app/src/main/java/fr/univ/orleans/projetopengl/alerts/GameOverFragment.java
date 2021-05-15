package fr.univ.orleans.projetopengl.alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import fr.univ.orleans.projetopengl.R;
import fr.univ.orleans.projetopengl.basic.Game;
import fr.univ.orleans.projetopengl.launcher.OpenGLES20Activity;

public class GameOverFragment extends DialogFragment {

    public static final String TAG = "Game Over";

    public GameOverFragment() {
        Bundle args = new Bundle();
        args.putString(TAG, "title");
        this.setArguments(args);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        StringBuilder stringBuilder = new StringBuilder();
        int score = Game.getInstance().getScore();
        stringBuilder.append(getResources().getString(R.string.game_over))
                .append(" ")
                .append(score)
                .append("\n\n");
        if(Game.getInstance().isHasWon())
        {
            if(score < 3)
                stringBuilder.append("Félicitations ! C'est un excellent score.");
            else if(score < 5)
                stringBuilder.append("Pas mal, mais vous pouvez faire mieux !");
            else
                stringBuilder.append("Nul. Oops... Pardonnez-nous, c'est notre côté taquin :)");
        }
        else
            stringBuilder.append("Manque de chance ou de lucidité ? Réessayez...");



        builder.setMessage(stringBuilder.toString())
                .setPositiveButton(R.string.reset, (dialog, which) ->
                {
                    Game.getInstance().initializeGrid(OpenGLES20Activity.getmGLView());
                    ((OpenGLES20Activity)getActivity()).startCounter();
                })
                .setNegativeButton(R.string.quit, (dialog, which) -> {
                    Objects.requireNonNull(getActivity()).finish();
                    System.exit(0);
                });
        return builder.create();
    }
}
