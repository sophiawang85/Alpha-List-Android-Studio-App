package com.example.alphalist.ui.newitem;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.alphalist.R;
import com.example.alphalist.model.Cat;
import com.example.alphalist.model.Items;
import com.example.alphalist.model.ItemsList;
import com.example.alphalist.model.Transaction;
import com.example.alphalist.ui.it.ItemListViewModel;
import com.example.alphalist.ui.it.ItemsLVMFactory;
import com.example.alphalist.ui.formatter.DoubleInputFilter;
import com.example.alphalist.ui.formatter.IntInputFilter;
import org.apache.commons.lang3.StringUtils;

public class NewItemFragment extends Fragment {

    private ItemListViewModel shoppingListViewModel;
    private NewItemViewModel viewModel;
    private ItemsList shoppingGroupWithMerchandise;
    private Items updateMerchandise;
    private EditText editTextName;
    private AutoCompleteTextView editTextMerchandiseType;
    private EditText editTextTargetPrice;
    private EditText editTextActualPrice;
    private EditText editTextPurchaseUnits;

    public static NewItemFragment newInstance() {
        return new NewItemFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_new_item, container, false);
        this.viewModel = new ViewModelProvider(this).get(NewItemViewModel.class);

        this.shoppingGroupWithMerchandise = getSelectedShoppingGroup();
        this.updateMerchandise = getSelectedMerchandise();

        final ItemsLVMFactory shoppingListViewModelFactory = new ItemsLVMFactory(requireContext());
        this.shoppingListViewModel = new ViewModelProvider(requireActivity(), shoppingListViewModelFactory).get(ItemListViewModel.class);

        this.editTextName = rootView.findViewById(R.id.editTextName);
        this.editTextMerchandiseType = rootView.findViewById(R.id.editTextItemType);
        this.editTextTargetPrice = rootView.findViewById(R.id.editTextTargetPrice);
        this.editTextActualPrice = rootView.findViewById(R.id.editTextActualPrice);
        this.editTextPurchaseUnits = rootView.findViewById(R.id.editTextPurchasedUnits);

        if (this.updateMerchandise != null) {
            this.editTextName.setText(updateMerchandise.getName());
            this.editTextMerchandiseType.setText(updateMerchandise.getType());
            if (updateMerchandise.getPrice() != null) {
                this.editTextTargetPrice.setText(String.valueOf(updateMerchandise.getPrice()));
            }
        }

        setAutoCompleteTextView();
        setupPriceTextEditFormatter();
        setupButtonOnClickListener(rootView);

