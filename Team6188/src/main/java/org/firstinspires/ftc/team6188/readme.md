Team 6188 - SiBorgs
Code for team 6188 SiBorgs 2018-2019 competition season

Wireless ADB:

    Connect the device via USB and make sure debugging is working.
    adb tcpip 5555
    find the IP address with adb shell netcfg or adb shell ifconfig with 6.0 and higher
    adb connect <DEVICE_IP_ADDRESS>:5555
    Disconnect USB and proceed with wireless debugging.
    adb -s <DEVICE_IP_ADDRESS>:5555 usb to switch back when done.
