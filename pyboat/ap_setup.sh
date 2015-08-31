#!/bin/bash
#changes in version 1.1
#	- setup verifies WPA password length
#	- RTL8192CU added as the one needing special hostapd
#	- /etc/network/interfaces in now appended, not overvritten
#changes in version 1.2
#google public DNSes used
#apt-get update added

#variables init
run_time=`date +%Y%m%d%H%M`
post_link="http://http://raspberry-at-home.com/hotspot-wifi-access-point/"
log_file="ap_setup_log.${run_time}"

cat /dev/null > ${log_file}

#  AP settings ##############################################  CHANGE THIS (if needed)#

AP_CHANNEL=1

# /AP settings ############################################## /CHANGE THIS #

#file info & disclaimer
echo ""
echo " ====================================================================== "          | tee -a ${log_file}
echo "title           :ap_setup_final.sh"                                                | tee -a ${log_file}
echo "description     :Automatic access point setup for raspberry pi."                   | tee -a ${log_file}
echo "author          :Jacek Tokar (jacek@raspberry-at-home.com)"		                 | tee -a ${log_file}
echo "author          :Tomasz Szczerba (tomek@raspberry-at-home.com)"	                 | tee -a ${log_file}
echo "author site     :raspberry-at-home.com"                                            | tee -a ${log_file}
echo "full guide      :${post_link}"                                                     | tee -a ${log_file}
echo "date            :20130601"                                                         | tee -a ${log_file}
echo "version         :1.0"                                                              | tee -a ${log_file}
echo "usage           :sudo ./ap_setup.sh"                                               | tee -a ${log_file}
echo "You can improve the script. Once you do it, share it with us. Keep credentials!"	 | tee -a ${log_file}	
echo " ====================================================================== "          | tee -a ${log_file}
echo " DISCLAIMER:   "                                                                   | tee -a ${log_file}
echo "     Jacek, raspberry-at-home.com or anyone else on this blog is not responsible"  | tee -a ${log_file}
echo "     for bricked devices, dead SD cards, thermonuclear war, or any other things "  | tee -a ${log_file}
echo "     script may break. You are using this at your own responsibility...."          | tee -a ${log_file}
echo "     				....and usually it works just fine :)"          				 | tee -a ${log_file}
echo " ====================================================================== "          | tee -a ${log_file}
read -n 1 -p "Do you accept above terms? (y/n)" terms_answer
echo "" 

if [ "${terms_answer,,}" = "y" ]; then
        echo "Thank you!"                                                                | tee -a ${log_file}
else
        echo "Head to ${post_link} read, comment and let us clear your doubts :)"        | tee -a ${log_file}
        exit 1
fi

echo "Updating repositories..."
apt-get update

read -p "Please provide your new SSID to be broadcasted by RPi (i.e. My_Raspi_AP): " AP_SSID
read -s -p "Please provide password for your new wireless network (8-63 characters): " AP_WPA_PASSPHRASE
echo ""
if [ `echo $AP_WPA_PASSPHRASE | wc -c` -lt 8 ] || [ `echo $AP_WPA_PASSPHRASE | wc -c` -gt 63 ]; then
	echo "Sorry, but the password is either to long or too short. Setup will now exit. Start again."
	exit 9
fi  
echo ""
#if [ -f /etc/sysctl.conf.bak ]; then
#        echo "File /etc/sysctl.conf.bak was found. Most likely you have run the setup already. Your prevous configuration was backed up "
#        echo "in several .bak files. Running setup again would overwrite the backup files. If you want to run setup again, remove /etc/sysctl.conf.bak."
#        exit 1;
#fi


if [ `lsusb | grep "RTL8188CUS\|RTL8192CU" | wc -l` -ne 0 ]; then
        echo "Your WiFi is based on the chipset that requires special version of hostapd."              | tee -a ${log_file}
        echo "Setup will download it for you."                                                          | tee -a ${log_file}
        CHIPSET="yes"
