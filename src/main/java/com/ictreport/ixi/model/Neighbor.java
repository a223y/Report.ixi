package com.ictreport.ixi.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.security.PublicKey;

public class Neighbor {

    private static final Logger LOGGER = LogManager.getLogger(Neighbor.class);
    private InetSocketAddress socketAddress;
    private String uuid = null;
    private String reportIxiVersion = null;
    private int pingCount = 0;
    private int metadataCount = 0;
    private int invalidCount = 0;

    public Neighbor(final InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public boolean sentPacket(final DatagramPacket packet) {
        boolean sameIP = sentPacketFromSameIP(packet);
        boolean samePort = socketAddress.getPort() == packet.getPort();
        return sameIP && samePort;
    }

    public boolean sentPacketFromSameIP(final DatagramPacket packet) {
        if (socketAddress == null) {
            return false;
        }
        return socketAddress.getAddress().getHostAddress().equals(packet.getAddress().getHostAddress());
    }

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    /**
     * @return the socketAddress
     */
    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    /**
     * @param socketAddress the socketAddress to set
     */
    public void setSocketAddress(final InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public String getReportIxiVersion() {
        return reportIxiVersion;
    }

    public void setReportIxiVersion(final String reportIxiVersion) {
        this.reportIxiVersion = reportIxiVersion;
    }

    public void incrementPingCount() {
        pingCount++;
    }

    public int getPingCount() {
        return pingCount;
    }

    public void incrementMetadataCount() {
        metadataCount++;
    }

    public int getMetadataCount() {
        return metadataCount;
    }

    public void incrementInvalidCount() {
        invalidCount++;
    }

    public int getInvalidCount() {
        return invalidCount;
    }

    public void resetMetrics() {
        metadataCount = 0;
        pingCount = 0;
        invalidCount = 0;
    }

    public void resolveHost() {
        try {
            if (!socketAddress.getAddress().equals(InetAddress.getByName(socketAddress.getHostName()))) {
                socketAddress = new InetSocketAddress(socketAddress.getHostName(), socketAddress.getPort());
            }
        } catch (UnknownHostException e) {
            LOGGER.warn("Failed to resolve host for: " + socketAddress.getHostString());
        }
    }
}