package com.example.problem_catalog.gui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.problem_catalog.R;

import java.util.List;



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<String> problemsList;

    public RecyclerViewAdapter(List<String> problemsList){
        this.problemsList = problemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(problemsList.get(position));
    }

    @Override
    public int getItemCount() {
        return problemsList.size();
    }

    public void setProblemsList(List<String> newProblemsList) {
        int oldSize = problemsList.size();
        problemsList.clear();
        problemsList.addAll(newProblemsList);

        if (newProblemsList.size() > oldSize) {
            notifyItemRangeInserted(oldSize, newProblemsList.size() - oldSize);
        } else if (newProblemsList.size() < oldSize) {
            notifyItemRangeRemoved(newProblemsList.size(), oldSize - newProblemsList.size());
        }
        notifyItemRangeChanged(0, newProblemsList.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textItem);
        }
    }
}
