package com.github.kamiiroawase.specialchars.view.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.kamiiroawase.specialchars.R
import com.github.kamiiroawase.specialchars.databinding.ActivityMainBinding
import com.github.kamiiroawase.specialchars.view.fragment.DuanyuFragment
import com.github.kamiiroawase.specialchars.view.fragment.WodeFragment
import com.github.kamiiroawase.specialchars.view.fragment.ZitiFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navFragments: Map<Int, Fragment>

    private fun getFragmentByNavId(id: Int): Fragment = navFragments[id]
        ?: error("Fragment not found for id=$id")

    private var currentNavItemId = R.id.navigationZiti

    companion object {
        var initializedFragmentCount = 0
        private var lastBackPressTimestamp = 0L
        private const val EXIT_CONFIRM_INTERVAL = 2000L
        private const val STATE_CURRENT_NAV_ITEM_ID = "CURRENT_ITEM_ID"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(STATE_CURRENT_NAV_ITEM_ID, currentNavItemId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        restoreSavedState(savedInstanceState)
        initNavFragments(savedInstanceState != null)
        setupBottomNavListener()
        initBackPressHandler()
    }

    private fun restoreSavedState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            currentNavItemId =
                savedInstanceState.getInt(STATE_CURRENT_NAV_ITEM_ID, R.id.navigationZiti)
        }
    }

    private fun initNavFragments(isRestoring: Boolean) {
        navFragments = mapOf(
            R.id.navigationZiti to getNavFragment(R.id.navigationZiti) { ZitiFragment() },
            R.id.navigationDuanyu to getNavFragment(R.id.navigationDuanyu) { DuanyuFragment() },
            R.id.navigationWode to getNavFragment(R.id.navigationWode) { WodeFragment() }
        )

        if (!isRestoring) {
            binding.root.visibility = View.INVISIBLE
        }


        lifecycleScope.launch {
            while (initializedFragmentCount < 5) {
                delay(100)
            }

            supportFragmentManager.beginTransaction().apply {
                navFragments.forEach { _, fragment ->
                    hide(fragment)
                }

                show(getFragmentByNavId(currentNavItemId))

                commitNow()
            }

            updateRootBackgroundColor()

            if (isRestoring) {
                binding.bottomNav.selectedItemId = currentNavItemId
            } else {
                binding.root.visibility = View.VISIBLE
            }
        }
    }

    private fun setupBottomNavListener() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            switchNavFragment(item.itemId)
            true
        }
    }

    private fun initBackPressHandler() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastBackPressTimestamp >= EXIT_CONFIRM_INTERVAL) {
                        lastBackPressTimestamp = currentTime
                        showToast(R.string.zaianyicituichuyingyong)
                    } else {
                        finish()
                    }
                }
            }
        )
    }

    private fun switchNavFragment(itemId: Int) {
        if (currentNavItemId != itemId) {
            supportFragmentManager.beginTransaction().apply {
                hide(getFragmentByNavId(currentNavItemId))
                show(getFragmentByNavId(itemId))
                commitNow()
            }

            currentNavItemId = itemId
        }

        updateRootBackgroundColor()
    }

    private inline fun <reified T : Fragment> getNavFragment(
        id: Int,
        noinline factory: () -> T
    ): T {
        val existingFragment = supportFragmentManager
            .findFragmentByTag(id.toString())

        return if (existingFragment == null) {
            val newFragment = factory()

            supportFragmentManager.beginTransaction().apply {
                add(R.id.navHostFragment, newFragment, id.toString())
                commitNow()
            }

            newFragment
        } else {
            existingFragment as T
        }
    }

    private fun updateRootBackgroundColor() {
        val color = if (currentNavItemId == R.id.navigationZiti) {
            "#FFEDED".toColorInt()
        } else {
            Color.TRANSPARENT
        }

        binding.root.setBackgroundColor(color)
    }
}
