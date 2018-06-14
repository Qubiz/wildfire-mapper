package nl.dronexpert.wildfiremapper.ui.devicescan;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import nl.dronexpert.wildfiremapper.R;
import nl.dronexpert.wildfiremapper.data.database.model.BleDevice;
import nl.dronexpert.wildfiremapper.di.annotations.ApplicationContext;

import static android.widget.AdapterView.OnItemClickListener;
import static com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic.Icon;

/**
 * Created by Mathijs de Groot on 12/06/2018.
 */
public class DeviceScanResultsAdapter extends ArrayAdapter<BleDevice> {

    private ArrayList<BleDevice> scanResults;
    private OnItemClickListener onItemClickListener;

    public DeviceScanResultsAdapter(@NonNull Context context) {
        super(context, 0);
        scanResults = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(R.layout.scan_list_item, parent, false);
        }

        BleDevice scanResult = scanResults.get(position);

        TextView textViewName = (TextView) convertView.findViewById(R.id.device_name);
        TextView textViewMacAddress = (TextView) convertView.findViewById(R.id.device_address);

        String name = (scanResult.getName() != null) ? scanResult.getName() : "Unknown";

        ImageView imageViewConnectionState = (ImageView) convertView.findViewById(R.id.icon_connected);

        textViewName.setText(name);
        textViewMacAddress.setText(scanResult.getMacAddress());

        imageViewConnectionState.setImageDrawable(new IconicsDrawable(getContext())
                .icon(scanResult.getIsConnected() ? Icon.gmi_bluetooth_connected : Icon.gmi_bluetooth)
                .color(getContext().getResources().getColor(R.color.colorPrimaryDark))
                .sizeDp(16));

        return convertView;
    }

    @Override
    public int getCount() {
        return scanResults.size();
    }

    @Nullable
    @Override
    public BleDevice getItem(int position) {
        return scanResults.get(position);
    }

    @Override
    public void add(@Nullable BleDevice device) {
        if (device != null) {
            scanResults.add(device);
            notifyDataSetChanged();
        }
    }

    @Override
    public void addAll(BleDevice... devices) {
        scanResults.addAll(Arrays.asList(devices));
        notifyDataSetChanged();
    }

    @Override
    public void addAll(@NonNull Collection<? extends BleDevice> collection) {
        scanResults.addAll(collection);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        scanResults.clear();
        notifyDataSetChanged();
    }

}
