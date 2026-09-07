package com.example.alphalist.ui.adapter;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.alphalist.R;
import com.example.alphalist.model.Items;
import com.example.alphalist.model.ItemsList;
import com.example.alphalist.ui.it.ItemListViewModel;

import java.util.List;


public class ItemsListA extends ArrayAdapter<Items>{
    private final Context context;
    private final ItemListViewModel viewModel;
    private final ItemsList selectedShoppingGroup;

    private final String[] itTypes;

    public ItemsListA(@NonNull Context context, @NonNull List<Items> objects, @NonNull ItemListViewModel viewModel, @NonNull ItemsList selectedShoppingGroup) {
        super(context, 0, objects);
        this.context = context;
        this.viewModel = viewModel;
        this.selectedShoppingGroup = selectedShoppingGroup;
        this.itTypes = viewModel.getExistingItemsTypes();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.singleitem, parent, false);
        }

        final Items merchandise = getItem(position);
        ((TextView) convertView.findViewById(R.id.textViewName)).setText(merchandise.getName());
        ((TextView) convertView.findViewById(R.id.textViewType)).setText(merchandise.getType().toString());
        if(merchandise.getPrice() != null) {
            ((TextView) convertView.findViewById(R.id.textViewTargetPrice)).setText(formatPrice(merchandise));
        }

        ImageButton buttonRemove = convertView.findViewById(R.id.buttonRemove);
        buttonRemove.setTag(position);
        convertView.setTag(position);

        setupViewItemClickListener(convertView, position);
        setupDeleteButtonClickListener(buttonRemove);

        return convertView;
    }

    private void setupViewItemClickListener(View convertView, int position) {
        convertView.setOnClickListener(v -> {
            Items selectedMerchandise = getItem(position);
            if (selectedMerchandise != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedShoppingGroup", selectedShoppingGroup);
                bundle.putParcelable("selectedMerchandise", selectedMerchandise);
                bundle.putStringArray("merchandiseTypes", itTypes);
                Navigation.findNavController(v).navigate(R.id.action_add_merchandise_to_shopping_group, bundle);
                //NavDirections action = ShoppingListFragmentDirections.actionEditMerchandiseInShoppingGroup(selectedShoppingGroup, selectedMerchandise, merchandiseTypes);
                //Navigation.findNavController(v).navigate(action);
            }
        });
    }

    private void setupDeleteButtonClickListener(ImageButton buttonRemove) {
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = (int) v.getTag();
                Items selectedMerchandise = getItem(clickedPosition);
                if (selectedMerchandise != null) {
                    new AlertDialog.Builder(context).setTitle("Confirm Deletion")
                            .setMessage(String.format("Are you sure you want to delete merchandise: %s?",
                                    selectedMerchandise.getName()))
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                                //    No need to delete merchandise manually because have set up Cascade Deletion for Merchandise,
                                //    see entity annotation for class Merchandise
                                viewModel.delete(selectedMerchandise);
                            }).setNegativeButton(android.R.string.no, null).show();
                }
            }
        });

    }

    private String formatPrice(final Items merchandise) {
        return String.valueOf(merchandise.getPrice());
    }

}
