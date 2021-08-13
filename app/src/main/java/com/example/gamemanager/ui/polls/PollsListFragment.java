package com.example.gamemanager.ui.polls;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.gamemanager.R;
import com.example.gamemanager.model.Model;
import com.example.gamemanager.model.Poll;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class PollsListFragment extends Fragment {

    private PollsViewModel pollsViewModel;
    FloatingActionButton addBtn;
    PollAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    RecyclerView list;

    NavigationView navigationView;
    View emailView;
    TextView navEmail;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pollsViewModel =
                new ViewModelProvider(this).get(PollsViewModel.class);

        pollsViewModel.getData().observe(getViewLifecycleOwner(),
                (data) -> {
                    adapter.notifyDataSetChanged();
                });

        View root = inflater.inflate(R.layout.fragment_polls_list, container, false);

        navigationView =  getActivity().findViewById(R.id.nav_view);
        emailView = navigationView.getHeaderView(0);
        navEmail = (TextView)emailView.findViewById(R.id.textView);

        list = root.findViewById(R.id.polllist_recyclerv);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(root.getContext());
        list.addItemDecoration(new DividerItemDecoration(list.getContext(),DividerItemDecoration.VERTICAL));
        list.setLayoutManager(manager);

        adapter = new PollAdapter();
        list.setAdapter(adapter);
        setupProgressListener();

        swipeRefresh = root.findViewById(R.id.polllist_swiperefresh);
        swipeRefresh.setOnRefreshListener(()-> {
            pollsViewModel.refresh();
        });

        addBtn = root.findViewById(R.id.polllist_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: date picker

            }
        });

        return root;
    }


    private void setupProgressListener() {
        Model.instance.pollsLoadingState.observe(getViewLifecycleOwner(),(state)-> {
            switch (state) {
                case loaded:
                    swipeRefresh.setRefreshing(false);
                    break;
                case loading:
                    swipeRefresh.setRefreshing(true); // already have progressbar while loading
                    break;
                case error:
                    //TODO: display error msg
            }
        });
    }



    class PollViewHolder extends RecyclerView.ViewHolder  {
        ImageView imageV;
        TextView nameTv;
        public PollViewHolder(@NonNull View itemView) {
            super(itemView);
            imageV = itemView.findViewById(R.id.pollrow_imagev);
            nameTv = itemView.findViewById(R.id.pollrow_name);
        }

        public void bind(Poll poll) {
            //nameTv.setText(poll.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("poll",poll);
                    Navigation.findNavController(v).navigate(R.id.action_nav_polls_to_pollInfoFragment, bundle);
                }
            });
//            imageV.setImageResource(R.drawable.download);
            //TODO: poll image
        }

//        @Override
//        public void onClick(View v) {
//            Log.d("TAG","TEST"+v.findViewById(R.id.gangrow_name).findViewById(R.id.gangrow_name));
//        }
    }

    class PollAdapter extends RecyclerView.Adapter<PollsListFragment.PollViewHolder> {
        @NonNull
        @Override
        public PollsListFragment.PollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.poll_list_row,parent,false);
            PollsListFragment.PollViewHolder holder = new PollsListFragment.PollViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull PollsListFragment.PollViewHolder holder, int position) {
            Poll poll = pollsViewModel.getData().getValue().get(position);
            holder.bind(poll);
        }

        @Override
        public int getItemCount() {
            if(pollsViewModel.getData().getValue() != null) {
                int length = pollsViewModel.getData().getValue().size();
                Poll.uniqueId = Long.valueOf(length); // get size of list to update unique id of polls
                return length;
            }
            else
                return 0;
        }
    }
}