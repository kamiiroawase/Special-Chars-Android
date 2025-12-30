package com.github.kamiiroawase.specialchars.activity

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.kamiiroawase.specialchars.R
import com.github.kamiiroawase.specialchars.databinding.ActivityMainBinding
import com.github.kamiiroawase.specialchars.fragment.DuanyuFragment
import com.github.kamiiroawase.specialchars.fragment.WodeFragment
import com.github.kamiiroawase.specialchars.fragment.ZitiFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var fragments: Map<Int, Fragment>

    private var currentItemId = R.id.navigationZiti
    private var currentItemKey = "CURRENT_ITEM_ID"

    companion object {
        var fragmentInitCount = 0
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(currentItemKey, currentItemId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            binding.root.visibility = View.INVISIBLE

            setUpFragments {
                binding.root.visibility = View.VISIBLE
            }
        } else {
            restoreFragments(savedInstanceState)
        }

        setUpItemSelectedListener()

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    moveTaskToBack(true)
                }
            }
        )
    }

    private fun setUpItemSelectedListener() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            switchFragment(item.itemId)
            true
        }
    }

    private fun setUpFragments(callback: (() -> Unit)? = null) {
        fragments = mapOf(
            R.id.navigationZiti to ZitiFragment(),
            R.id.navigationDuanyu to DuanyuFragment(),
            R.id.navigationWode to WodeFragment()
        )

        val transaction = supportFragmentManager.beginTransaction()

        fragments.forEach { (key: Int, value: Fragment) ->
            transaction.add(R.id.navHostFragment, value, key.toString())
        }

        transaction.commitNowAllowingStateLoss()

        lifecycleScope.launch {
            while (fragmentInitCount < 5) {
                delay(100)
            }

            currentItemId = R.id.navigationZiti

            val transaction = supportFragmentManager.beginTransaction()

            fragments.forEach { (key: Int, value: Fragment) ->
                if (key != currentItemId) {
                    transaction.hide(value)
                }
            }

            transaction.commitNowAllowingStateLoss()

            callback?.invoke()
        }
    }

    private fun restoreFragments(savedInstanceState: Bundle) {
        currentItemId = savedInstanceState.getInt(currentItemKey, R.id.navigationZiti)

        val zitiFragment = supportFragmentManager
            .findFragmentByTag(R.id.navigationZiti.toString())
                as? ZitiFragment
            ?: ZitiFragment()
        val duanyuFragment = supportFragmentManager
            .findFragmentByTag(R.id.navigationDuanyu.toString())
                as? DuanyuFragment
            ?: DuanyuFragment()
        val wodeFragment = supportFragmentManager
            .findFragmentByTag(R.id.navigationWode.toString())
                as? WodeFragment
            ?: WodeFragment()

        fragments = mapOf(
            R.id.navigationZiti to zitiFragment,
            R.id.navigationDuanyu to duanyuFragment,
            R.id.navigationWode to wodeFragment
        )
    }

    private fun switchFragment(itemId: Int) {
        if (itemId == currentItemId) return

        val showFragment = fragments[itemId] ?: return
        val hideFragment = fragments[currentItemId]!!

        val transaction = supportFragmentManager.beginTransaction()

        transaction.hide(hideFragment)
        transaction.show(showFragment)

        transaction.commitAllowingStateLoss()

        currentItemId = itemId
    }
}