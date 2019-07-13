package com.sillylife.dd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.sillylife.dd.DDApplication
import com.sillylife.dd.R
import com.sillylife.dd.adapters.HomeAdapter
import com.sillylife.dd.database.entities.MixedItemDataEntity
import com.sillylife.dd.database.repo.MixedItemDataRepo
import com.sillylife.dd.modal.MixedDataItem
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    companion object {
        val TAG = HomeFragment::class.java.simpleName
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()

        val deepLinkRepo = MixedItemDataRepo(DDApplication.getInstance())
        deepLinkRepo.insert(MixedItemDataEntity(0,"SHub","sfef"))
    }


    private fun setAdapter() {
        val adapter = HomeAdapter(activity!!, getDummyData()) { any, pos ->
            if (any is MixedDataItem) {
                showToast(any.title!!, Toast.LENGTH_SHORT)
            }
        }
        rcv?.layoutManager = LinearLayoutManager(context)
        //rrcv?.setHasFixedSize(true)
        //rcv?.addItemDecoration(DividerItemDecorator(ContextCompat.getDrawable(context, R.drawable.separator)))
        rcv?.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun getDummyData(): ArrayList<MixedDataItem> {
        val list = ArrayList<MixedDataItem>()
        list.add(MixedDataItem("SHubham", "saf"))
        list.add(MixedDataItem("SHubfasham", "safasf"))
        list.add(MixedDataItem("SHubasfham", "sfasaf"))
        list.add(MixedDataItem("asfSHubham", "sfaaf"))
        return list
    }
}