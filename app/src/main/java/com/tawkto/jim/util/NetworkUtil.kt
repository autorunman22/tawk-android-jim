package com.tawkto.jim.util
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission

class NetworkUtil
@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
constructor(private val context: Context, private val netCallback: NetCallback) {

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            netCallback.onOnline()
        }

        override fun onLost(network: Network) {
            netCallback.onOffline()
        }
    }

    fun startNetworkCallback() {
        val cm: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()

        /**
         * Check if version code is greater than API 24
         * */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            cm.registerDefaultNetworkCallback(networkCallback)
        } else {
            cm.registerNetworkCallback(builder.build(), networkCallback)
        }
    }

    fun stopNetworkCallback() {
        val cm: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
    }


}