package com.arvifox.arvi.utils

import android.app.*
import android.bluetooth.BluetoothAdapter
import android.content.*
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.LocationManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.BaseColumns
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.arvifox.arvi.utils.FileUtils.getFileProviderUri
import java.io.File

object ActionUtils {
    /**
     * extra back to main
     */
    const val KEY_EXTRA_BACK_TO_MAIN = "KEY_EXTRA_BACK_TO_MAIN"
    /**
     * login should displayed as dialog to preserve last ui state
     */
    const val KEY_LOGIN_VOLATILE = "KEY_LOGIN_VOLATILE"
    /**
     * requestCode for BT coupling
     */
    const val REQUEST_CODE_COUPLE_BT = 25
    /**
     * requestCode for BT enabling
     */
    const val REQUEST_CODE_ENABLE_BT = 26
    /**
     * requestCode for check list actions
     */
    const val REQUEST_CODE_ACTION_CHECK_LIST = 27
    /**
     * MIME data type for selecting all image types.
     */
    const val MIME_TYPE_IMAGE_ALL = "image/*"
    /**
     * The name of the Intent-extra used to specify the expected X ratio of the image to crop.
     * Constant-Value: "aspectX"
     */
    const val PHOTO_EXTRA_ASPECT_X = "aspectX"
    /**
     * The name of the Intent-extra used to specify the expected Y ratio of the image to crop.
     * Constant-Value: "aspectY"
     */
    const val PHOTO_EXTRA_ASPECT_Y = "aspectY"
    /**
     * The name of the Intent-extra used to specify the expected width (in integer pixel) of the image to crop.
     * Constant-Value: "outputX"
     */
    const val PHOTO_EXTRA_OUTPUT_X = "outputX"
    /**
     * The name of the Intent-extra used to specify the expected height (in integer pixel) of the image to crop.
     * Constant-Value: "outputY"
     */
    const val PHOTO_EXTRA_OUTPUT_Y = "outputY"
    /**
     * The name of the Intent-extra used to crop an image.
     * Constant-Value: "crop"
     */
    const val PHOTO_EXTRA_CROP = "crop"
    /**
     * prefix for calls
     */
    private const val CALL_PROTOCOL_PREFIX = "tel:"
    /**
     * Url for apps in google market
     */
    private const val GOOGLE_PLAY_STORE_APPS_URL = "https://play.google.com/store/apps"
    /**
     * Google Maps place link
     */
    private const val PLAIN_GOOGLE_MAPS_PLACE_LINK =
        "https://www.google.com/maps/place/%s+%s"

