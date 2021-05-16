package fr.univ.orleans.projetopengl.alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import fr.univ.orleans.projetopengl.R;
import fr.univ.orleans.projetopengl.audio.AudioManager;
import fr.univ.orleans.projetopengl.basic.GameController;
import fr.univ.orleans.projetopengl.launcher.OpenGLES20Activity;

/**
 * Alert dialog class when the game is finished
 */
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
        int score = GameController.getInstance().getScore();
        stringBuilder.append(getResources().getString(R.string.game_over))
                .append(" ")
                .append(score)
                .append("\n\n");
        if(GameController.getInstance().isHasWon())
        {
            if(score < 25)
                stringBuilder.append("Félicitations ! C'est un excellent score.");
            else if(score < 45)
                stringBuilder.append("Pas mal, mais vous pouvez faire mieux !");
            else
                stringBuilder.append("Bof. Oops... Pardonnez-nous, c'est notre côté taquin :-)");
        }
        else
        {
            stringBuilder.append("Perdu ! Manque de chance ou de lucidité ? Réessayez...");
            GameController.getInstance().playAudio(AudioManager.TAG_LOSE);
        }

        builder.setMessage(stringBuilder.toString())
                .setPositiveButton(R.string.reset, (dialog, which) ->
                {
                    GameController.getInstance().initializeGrid(OpenGLES20Activity.getmGLView());
                })
                .setNegativeButton(R.string.quit, (dialog, which) -> {
                    Objects.requireNonNull(getActivity()).finish();
                    System.exit(0);
                });
        return builder.create();
    }
}
