package com.ashwinrao.boxray.util;

import com.ashwinrao.boxray.data.Box;

public interface ListChangeListener {

    void edit(Box box);

    void delete(Box box);
}
