package com.ashwinrao.boxray.viewmodel;

import android.app.Application;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.data.BoxRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BoxViewModel extends ViewModel {

    private Object[] boxFields;
    private List<String> boxItems;
    private MutableLiveData<Boolean> canEnableNextButton;

    private final BoxRepository repository;


    public BoxViewModel(@NonNull Application application) {
        repository = ((Boxray) application).getRepository();
        canEnableNextButton = new MutableLiveData<>();
        boxFields = new Object[7];
    }

    // PERTAINING TO ADDFRAGMENT VIEWPAGER

    // Can be get/set from any page, and indicates whether the hosting ViewPager should enable forward navigation (disabled by default)
    public void setCanEnableNextButton(boolean pageFieldsSatisfied) { canEnableNextButton.setValue(pageFieldsSatisfied); }

    public LiveData<Boolean> getCanEnableNextButton() { return canEnableNextButton; }

    // Allows for incremental building of a model object from text field input spread across several pages
    public void saveBoxField(final int boxFieldIndex, Object o) { boxFields[boxFieldIndex] = o; }

    // Allows for contents field in Box constructor to be set independently so as to avoid casting issues in boxFields array
    public void setBoxItems(List<String> boxItems) { this.boxItems = boxItems; }

    // Iterates through array storing model object fields to determine if saving the box is possible (all indices must point to non-null references)
    private boolean canSave() {
        for (Object o : boxFields) {
            if(o == null) {
                return false;
            }
        }
        return true;
    }

    public void save() {
        if(canSave()) {
            repository.saveBox(new Box((int) boxFields[0], (String) boxFields[1], (String) boxFields[2], (String) boxFields[3], (String) boxFields[4], this.boxItems, (boolean) boxFields[6]));
        }
    }


    // PERTAINING TO LISTFRAGMENT RECYCLERVIEW

    public LiveData<List<Box>> getBoxes() {
        return repository.getBoxes();
    }

    public LiveData<Box> getBoxByID(int id) {
        return repository.getBoxByID(id);
    }
}
