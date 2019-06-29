package com.ashwinrao.locrate.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.ashwinrao.locrate.Locrate;
import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.databinding.FragmentListBinding;
import com.ashwinrao.locrate.util.callback.BackNavCallback;
import com.ashwinrao.locrate.util.callback.OnScrollCallback;
import com.ashwinrao.locrate.view.activity.AddActivity;
import com.ashwinrao.locrate.view.activity.MainActivity;
import com.ashwinrao.locrate.view.adapter.ListPagerAdapter;
import com.ashwinrao.locrate.view.pages.ListBoxesPageFragment;
import com.ashwinrao.locrate.view.pages.ListItemsPageFragment;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import javax.inject.Inject;

import static com.ashwinrao.locrate.util.UnitConversion.dpToPx;


public class ListFragment extends Fragment implements BackNavCallback, OnScrollCallback {

    private TabLayout tabLayout;
    private BoxViewModel viewModel;
    private ExtendedFloatingActionButton fab;

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
        setHasOptionsMenu(true);
        ((MainActivity) Objects.requireNonNull(getActivity())).registerBackNavigationListener(this);
        viewModel = ViewModelProviders.of(getActivity(), factory).get(BoxViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentListBinding binding = FragmentListBinding.inflate(inflater);
        final AppCompatActivity parent = ((MainActivity) getActivity());
        setupToolbar(Objects.requireNonNull(parent), binding.toolbar);
        setupTabLayout(binding.listTabLayout, binding.listViewPager);
        setupEFAB(binding.fab);
        return binding.getRoot();
    }

    private void inflateBottomSheet() {
        final BottomSheetFragment bottomSheet = new BottomSheetFragment();
        bottomSheet.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), bottomSheet.getTag());
    }

    private void setupEFAB(@NonNull ExtendedFloatingActionButton efab) {
        fab = efab;
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddActivity.class);
            startActivity(intent);
            v.setEnabled(false);
        });
    }

    private void setupTabLayout(@NonNull TabLayout tabLayout, @NonNull ViewPager viewPager) {
        this.tabLayout = tabLayout;

        final ListBoxesPageFragment boxesPage = new ListBoxesPageFragment();
        final ListItemsPageFragment itemsPage = new ListItemsPageFragment();

        boxesPage.setScrollCallback(this);

        final ListPagerAdapter listPagerAdapter = new ListPagerAdapter(getChildFragmentManager(), boxesPage, itemsPage);
        viewPager.setAdapter(listPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupToolbar(@NonNull AppCompatActivity parent, @NonNull Toolbar toolbar) {
        parent.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> inflateBottomSheet());
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_overflow, Objects.requireNonNull(getActivity()).getTheme()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!fab.isEnabled()) {
            fab.setEnabled(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_toolbar_list, menu);

        MenuItem search = menu.findItem(R.id.toolbar_search);
        SearchView searchView = (SearchView) search.getActionView();
        configureSearchView(searchView);
    }

    private void configureSearchView(SearchView searchView) {
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search boxes");
        searchView.setPadding(dpToPx(Objects.requireNonNull(getActivity()), -16f), 0, 0, dpToPx(getActivity(), -1f));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                listAdapter.getFilter().filter(newText);
                // todo send newText to ListAdapter in ListBoxesPageFragment via callback
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() { }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) Objects.requireNonNull(getActivity())).unregisterBackNavigationListener();
    }

    @Override
    public void onScroll(int x, int y) {
//        tabLayout.setScrollX(x);
//        tabLayout.setScrollY(y);
    }
}
