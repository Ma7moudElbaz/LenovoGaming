package com.cat.lenovogaming.tournaments.tournament_game_details;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cat.lenovogaming.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class DetailsDialog extends DialogFragment {


    String date,game,organizer,type,prize,rules;

    public DetailsDialog(String date, String game, String organizer, String type, String prize, String rules) {
        this.date = date;
        this.game = game;
        this.organizer = organizer;
        this.type = type;
        this.prize = prize;
        this.rules = rules;
    }


    TextView datetv,gametv,organizertv,typetv,prizetv,rulestv;
    ImageView close;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_details, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        datetv = view.findViewById(R.id.dialog_date_txt);
        gametv = view.findViewById(R.id.dialog_game_txt);
        organizertv = view.findViewById(R.id.dialog_organizer_txt);
        typetv = view.findViewById(R.id.dialog_type_txt);
        prizetv = view.findViewById(R.id.dialog_prize_txt);
        rulestv = view.findViewById(R.id.dialog_rules_txt);
        close = view.findViewById(R.id.dialog_close);

        datetv.setText(date);
        gametv.setText(game);
        organizertv.setText(organizer);
        typetv.setText(type);
        prizetv.setText(prize);
        rulestv.setText(Html.fromHtml(rules));

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}
