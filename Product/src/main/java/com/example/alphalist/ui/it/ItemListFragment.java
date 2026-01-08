package com.example.alphalist.ui.it;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alphalist.R;
import com.example.alphalist.model.Items;
import com.example.alphalist.model.ItemsList;
import com.example.alphalist.ui.adapter.ItemsListA;
import com.example.alphalist.ui.cat.CatFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemListFragment extends Fragment {
    private final static String TITLE = "Shopping Group: %s";
    private ItemListViewModel viewModel;
    private ItemsListA adapter;
    private ItemsList selectedShoppingGroup;

    public static ItemListFragment newInstance() {
        return new ItemListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.selectedShoppingGroup = getSelectedShoppingGroup();
        final ItemsLVMFactory shoppingListViewModelFactory = new ItemsLVMFactory(requireContext());
        this.viewModel = new ViewModelProvider(requireActivity(), shoppingListViewModelFactory).get(ItemListViewModel.class);

        final View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);
        TextView label = rootView.findViewById(R.id.textViewShoppingGroupName);
        label.setText(String.format(TITLE, selectedShoppingGroup.cat.getName()));

        //set ListView header and adapter
        View headerView = inflater.inflate(R.layout.singleitem_header, null);
        ListView listView = rootView.findViewById(R.id.merchandise_list_view);
        listView.addHeaderView(headerView);

        adapter = new ItemsListA(getContext(), new ArrayList<>(), this.viewModel, this.selectedShoppingGroup);
        listView.setAdapter(adapter);

        viewModel.getItems().observe(getViewLifecycleOwner(), merchandises -> {
            adapter.clear();
            List<Items> merchandiseList = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                merchandiseList = merchandises.stream()
                        .filter(m -> m.getCatid() == selectedShoppingGroup.cat.getId())
                        .collect(Collectors.toList());
            }
            adapter.addAll(merchandiseList);
        });

        setUpEventListener(rootView);
        return rootView;
    }


    private void setUpEventListener(View rootView) {
        ImageButton addButton = rootView.findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedShoppingGroup", selectedShoppingGroup);
                bundle.putStringArray("merchandiseTypes", viewModel.getExistingItemsTypes());
                Navigation.findNavController(v).navigate(R.id.action_add_merchandise_to_shopping_group, bundle);
            }
        });
    }

    private ItemsList getSelectedShoppingGroup() {
        //ShoppingListFragmentArgs is not generated, so let's use the following method to get object passed by Safe Args
        if (getArguments() != null && getArguments().containsKey("selectedShoppingGroup")) {
            return getArguments().getParcelable("selectedShoppingGroup");
        } else {
            throw new RuntimeException("Missing shopping group!");
        }
    }
}