package com.thedevwolf.githubsearch.Data.DataSourceFactory

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.PageKeyedDataSource
import com.thedevwolf.githubsearch.Api.Api
import com.thedevwolf.githubsearch.Data.SearchDataSource
import com.thedevwolf.githubsearch.model.Repo
import io.reactivex.disposables.CompositeDisposable

class SearchDataSourceFactory(val api: Api,val compositeDisposable: CompositeDisposable,val query:String):DataSource.Factory<Int,Repo>() {
     val searchLiveDataSource:MutableLiveData<PageKeyedDataSource<Int,Repo>> = MutableLiveData()


    override fun create(): DataSource<Int, Repo> {
        val searchDataSource= SearchDataSource(api, compositeDisposable, query)

        searchLiveDataSource.postValue(searchDataSource)

        return searchDataSource
    }


}