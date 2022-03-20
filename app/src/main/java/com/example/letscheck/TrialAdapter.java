package com.example.letscheck;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.letscheck.databinding.RecyclerRowBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrialAdapter extends RecyclerView.Adapter<TrialAdapter.TrialHolder> {
    private List<Trial> trialArrayList = Collections.emptyList();
    private AdapterView.OnItemClickListener onItemClickListener;

    public List<Trial> getTrialArrayList() {
        return trialArrayList;
    }

    public void setTrialArrayList(List<Trial> trialArrayList) {
        this.trialArrayList = trialArrayList;
        notifyDataSetChanged();
    }

    public TrialAdapter(){
    }

    @NonNull
    @Override
    public TrialHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        TrialHolder trialHolder = new TrialHolder(recyclerRowBinding);
        trialHolder.setOnItemClickListener(onItemClickListener);
        return trialHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrialHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.binding.recyclerViewTextView.setText(trialArrayList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return trialArrayList.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class TrialHolder extends RecyclerView.ViewHolder{
        private RecyclerRowBinding binding;

        public TrialHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(null,view,getAdapterPosition(),getItemId());
                }
            });
            binding.popupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(null,view,getAdapterPosition(),getItemId());
                }
            });
        }
    }


}
