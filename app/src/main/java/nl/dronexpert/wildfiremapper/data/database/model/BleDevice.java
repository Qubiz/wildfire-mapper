package nl.dronexpert.wildfiremapper.data.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Mathijs de Groot on 12/06/2018.
 */
@Entity(nameInDb = "ble_device")
public class BleDevice {

    @Id(autoincrement = false)
    private Long id;

    @Property(nameInDb = "name")
    private String name;

    @Unique
    @Property(nameInDb = "mac_address")
    private String macAddress;

    @Property(nameInDb = "connected")
    private boolean isConnected;

    @Property(nameInDb = "created_at")
    private String createdAt;

    @Property(nameInDb = "updated_at")
    private String updatedAt;

    @Generated(hash = 1288523862)
    public BleDevice(Long id, String name, String macAddress, boolean isConnected,
            String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.macAddress = macAddress;
        this.isConnected = isConnected;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Generated(hash = 1527739491)
    public BleDevice() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public boolean getIsConnected() {
        return this.isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
