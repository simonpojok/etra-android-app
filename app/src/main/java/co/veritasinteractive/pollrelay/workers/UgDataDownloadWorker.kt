package co.veritasinteractive.pollrelay.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import co.veritasinteractive.pollrelay.UG_DATA_URL
import co.veritasinteractive.pollrelay.data.PollRelayDatabase
import co.veritasinteractive.pollrelay.data.dao.UgDataDao
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class UgDataDownloadWorker(
    private val context: Context,
    workerParams: WorkerParameters): Worker(
    context,
    workerParams
) {
    private var ugDataDao: UgDataDao = PollRelayDatabase.getDatabase(context).ugDataDao()


    override fun doWork(): Result {
       val jsonRequest = JsonObjectRequest(
           Request.Method.GET,
           UG_DATA_URL,
           JSONObject(),
           {
                Log.d("Worker", "Response JSON")
           },
           {
               Log.d("Worker", "Response Error")
           }

       )
        val cache = DiskBasedCache(context.cacheDir, 1024 * 1024)
        val network = BasicNetwork(HurlStack())
        val requestQueue = RequestQueue(cache, network).apply {
            start()
        }
        requestQueue.add(jsonRequest)
        return Result.success()
    }
}