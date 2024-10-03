package com.intelehealth.appointment.features.schedule_appointment;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.intelehealth.appointment.AppointmentBuilder;
import com.intelehealth.appointment.R;
import com.intelehealth.appointment.data.remote.response.SlotInfo;
import org.json.JSONObject;

import java.util.List;

public class PickUpTimeSlotsAdapter extends RecyclerView.Adapter<PickUpTimeSlotsAdapter.GenericViewHolder> {
    private static final String TAG = "PickUpTimeSlotsAdapter";
    Context context;
    List<SlotInfo> mItemList;
    private OnItemSelection mOnItemSelection;
    String appointmentSlot;
    //   OnItemClickListener listener;
    private int selectedPos = -1;

    public interface OnItemSelection {
        public void onSelect(SlotInfo slotInfo);
    }

    public PickUpTimeSlotsAdapter(Context context, List<SlotInfo> itemList, String appointmentSlot,
                                  OnItemSelection onItemSelection) {
        this.context = context;
        this.mItemList = itemList;
        this.appointmentSlot = appointmentSlot;
        this.mOnItemSelection = onItemSelection;

    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pick_up_time_slot_ui2, parent, false);
        return new GenericViewHolder(itemView);
    }

    private JSONObject mThisScreenLanguageJsonObject = new JSONObject();

    public void setLabelJSON(JSONObject json) {
        mThisScreenLanguageJsonObject = json;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        if (holder instanceof GenericViewHolder) {
            GenericViewHolder genericViewHolder = (GenericViewHolder) holder;
            genericViewHolder.slotInfo = mItemList.get(position);

            genericViewHolder.tvTime.setText(genericViewHolder.slotInfo.getSlotTime().toLowerCase());
            genericViewHolder.tvDuration.setText(String.format("%d %s", genericViewHolder.slotInfo.getSlotDuration(), context.getString(R.string.minutes_txt)));
            changeToSelect(selectedPos, position, holder);

            holder.layoutParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemSelection != null) {
                        //  int position = getAdapterPosition();
                        Log.d(TAG, "onClick: getAdapterPosition : " + holder.getAdapterPosition());
                        Log.d(TAG, "onClick: RecyclerView.NO_POSITION : " + RecyclerView.NO_POSITION);

                        if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                            mOnItemSelection.onSelect(holder.slotInfo);
                            notifyItemChanged(selectedPos);
                            selectedPos = holder.getAdapterPosition();
                            notifyItemChanged(selectedPos);
                        }
                    } else {
                        Log.d(TAG, "onClick:listener is null");
                    }
                }
            });
        }
    }

    public void changeToSelect(int selectedPos, int position, GenericViewHolder holder) {
        holder.layoutParent.setSelected(selectedPos == position);
//        if (selectedPos == position) {
//            holder.tvTime.setTextColor(ContextCompat.getColor(context,R.color.textColorWhite));
//            holder.layoutParent.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_selcted_time_slot_ui2));
//        } else {
//
//            holder.layoutParent.setBackground(ContextCompat.getDrawable(context,R.drawable.ui2_bg_disabled_time_slot));
//            holder.tvTime.setTextColor(ContextCompat.getColor(context,R.color.textColorGray));
//        }
    }


    @Override
    public int getItemCount() {
        return mItemList.size();
    }


    public class GenericViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvDuration;
        SlotInfo slotInfo;
        LinearLayout layoutParent;

        public GenericViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime_new);
            tvDuration = itemView.findViewById(R.id.tvDuration_new);
            layoutParent = itemView.findViewById(R.id.parent_time_slot);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(context)
                            .setMessage(context.getResources().getString(R.string.appointment_booking_confirmation_txt)
                                    + "\n\n" + context.getString(R.string.slot_info) + "- \n" + slotInfo.getSlotDate()
                                    + "\n" + slotInfo.getSlotTime()
                                    + "\n" /*+ StringUtils.getTranslatedDays(slotInfo.getSlotDay(), AppointmentBuilder.INSTANCE.getLanguage())*/
                            )
                            //set positive button
                            .setPositiveButton("yes"/*context.getString(R.string.yes)*/, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mOnItemSelection.onSelect(slotInfo);
                                }
                            })
                            //set negative button
                            .setNegativeButton("no"/*context.getString(R.string.no)*/, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();

                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnItemClickListener {
        void onItemClick(SlotInfo slotInfo);
    }
}
