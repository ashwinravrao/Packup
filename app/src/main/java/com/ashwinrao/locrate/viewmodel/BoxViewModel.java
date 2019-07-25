package com.ashwinrao.locrate.viewmodel;

import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.data.repo.BoxRepository;
import com.ashwinrao.locrate.view.adapter.BoxesAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Filter;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class BoxViewModel extends ViewModel {

    private Box box = new Box();
    private final BoxRepository repo;

    BoxViewModel(BoxRepository repo) {
        this.repo = repo;
    }

    public Box getBox() {
        return this.box;
    }

    public LiveData<Box> getBoxByNumber(int id) {
        return repo.getBoxByNumber(id);
    }

    public LiveData<Box> getBoxByUUID(String uuid) {
        return repo.getBoxByUUID(uuid);
    }

    public LiveData<List<Box>> getBoxes() {
        return repo.getBoxes();
    }

    public boolean saveBox(@NonNull List<String> categories) {
        if (box.getName() != null) {
            box.setCategories(categories);
            repo.insert(this.box);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Temporary fix until a more elegant solution is found
     * <p>
     * Box category tags are stored in a List<String>.
     * Querying the database for all boxes' categories returns
     * List<String>, not the expected List<List<String>>.
     * This method implements a SwitchMap to do the conversion
     * and returns a flattened list.
     *
     * @return LiveData<List< String>> allCategories.
     */

    public LiveData<List<String>> getAllBoxCategories() {
        return Transformations.switchMap(repo.getAllBoxCategories(), value -> {
            final MutableLiveData<List<String>> mld = new MutableLiveData<>();
            final List<List<String>> contents = new ArrayList<>();
            for (String s : value) {
                final String sub = s.substring(2, s.length() - 2);
                final List<String> split = Arrays.asList(sub.split("\",\""));
                contents.add(split);
            }
            mld.setValue(contents.stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList()));
            return mld;
        });
    }

    public void delete(Box box) {
        repo.delete(box);
    }

    public void deleteMultiple(List<Box> boxes) {
        repo.delete(boxes.toArray(new Box[0]));
    }
}
