package kr.co.first2000.chkconnectionasping

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var IMonitor: InternetStateMonitor
    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        handler = Handler {msg ->
            when(msg.what) {
//                네트워크 연결손실상태에서 연결됨이 확인되면.
                NETWORK_STATE_FINE -> {
                    NetworkTxt.text = "네트워크연결됨."
                    NetworkImg.setImageResource(R.drawable.ic_network_good)
                    return@Handler true
                }
//                네트워크 연결됨상태에서 연결손실이 확인되면.
                NETWORK_STATE_WRONG -> {
                    NetworkTxt.text = "네트워크연결손실."
                    NetworkImg.setImageResource(R.drawable.ic_network_lost)
                    return@Handler true
                }
                else ->
                    return@Handler false
            }
        }
        IMonitor = InternetStateMonitor(handler)
        IMonitor.isDaemon = true
        IMonitor.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        IMonitor.EndMonitoring()
    }
}
