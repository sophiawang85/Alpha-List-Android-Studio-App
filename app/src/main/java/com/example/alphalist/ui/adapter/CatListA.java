package com.example.alphalist.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.alphalist.R;
import com.example.alphalist.model.ItemsList;
import com.example.alphalist.ui.home.HomeViewModel;

import java.util.List;

public class CatListA extends ArrayAdapter<ItemsList> {

    private final Context context;
    private final HomeViewModel hvm;
    public CatListA(@NonNull Context context, @NonNull List<ItemsList> objects, @NonNull HomeViewModel hvm) {
        super(context, 0, objects);
        this.context = context;
        this.hvm = hvm;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.singlecat, parent, false);
        }

        final ItemsList itlist = getItem(position);
        ((TextView) convertView.findViewById(R.id.textViewName)).setText(itlist.cat.getName());

        ((TextView) convertView.findViewById(R.id.textViewItems)).setText(String.valueOf(itlist.itemslist == null ? 0 : itlist.itemslist.size()));
        final ImageButton buttonDelete = convertView.findViewById(R.id.buttonDelete);
        buttonDelete.setTag(position);
        convertView.setTag(position);

        setupViewItemClickListener(convertView, position);
        setupdelbClickListener(buttonDelete);
        return convertView;
    }

    private void setupdelbClickListener(ImageButton delb){
        delb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                int clickedPosition = (int) v.getTag();
                ItemsList selectedShoppingGroup = getItem(clickedPosition);
                if (selectedShoppingGroup != null) {
                    new AlertDialog.Builder(context).setTitle("Confirm Deletion")
                            .setMessage(String.format("Are you sure you want to delete this shopping group, name: %s?",
                                    selectedShoppingGroup.cat.getName()))
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                                //    No need to delete merchandise manually because have set up Cascade Deletion for Merchandise,
                                //    see entity annotation for class Merchandise
                                hvm.delete(selectedShoppingGroup.cat);
                            }).setNegativeButton(android.R.string.no, null).show();
                }
            }
        });
    }
    private void setupViewItemClickListener(View convertView, int position) {
        convertView.setOnClickListener(v -> {
            ItemsList selectedShoppingGroup = getItem(position);
            if (selectedShoppingGroup != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedShoppingGroup", getItem(position));
                Navigation.findNavController(v).navigate(R.id.action_edit_shopping_list, bundle);
                //NavDirections action = HomeFragmentDirections.actionEditShoppingList(selectedShoppingGroup);
                //Navigation.findNavController(v).navigate(action);
            }
        });
    }

}
