package com.arvifox.arvi.utils;
import android.content.Context;
import android.content.SyncResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

public final class GooglePlayServicesUtils {

  /**
   * no instances
   */
  private GooglePlayServicesUtils() {
  }

  /**
   * check and update the security provider for google play services
   *
   * @param context    a Context.
   * @param syncResult {@link SyncResult}
   * @return provider is up-to-date
   */
  public static boolean installSSLProvider(final Context context, final SyncResult syncResult) {
    try {
      ProviderInstaller.installIfNeeded(context);
    } catch (GooglePlayServicesRepairableException e) {

      // Indicates that Google Play services is out of date, disabled, etc.

      // Prompt the user to install/update/enable Google Play services.
      GoogleApiAvailability.getInstance().showErrorNotification(context, e.getConnectionStatusCode());

      // Notify the SyncManager that a soft error occurred.
      if (syncResult != null) {
        syncResult.stats.numIoExceptions++;
      }
      return false;

    } catch (GooglePlayServicesNotAvailableException e) {
      // Indicates a non-recoverable error; the ProviderInstaller is not able
      // to install an up-to-date Provider.

      // Notify the SyncManager that a hard error occurred.
      if (syncResult != null) {
        syncResult.stats.numAuthExceptions++;
      }
      return false;
    }

    return true;
  }

  /**
   * check and update the security provider for google play services
   *
   * @param context  a Context.
   * @param listener {@link ProviderInstaller.ProviderInstallListener}
   */
  public static void installSSLProviderAsync(final Context context,
      final ProviderInstaller.ProviderInstallListener listener) {
    ProviderInstaller.installIfNeededAsync(context, listener);
  }

  /**
   * Open Market to install Google Play Services
   *
   * @param context App context
   */
  public static void installGooglePlayServices(final Context context) {
    ActionUtils.startPlayStoreByApplicationPackageUri(context, GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE);
  }

  /**
   * Check if GooglePlayServices are ready to use
   *
   * @param context context
   * @return play services available
   */
  public static boolean isGooglePlayServiceAvailable(final Context context) {
    int resp = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
    return resp == ConnectionResult.SUCCESS;
  }

  /**
   * Check if GooglePlayServices are ready to use
   *
   * @param context context
   * @return play services available
   */
  public static int getGooglePlayServicesAvailability(final Context context) {
    return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
  }
}
