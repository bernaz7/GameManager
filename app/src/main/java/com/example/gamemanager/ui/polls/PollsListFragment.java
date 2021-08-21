package com.example.gamemanager.ui.polls;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
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
                Navigation.findNavController(v).navigate(R.id.action_nav_polls_to_newPollFragment);
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
        TextView titleTv;
        TextView optionsTv;
        Button runningBtn;
        public PollViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.pollrow_title_text);
            optionsTv = itemView.findViewById(R.id.pollrow_options_text);
            runningBtn = itemView.findViewById(R.id.pollrow_running_toggle);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Poll poll) {
            titleTv.setText(poll.getManager() + " - " + poll.getDateCrated());
            optionsTv.setText("Options: "+ poll.options.size());
            if(poll.isRunning) {
                runningBtn.setText("Running");
                runningBtn.setBackgroundColor(getResources().getColor(R.color.orange));
            }
            else {
                runningBtn.setText("Ended");
                runningBtn.setBackgroundColor(getResources().getColor(R.color.gray));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("poll",poll);
                    Navigation.findNavController(v).navigate(R.id.action_nav_polls_to_pollInfoFragment, bundle);
                }
            });

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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