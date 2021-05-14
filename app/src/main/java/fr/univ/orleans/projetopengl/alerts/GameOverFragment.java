package fr.univ.orleans.projetopengl.alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import fr.univ.orleans.projetopengl.R;
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
        System.out.println(this.scoreGameOver);
        stringBuilder.append(R.string.game_over).append(this.scoreGameOver);
        builder.setMessage(stringBuilder.toString())
                .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(Objects.requireNonNull(getActivity()).getIntent());
                        getActivity().finish();
                        getActivity().overridePendingTransition(0,0);
                    }
                })
                .setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Objects.requireNonNull(getActivity()).finish();
                        System.exit(0);
                    }
                });
        return builder.create();
    }
}
