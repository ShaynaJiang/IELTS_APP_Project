//package com.example.gproject.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.gproject.R;
//
//import java.util.List;
//public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.BaseViewHolder> {
//
//    private Context context;
//    private List<CardData> dataList;
//    private LayoutInflater mInflater;
//    private OnScrollListener mOnScrollListener;
//
//    public RecyclerAdapter(Context context, List<CardData> dataList) {
//        this.context = context;
//        this.dataList = dataList;
//        this.mInflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = mInflater.inflate(R.layout.word_card, parent, false);
//        return new ViewHolder(context, view);
//    }
//
//    @Override
//    public void onBindViewHolder(BaseViewHolder holder, int position) {
//        switch (getItemViewType(position)) {
//            case DataBean.PARENT_ITEM:
//                ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
//                parentViewHolder.bindView(dataList.get(position), position, itemClickListener);
//                break;
//            case DataBean.CHILD_ITEM:
//                ChildViewHolder childViewHolder = (ChildViewHolder) holder;
//                childViewHolder.bindView(dataList.get(position), position);
//                break;
//        }
//    }
//    public static abstract class BaseViewHolder extends RecyclerView.ViewHolder {
//        public BaseViewHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }
//    @Override
//    public int getItemCount() {
//        return dataList.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return dataList.get(position).getType();
//    }
//
//    private ItemClickListener itemClickListener = new ItemClickListener() {
//        @Override
//        public void onExpandChildren(CardData data) {
//            int position = getCurrentPosition(data.getWord());//确定当前点击的item位置
//            CardData childData = getChildCardData(data);//获取要展示的子布局数据对象
//            if (childData == null) {
//                return;
//            }
//            add(childData, position + 1);//在当前的item下方插入
//            if (position == dataList.size() - 2 && mOnScrollListener != null) { //如果点击的item为最后一个
//                mOnScrollListener.scrollTo(position + 1);//向下滚动，使子布局能够完全展示
//            }
//        }
//
//        @Override
//        public void onHideChildren(CardData data) {
//            int position = getCurrentPosition(data.getWord());//确定当前点击的item位置
//            CardData childData = data.getChildData();//获取子布局对象
//            if (childData == null) {
//                return;
//            }
//            remove(position + 1);//删除
//            if (mOnScrollListener != null) {
//                mOnScrollListener.scrollTo(position);
//            }
//        }
//    };
//
//    private DataBean getChildDataBean(DataBean bean) {
//        DataBean child = new DataBean();
//        child.setType(1);
//        child.setParentLeftTxt(bean.getParentLeftTxt());
//        child.setParentRightTxt(bean.getParentRightTxt());
//        child.setChildLeftTxt(bean.getChildLeftTxt());
//        child.setChildRightTxt(bean.getChildRightTxt());
//        return child;
//    }
//
//    /**
//     * 滚动监听接口
//     */
//    public interface OnScrollListener {
//        void scrollTo(int pos);
//    }
//
//    public void setOnScrollListener(OnScrollListener onScrollListener) {
//        this.mOnScrollListener = onScrollListener;
//    }
//}
