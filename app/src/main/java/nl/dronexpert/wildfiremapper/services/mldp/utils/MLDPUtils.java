package nl.dronexpert.wildfiremapper.services.mldp.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;

public final class MLDPUtils {

    /**
     *
     * @param characteristic
     * @return
     */
    public static boolean isCharacteristicNotifiable(BluetoothGattCharacteristic characteristic) {
        return (characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
    }

    /**
     *
     * @param characteristic
     * @return
     */
    public static boolean isCharacteristicReadable(BluetoothGattCharacteristic characteristic) {
        return ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0);
    }

    /**
     *
     * @param characteristic
     * @return
     */
    public static boolean isCharacteristicWritable(BluetoothGattCharacteristic characteristic) {
        return (characteristic.getProperties() & (BluetoothGattCharacteristic.PROPERTY_WRITE
                | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0;
    }

    /**
     *
     * @param service
     * @return
     */
    public static boolean isPrivateService(BluetoothGattService service) {
        return service.getUuid().equals(MLDPConstants.UUID_MLDP_PRIVATE_SERVICE)
                || service.getUuid().equals(MLDPConstants.UUID_TRANSPARENT_PRIVATE_SERVICE);
    }

    public static boolean isBleClientReady(RxBleClient rxBleClient) {
        return rxBleClient.getState() == RxBleClient.State.READY;
    }

    public static void setWriteTypeNoResponse(BluetoothGattCharacteristic characteristic) {
        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
    }

    public static void enableNotifications(BluetoothGattCharacteristic transparentTxDataCharacteristic, RxBleConnection rxBleConnection) {
        BluetoothGattDescriptor descriptor = transparentTxDataCharacteristic.getDescriptor(MLDPConstants.UUID_CHAR_NOTIFICATION_DESCRIPTOR);
        rxBleConnection.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
    }

    private MLDPUtils() {
        // This class is not publicly instantiable
    }

}
