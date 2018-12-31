package kr.co.first2000.chkconnectionasping

import android.os.Handler
import android.util.Log
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

//    0: 네트워크 정상, 1: 네트워크 비정상.
const val NETWORK_STATE_FINE: Int = 0
const val NETWORK_STATE_WRONG: Int = 1

class InternetStateMonitor: Thread {

    private val TAG: String = "INTERNET_MONITOR => "
    private val handler: Handler
    private var StillChecking = false

//    전에보낸 Message판독.
//    Wrong를 보냈으면 false
//    Fine을 보냈으면 true
//    처음시작시 null
    private var switcher: Boolean? = null

    constructor(handler: Handler) {
        this.handler = handler
        this.StillChecking = true
    }

    fun EndMonitoring() {
        this.StillChecking = false
    }

    override fun run() {
        super.run()

//        소켓과 구글DNS서버주소 지정.
        val sockAddr = InetSocketAddress("8.8.8.8", 53)

        while (StillChecking) {
            try {
//                ping 요청 후 TIMEOUT: 1500 을 지날시 IOException/InterruptedIOException/SocketTimeoutException 발생
                val soc = Socket()
                soc.connect(sockAddr, 500)
                soc.close()
                if (!switcher!! || switcher == null) {
                    handler.sendEmptyMessage(NETWORK_STATE_FINE)
                    switcher = true
                }
            } catch (ie: IOException) {
                if (switcher!! || switcher == null) {
                    handler.sendEmptyMessage(NETWORK_STATE_WRONG)
                    switcher = false
                }
            }
//            3s 텀
            sleep(1000)
        }

    }
    
}