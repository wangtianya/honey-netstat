package com.qjuzi.qnet.pages.home.presenter

import android.graphics.Color
import android.text.TextUtils
import com.qjuzi.qnet.R
import com.qjuzi.qnet.common.broadcast.MyNetworkReceiver
import com.qjuzi.qnet.common.broadcast.NetworkChangedListener
import com.qjuzi.qnet.common.tools.thread.ThreadUtil
import com.qjuzi.qnet.common.tools.util.ScreenManager
import com.qjuzi.qnet.pages.home.HomeHelper
import com.qjuzi.qnet.pages.home.HomeModel
import com.qjuzi.yaa.core.context.YaaContext
import com.qjuzi.yaa.core.util.ScreenUtil
import com.qjuzi.yaa.databinding.BaseRecycleViewHeaderFooterAdapter
import com.qjuzi.yaa.net.ping2.PingTaskFactory
import com.qjuzi.yaa.net.ping2.inteface.PingListener
import com.qjuzi.yaa.net.ping2.inteface.PingResult
import com.qjuzi.yaa.net.ping2.inteface.PingRow
import com.qjuzi.yaa.net.ping2.inteface.PingTask
import com.qjuzi.yaa.net.traffic.CurrentTrafficStats

class DelayTaskPresenter(val homeModel: HomeModel) {

    private var pingTask: PingTask? = null

    fun startDelayDataUpdateTask() {
        ThreadUtil.runOnNotUI(Runnable { startDelayDataUpdate() })
    }

    fun stopDelayDataUpdateTask() {
        pingTask?.stop()
    }

    private fun startDelayDataUpdate() {
        stopDelayDataUpdateTask()
        pingTask = PingTaskFactory.newOne("www.baidu.com", object : PingListener {
            var i: Int = 0
            override fun onStart(row: PingRow) {
                homeModel.statusColor.set(Color.GRAY)
                homeModel.delay.set("-")
            }

            override fun onProgress(row: PingRow) {
                if (!MyNetworkReceiver.isAvailable || i++ % 2 == 0) return
                homeModel.delay.set(HomeHelper.getDelayStr(row.time))
                homeModel.statusColor.set(HomeHelper.getStatusColorByDelay(row.time))
            }

            override fun onFinish(result: PingResult) {
                homeModel.delay.set("-")
                try {
                    Thread.sleep(1500)
                } finally {
                    startDelayDataUpdate()
                }
            }
        })
        pingTask?.start()
    }
}