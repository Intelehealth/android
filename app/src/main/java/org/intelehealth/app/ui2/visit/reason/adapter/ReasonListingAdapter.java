package org.intelehealth.app.ui2.visit.reason.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.intelehealth.app.R;
import org.intelehealth.app.appointment.model.SlotInfo;
import org.intelehealth.app.ui2.visit.model.ReasonGroupData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ReasonListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private Context mContext;
    private List<ReasonGroupData> mItemList = new ArrayList<ReasonGroupData>();

    public interface OnItemSelection {
        public void onSelect(String data);
    }

    private OnItemSelection mOnItemSelection;

    public ReasonListingAdapter(RecyclerView recyclerView, Context context, List<ReasonGroupData> itemList, OnItemSelection onItemSelection) {
        mContext = context;
        mItemList = itemList;
        mOnItemSelection = onItemSelection;
        //mAnimator = new RecyclerViewAnimator(recyclerView);
    }

    private JSONObject mThisScreenLanguageJsonObject = new JSONObject();

    public void setLabelJSON(JSONObject json) {
        mThisScreenLanguageJsonObject = json;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ui2_all_reason_list_item, parent, false);
        /**
         * First item's entrance animations.
         */
        //mAnimator.onCreateViewHolder(itemView);

        return new GenericViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GenericViewHolder) {
            GenericViewHolder genericViewHolder = (GenericViewHolder) holder;
            genericViewHolder.reasonGroupData = mItemList.get(position);

            genericViewHolder.tvAlphabets.setText(genericViewHolder.reasonGroupData.getAlphabet());

            ReasonChipsGridAdapter reasonChipsGridAdapter = new ReasonChipsGridAdapter(genericViewHolder.recyclerView, mContext, genericViewHolder.reasonGroupData.getReasons(), new ReasonChipsGridAdapter.OnItemSelection() {
                @Override
                public void onSelect(String data) {
                    mOnItemSelection.onSelect(data);
                }
            });
            genericViewHolder.recyclerView.setAdapter(reasonChipsGridAdapter);


        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    private class GenericViewHolder extends RecyclerView.ViewHolder {
        TextView tvAlphabets;
        ReasonGroupData reasonGroupData;
        RecyclerView recyclerView;

        GenericViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.rcv_container);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));

            tvAlphabets = itemView.findViewById(R.id.tv_alphabets);


        }


    }


}

