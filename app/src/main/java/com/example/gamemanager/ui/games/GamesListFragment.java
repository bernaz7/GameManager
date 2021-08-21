package com.example.gamemanager.ui.games;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.gamemanager.model.Game;
import com.example.gamemanager.model.Model;
import com.example.gamemanager.ui.polls.PollsListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class GamesListFragment extends Fragment {

    private GamesViewModel gamesViewModel;
    FloatingActionButton addBtn;
    GameAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    RecyclerView list;

    NavigationView navigationView;
    View emailView;
    TextView navEmail;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        gamesViewModel =
                new ViewModelProvider(this).get(GamesViewModel.class);

        gamesViewModel.getData().observe(getViewLifecycleOwner(),
                (data) -> {
                    adapter.notifyDataSetChanged();
                });

        View root = inflater.inflate(R.layout.fragment_games_list, container, false);

        navigationView =  getActivity().findViewById(R.id.nav_view);
        emailView = navigationView.getHeaderView(0);
        navEmail = (TextView)emailView.findViewById(R.id.textView);

        list = root.findViewById(R.id.gamelist_recyclerv);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(root.getContext());
        list.addItemDecoration(new DividerItemDecoration(list.getContext(),DividerItemDecoration.VERTICAL));
        list.setLayoutManager(manager);

        adapter = new GameAdapter();
        list.setAdapter(adapter);
        setupProgressListener();

        swipeRefresh = root.findViewById(R.id.gamelist_swiperefresh);
        swipeRefresh.setOnRefreshListener(()-> {
            gamesViewModel.refresh();
        });

        addBtn = root.findViewById(R.id.gamelist_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_games_to_newGameFragment);
            }
        });

        return root;
    }

    private void setupProgressListener() {
        Model.instance.gamesLoadingState.observe(getViewLifecycleOwner(),(state)-> {
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



    class GameViewHolder extends RecyclerView.ViewHolder  {
        TextView titleTv;
        TextView timeTv;
        TextView usersTv;
        TextView locationTv;
        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.gamerow_title_text);
            timeTv = itemView.findViewById(R.id.gamerow_time_text);
            usersTv = itemView.findViewById(R.id.gamerow_users_text);
            locationTv = itemView.findViewById(R.id.gamerow_location_text);
        }

        public void bind(Game game) {
            titleTv.setText(game.getName());
            timeTv.setText(game.getTime());
            usersTv.setText("Registered: " + game.getUsers().size());
            locationTv.setText("Location: " + game.getLocation());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("game",game);
                    Navigation.findNavController(v).navigate(R.id.action_nav_games_to_gameInfoFragment, bundle);
                }
            });

        }
    }

    class GameAdapter extends RecyclerView.Adapter<GamesListFragment.GameViewHolder> {
        @NonNull
        @Override
        public GamesListFragment.GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.game_list_row,parent,false);
            GamesListFragment.GameViewHolder holder = new GamesListFragment.GameViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull GamesListFragment.GameViewHolder holder, int position) {
            Game game = gamesViewModel.getData().getValue().get(position);
            holder.bind(game);
        }


        @Override
        public int getItemCount() {
            if(gamesViewModel.getData().getValue() != null) {
                int length = gamesViewModel.getData().getValue().size();
                Game.uniqueId = Long.valueOf(length); // get size of list to update unique id of games
                return length;
            }
            else
                return 0;
        }
    }
    
    
}