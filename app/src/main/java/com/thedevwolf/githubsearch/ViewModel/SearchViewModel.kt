package com.thedevwolf.githubsearch.ViewModel

import android.arch.lifecycle.*
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.thedevwolf.githubsearch.model.Repo
import android.arch.paging.PageKeyedDataSource
import com.thedevwolf.githubsearch.Api.Api
import com.thedevwolf.githubsearch.Data.SearchDataSource
import com.thedevwolf.githubsearch.Data.DataSourceFactory.SearchDataSourceFactory
import com.thedevwolf.githubsearch.model.RepoSearchResult
import io.reactivex.disposables.CompositeDisposable


class SearchViewModel:ViewModel(){
    var searchPagedList:LiveData<PagedList<Repo>> ?= null
    var searchDataSource: LiveData<PageKeyedDataSource<Int, Repo>>? = null

    //store search query
    private val queryLiveData = MutableLiveData<String>()


    //store search result
    private val repoResult: LiveData<RepoSearchResult> = Transformations.map(queryLiveData) {
        initSearch(it)
    }

    //cache search result
    val repos: LiveData<PagedList<Repo>> = Transformations.switchMap(repoResult
    ) { it -> it.data }


    val compositeDisposable=CompositeDisposable()




    fun initSearch(query: String): RepoSearchResult{
        val searchViewModelFactory=SearchDataSourceFactory(Api.create(),compositeDisposable,query)

        searchDataSource=searchViewModelFactory.searchLiveDataSource

        val config=PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(SearchDataSource.PAGE_SIZE)
                .setInitialLoadSizeHint(SearchDataSource.PAGE_SIZE*2)
                .build()


        //Building the paged list
        searchPagedList = LivePagedListBuilder<Int,Repo>(searchViewModelFactory,config)
                .setInitialLoadKey(0)
                .build()
        //return result
        return RepoSearchResult(searchPagedList!!)
    }



    //set new search
    fun searchRepos(query: String){
        queryLiveData.postValue(query)
    }

        //get last query
        fun lastQueryValue(): String? = queryLiveData.value


        fun replaceSubscription(lifecycleOwner: LifecycleOwner,query: String){
            searchPagedList!!.removeObservers(lifecycleOwner)
            initSearch(query)
        }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}