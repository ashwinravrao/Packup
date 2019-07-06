package com.ashwinrao.locrate.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ashwinrao.locrate.data.model.Move;
import com.ashwinrao.locrate.data.repo.MoveRepository;

import java.util.List;

public class MoveViewModel extends ViewModel {

    private final MoveRepository repo;

    MoveViewModel(MoveRepository repo) {
        this.repo = repo;
    }

    public LiveData<List<Move>> getMoves() {
        return repo.getMoves();
    }

}
