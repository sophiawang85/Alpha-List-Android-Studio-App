package com.example.alphalist.ui.cat;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.alphalist.R;
import com.example.alphalist.model.ItemsList;
import com.example.alphalist.ui.home.HVMFactory;
import com.example.alphalist.model.Cat;
import com.example.alphalist.ui.home.HomeViewModel;

import org.apache.commons.lang3.StringUtils;

public class CatFragment extends Fragment {
    private CatViewModel viewModel;
    private HomeViewModel hvm;
    private ItemsList updateCat;
    private EditText editTextName;
    private EditText editTextDescription;

    public static CatFragment newInstance() {
        return new CatFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_newcat, container, false);
        this.viewModel = new ViewModelProvider(this).get(CatViewModel.class);
        final HVMFactory hvmf = new HVMFactory(requireContext());
        this.hvm = new ViewModelProvider(requireActivity(), hvmf).get(HomeViewModel.class);

        this.editTextName = rootView.findViewById(R.id.editTextName);
        this.editTextDescription = rootView.findViewById((R.id.editTextDescription));

        setupDoneButtonClickListener(rootView);
        setupCancelButtonClickListener(rootView);
        return rootView;
    }
    private void setupDoneButtonClickListener(final View rootView) {
        Button commitButton = rootView.findViewById(R.id.buttonDone);
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkRequiredFields()) return;
                boolean result = false;
                if (updateCat != null) {
                    updateCat();
                } else {
                    createCat();
                }
                Navigation.findNavController(v).navigateUp();
            }
        });
    }
    private boolean updateCat() {
        //update a existing shopping group
        updateCat.cat.setName(editTextName.getText().toString());
        updateCat.cat.setDescription(editTextDescription.getText().toString());
        hvm.update(updateCat.cat);
        return true;
    }

    private boolean createCat() {
        Cat c = viewModel.createCat(editTextName.getText().toString(), editTextDescription.getText().toString());
        hvm.insert(c);
        return true;
    }
    private void setupCancelButtonClickListener (final View rootView) {
        Button cancelButton = rootView.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
    }
    private boolean checkRequiredFields () {
        if (StringUtils.isBlank(editTextName.getText().toString())) {
            new AlertDialog.Builder(getContext()).setTitle("Missing Value for Name")
                    .setMessage("Please enter a value for: Name.")
                    .setPositiveButton("OK", null)
                    .show();
            return false;
        }

        final String name = editTextName.getText().toString();
        if (updateCat == null || !updateCat.cat.getName().trim().equals(name)) {
            if (hvm.getCat() != null
                    && hvm.getCat().getValue() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (hvm.getCat().getValue().stream().anyMatch(
                            merchandise -> merchandise.cat.getName().equals(name))) {
                        new AlertDialog.Builder(getContext()).setTitle("Duplicate Name")
                                .setMessage(
                                        String.format(
                                                "Duplicate Name: %s, please enter a different Name.",
                                                name))
                                .setPositiveButton("OK", null)
                                .show();
                        return false;
                    }
                }
            }
        }
        return true;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("selectedShoppingGroup")) {
            updateCat = getArguments().getParcelable("selectedShoppingGroup");
            this.editTextName.setText(updateCat.cat.getName());
            this.editTextDescription.setText(updateCat.cat.getDescription());
        } else {
            updateCat = null;
        }
    }

}