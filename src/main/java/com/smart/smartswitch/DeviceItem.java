package com.smart.smartswitch;

public class DeviceItem {
    private int deviceImage;
    private String deviceName;

    public DeviceItem(int deviceImage1, String deviceName1)  {

        deviceImage = deviceImage1;
        deviceName = deviceName1;
    }

    public int getDeviceImage() {
        return deviceImage;
    }


    public String getDeviceName()   {
        return deviceName;
    }

}
