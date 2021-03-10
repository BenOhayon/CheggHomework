package homework.chegg.com.chegghomework.view.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import homework.chegg.com.chegghomework.R
import homework.chegg.com.chegghomework.model.Consts
import homework.chegg.com.chegghomework.model.DataModel
import homework.chegg.com.chegghomework.model.SourceProvider
import homework.chegg.com.chegghomework.model.cache.SourceACacheManager
import homework.chegg.com.chegghomework.model.cache.SourceBCacheManager
import homework.chegg.com.chegghomework.model.cache.SourceCCacheManager
import homework.chegg.com.chegghomework.model.managers.DialogManager
import homework.chegg.com.chegghomework.model.network.RequestBuilder
import homework.chegg.com.chegghomework.model.parsers.SourceAParser
import homework.chegg.com.chegghomework.model.parsers.SourceBParser
import homework.chegg.com.chegghomework.model.parsers.SourceCParser
import homework.chegg.com.chegghomework.view.adapters.DataModelsAdapter
import homework.chegg.com.chegghomework.viewmodel.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var toolbar: Toolbar? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var viewModel: MainActivityViewModel
    private var listAdapter: DataModelsAdapter? = null
    private var progressBar: View? = null
    private var sourceProviders: Map<String, SourceProvider> = mapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUI()
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.getReceivedLiveData().observe(this) { dataModels ->
            if (dataModels.isNotEmpty()) {
                progressBar?.visibility = View.GONE
                listAdapter?.setData(dataModels)
                startListItemAnimation()
            } else {
                progressBar?.visibility = View.GONE
                DialogManager.showRequestFailedDialog(this)
            }
        }

        viewModel.getCachedLiveData().observe(this) { dataModels ->
            viewModel.updateUI = true
            if (sourceProviders.isEmpty())
                sourceProviders = createTestData()

            val dataModelsToInclude = mutableListOf<DataModel>()
            dataModels.forEach {
                if (sourceProviders[it.staleType]?.cacheManager?.hasElapsedStaleTime(it)!!) {
                    viewModel.updateUI = false
                    viewModel.deleteData(it)
                } else
                    dataModelsToInclude.add(it)
            }

            if (dataModelsToInclude.isEmpty() || dataModels.isEmpty()) {
                progressBar?.visibility = View.GONE
            } else if (viewModel.updateUI) {
                progressBar?.visibility = View.GONE
                listAdapter?.setData(dataModels)
                startListItemAnimation()
            }
        }
    }

    private fun startListItemAnimation() {
        val layoutAnimationController = AnimationUtils.loadLayoutAnimation(recyclerView?.context, R.anim.recyclerview_layout_slide)
        recyclerView?.layoutAnimation = layoutAnimationController
        recyclerView?.adapter?.notifyDataSetChanged()
        recyclerView?.scheduleLayoutAnimation()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setSourceProviders(createTestData())
    }

    private fun setupToolbar() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    private fun buildUI() {
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.mainActivityProgressBar)
        setupToolbar()
        setupList()
    }

    private fun setupList() {
        recyclerView = findViewById<View>(R.id.my_recycler_view) as RecyclerView
        listAdapter = DataModelsAdapter()
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = listAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                onRefreshData()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // TODO fetch data from all data sources, aggregate data and display in RecyclerView
    private fun onRefreshData() {
        progressBar?.visibility = View.VISIBLE
        sourceProviders = createTestData()
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getData(sourceProviders)
        }
    }

    private fun createTestData() : Map<String, SourceProvider> {
        val sourceACacheSaver = SourceACacheManager()
        val sourceBCacheSaver = SourceBCacheManager()
        val sourceCCacheSaver = SourceCCacheManager()
        val sourceAProvider = SourceProvider(RequestBuilder(Consts.DATA_SOURCE_A_URL, SourceAParser()), sourceACacheSaver)
        val sourceBProvider = SourceProvider(RequestBuilder(Consts.DATA_SOURCE_B_URL, SourceBParser()), sourceBCacheSaver)
        val sourceCProvider = SourceProvider(RequestBuilder(Consts.DATA_SOURCE_C_URL, SourceCParser()), sourceCCacheSaver)

        return mapOf(
                sourceAProvider.cacheManager.staleType to sourceAProvider,
                sourceBProvider.cacheManager.staleType to sourceBProvider,
                sourceCProvider.cacheManager.staleType to sourceCProvider
        )
    }
}