package com.example.alphalist.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.alphalist.R;
import com.example.alphalist.databinding.FragmentHomeBinding;
import com.example.alphalist.ui.adapter.CatListA;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ListView catListView;

    private HomeViewModel homeViewModel;
    private CatListA adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final HVMFactory hvm = new HVMFactory(requireContext());
        this.homeViewModel = new ViewModelProvider(requireActivity(), hvm).get(HomeViewModel.class);
        this.binding = FragmentHomeBinding.inflate(inflater, container, false);

        View header = inflater.inflate(R.layout.singlecat_header, null);
        this.catListView = binding.fragmentHomeView;
        this.catListView.addHeaderView(header);

        adapter = new CatListA(getContext(), new ArrayList<>(), homeViewModel);
        this.catListView.setAdapter(adapter);

        homeViewModel.getCat().observe(getViewLifecycleOwner(), itemsLists -> {
            adapter.clear();
            adapter.addAll(itemsLists);
        });
        setUpEventListener();
        return binding.getRoot();
    }

    private void setUpEventListener() {
        ImageButton addButton = binding.addButton;

        TooltipCompat.setTooltipText(addButton, "Create a new Shopping Group");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_create_new_shopping_group);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}