    /**
     * restart the application
     *
     * @param ctx Context
     */
    fun restartApplication(ctx: Context) {
        val launchIntent =
            ctx.packageManager.getLaunchIntentForPackage(ctx.packageName)
        val pendingStartIntent = PendingIntent
            .getActivity(ctx, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mgr =
            ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr[AlarmManager.RTC, System.currentTimeMillis() + 1000] = pendingStartIntent
        System.exit(2)
    }

    /**
     * is mobile phone charging at the moment
     * (it also checks if is plugged in and has 100 percentage battery level)
     *
     * @param context a Context
     * @return true or false
     */
    fun isCharging(context: Context): Boolean {
        var charging = false
        val batteryIntent = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        var status = -1
        if (batteryIntent != null) {
            status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        }
        val batteryCharge =
            status == BatteryManager.BATTERY_STATUS_CHARGING
        var chargePlug = 0
        if (batteryIntent != null) {
            chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        }
        val usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
        val acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC
        if (batteryCharge) charging = true
        if (usbCharge) charging = true
        if (acCharge) charging = true
        return charging
    }

    /**
     * start a navigation app via intent
     *
     * @param context   Context
     * @param latitude  geo-coordinate
     * @param longitude geo-coordinate
     * @param options   option [walk,drive..]
     */
    fun startNavigationApp(
        context: Context, latitude: Double?, longitude: Double?,
        options: String?
    ) {
        if (latitude == null || longitude == null) {
            return
        }
        var navigationOptions = options
        if (navigationOptions == null) {
            navigationOptions = ""
        }
        try {
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    "google.navigation:q=" + latitude + "," + longitude
                            + "&" + navigationOptions
                )
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (activityNotFoundException: ActivityNotFoundException) {
        }
    }

    /**
     * call a phone number
     *
     * @param context Context
     * @param phone   phone number
     */
    fun call(context: Context, phone: String) {
        try {
            val callIntent =
                Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse(CALL_PROTOCOL_PREFIX + phone)
            context.startActivity(callIntent)
        } catch (e: ActivityNotFoundException) {
        }
    }

    /**
     * call a phone number
     *
     * @param context Context
     * @param uri     uri to call
     */
    @Throws(SecurityException::class)
    fun call(context: Context, uri: Uri?) {
        try {
            val callIntent =
                Intent(Intent.ACTION_CALL)
            callIntent.data = uri
            context.startActivity(callIntent)
        } catch (e: ActivityNotFoundException) {
        }
    }
    /**
     * fire an intent to open an url in browser
     *
     * @param context    Context
     * @param url        String
     * @param resultCode Result code to resume activity or fragment
     */
    /**
     * fire an intent to open an url in browser
     *
     * @param context Context
     * @param url     String
     */
    @JvmOverloads
    fun openURL(
        context: Context,
        url: String?,
        resultCode: Int = -1
    ) {
        try {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
            //            if (resultCode > -1 && context instanceof Activity) {
//                ((Activity) context).startActivityForResult(browserIntent, resultCode);
//            } else {
            context.startActivity(browserIntent)
            //            }
        } catch (e: ActivityNotFoundException) {
        }
    }

    /**
     * hides the soft keyboard
     *
     * @param activity activity
     */
    fun hideSoftKeyboard(activity: Activity?) {
        if (activity == null || activity.currentFocus == null) {
            return
        }
        val inputMethodManager = activity
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

    /**
     * hides the soft keyboard for fragments
     *
     * @param activity activity
     * @param view     view
     */
    fun hideKeyboard(activity: Activity?, view: View?) {
        if (activity == null || view == null) {
            return
        }
        val inputMethodManager = activity
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Show soft input
     *
     * @param view view to focus on for input
     */
    fun showKeyboard(view: View?) {
        if (view == null) {
            return
        }
        view.requestFocus()
        val inputMethodManager =
            view.context.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            0
        )
    }

    /**
     * Show soft input
     *
     * @param dialog dialog to focus on for input
     */
    fun showKeyboard(dialog: Dialog?) {
        if (dialog == null) {
            return
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    /**
     * sends an mail intent for empty mail
     *
     * @param context     [Context]
     * @param mailAddress recipient
     */
    fun sendPlainEmail(context: Context, mailAddress: String?) {
        try {
            val intent =
                Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(mailAddress ?: "")
            )
            context.startActivity(Intent.createChooser(intent, ""))
        } catch (e: ActivityNotFoundException) {
        }
    }

    /**
     * sends an mail intent for empty mail with attachment
     *
     * @param context     [Context]
     * @param mailAddress recipient
     * @param filePath    path to file
     */
    fun sendEmailWithAttachment(
        context: Context,
        mailAddress: String?,
        filePath: String?
    ) {
        var file: File? = null
        if (filePath != null && filePath != "") {
            file = File(filePath)
        }
        try {
            val intent =
                Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(mailAddress ?: "")
            )
            if (file != null) {
                intent.putExtra(
                    Intent.EXTRA_STREAM,
                    getFileProviderUri(context, file)
                )
            }
            context.startActivity(Intent.createChooser(intent, ""))
        } catch (e: ActivityNotFoundException) {
        }
    }

    /**
     * sends an mail intent for filled mail
     *
     * @param context      [Context]
     * @param subject      mail subject
     * @param content      mail content
     * @param chooserTitle title of chooser
     * @param recipients   array of recipients
     */
    fun sendEmail(
        context: Context, subject: String?, content: String?,
        chooserTitle: String?, recipients: Array<String?>?
    ) {
        try {
            val intent =
                Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, content)
            context.startActivity(Intent.createChooser(intent, chooserTitle))
        } catch (e: ActivityNotFoundException) {
        }
    }

    /**
     * open share dialog to share a file with an app
     *
     * @param activity [Context]
     * @param filePath path to file
     */
    fun shareFile(activity: Activity, filePath: String?) {
        if (filePath != null && filePath != "") { // start share intent
            val file = File(filePath)
            shareFile(activity, file)
        }
    }

    /**
     * open share dialog to share a file with an app
     *
     * @param fragment [Context]
     * @param filePath path to file
     */
    fun shareFileForResult(
        fragment: Fragment,
        filePath: String?,
        requestCode: Int
    ) {
        if (filePath != null && filePath != "") { // start share intent
            val file = File(filePath)
            shareFileForResult(fragment, file, requestCode)
        }
    }

    /**
     * open share dialog to share a file with an app
     *
     * @param fragment [Context]
     * @param file     file
     */
    fun shareFileForResult(
        fragment: Fragment,
        file: File,
        requestCode: Int
    ) {
        if (!file.exists()) {
            return
        }
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(
            Intent.EXTRA_STREAM,
            getFileProviderUri(fragment.requireContext(), file)
        )
        shareIntent.type = "*/*"
        fragment.startActivityForResult(
            Intent.createChooser(shareIntent, ""),
            requestCode
        )
    }

    /**
     * open share dialog to share a file with an app
     *
     * @param activity [Context]
     * @param file     file
     */
    fun shareFile(activity: Activity, file: File) {
        if (!file.exists()) {
            return
        }
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(
            Intent.EXTRA_STREAM,
            getFileProviderUri(activity, file)
        )
        shareIntent.type = "*/*"
        activity.startActivity(Intent.createChooser(shareIntent, ""))
    }

    /**
     * decides if app is installed on users device
     *
     * @param context Context
     * @param uri     uri to package of app
     * @return installed or not
     */
    fun appInstalledOrNot(context: Context, uri: String): Boolean {
        val pm = context.packageManager
        val isAppInstalled: Boolean
        isAppInstalled = try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return isAppInstalled
    }

    /**
     * starts an activity by uri of main package
     *
     * @param context    ctx
     * @param packageUri main package for searching launch intent
     */
    fun startActivityByPackageUri(
        context: Context,
        packageUri: String?
    ) {
        val pm = context.packageManager
        val intent = pm.getLaunchIntentForPackage(packageUri!!)
        context.startActivity(intent)
    }

    /**
     * open an app in play store
     *
     * @param context    ctx
     * @param packageUri package for target app
     */
    @JvmStatic
    fun startPlayStoreByApplicationPackageUri(
        context: Context,
        packageUri: String
    ) {
        context.startActivity(getPlayStoreByApplicationPackageUri(context, packageUri))
    }

    /**
     * return the intent to open an app in play store
     *
     * @param context    ctx
     * @param packageUri package for target app
     * @return intent to launch
     */
    fun getPlayStoreByApplicationPackageUri(
        context: Context?,
        packageUri: String
    ): Intent {
        val intent =
            Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=$packageUri")
        return intent
    }

    /**
     * Go to the bluetooth settings / pairing dialog.
     *
     * @param activity FragmentActivity
     */
    fun goToPairingDialog(activity: FragmentActivity) {
        val showDevicesIntent =
            Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
        activity.startActivityForResult(showDevicesIntent, REQUEST_CODE_COUPLE_BT)
    }

    /**
     * open an app in play store by http uri
     *
     * @param context    ctx
     * @param packageUri main package for searching app
     */
    fun startPlayStoreByHttpUri(
        context: Context,
        packageUri: String
    ) {
        val intent =
            Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(GOOGLE_PLAY_STORE_APPS_URL + "details?id=" + packageUri)
        context.startActivity(intent)
    }

    /**
     * resolves the package name of an app
     *
     * @param context Context
     * @return package name as string
     */
    fun resolvePackageName(context: Context): String {
        val intent =
            Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val resolveInfo = context.packageManager
            .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolveInfo?.activityInfo?.packageName.orEmpty()
    }

    /**
     * starts the dealer app if installed on device
     *
     * @param context    Context
     * @param packageUri uri to package of dealers app
     */
    fun startAppOrStore(context: Context, packageUri: String) {
        if (appInstalledOrNot(context, packageUri)) { // app installed
            startActivityByPackageUri(context, packageUri)
            // app not installed
        } else {
            try {
                startPlayStoreByApplicationPackageUri(context, packageUri)
            } catch (e: ActivityNotFoundException) {
            }
        }
    }

    /**
     * @param context Context
     * @return the package info code of the app. if the package name can not be resolved, which should never happen in our
     * case since we use `getPackageName()`
     */
    fun readApplicationPackageInfo(context: Context): PackageInfo? {
        try {
            return context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return null
    }

    /**
     * start camera app via intent with specific result code
     *
     * @param activity   Activity
     * @param resultCode int
     */
    fun startOpenCameraExternalDisk(activity: Activity, resultCode: Int) {
        val i = Intent(
            MediaStore.ACTION_IMAGE_CAPTURE,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(i, resultCode)
    }

    /**
     * start camera app via intent with specific result code
     *
     * @param activity   Activity
     * @param resultCode int
     */
    fun startOpenCamera(activity: Activity, resultCode: Int) {
        val takePictureIntent =
            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity.startActivityForResult(takePictureIntent, resultCode)
    }

    /**
     * Let the user activate bluetooth.
     *
     * @param activity base view
     */
    fun gotoEnableBluetoothDialog(activity: Activity) {
        val enableIntent =
            Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        activity.startActivityForResult(enableIntent, REQUEST_CODE_ENABLE_BT)
    }

    /**
     * check if location settings are enabled in os
     *
     * @param context a Context.
     * @return location settings are enabled in os
     */
    fun isLocationSettingEnabled(context: Context): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled =
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val networkEnabled =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gpsEnabled || networkEnabled
    }

    /**
     * is device known in system devices list
     *
     * @param deviceName name of device
     * @return is known or not
     */
    fun isBtAdapterPaired(deviceName: String): Boolean {
        val bluetoothAdapter =
            BluetoothAdapter.getDefaultAdapter()
        val pairedDevices =
            bluetoothAdapter.bondedDevices
        for (device in pairedDevices) {
            if (device.name == deviceName) {
                return true
            }
        }
        return false
    }

    /**
     * device has a camera?
     *
     * @param ctx context
     * @return true or false
     */
    fun hasCamera(ctx: Context): Boolean {
        return ctx.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    /**
     * starts android image gallery with specific result code
     *
     * @param fragment   Fragment
     * @param resultCode int
     */
    fun startPickImageFromGallery(
        fragment: Fragment,
        resultCode: Int
    ) {
        val i = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        fragment.startActivityForResult(i, resultCode)
    }

    /**
     * starts android image gallery with specific result code
     *
     * @param activity   Activity
     * @param resultCode int
     */
    fun startPickImageFromGallery(activity: Activity, resultCode: Int) {
        val intent = createPickImageFromGalleryIntent()
        activity.startActivityForResult(intent, resultCode)
    }

    /**
     * starts android image gallery with specific result code
     *
     * @param fragment   Activity
     * @param resultCode int
     */
    fun startPickImageFromGallery(fragment: android.app.Fragment, resultCode: Int) {
        val intent = createPickImageFromGalleryIntent()
        fragment.startActivityForResult(intent, resultCode)
    }

    /**
     * check if device can crop images
     *
     * @param context [Context]
     * @return true or false
     */
    fun hasImageCropFunctionality(context: Context): Boolean {
        val intent =
            Intent("com.android.camera.action.CROP")
        intent.type = MIME_TYPE_IMAGE_ALL
        val list =
            context.packageManager.queryIntentActivities(intent, 0)
        return list.size >= 1
    }

    /**
     * Starts an intent to crop a photo and returns the bitmap in extras
     * <P></P>
     * Source uri in content uri format may description
     *
     * @param resultCode the result code
     * @param sourceUri  sourceUri as content uri
     * @param aspectX    dimen for x
     * @param aspectY    dimen for y
     * @param outputX    width in pixel
     * @param outputY    height in pixel
     */
    fun startCropImage(
        activity: Activity,
        resultCode: Int,
        sourceUri: Uri,
        destinationUri: Uri,
        aspectX: Int,
        aspectY: Int,
        outputX: Int,
        outputY: Int
    ) {
        var contentUri: Uri? = null
        var cursor: Cursor? = null
        try {
            val projection = arrayOf(
                BaseColumns._ID,
                MediaStore.Images.Media.DATA
            )
            cursor = activity.contentResolver.query(sourceUri, projection, null, null, null)
            if (cursor == null) {
                return
            }
            if (!cursor.moveToFirst()) {
                return
            }
            val id = cursor.getLong(0)
            contentUri =
                Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + File.separator + id.toString())
        } catch (e: Exception) {
        } finally {
            cursor?.close()
        }
        val cropIntent = createCropIntent(
            activity, contentUri ?: sourceUri, destinationUri,
            aspectX, aspectY, outputX, outputY
        )
        grantPermissionsForIntent(activity, cropIntent, destinationUri)
        try {
            activity.startActivityForResult(cropIntent, resultCode)
        } catch (exception: SecurityException) {
        }
    }

    /**
     * Starts an intent to crop a photo and returns the bitmap in extras
     * <P></P>
     * Source uri in content uri format may description
     *
     * @param sourceUri sourceUri as content uri
     * @param aspectX   dimen for x
     * @param aspectY   dimen for y
     * @param outputX   width in pixel
     * @param outputY   height in pixel
     */
    fun createCropImageIntent(
        context: Context,
        sourceUri: Uri,
        destinationUri: Uri,
        aspectX: Int,
        aspectY: Int,
        outputX: Int,
        outputY: Int
    ): Intent? {
        var contentUri: Uri? = null
        var cursor: Cursor? = null
        try {
            val projection = arrayOf(
                BaseColumns._ID,
                MediaStore.Images.Media.DATA
            )
            cursor = context.contentResolver.query(sourceUri, projection, null, null, null)
            if (cursor == null) {
                return null
            }
            if (!cursor.moveToFirst()) {
                return null
            }
            val id = cursor.getLong(0)
            contentUri =
                Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + File.separator + id.toString())
        } catch (e: Exception) {
        } finally {
            cursor?.close()
        }
        val cropIntent = createCropIntent(
            context, contentUri ?: sourceUri, destinationUri,
            aspectX, aspectY, outputX, outputY
        )
        grantPermissionsForIntent(context, cropIntent, destinationUri)
        return cropIntent
    }

    /**
     * Starts an intent to crop a photo and returns the bitmap in extras
     *
     * @param resultCode     the result code
     * @param sourceImageUri the image uri
     * @param aspectX        dimen for x
     * @param aspectY        dimen for y
     * @param outputX        width in pixel
     * @param outputY        height in pixel
     */
    fun startCropImage(
        fragment: Fragment, resultCode: Int, sourceImageUri: Uri,
        destinationImageUri: Uri, aspectX: Int, aspectY: Int, outputX: Int, outputY: Int
    ) {
        val cropIntent = createCropIntent(
            fragment.activity, sourceImageUri, destinationImageUri, aspectX,
            aspectY, outputX, outputY
        )
        grantPermissionsForIntent(
            fragment.activity,
            cropIntent,
            destinationImageUri
        )
        fragment.startActivityForResult(cropIntent, resultCode)
    }

    /**
     * Create and start a service intent
     *
     * @param context      a context
     * @param serviceClass Class type for service intent
     * @param intentAction Intent action
     */
    fun startServiceIntent(
        context: Context,
        serviceClass: Class<out IntentService?>?,
        intentAction: String?
    ) {
        val serviceIntent = Intent(context, serviceClass)
        serviceIntent.action = intentAction
        context.startService(serviceIntent)
    }

    /**
     * Create and start a service intent
     *
     * @param context        a context
     * @param broadcastClass Class type for broadcast intent
     */
    fun sendBroadcastByIntent(
        context: Context,
        broadcastClass: Class<out BroadcastReceiver?>?,
        sourceIntent: Intent
    ) {
        val serviceIntent = Intent(context, broadcastClass)
        serviceIntent.action = sourceIntent.action
        context.sendBroadcast(serviceIntent)
    }

    /**
     * openes the app details screen
     *
     * @param context a Context.
     */
    fun startInstalledAppDetailsActivity(context: Activity?) {
        if (context == null) {
            return
        }
        val i = Intent()
        i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.data = Uri.parse("package:" + context.packageName)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        context.startActivity(i)
    }
    /**
     * Start an activity and clear back stack
     *
     * @param context             [Activity]
     * @param targetActivityClass [Class]
     * @param extras              Bundle with extras
     */
    /**
     * Start an activity and clear back stack
     *
     * @param context             [Activity]
     * @param targetActivityClass [Class]
     */
    @JvmOverloads
    fun startTopLevelActivity(
        context: Context,
        targetActivityClass: Class<*>?,
        extras: Bundle? = null
    ) {
        val intent = Intent(context, targetActivityClass)
        if (extras != null) {
            intent.putExtras(extras)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * grant FLAG_GRANT_WRITE_URI_PERMISSION and Intent.FLAG_GRANT_READ_URI_PERMISSION to allow writing and reading the FileProvider uri
     *
     * @param ctx    a Context
     * @param intent intent to filter possible destination apps
     * @param uri    uri to grant
     */
    fun grantPermissionsForIntent(
        ctx: Context?,
        intent: Intent,
        uri: Uri?
    ) {
        val resInfoList =
            ctx!!.packageManager
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            ctx.grantUriPermission(
                packageName, uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }

    /**
     * @return A bundle with information to display main activity on back pressed
     */
    fun createBackToMainBundle(): Bundle {
        val bundle = Bundle()
        bundle.putBoolean(KEY_EXTRA_BACK_TO_MAIN, true)
        return bundle
    }

    /**
     * Kill application process
     */
    fun closeApplication() {
        Process.killProcess(Process.myPid())
    }

    /**
     * check if call is active currently
     *
     * @param context a Context
     * @return true or false
     */
    fun isCallActive(context: Context): Boolean {
        val manager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return manager.callState != TelephonyManager.CALL_STATE_IDLE
    }

    /**
     * Replaces the last six digits of a given vin with 0
     *
     * @return anonymized vin
     */
    private fun anonymizeVin(vin: String?): String? {
        return if (vin != null && vin.length == 17) {
            vin.substring(0, vin.length - 6) + "000000"
        } else vin
    }

    /**
     * Replaces the last three digits of a given obd adapter id with 0
     *
     * @return anonymized obd adapter ID
     */
    private fun anonymizeObdAdapterId(obdAdapterId: String?): String? {
        return if (obdAdapterId != null && obdAdapterId.length == 10) {
            obdAdapterId.substring(0, obdAdapterId.length - 3) + "000"
        } else obdAdapterId
    }

    /**
     * Starts system screen with push-notifications settings
     *
     * @param context - activity context
     */
    fun startNotificationPreferences(context: Context) {
        val intent = Intent()
        val packageName = context.packageName
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", packageName)
            intent.putExtra("app_uid", context.applicationInfo.uid)
        } else {
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse("package:$packageName")
        }
        context.startActivity(intent)
    }

    fun showKeyboard(
        context: Context,
        textView: TextView
    ) {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT)
    }

    fun createPickImageFromGalleryIntent(): Intent {
        return Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
    }

    /**
     * Create a crop intent
     *
     * @param sourceImageUri      uri of image to crop
     * @param destinationImageUri the image uri
     * @param aspectX             dimen for x
     * @param aspectY             dimen for y
     * @param outputX             width in pixel
     * @param outputY             height in pixel
     * @return crop intent
     */
    private fun createCropIntent(
        ctx: Context?,
        sourceImageUri: Uri,
        destinationImageUri: Uri,
        aspectX: Int,
        aspectY: Int,
        outputX: Int,
        outputY: Int
    ): Intent {
        val intent =
            Intent("com.android.camera.action.CROP")
        // indicate image type and Uri
        intent.setDataAndType(sourceImageUri, ctx!!.contentResolver.getType(sourceImageUri))
        // set crop properties
        intent.putExtra(PHOTO_EXTRA_CROP, "true")
        // indicate aspect of desired crop
        if (aspectX > 0) {
            intent.putExtra(PHOTO_EXTRA_ASPECT_X, aspectX)
        }
        if (aspectY > 0) {
            intent.putExtra(PHOTO_EXTRA_ASPECT_Y, aspectY)
        }
        // indicate output X and Y
        if (outputX != -1 && outputY != -1) {
            intent.putExtra(PHOTO_EXTRA_OUTPUT_X, outputX)
            intent.putExtra(PHOTO_EXTRA_OUTPUT_Y, outputY)
        }
        //intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, destinationImageUri)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        return intent
    }
}