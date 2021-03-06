package com.colin.shelf.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.colin.shelf.BR
import com.colin.shelf.R
import com.colin.shelf.databinding.FragmentShelfLayoutBinding
import com.colin.shelf.mvvm.model.ShelfModel
import com.colin.shelf.mvvm.viewmodel.ShelfViewModel
import com.colin.shelf.ui.adapter.ShelfAdapter
import com.colin.shelf.widget.RecycleViewDivider
import com.ifenghui.commonlibrary.base.ui.fragment.BaseFragment
import com.ifenghui.commonlibrary.utils.RecyclerViewManagerUtils

class ShelfFragment: BaseFragment<FragmentShelfLayoutBinding, ShelfViewModel>() {

    override fun onBindLayout(): Int {
       return R.layout.fragment_shelf_layout
    }

    override fun onBindViewModel(): Class<ShelfViewModel> {
        return ShelfViewModel::class.java
    }

    override fun onBindVariableId(): Int {
        return BR.shelfViewModel
    }

    override fun <T : ViewModel> createViewModel(modelClass: Class<T>): T {
        return mActivity()?.application?.let { ShelfViewModel(it, ShelfModel(mActivity()?.application!!)) } as T
    }

    companion object {
        private var INSTANCE:ShelfFragment?=null
        fun newInstance(): Fragment? {
            if (INSTANCE == null) {
                synchronized(ShelfFragment::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = ShelfFragment()
                    }
                }
            }
            return INSTANCE ?: ShelfFragment()
        }
    }

    private var shelfAdapter:ShelfAdapter?=null

    /**
     * 初始化书架
     */
    override fun onCreateDelay(bundle: Bundle?) {

        RecyclerViewManagerUtils.setGridLayoutManager(mBinding?.recyclerView, mActivity(), 2)
        mBinding?.recyclerView?.addItemDecoration(object :RecycleViewDivider(mActivity(),0,R.mipmap.bookcase_bg,0,0){})
        shelfAdapter = ShelfAdapter(mActivity())
        mViewModel?.listData?.observe(this, Observer {
            shelfAdapter?.setDatas(mViewModel?.listData?.value)
        })
        mBinding?.recyclerView?.adapter = shelfAdapter
        mViewModel?.getShelfData(1,true)
    }


    override fun onReloadData() {
        super.onReloadData()
        mViewModel?.getShelfData(1,true)
    }


    override fun refreshMainData() {
        super.refreshMainData()
        shelfAdapter?.notifyDataSetChanged()
    }
}