package com.ashwinrao.locrate.view.pages;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.locrate.Locrate;
import com.ashwinrao.locrate.data.model.Move;
import com.ashwinrao.locrate.databinding.FragmentListMovesPageBinding;
import com.ashwinrao.locrate.view.adapter.MovesAdapter;
import com.ashwinrao.locrate.viewmodel.MoveViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class ListMovesPageFragment extends Fragment {

    private LiveData<List<Move>> moves;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Locrate) context.getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moves = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(MoveViewModel.class).getMoves();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentListMovesPageBinding binding = FragmentListMovesPageBinding.inflate(inflater);
        initializeRecyclerViews(binding.upcomingRecyclerView, binding.completedSection);
        return binding.getRoot();
    }

    private void initializeRecyclerViews(@NonNull RecyclerView upcoming, @NonNull ViewGroup completedSection) {
        upcoming.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        final MovesAdapter adapter = new MovesAdapter(Objects.requireNonNull(getContext()));
        upcoming.setAdapter(adapter);
        moves.observe(this, moves -> {
            if(moves != null) {
                adapter.setMoves(moves);
            }
            upcoming.setAdapter(adapter);
        });
    }
}
