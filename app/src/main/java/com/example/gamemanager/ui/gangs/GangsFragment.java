package com.example.gamemanager.ui.gangs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.gamemanager.R;
import com.example.gamemanager.model.Gang;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class GangsFragment extends Fragment {

    private GangsViewModel gangsViewModel;
    FloatingActionButton addBtn;
    MyAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        gangsViewModel =
                new ViewModelProvider(this).get(GangsViewModel.class);

        gangsViewModel.getData().observe(getViewLifecycleOwner(),
                (data) -> {
            adapter.notifyDataSetChanged();
                });

        View root = inflater.inflate(R.layout.fragment_gangs, container, false);
        final TextView textView = root.findViewById(R.id.ganglist_textview);

        addBtn = root.findViewById(R.id.ganglist_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_newgangfragment_open);
            }
        });

        return root;
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(gangsViewModel.getData().getValue() != null)
                return gangsViewModel.getData().getValue().size();
            else
                return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.gang_list_row,null);
            }
            ImageView imageV = convertView.findViewById(R.id.gangrow_imagev);
            TextView idTv = convertView.findViewById(R.id.gangrow_id);
            TextView nameTv = convertView.findViewById(R.id.gangrow_name);
            Gang gang = gangsViewModel.getData().getValue().get(position);
            nameTv.setText(gang.getName());
            idTv.setText(gang.getId());
            imageV.setImageResource(R.drawable.download);
            //TODO: gang image
//            if(student.avatar != null && student.avatar != "") {
//                Picasso.get().load(student.avatar).placeholder(R.drawable.download)
//                        .error(R.drawable.download).into(imageV);
//            }


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
}