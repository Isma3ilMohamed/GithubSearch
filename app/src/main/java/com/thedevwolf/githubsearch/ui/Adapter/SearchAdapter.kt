package com.thedevwolf.githubsearch.ui.Adapter

import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.content.Intent
import android.net.Uri
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thedevwolf.githubsearch.R
import com.thedevwolf.githubsearch.model.Repo
import kotlinx.android.synthetic.main.repo_view_item.view.*

class SearchAdapter:PagedListAdapter<Repo,SearchAdapter.ViewHolder>(REPO_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.repo_view_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindItem(getItem(position))
        holder.itemView.setOnClickListener {
            getItem(position)?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                holder.itemView.context.startActivity(intent)
            }
        }

    }

    override fun getCurrentList(): PagedList<Repo>? {
        return super.getCurrentList()
    }



    fun clear(){
        if(currentList!!.size>0){

            notifyItemRangeRemoved(0,currentList!!.size-1)
        }

    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bindItem(item: Repo?) {
            if (item!=null){
               populateData(item)
            }else{
                val resource=itemView.resources

                itemView.repo_name.text=resource.getString(R.string.loading)
                itemView.repo_description.visibility=View.GONE
                itemView.repo_language.visibility=View.GONE
                itemView.repo_stars.text=resource.getString(R.string.unknown)
                itemView.repo_forks.text=resource.getString(R.string.unknown)
            }

        }

        private fun populateData(item: Repo?) {
            itemView.repo_name.text=item!!.name

            if (item.description!=null){
                itemView.repo_description.text=item!!.description
            }else{
                itemView.repo_description.visibility=View.GONE
            }

            if (item.language!=null){
                itemView.repo_language.text=item!!.language
            }else{
                itemView.repo_language.visibility=View.GONE
            }

            itemView.repo_stars.text=item!!.stars.toString()
            itemView.repo_forks.text=item!!.forks.toString()
        }

    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                    oldItem.fullName == newItem.fullName

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                    oldItem == newItem
        }
    }
}