package com.ashwinrao.locrate.util;

import com.ashwinrao.locrate.data.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemPropertiesFilter {

    private List<Item> items;

    public ItemPropertiesFilter(List<Item> items) {
        this.items = items;
    }

    public List<Item> filter(CharSequence constraint, boolean onName) {
        List<Item> result = new ArrayList<>();
        if(constraint == null || constraint.length() == 0) {
            result.addAll(items);
        } else {
            final String constr = constraint.toString().toLowerCase();
            for (Item item : items) {
                if (onName && item.getName().toLowerCase().contains(constr)) {
                    result.add(item);
                }
            }
        }
        return result;
    }

}
