package com.thedevwolf.githubsearch.Data

import android.arch.paging.PageKeyedDataSource
import com.thedevwolf.githubsearch.Api.Api
import com.thedevwolf.githubsearch.model.Repo
import com.thedevwolf.githubsearch.model.RepoSearchResponse
import io.reactivex.disposables.CompositeDisposable

class SearchDataSource(val api: Api,
                       val compositeDisposable: CompositeDisposable,
                       val query: String) : PageKeyedDataSource<Int, Repo>() {

    companion object {
        //the size of a page that we want
        val PAGE_SIZE = 10
        //
        var PAGE = 1


    }


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Repo>) {
        compositeDisposable.add(
                api.searchRepos(query+Api.IN_QUALIFIER, PAGE, PAGE_SIZE)
                        .subscribe({ t: RepoSearchResponse? ->
                            callback.onResult(t!!.items, null, PAGE +1)
                        }, {

                        })

        )

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {
        compositeDisposable.add(
                api.searchRepos(query+Api.IN_QUALIFIER, params.key, PAGE_SIZE)
                        .subscribe({ t: RepoSearchResponse? ->
                            callback.onResult(t!!.items, params.key+1)
                        }, {

                        })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {

    }
}