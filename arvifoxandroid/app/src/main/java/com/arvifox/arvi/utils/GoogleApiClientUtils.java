package com.arvifox.arvi.utils;

import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

public final class GoogleApiClientUtils {

    /**
     * Log Tag
     */
    public static final String TAG = "GoogleApiClientUtils: ";

    /**
     * no instances
     */
    private GoogleApiClientUtils() {
    }

    /**
     * connect the client if available and not connected.
     *
     * @param googleApiClient the client.
     */
    public static void connect(final GoogleApiClient googleApiClient) {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                return;
            }

            googleApiClient.connect();
        }
    }

    /**
     * disconnect the client if available and connected.
     *
     * @param googleApiClient the client.
     */
    public static void disconnect(final GoogleApiClient googleApiClient) {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    /**
     * connect the client if available and not connected.
     *
     * @param googleApiClient the client.
     * @param timeout         the timeout as int
     * @param timeUnit        the time unit
     */
    public static void blockingConnect(final GoogleApiClient googleApiClient, final int timeout,
                                       final TimeUnit timeUnit) {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                return;
            }

            googleApiClient.blockingConnect(timeout, timeUnit);
        }
    }

    /**
     * disable location updates
     *
     * @param googleApiClient  {@link GoogleApiClient}
     * @param locationListener {@link LocationListener}
     */
    public static void disableLocationUpdates(final GoogleApiClient googleApiClient,
                                              final LocationListener locationListener) {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationListener);
        }
    }

    /**
     * enable location updates
     *
     * @param googleApiClient  {@link GoogleApiClient}
     * @param locationListener {@link LocationListener}
     */
    public static void enableLocationUpdates(final GoogleApiClient googleApiClient,
                                             final LocationRequest locationRequest, final LocationListener locationListener) throws SecurityException {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
        }
    }

    /**
     * retrieve last known location
     *
     * @param googleApiClient the client
     * @return the last known location
     * @throws SecurityException permissions missing
     */
    @Nullable
    public static Location getLastLocation(final GoogleApiClient googleApiClient) throws SecurityException {
        return LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }
}
