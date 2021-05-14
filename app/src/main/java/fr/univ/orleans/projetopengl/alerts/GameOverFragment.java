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
    private final int scoreGameOver;

    public GameOverFragment(int score) {
        Bundle args = new Bundle();
        args.putString(TAG, "title");
        this.setArguments(args);
        this.scoreGameOver = score;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getResources().getString(R.string.game_over)).append(" ").append(this.scoreGameOver);
        builder.setMessage(stringBuilder.toString())
                .setPositiveButton(R.string.reset, (dialog, which) -> Game.getInstance().initializeGrid(OpenGLES20Activity.getmGLView()))
                .setNegativeButton(R.string.quit, (dialog, which) -> {
                    Objects.requireNonNull(getActivity()).finish();
                    System.exit(0);
                });
        return builder.create();
    }
}
