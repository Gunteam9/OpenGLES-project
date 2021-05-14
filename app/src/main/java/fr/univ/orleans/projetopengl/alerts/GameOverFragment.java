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

    public static String TAG = "Game Over";

    public GameOverFragment() {
        Bundle args = new Bundle();
        args.putString(TAG, "title");
        this.setArguments(args);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.game_over)
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