else
        echo "Some of the WiFi chipset require special version of hostapd."                             | tee -a ${log_file}
        echo "Please answer yes if you want to have different version of hostapd downloaded."           | tee -a ${log_file}
        echo "(it is not recommended unless you had experienced issues with running regular hostapd)" | tee -a ${log_file}
        read ANSWER
        if [ ${ANSWER,,} = "yes" ]; then
                CHIPSET="yes"
        else
                CHIPSET="no"
        fi
fi

echo "Checking network interfaces..."                                                                   | tee -a ${log_file}
NONIC=`netstat -i | grep ^wlan | cut -d ' ' -f 1 | wc -l`

if [ ${NONIC} -lt 1 ]; then
        echo "There are no wireless network interfaces... Exiting"                                               | tee -a ${log_file}
        exit 1
elif [ ${NONIC} -gt 1 ]; then
        echo "You have more than one wlan interface. Please select the interface to become AP: "         | tee -a ${log_file}
        select INTERFACE in `netstat -i | grep ^wlan | cut -d ' ' -f 1`
        do
                NIC=${INTERFACE}
		break
        done
        exit 1
else
        NIC=`netstat -i | grep ^wlan | cut -d ' ' -f 1`
fi

#echo "Please select network interface you use to connect to the internet:"
#DNS="192.168.42.1"
#select INETNIC in `netstat -i | grep -v lo\|${NIC}\|Iface\|Kernel`
#do
read -p "Please provide network interface that will be used as WAN connection (i.e. eth0): " WAN 
DNS=`netstat -rn | grep ${WAN} | grep UG | tr -s " " "X" | cut -d "X" -f 2`
echo "DNS will be set to " ${DNS}               								| tee -a ${log_file}
echo "You can change DNS addresses for the new network in /etc/dhcp/dhcpd.conf"   | tee -a ${log_file}
 #       break;
#done
echo ""
read -p "Please provide your new AP network (i.e. 192.168.10.X). Remember to put X at the end!!!  " NETWORK 

if [ `echo ${NETWORK} | grep X$ | wc -l` -eq 0 ]; then
	echo "Invalid AP network provided... No X was found at the end of you input. Setup will now exit."
	exit 4
fi	
AP_ADDRESS=`echo ${NETWORK} | tr \"X\" \"1\"`
AP_UPPER_ADDR=`echo ${NETWORK} | tr \"X\" \"9\"`
AP_LOWER_ADDR=`echo ${NETWORK} | tr \"X\" \"2\"`
SUBNET=`echo ${NETWORK} | tr \"X\" \"0\"`


echo ""
echo ""
echo "+========================================================================"
echo "Your network settings will be:"                                                                   | tee -a ${log_file}
echo "AP NIC address: ${AP_ADDRESS}  "                                                                  | tee -a ${log_file}
echo "Subnet:  ${SUBNET} "																				| tee -a ${log_file}
echo "Addresses assigned by DHCP will be from  ${AP_LOWER_ADDR} to ${AP_UPPER_ADDR}"                    | tee -a ${log_file}
echo "Netmask: 255.255.255.0"                                                                           | tee -a ${log_file}
#echo "DNS: ${DNS}        "                                                                              | tee -a ${log_file}
echo "WAN: ${WAN}"																						| tee -a ${log_file}

read -n 1 -p "Continue? (y/n):" GO
echo ""
        if [ ${GO,,} = "y" ]; then
                sleep 1
        else
				exit 2
        fi


echo "Setting up  $NIC"                                                                                 | tee -a ${log_file}




echo "Downloading and installing packages: hostapd isc-dhcp-server iptables."                           | tee -a ${log_file}
echo ""
apt-get -y install hostapd isc-dhcp-server iptables                                                     | tee -a ${log_file} 
service hostapd stop | tee -a ${log_file} > /dev/null
service isc-dhcp-server stop  | tee -a ${log_file}  > /dev/null
echo ""                                                                                                 | tee -a ${log_file} 

echo "Backups:"                                                                                         | tee -a ${log_file}

if [ -f /etc/dhcp/dhcpd.conf ]; then
        cp /etc/dhcp/dhcpd.conf /etc/dhcp/dhcpd.conf.bak.${run_time}
        echo " /etc/dhcp/dhcpd.conf to /etc/dhcp/dhcpd.conf.bak.${run_time}"                              | tee -a ${log_file}
