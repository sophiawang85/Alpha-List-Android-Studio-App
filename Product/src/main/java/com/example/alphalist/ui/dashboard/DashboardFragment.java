package com.example.alphalist.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.alphalist.R;
import com.example.alphalist.databinding.FragmentDashboardBinding;
import com.example.alphalist.model.Transaction;
import com.example.alphalist.ui.adapter.TransListA;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardFragment extends Fragment {

    private DashboardViewModel viewModel;
    private TransListA adapter;
    private View root;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final DashboardVMFactory dashboardViewModelFactory = new DashboardVMFactory(requireContext());
        this.viewModel = new ViewModelProvider(requireActivity(), dashboardViewModelFactory).get(DashboardViewModel.class);

        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        setupQueryButtonClickListener(root);

        final ListView transactionListView = root.findViewById(R.id.listViewTransaction);

        View headerView = inflater.inflate(R.layout.trans_list_header, null);
        transactionListView.addHeaderView(headerView);
        setupClickListenerForHeader(headerView);

        adapter = new TransListA(getContext(), new ArrayList<>());

        transactionListView.setAdapter(adapter);
        adapter.clear();
        viewModel.getTransactions().observe(getViewLifecycleOwner(), transactionList -> {
            adapter.clear();
            adapter.addAll(transactionList);
            populateMerchandiseTypePieChart(root, transactionList);
        });
        transactionListView.setVerticalScrollBarEnabled(true);
        return root;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void refreshData(final View root, List<String> merchandiseNameFilers, List<String> merchandiseTypeFilters) {
        viewModel.getTransactions().observe(getViewLifecycleOwner(), transactionList -> {//gets the List of all history Items
            adapter.clear();
            List<Transaction> transactionsAfterNameFilters = transactionList.stream().filter(transaction -> merchandiseNameFilers.isEmpty()
                    || merchandiseNameFilers.contains(transaction.getItemname())).collect(Collectors.toList());

            List<Transaction> transactionsAfterTypeFilters = transactionsAfterNameFilters.stream().filter(transaction -> merchandiseTypeFilters.isEmpty()
                    || merchandiseTypeFilters.contains(transaction.getItemType())).collect(Collectors.toList());
            adapter.addAll(transactionsAfterTypeFilters);
            populateMerchandiseTypePieChart(root, transactionsAfterTypeFilters);

        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void populateAutoCompleteFilters(final View root, final List<Transaction> transactionList) {
        List<String> merchandiseNames = transactionList
                .stream()
                .map(Transaction::getItemname)
                .distinct()
                .collect(Collectors.toList());
        MultiAutoCompleteTextView multiAutoCompleteTextViewMerchandiseName = root.findViewById(R.id.multiAutoCompleteTextViewMerchandiseName);
        ArrayAdapter<String> merchandiseNameAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, merchandiseNames);
        multiAutoCompleteTextViewMerchandiseName.setAdapter(merchandiseNameAdapter);
        multiAutoCompleteTextViewMerchandiseName.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        List<String> merchandiseTypes = transactionList
                .stream()
                .map(Transaction::getItemType)
                .distinct()
                .collect(Collectors.toList());
        MultiAutoCompleteTextView multiAutoCompleteTextViewMerchandiseType = root.findViewById(R.id.multiAutoCompleteTextViewMerchandiseType);
        ArrayAdapter<String> merchandiseTypeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, merchandiseTypes);
        multiAutoCompleteTextViewMerchandiseType.setAdapter(merchandiseTypeAdapter);
        multiAutoCompleteTextViewMerchandiseType.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void populateMerchandiseTypePieChart(final View root, final List<Transaction> transactionList) {
        final PieChart pieChart = root.findViewById(R.id.pieChart);
        final Map<String, Double> amountByMerchandiseType = transactionList
                .stream()
                .collect(Collectors.groupingBy(Transaction::getItemType,
                        Collectors.summingDouble(Transaction::getActualPrice)));
        float total = 0f;
        final ArrayList<PieEntry> entries = new ArrayList<>();

        for(final Map.Entry<String, Double> entry : amountByMerchandiseType.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
            total += entry.getValue().floatValue();//for each type in the histroy pie, create a section for it.
        }

        final PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        final PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentValueFormatter(total));
        data.setValueTextSize(12f);
        pieChart.setData(data);

        pieChart.setDrawHoleEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelTextSize(14f);
        pieChart.getLegend().setEnabled(true);
        Legend legend = pieChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);

        pieChart.invalidate();
    }
    private void setupQueryButtonClickListener(final View root) {
        final Button buttonMerchandiseNameQuery = root.findViewById(R.id.buttonMerchandiseNameQuery);
        buttonMerchandiseNameQuery.setVisibility(View.INVISIBLE);
        final Button buttonMerchandiseTypeQuery = root.findViewById(R.id.buttonMerchandiseTypeQuery);
        buttonMerchandiseTypeQuery.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                List<String> selectedMerchandiseNames = getValuesFromMultiAutoCompleteTextView(root, R.id.multiAutoCompleteTextViewMerchandiseName);
                List<String> selectedMerchandiseTypes = getValuesFromMultiAutoCompleteTextView(root, R.id.multiAutoCompleteTextViewMerchandiseType);
                refreshData(root, selectedMerchandiseNames, selectedMerchandiseTypes);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<String> getValuesFromMultiAutoCompleteTextView(final View root, int resId) {
        MultiAutoCompleteTextView multiAutoCompleteTextView = root.findViewById(resId);
        String text = multiAutoCompleteTextView.getText().toString().trim();
        if (StringUtils.isEmpty(text.trim())) {
            return new ArrayList<>();
        }
        return Arrays.stream(text.split(",")).map(String::trim).collect(Collectors.toList());
    }
    private void setupClickListenerForHeader(View headerView) {

        final TextView merchandiseName = headerView.findViewById(R.id.textViewItemName);
        final TextView merchandiseType = headerView.findViewById(R.id.textViewItemType);
        final TextView transactionDate = headerView.findViewById(R.id.textViewTransactionDate);
        final TextView purchasePrice = headerView.findViewById(R.id.textViewActualPrice);
        final TextView purchasedUnits = headerView.findViewById(R.id.textViewPurchasedUnits);


        merchandiseName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.sortDataByColumn(0);
            }
        });

        merchandiseType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.sortDataByColumn(1);
            }
        });

        transactionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.sortDataByColumn(2);
            }
        });

        purchasePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.sortDataByColumn(3);
            }
        });
        purchasedUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.sortDataByColumn(4);
            }
        });
    }

    private static class PercentValueFormatter extends ValueFormatter {
        private float total = 0f;
        public PercentValueFormatter(float total) {
            this.total = total;
        }
        @Override
        public String getPieLabel(float value, PieEntry pieEntry) {
            float originalValue = pieEntry.getValue();
            float percentage = (originalValue / total) * 100;
            return String.format(Locale.getDefault(), "%.2f (%.0f%%)", originalValue, percentage);
        }
    }
}