package com.android.adminportal.ui.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import com.android.adminportal.R

abstract class BaseActivity<T : ViewDataBinding, V : ViewModel> : AppCompatActivity() {

    lateinit var viewDataBinding: T
    lateinit var mViewModel: V

    abstract fun setBindingVariable(): Int

    @LayoutRes
    abstract fun setLayoutId(): Int

    abstract fun setViewModel(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, setLayoutId())
        mViewModel = setViewModel()
        viewDataBinding.setVariable(setBindingVariable(), mViewModel)
        viewDataBinding.executePendingBindings()
    }

    /*
    * Load fragment with backstack
    * */
    fun loadFragment(fragment: Fragment, @IdRes containerViewId: Int) {
        if (!supportFragmentManager.isDestroyed && !supportFragmentManager.isStateSaved) {
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            transaction.replace(containerViewId, fragment)
            transaction.addToBackStack(fragment.javaClass.name)
            transaction.commit()
        }
    }

    /*
    * Load fragment without backstack
    * */
    fun loadDefaultFragment(fragment: Fragment?, @IdRes containerViewId: Int) {
        if (!supportFragmentManager.isDestroyed && !supportFragmentManager.isStateSaved) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(containerViewId, fragment!!)
            transaction.commit()
        }
    }
}