fi

if [ -f /etc/hostapd/hostapd.conf ]; then
        cp /etc/hostapd/hostapd.conf /etc/hostapd/hostapd.conf.bak.${run_time}
        echo "/etc/hostapd/hostapd.conf to /etc/hostapd/hostapd.conf.bak.${run_time}"                   | tee -a ${log_file}
fi

if [ -f /etc/default/isc-dhcp-server ]; then
        cp /etc/default/isc-dhcp-server /etc/default/isc-dhcp-server.bak.${run_time}
        echo "/etc/default/isc-dhcp-server to /etc/default/isc-dhcp-server.bak.${run_time}"             | tee -a ${log_file}
fi

if [ -f /etc/sysctl.conf ]; then
        cp /etc/sysctl.conf /etc/sysctl.conf.bak.${run_time}
        echo "/etc/sysctl.conf /etc/sysctl.conf.bak.${run_time}"                                        | tee -a ${log_file}
fi

if [ -f /etc/network/interfaces ]; then
        cp /etc/network/interfaces /etc/network/interfaces.bak.${run_time}
        echo "/etc/network/interfaces to /etc/network/interfaces.bak.${run_time}"                       | tee -a ${log_file}
fi

 
echo "Setting up AP..."                                                                                 | tee -a ${log_file} 


echo "Configure: /etc/default/isc-dhcp-server"                                                          | tee -a ${log_file} 
echo "DHCPD_CONF=\"/etc/dhcp/dhcpd.conf\""                         >  /etc/default/isc-dhcp-server
echo "INTERFACES=\"$NIC\""                                         >> /etc/default/isc-dhcp-server

echo "Configure: /etc/default/hostapd"                                                          | tee -a ${log_file} 
echo "DAEMON_CONF=\"/etc/hostapd/hostapd.conf\""                   > /etc/default/hostapd

echo "Configure: /etc/dhcp/dhcpd.conf"                                                          | tee -a ${log_file} 
echo "ddns-update-style none;"                                     >  /etc/dhcp/dhcpd.conf
echo "default-lease-time 86400;"                                     >> /etc/dhcp/dhcpd.conf
echo "max-lease-time 86400;"                                        >> /etc/dhcp/dhcpd.conf
echo "subnet ${SUBNET} netmask 255.255.255.0 {"                    >> /etc/dhcp/dhcpd.conf
echo "  range ${AP_LOWER_ADDR} ${AP_UPPER_ADDR}  ;"                >> /etc/dhcp/dhcpd.conf
echo "  option domain-name-servers 8.8.8.8, 8.8.4.4  ;"                       >> /etc/dhcp/dhcpd.conf
echo "  option domain-name \"home\";"                              >> /etc/dhcp/dhcpd.conf
echo "  option routers " ${AP_ADDRESS} " ;"                        >> /etc/dhcp/dhcpd.conf
echo "}"                                                           >> /etc/dhcp/dhcpd.conf

echo "Configure: /etc/hostapd/hostapd.conf"                                                     | tee -a ${log_file} 
if [ ! -f /etc/hostapd/hostapd.conf ]; then
	touch /etc/hostapd/hostapd.conf
fi
	
echo "interface=$NIC"                                    >  /etc/hostapd/hostapd.conf
echo "ssid=${AP_SSID}"                                   >> /etc/hostapd/hostapd.conf
echo "channel=${AP_CHANNEL}"                             >> /etc/hostapd/hostapd.conf
echo "# WPA and WPA2 configuration"                      >> /etc/hostapd/hostapd.conf
echo "macaddr_acl=0"                                     >> /etc/hostapd/hostapd.conf
echo "auth_algs=1"                                       >> /etc/hostapd/hostapd.conf
echo "ignore_broadcast_ssid=0"                           >> /etc/hostapd/hostapd.conf
echo "wpa=2"                                             >> /etc/hostapd/hostapd.conf
echo "wpa_passphrase=${AP_WPA_PASSPHRASE}"               >> /etc/hostapd/hostapd.conf
echo "wpa_key_mgmt=WPA-PSK"                              >> /etc/hostapd/hostapd.conf
echo "wpa_pairwise=TKIP"                                 >> /etc/hostapd/hostapd.conf
echo "rsn_pairwise=CCMP"                                 >> /etc/hostapd/hostapd.conf
echo "# Hardware configuration"                          >> /etc/hostapd/hostapd.conf
if [ ${CHIPSET} = "yes" ]; then

	echo "driver=rtl871xdrv"                         >> /etc/hostapd/hostapd.conf
	echo "ieee80211n=1"                              >> /etc/hostapd/hostapd.conf
    echo "device_name=RTL8192CU"                     >> /etc/hostapd/hostapd.conf
    echo "manufacturer=Realtek"                      >> /etc/hostapd/hostapd.conf
