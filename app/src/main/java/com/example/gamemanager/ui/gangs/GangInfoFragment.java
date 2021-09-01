package com.example.gamemanager.ui.gangs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gamemanager.R;
import com.example.gamemanager.model.Gang;
import com.example.gamemanager.model.Model;
import com.example.gamemanager.model.UserData;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class GangInfoFragment extends Fragment {
    Gang gang;
    View root;
    TextView nameTv;
    ProgressBar progressBar;
    Button saveBtn;
    Button deleteBtn;
    Button createGameBtn;

    ListView list;
    MyAdapter adapter;

    NavigationView navigationView;
    View emailView;
    TextView navEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_gang_info, container, false);
        gang = (Gang) getArguments().getSerializable("gang");

        list = root.findViewById(R.id.ganginfo_user_listv);
        adapter = new MyAdapter();
        list.setAdapter(adapter);

        progressBar = root.findViewById(R.id.ganginfo_progbar);
        progressBar.setVisibility(View.INVISIBLE);

        navigationView =  getActivity().findViewById(R.id.nav_view);
        emailView = navigationView.getHeaderView(0);
        navEmail = (TextView)emailView.findViewById(R.id.drawer_user_text);
        nameTv = root.findViewById(R.id.ganginfo_name_text);
        saveBtn = root.findViewById(R.id.ganginfo_save_btn);
        deleteBtn = root.findViewById(R.id.ganginfo_delete_btn);
        createGameBtn = root.findViewById(R.id.ganginfo_creategame_btn);

        // if manager
        if(navEmail.getText().toString().compareTo(gang.getManager()) == 0) {
            nameTv.setText(gang.getName().toString());

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                    progressBar.setVisibility(View.VISIBLE);
                    saveBtn.setEnabled(false);

                    saveGang();
                }
            });

            createGameBtn.setVisibility(View.VISIBLE);
            createGameBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("usersList",gang.getMembers());
                    Navigation.findNavController(v).navigate(R.id.action_gangInfoFragment_to_newGameFragment,bundle);
                }
            });

            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.instance.deleteGang(gang, ()-> {
                        new SweetAlertDialog(getActivity()) // https://ourcodeworld.com/articles/read/928/how-to-use-sweet-alert-dialogs-in-android
                                .setTitleText("")
                                .setContentText("Gang deleted")
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
        else { // if not manager
            nameTv.setText(gang.getName().toString());

            if (!gang.getMembers().contains(navEmail.getText().toString())) {
                saveBtn.setText("Join Gang");
                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gang.addMember(navEmail.getText().toString());
                        new SweetAlertDialog(getActivity())
                                .setTitleText("")
                                .setContentText("Joined gang!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                })
                                .show();
                        saveGang();
                    }
                });
            } else {
                saveBtn.setText("Leave gang");
                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        gang.removeMember(navEmail.getText().toString());
                        ArrayList<String> tempMembers = gang.getMembers();
                        tempMembers.remove(navEmail.getText().toString());
                        gang.setMembers(tempMembers);
                        new SweetAlertDialog(getActivity())
                                .setTitleText("")
                                .setContentText("Left Gang!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                })
                                .show();
                        saveGang();
                    }
                });
            }
        }
        return root;
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return gang.getMembers().size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.user_list_row,null);
            }

            TextView nameTv = convertView.findViewById(R.id.userrow_user_text);
            ImageView imageV = convertView.findViewById(R.id.userrow_imagev);
            Button mgrBtn = convertView.findViewById(R.id.userrow_manager_btn);
            String user = gang.getMembers().get(position);
            UserData userData = Model.instance.getUserDataByEmail(user);

            nameTv.setText(user);

            if(userData.imageUrl != null && userData.imageUrl != "") {
                Picasso.get().load(userData.imageUrl).placeholder(R.drawable.download)
                        .error(R.drawable.download).into(imageV);
            }

            if(user.compareTo(gang.getManager()) == 0) {
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

    private void saveGang() {
        gang.setName(nameTv.getText().toString());
        gang.setMembers(gang.members);
        Model.instance.saveGang(gang, ()-> {
            Navigation.findNavController(root).navigateUp();
        });
    }
}