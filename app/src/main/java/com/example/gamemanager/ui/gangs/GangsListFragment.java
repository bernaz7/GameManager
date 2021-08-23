package com.example.gamemanager.ui.gangs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.gamemanager.R;
import com.example.gamemanager.model.Gang;
import com.example.gamemanager.model.Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class GangsListFragment extends Fragment {

    private GangsViewModel gangsViewModel;
    FloatingActionButton addBtn;
    GangAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    RecyclerView list;

    NavigationView navigationView;
    View emailView;
    TextView navEmail;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        gangsViewModel =
                new ViewModelProvider(this).get(GangsViewModel.class);

        gangsViewModel.getData().observe(getViewLifecycleOwner(),
                (data) -> {
            adapter.notifyDataSetChanged();
                });

        View root = inflater.inflate(R.layout.fragment_gangs_list, container, false);

        navigationView =  getActivity().findViewById(R.id.nav_view);
        emailView = navigationView.getHeaderView(0);
        navEmail = (TextView)emailView.findViewById(R.id.textView);

//        loginView = root.findViewById(R.id.ganglist_login_tv);
//        loginView.setEnabled(false);

        list = root.findViewById(R.id.ganglist_recyclerv);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(root.getContext());
        list.addItemDecoration(new DividerItemDecoration(list.getContext(),DividerItemDecoration.VERTICAL));
        list.setLayoutManager(manager);

        adapter = new GangAdapter();
        list.setAdapter(adapter);
        setupProgressListener();

        swipeRefresh = root.findViewById(R.id.ganglist_swiperefresh);
        swipeRefresh.setOnRefreshListener(()-> {
            gangsViewModel.refresh();
        });

        addBtn = root.findViewById(R.id.ganglist_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_newgangfragment_open);
            }
        });


        return root;
    }

    private void setupProgressListener() {
        Model.instance.gangsLoadingState.observe(getViewLifecycleOwner(),(state)-> {
            switch (state) {
                case loaded:
//                    progressBar.setVisibility(View.GONE);
                    swipeRefresh.setRefreshing(false);
                    break;
                case loading:
//                    progressBar.setVisibility(View.VISIBLE);
                    swipeRefresh.setRefreshing(true); // already have progressbar while loading
                    break;
                case error:
                    //TODO: display error msg
            }
        });
    }

    class GangViewHolder extends RecyclerView.ViewHolder  {
        ImageView imageV;
        TextView nameTv;
        public GangViewHolder(@NonNull View itemView) {
            super(itemView);
            imageV = itemView.findViewById(R.id.gangrow_imagev);
            nameTv = itemView.findViewById(R.id.gangrow_title_text);
        }

        public void bind(Gang gang) {
            nameTv.setText(gang.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("gang",gang);
                    Navigation.findNavController(v).navigate(R.id.action_nav_gangs_to_gangInfoFragment, bundle);
                }
            });
//            imageV.setImageResource(R.drawable.download);
            //TODO: gang image
        }

//        @Override
//        public void onClick(View v) {
//            Log.d("TAG","TEST"+v.findViewById(R.id.gangrow_name).findViewById(R.id.gangrow_name));
//        }
    }

    class GangAdapter extends RecyclerView.Adapter<GangViewHolder> {
        @NonNull
        @Override
        public GangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.gang_list_row,parent,false);
            GangViewHolder holder = new GangViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull GangViewHolder holder, int position) {
            Gang gang = gangsViewModel.getData().getValue().get(position);
            holder.bind(gang);
        }

        @Override
        public int getItemCount() {
            if(gangsViewModel.getData().getValue() != null) {
                int length = gangsViewModel.getData().getValue().size();
                Gang.uniqueId = Long.valueOf(length); // get size of list to update unique id of gangs
                return length;
            }
            else
                return 0;
        }
    }
}