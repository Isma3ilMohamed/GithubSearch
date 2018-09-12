package com.thedevwolf.githubsearch.ui.Activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import butterknife.ButterKnife
import butterknife.OnTextChanged
import com.jakewharton.rxbinding2.widget.RxTextView
import com.thedevwolf.githubsearch.R
import com.thedevwolf.githubsearch.ViewModel.SearchViewModel
import com.thedevwolf.githubsearch.ui.Adapter.SearchAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: SearchAdapter
    private val compositeDisposable = CompositeDisposable()
    private lateinit var searchViewModel: SearchViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        searchViewModel = ViewModelProviders.of(this)
                .get(SearchViewModel::class.java)
        adapter = SearchAdapter()


        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter


        initAdapter()

    }

    @OnTextChanged(R.id.search_repo,callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected fun initSearch() {

        compositeDisposable.add(RxTextView.textChangeEvents(search_repo)
                .skipInitialValue()
                .debounce(1000, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t!!.text().isEmpty()) {
                        list.visibility = View.GONE
                        enterToSearch.visibility = View.VISIBLE
                    } else {
                        updateRepoListFromInput()

                    }
                }) {
                    Log.e("TAG", it.printStackTrace().toString())
                })
    }



    private fun updateRepoListFromInput() {


        //start new search
        search_repo.text.trim().let {
            if (it.isNotEmpty()) {
                list.scrollToPosition(0)
                searchViewModel.searchRepos(it.toString())
                adapter.submitList(null)
            }
        }
    }

    private fun initAdapter() {

        searchViewModel.repos!!.observe(this, Observer {
            showEmptyList(it?.size==0)
            adapter.submitList(it!!)
        })
    }

    private fun showEmptyList(b:Boolean){
        if(b){
            list.visibility = View.GONE
            emptyList.visibility = View.VISIBLE
        }else{
            list.visibility = View.VISIBLE
            enterToSearch.visibility = View.GONE
            emptyList.visibility = View.GONE
        }
    }

    companion object {
       val LAST_SEARCH="last_search"
        private const val DEFAULT_QUERY = "Android"

    }
}
