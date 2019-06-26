package com.ashwinrao.locrate.viewmodel;

import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoViewModel extends ViewModel {

    private int currentViewPagerPosition;
    private List<String> paths = new ArrayList<>();
    private List<File> files = new ArrayList<>();

    public void setPaths(List<String> paths) {
        if(paths.size() > 0) {
            this.paths.addAll(paths);
        }
        setFiles();
    }

    private void setFiles() {
        for (String path : paths) {
            files.add(new File(path));
        }
    }

    public void clearPaths() {
        this.paths.clear();
        this.files.clear();
    }

    public List<File> getFiles() {
        return this.files;
    }

    public File getFileWithPath(String path) {
        for (File file : files) {
            if(file.getAbsolutePath().equals(path)) {
                return file;
            }
        }
        return null;
    }

    public void setPathAtPosition(int position, String newPath) {
        paths.remove(position);
        paths.add(position, newPath);
    }

    public List<String> getPaths() {
        return this.paths;
    }

    public String getPathAtPosition(int position) {
        return paths.get(position);
    }

    public void setCurrentViewPagerPosition(int currentViewPagerPosition) {
        this.currentViewPagerPosition = currentViewPagerPosition;
    }

    public int getCurrentViewPagerPosition() {
        return this.currentViewPagerPosition;
    }
}
