package com.arvifox.arvi.simplemisc.phoneinfo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.appcompat.app.AppCompatActivity
import android.util.DisplayMetrics
import com.arvifox.arvi.R
import com.arvifox.arvi.utils.AndroidStorage
import kotlinx.android.synthetic.main.activity_phone_info.*


class PhoneInfoActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, PhoneInfoActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_info)
        tvPhoneInfo.text = composeProperties()
        btnStorage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "*/*"
            startActivityForResult(intent, 452)
            with(AndroidStorage) {
                internal(this@PhoneInfoActivity)
                pictureDirPublic()
                getPrivateAlbumStorageDir(this@PhoneInfoActivity)
                tvPathInfo.text = getPathInfo(this@PhoneInfoActivity)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 452 && resultCode == Activity.RESULT_OK) {
            val returnCursor = contentResolver.query(data?.data!!, null, null, null, null)
            val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
            returnCursor.moveToFirst()

            tvIntentInfo.text = "action=" + data?.action + "\n" +
                    "package=" + data?.`package` + "\n" +
                    "type=" + data?.type + "\n" +
                    "scheme=" + data?.scheme + "\n" +
                    "data=" + data?.dataString + "\n" +
                    "uri host=" + data?.data?.host + "\n" +
                    "uri authority=" + data?.data?.authority + "\n" +
                    "uri path=" + data?.data?.path + "\n" +
                    "uri query=" + data?.data?.query + "\n" +
                    "uri scheme=" + data?.data?.scheme + "\n" +
                    "uri userInfo=" + data?.data?.userInfo + "\n" +
                    "uri resolver data type MIME=" + contentResolver.getType(data?.data!!) + "\n" +
                    "name = " + returnCursor.getString(nameIndex) + "\n" +
                    "size = " + returnCursor.getLong(sizeIndex) + "\n"

            // also we can read from URI
            val inputStream = contentResolver.openInputStream(data?.data!!)
            returnCursor.close()
        }
    }

    private fun composeProperties(): String {
        val sb = StringBuilder()
        sb.append("Manufacturer = ").append(Build.MANUFACTURER).append("\n")
                .append("Model = ").append(Build.MODEL).append("\n")
                .append("Version = ").append(Build.VERSION.RELEASE).append("\n")
                .append("API Level = ").append(Build.VERSION.SDK_INT).append("\n")
                .append("Architecture = ").append(Build.CPU_ABI).append("\n")
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        sb.append("Density = ").append(dm.density).append("\n")
                .append("Resolution = (").append(dm.widthPixels / dm.density).append("dp, ")
                .append(dm.heightPixels / dm.density).append("dp)\n")
        return sb.toString()
    }
}