else
	echo "driver=nl80211"                            >> /etc/hostapd/hostapd.conf
fi

echo "hw_mode=g"                                         >> /etc/hostapd/hostapd.conf

echo "Configure: /etc/sysctl.conf"                                                              | tee -a ${log_file} 
echo "net.ipv4.ip_forward=1"                             >> /etc/sysctl.conf 

echo "Configure: iptables"                                                                      | tee -a ${log_file} 
iptables -t nat -A POSTROUTING -o ${WAN} -j MASQUERADE
iptables -A FORWARD -i ${WAN} -o ${NIC} -m state --state RELATED,ESTABLISHED -j ACCEPT
iptables -A FORWARD -i ${NIC} -o ${WAN} -j ACCEPT
sh -c "iptables-save > /etc/iptables.ipv4.nat"

echo "Configure: /etc/network/interfaces"                                                       | tee -a ${log_file} 
echo "auto ${NIC}"                                         >>  /etc/network/interfaces
echo "allow-hotplug ${NIC}"                                >> /etc/network/interfaces
echo "iface ${NIC} inet static"                           >> /etc/network/interfaces
echo "        address ${AP_ADDRESS}"                       >> /etc/network/interfaces
echo "        netmask 255.255.255.0"                     >> /etc/network/interfaces
echo "up iptables-restore < /etc/iptables.ipv4.nat"      >> /etc/network/interfaces




if [ ${CHIPSET,,} = "yes" ]; then 
	echo "Download and install: special hostapd version"                                           | tee -a ${log_file}
	wget "http://raspberry-at-home.com/files/hostapd.gz"                                           | tee -a ${log_file}
     gzip -d hostapd.gz
     chmod 755 hostapd
     cp hostapd /usr/sbin/
fi

ifdown ${NIC}                                                                                    | tee -a ${log_file}
ifup ${NIC}                                                                                      | tee -a ${log_file}
service hostapd start                                                                          | tee -a ${log_file}
service isc-dhcp-server start                                                                  | tee -a ${log_file}

echo ""                                                                                        | tee -a ${log_file}
read -n 1 -p "Would you like to start AP on boot? (y/n): " startup_answer                       
echo ""
if [ ${startup_answer,,} = "y" ]; then
        echo "Configure: startup"                                                              | tee -a ${log_file}
        update-rc.d hostapd enable                                                             | tee -a ${log_file}
        update-rc.d isc-dhcp-server enable                                                     | tee -a ${log_file}
else
        echo "In case you change your mind, please run below commands if you want AP to start on boot:"                       | tee -a ${log_file}
        echo "update-rc.d hostapd enable"                                                      | tee -a ${log_file}
        echo "update-rc.d isc-dhcp-server enable"                                              | tee -a ${log_file}
fi



echo ""                                                                                        | tee -a ${log_file}
echo "Do not worry if you see something like: [FAIL] Starting ISC DHCP server above... this is normal :)"               | tee -a ${log_file}
echo ""                                                                                        | tee -a ${log_file}
echo "REMEMBER TO RESTART YOUR RASPBERRY PI!!!"                                                | tee -a ${log_file}
echo ""                                                                                        | tee -a ${log_file}
echo "Enjoy and visit raspberry-at-home.com"                                                   | tee -a ${log_file}

exit 0
