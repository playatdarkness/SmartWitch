package com.smart.smartswitch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class AdapterForView extends RecyclerView.Adapter<AdapterForView.ViewHolder> {
    private ArrayList<DeviceItem> deviceItem;
    private OnItemClickListener mListener;

    public interface OnItemClickListener    {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public  static class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView deviceImage;
        public TextView deviceName;
        public ImageView deleteDevice;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            deviceImage = itemView.findViewById(R.id.imageView);
            deviceName = itemView.findViewById(R.id.deviceNameCard);
            deleteDevice = itemView.findViewById(R.id.delete_device);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null)    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)    {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            deleteDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null)    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)    {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public AdapterForView(ArrayList<DeviceItem> deviceItem1) {
        deviceItem = deviceItem1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_item,viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DeviceItem currentItem = deviceItem.get(i);
        viewHolder.deviceImage.setImageResource(currentItem.getDeviceImage());
        viewHolder.deviceName.setText(currentItem.getDeviceName());
    }

    @Override
    public int getItemCount() {
        return deviceItem.size();
    }
}
