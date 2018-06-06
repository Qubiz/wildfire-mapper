package nl.dronexpert.wildfiremapper.services.ble.mldp;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

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

    private MLDPUtils() {
        // This class is not publicly instantiable
    }
}
