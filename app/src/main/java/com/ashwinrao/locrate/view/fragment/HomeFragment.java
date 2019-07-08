package com.ashwinrao.locrate.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.ashwinrao.locrate.Locrate;
import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.databinding.FragmentHomeBinding;
import com.ashwinrao.locrate.util.callback.BackNavCallback;
import com.ashwinrao.locrate.view.activity.MainActivity;
import com.ashwinrao.locrate.view.adapter.HomePagerAdapter;
import com.ashwinrao.locrate.view.pages.BoxesPage;
import com.ashwinrao.locrate.view.pages.ItemsPage;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import javax.inject.Inject;

import static com.ashwinrao.locrate.util.UnitConversion.dpToPx;


public class HomeFragment extends Fragment implements BackNavCallback {

    private ViewPager viewPager;
    private BoxesPage boxesPage;
    private ItemsPage itemsPage;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater);
        final AppCompatActivity parent = ((MainActivity) getActivity());
        initializeToolbar(Objects.requireNonNull(parent), binding.toolbar);
        initializeTabLayout(binding.listTabLayout, binding.listViewPager);
        return binding.getRoot();
    }

    private void inflateBottomSheet() {
        final BottomSheetFragment bottomSheet = new BottomSheetFragment();
        bottomSheet.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), bottomSheet.getTag());
    }

    private void initializeTabLayout(@NonNull TabLayout tabLayout, @NonNull ViewPager viewPager) {
        this.boxesPage = new BoxesPage();
        this.itemsPage = new ItemsPage();

        final HomePagerAdapter listPagerAdapter = new HomePagerAdapter(getChildFragmentManager(), boxesPage, itemsPage);
        this.viewPager = viewPager;
        viewPager.setAdapter(listPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initializeToolbar(@NonNull AppCompatActivity parent, @NonNull Toolbar toolbar) {
        parent.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> inflateBottomSheet());
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_overflow, Objects.requireNonNull(getActivity()).getTheme()));
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

                if(viewPager.getCurrentItem() == 0) {
                    boxesPage.onQueryTextChange(newText);
                } else {
                    itemsPage.onQueryTextChange(newText);
                }

//                if (getCurrentlyVisibleFragment() == BoxesPage.class) {
//                    boxesPage.onQueryTextChange(newText);
//                }
//
//                if (getCurrentlyVisibleFragment() == ItemsPage.class) {
//                    itemsPage.onQueryTextChange(newText);
//                }
//
//                if (getCurrentlyVisibleFragment() == null) {
//                    Toast.makeText(getContext(), "NULL!!!", Toast.LENGTH_SHORT).show();
//                }

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) Objects.requireNonNull(getActivity())).unregisterBackNavigationListener();
    }
}
