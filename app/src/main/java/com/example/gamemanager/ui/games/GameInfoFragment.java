package com.example.gamemanager.ui.games;

import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;

import com.example.gamemanager.R;
import com.example.gamemanager.model.Game;
import com.example.gamemanager.model.Gang;
import com.example.gamemanager.model.Model;
import com.example.gamemanager.model.UserData;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GameInfoFragment extends Fragment {
    Game game;
    View root;
    TextView titleTv;
    TextView timeTv;
    TextView locationTv;
    ProgressBar progressBar;
    Button joinBtn;
    Button deleteBtn;

    ListView list;
    MyAdapter adapter;

    NavigationView navigationView;
    View emailView;
    TextView navEmail;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_game_info, container, false);
        game = (Game) getArguments().getSerializable("game");

        list = root.findViewById(R.id.gameinfo_user_listv);
        adapter = new MyAdapter();
        list.setAdapter(adapter);


        progressBar = root.findViewById(R.id.gameinfo_progbar);
        progressBar.setVisibility(View.INVISIBLE);

        navigationView =  getActivity().findViewById(R.id.nav_view);
        emailView = navigationView.getHeaderView(0);
        navEmail = (TextView)emailView.findViewById(R.id.textView);

        titleTv = root.findViewById(R.id.gameinfo_name_text);
        timeTv = root.findViewById(R.id.gameinfo_time_text);
        locationTv = root.findViewById(R.id.gameinfo_location_text);

        titleTv.setText(game.getName());
        timeTv.setText(game.getTime());
        locationTv.setText(game.getLocation());

        joinBtn = root.findViewById(R.id.gameinfo_join_btn);

        if(!game.users.contains(navEmail.getText().toString()))
        {
            joinBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    game.addUser(navEmail.getText().toString());
                    new SweetAlertDialog(getActivity()) // https://ourcodeworld.com/articles/read/928/how-to-use-sweet-alert-dialogs-in-android
                            .setTitleText("Joined game successfully!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                    joinBtn.setEnabled(false);

                    saveGame();
                }
            });
        }
        else
        {
            joinBtn.setText("Leave Game");
            joinBtn.setBackgroundColor(getResources().getColor(R.color.red_btn_bg_pressed_color));
            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    game.removeUser(navEmail.getText().toString());
                    joinBtn.setEnabled(false);

                    saveGame();
                }
            });

        }


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!game.users.contains(navEmail.getText().toString())) {
                    game.users.add(navEmail.getText().toString());
                    new SweetAlertDialog(getActivity()) // https://ourcodeworld.com/articles/read/928/how-to-use-sweet-alert-dialogs-in-android
                            .setTitleText("Joined game successfully!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                }
                else {
                    new SweetAlertDialog(getActivity()) // https://ourcodeworld.com/articles/read/928/how-to-use-sweet-alert-dialogs-in-android
                            .setTitleText("You've already joined this game!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                }
                joinBtn.setEnabled(false);

                saveGame();
            }
        });

        deleteBtn = root.findViewById(R.id.gameinfo_delete_btn);
        // if manager
        if (navEmail.getText().toString().compareTo(game.getManager().toString()) == 0)
        {
            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.instance.deleteGame(game, ()-> {
                        new SweetAlertDialog(getActivity()) // https://ourcodeworld.com/articles/read/928/how-to-use-sweet-alert-dialogs-in-android
                                .setTitleText("")
                                .setContentText("Game deleted")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        Navigation.findNavController(root).navigateUp();
                                    }
                                })
                                .show();
                    });
                }
            });
        }

        return root;
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return game.getUsers().size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.user_list_row,null);
            }

            TextView nameTv = convertView.findViewById(R.id.userrow_user_text);
            ImageView imageV = convertView.findViewById(R.id.userrow_imagev);
            Button mgrBtn = convertView.findViewById(R.id.userrow_manager_btn);
            String user = game.getUsers().get(position);
            UserData userData = Model.instance.getUserDataByEmail(user);

            nameTv.setText(user);

            if(userData.imageUrl != null && userData.imageUrl != "") {
                Picasso.get().load(userData.imageUrl).placeholder(R.drawable.download)
                        .error(R.drawable.download).into(imageV);
            }

            if(user.compareTo(game.getManager()) == 0) {
                mgrBtn.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    private void saveGame() {
        Model.instance.saveGame(game, ()-> {
            Navigation.findNavController(root).navigateUp();
        });
    }
}