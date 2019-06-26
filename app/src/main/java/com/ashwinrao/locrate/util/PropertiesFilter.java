package com.ashwinrao.locrate.util;

import com.ashwinrao.locrate.data.Box;

import java.util.ArrayList;
import java.util.List;

public class PropertiesFilter {

    private List<Box> boxes;

    public PropertiesFilter(List<Box> boxes) {
        this.boxes = boxes;
    }

    public List<Box> filter(CharSequence constraint, boolean onID, boolean onName, boolean onDescription) {
        List<Box> result = new ArrayList<>();
        if(constraint == null || constraint.length() == 0) {
            result.addAll(boxes);
        } else {
            final String constr = constraint.toString().toLowerCase();
            for (Box box : boxes) {
                if ((onID && String.valueOf(box.getId()).toLowerCase().contains(constr))
                        || (onName && box.getName().toLowerCase().contains(constr))
                        || (onDescription && box.getDescription().toLowerCase().contains(constr))) {
                    result.add(box);
                }
            }
        }
        return result;
    }

}
