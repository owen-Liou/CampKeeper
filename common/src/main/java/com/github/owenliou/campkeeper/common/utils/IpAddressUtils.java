package com.github.owenliou.campkeeper.common.utils;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.net.*;
import java.util.Enumeration;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/31
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Component
@Slf4j
public class IpAddressUtils {

    /**
     * 取得本機電腦名稱，若無法取得則回傳 IP 位址
     */
    public String getHostName() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostName = addr.getHostName();
            Assert.notNull(hostName, "Hostname is null");
            return hostName;
        } catch (UnknownHostException ex) {
            log.error("Hostname can not be resolved");
        }
        return getIpAddress();
    }

    /**
     * 取得本機 IP 位址
     */
    @SneakyThrows(SocketException.class)
    public String getIpAddress() {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();
            // 在這裡我們只關心有實際連接的介面
            if (iface.isLoopback() || !iface.isUp()) {
                continue;
            }

            Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while(addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                // 只顯示 IPv4 位址
                if (addr instanceof Inet4Address) {
                    return addr.getHostAddress();
                }
            }
        }
        return "Unknown";
    }

}
