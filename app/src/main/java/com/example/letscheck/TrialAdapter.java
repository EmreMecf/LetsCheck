package com.example.letscheck;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.letscheck.databinding.RecyclerRowBinding;

import java.util.ArrayList;

public class TrialAdapter extends RecyclerView.Adapter<TrialAdapter.TrialHolder> {
    ArrayList<Trial> trialArrayList;
    public TrialAdapter(ArrayList<Trial> trialArrayList){
        this.trialArrayList=trialArrayList;
    }

    @NonNull
    @Override
    public TrialHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new TrialHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrialHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.recyclerViewTextView.setText(trialArrayList.get(position).name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.itemView.getContext(),TiralActivity.class);
                intent.putExtra("info","old");
                intent.putExtra("artId",trialArrayList.get(position).id);
                holder.itemView.getContext().startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return trialArrayList.size();
    }

    public class TrialHolder extends RecyclerView.ViewHolder{
        private RecyclerRowBinding binding;

        public TrialHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }



}
