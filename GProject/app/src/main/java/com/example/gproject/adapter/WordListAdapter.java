package com.example.gproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.R;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<CardData> dataList; // 替换 YourDataModel 为实际的数据模型

    public CardAdapter(List<CardData> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // 使用你的布局文件
        View itemView = inflater.inflate(R.layout.word_card, parent, false);

        // 返回 ViewHolder
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 获取数据模型
        CardData data = dataList.get(position);

        // 设置数据到 ViewHolder 中
        holder.wordTextView.setText(data.getWord());
        holder.phoneticTextView.setText(data.getPhonetic());

//        holder.cardView.setOnClickListener(v -> {
//            data.setExpanded(!data.isExpanded());
//
//            // 调用 ItemClickListener 处理展开和隐藏子项的逻辑
//            if (data.getItemClickListener() != null) {
//                if (data.isExpanded()) {
//                    data.getItemClickListener().onExpandChildren(data);
//                } else {
//                    data.getItemClickListener().onHideChildren(data);
//                }
//            }
//
//            notifyDataSetChanged();
//        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public List<CardData> getDataList() {
        return dataList;
    }

    // ViewHolder 类
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView wordTextView;
        public TextView phoneticTextView;
        public View cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 找到布局中的元素
            wordTextView = itemView.findViewById(R.id.word_textview);
            phoneticTextView = itemView.findViewById(R.id.phonetic_textview);
        }
    }

}

