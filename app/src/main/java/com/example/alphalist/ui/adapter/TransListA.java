package com.example.alphalist.ui.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.alphalist.R;
import com.example.alphalist.model.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class TransListA extends ArrayAdapter<Transaction>{
    private static final int NUM_COLUMNS = 5;
    private final Context context;
    private final List<Transaction> transactions;
    private final List<Boolean> sortAscendingFlags;
    public TransListA(Context context, List<Transaction> transactions) {
        super(context, 0, transactions);
        this.context = context;
        this.transactions = transactions;
        sortAscendingFlags = new ArrayList<>(Collections.nCopies(NUM_COLUMNS, true));
    }
    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        final ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.trans_list_item, parent, false);
            holder = new ViewHolder();
            holder.bind(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Transaction t = transactions.get(pos);
        holder.populate(t);
        return convertView;
    }

    public void sortDataByColumn(int column) {
        boolean ascending = sortAscendingFlags.get(column);
        if (column == 0){
            sortByItemName(ascending);
        }
        else if (column == 1){
            sortByItemType(ascending);
        }
        else if (column == 2){

            sortByTransactionDate(ascending);
        }else if (column == 3){
            sortByPurchasePrice(ascending);

        }else if (column == 4) {
            sortByPurchasedUnits(ascending);
        }
        super.notifyDataSetChanged();
        sortAscendingFlags.set(column, !ascending);
    }
    private void sortByPurchasedUnits(boolean ascending) {
        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction item1, Transaction item2) {
                return ascending ? item1.getPurchasedUnit().compareTo(item2.getPurchasedUnit()) :
                        item2.getPurchasedUnit().compareTo(item1.getPurchasedUnit());
            }
        });
    }
    private void sortByTransactionDate(boolean ascending) {
        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction item1, Transaction item2) {
                return ascending ? item1.getDate().compareTo(item2.getDate()) :
                        item2.getDate().compareTo(item1.getDate());
            }
        });
    }
    private void sortByItemName(boolean ascending) {
        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction item1, Transaction item2) {
                return ascending ? item1.getItemname().compareTo(item2.getItemname()) :
                        item2.getItemname().compareTo(item1.getItemname());
            }
        });
    }

    private void sortByItemType(boolean ascending) {
        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction item1, Transaction item2) {
                return ascending ? item1.getItemType().compareTo(item2.getItemType()) :
                        item2.getItemType().compareTo(item1.getItemType());
            }
        });
    }

    private void sortByPurchasePrice(boolean ascending) {
        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction item1, Transaction item2) {
                return ascending ? item1.getActualPrice().compareTo(item2.getActualPrice()) :
                        item2.getActualPrice().compareTo(item1.getActualPrice());
            }
        });
    }
    private static class ViewHolder {
        TextView textViewItemName;
        TextView textViewItemType;
        TextView textViewTransactionDate;
        TextView textViewPurchasePrice;
        TextView getTextViewPurchasedUnits;

        public void bind(View convertView) {
            this.textViewItemName = convertView.findViewById(R.id.textViewItemName);
            this.textViewItemType = convertView.findViewById(R.id.textViewItemType);
            this.textViewTransactionDate = convertView.findViewById(R.id.textViewTransactionDate);
            this.textViewPurchasePrice = convertView.findViewById(R.id.textViewActualPrice);
            this.getTextViewPurchasedUnits = convertView.findViewById(R.id.textViewPurchasedUnits);
        }

        public void populate(Transaction transaction) {
            this.textViewItemName.setText(transaction.getItemname());
            this.textViewItemType.setText(transaction.getItemType());
            this.textViewTransactionDate.setText(transaction.getDate());
            this.textViewPurchasePrice.setText(String.valueOf(transaction.getActualPrice()));
            this.getTextViewPurchasedUnits.setText(String.valueOf(transaction.getPurchasedUnit()));
        }
    }

}