        return rootView;
    }

    private void setAutoCompleteTextView() {
        final String[] merchandiseTypes = getMerchandiseTypes();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, merchandiseTypes);
        editTextMerchandiseType.setAdapter(adapter);
        editTextMerchandiseType.setThreshold(1);
    }

    private void setupPriceTextEditFormatter() {
        final InputFilter[] inputFilters = {new DoubleInputFilter()};
        final InputFilter[] intInputFilters = {new IntInputFilter()};

        this.editTextTargetPrice.setFilters(inputFilters);
        this.editTextActualPrice.setFilters(inputFilters);
        this.editTextPurchaseUnits.setFilters(intInputFilters);

        //this.editTextTargetPrice.addTextChangedListener(new DoubleTextWatcher(this.editTextTargetPrice));
        //this.editTextActualPrice.addTextChangedListener(new DoubleTextWatcher(this.editTextActualPrice));
    }

    private void setupButtonOnClickListener (final View rootView) {
        final Button commitButton = rootView.findViewById(R.id.buttonDone);
        final Button cancelButton = rootView.findViewById(R.id.buttonCancel);

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkRequiredFields()) return;
                final Items merchandise = updateMerchandise != null ? updateMerchandise() : createMerchandise();
                final Transaction transaction = createTransaction(shoppingGroupWithMerchandise.cat, merchandise);
                if (transaction != null) {
                    new AlertDialog.Builder(getContext()).setTitle("New purchase transaction")
                            .setMessage(String.format("A new purchase transaction has been recorded at %s for merchandise: %s, purchase price: %s.",
                                    transaction.getDate(),
                                    transaction.getItemname(),
                                    String.valueOf(transaction.getActualPrice())))
                            .setPositiveButton("OK", null)
                            .show();
                }
                Navigation.findNavController(v).navigateUp();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
    }

    private Items updateMerchandise() {
        //update a existing merchandise
        updateMerchandise.setName(editTextName.getText().toString());
        updateMerchandise.setType(editTextMerchandiseType.getText().toString());
        updateMerchandise.setPrice(getPrice(editTextTargetPrice));
        shoppingListViewModel.update(updateMerchandise);
        return updateMerchandise;
    }

    private Items createMerchandise() {
        //create a new merchandise
        Items merchandise = viewModel.createMerchandise(
                editTextName.getText().toString(),
                editTextMerchandiseType.getText().toString(),
                getPrice(editTextTargetPrice),
                shoppingGroupWithMerchandise.cat.getId());
        shoppingListViewModel.insert(merchandise);
        return merchandise;
    }

    private Transaction createTransaction(Cat shoppingGroup, Items merchandise){
        final Double purchasePrice = getPrice(editTextActualPrice);
        final Integer purchaseUnit = getInt(editTextPurchaseUnits);

        if (purchasePrice != null) {
            final Transaction transaction;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                transaction = viewModel.createTransaction(shoppingGroup, merchandise, purchasePrice, purchaseUnit);
                this.shoppingListViewModel.insert(transaction);
                return transaction;
            }
        }
        return null;
    }

    private Double getPrice(EditText editText) {
        if (StringUtils.isBlank(editText.getText().toString())) {
            return null;
        } else {
            return Double.parseDouble(editText.getText().toString().trim());
        }
    }
    private Integer getInt(EditText editText) {
        if (StringUtils.isBlank(editText.getText().toString())) {
            return null;
        } else {
            return Integer.parseInt(editText.getText().toString().trim());
        }
    }
    private boolean checkRequiredFields () {
        if (StringUtils.isBlank(editTextName.getText().toString())) {
            showAlert("Name");
            return false;
        }
        if (StringUtils.isBlank(editTextMerchandiseType.getText().toString())) {
            showAlert("Merchandise Type");
            return false;
        }
        if (StringUtils.isNoneBlank(editTextActualPrice.getText().toString())
                != StringUtils.isNoneBlank(editTextPurchaseUnits.getText().toString())) {
            showAlert("Purchased Price or Purchased Unit(s)");
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final String name = this.editTextName.getText().toString().trim();
            if (updateMerchandise == null || !updateMerchandise.getName().trim().equals(name)) {
                if (shoppingGroupWithMerchandise != null
                        && shoppingGroupWithMerchandise.itemslist.stream().anyMatch(
                                merchandise -> merchandise.getName().equals(name))
                ) {
                    new AlertDialog.Builder(getContext()).setTitle("Duplicate Name")
                            .setMessage(String.format("Duplicate Name: %s, please enter a different value.", name))
                            .setPositiveButton("OK", null)
                            .show();
                    return false;
                }
            }
        }
        return true;
    }

    private void showAlert(String requiredFieldName) {
        new AlertDialog.Builder(getContext()).setTitle("Missing Value")
                .setMessage(String.format("Please enter a value for: %s.", requiredFieldName))
                .setPositiveButton("OK", null)
                .show();
    }

    private ItemsList getSelectedShoppingGroup() {
        if (getArguments() != null && getArguments().containsKey("selectedShoppingGroup")) {
            return getArguments().getParcelable("selectedShoppingGroup");
        } else {
            throw new RuntimeException("Missing shopping group!");
        }
    }

    private Items getSelectedMerchandise() {
        if (getArguments() != null && getArguments().containsKey("selectedMerchandise")) {
            return getArguments().getParcelable("selectedMerchandise");
        } else {
            return null;
        }
    }

    private String[] getMerchandiseTypes() {
        if (getArguments() != null && getArguments().containsKey("merchandiseTypes")) {
            return getArguments().getStringArray("merchandiseTypes");
        } else {
            return new String[0];
        }
    }
}