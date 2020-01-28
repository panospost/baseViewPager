package com.example.baseapp.shared.views


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp

import com.example.baseapp.R
import java.lang.IllegalArgumentException

/**
 * A simple [Fragment] subclass.
 */
class BaseFragment : Fragment() {

    private val defaultInt = -1
    private var layoutRes: Int = -1
    private var toolbarId: Int = -1
    private var navHostId: Int = -1
    // root destinations
     private val rootDestinations =
        setOf(R.id.home_dest, R.id.library_dest, R.id.settings_dest)
    // nav config with root destinations
    private val appBarConfig = AppBarConfiguration(rootDestinations)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            Log.i("bundleh", it.toString())
            layoutRes = it.getInt(KEY_LAYOUT, 0)
            toolbarId = it.getInt(KEY_TOOLBAR, 0)
            navHostId = it.getInt(KEY_NAV_HOST,0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return if (layoutRes == defaultInt) null
        else inflater.inflate(layoutRes, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (toolbarId == defaultInt || navHostId == defaultInt) return
        // find navController using navHostFragment
        // setup navigation with toolbar
        val toolbar = requireActivity().findViewById<Toolbar>(toolbarId)
        val navController =  requireActivity().findNavController(navHostId)

        NavigationUI.setupWithNavController(toolbar, navController, appBarConfig)

    }

    fun onBackPressed(): Boolean {
        Log.i("navhost id", navHostId.toString())

        return requireActivity().findNavController(navHostId)
            .navigateUp(appBarConfig)
    }

    companion object {

        private const val KEY_LAYOUT = "layout_key"
        private const val KEY_NAV_HOST = "nav_host_key"
        private const val KEY_TOOLBAR = "toolbar_key"

        fun newInstance(layoutRes: Int, toolbarId: Int, navHostId: Int) = BaseFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_LAYOUT, layoutRes)
                putInt(KEY_NAV_HOST, navHostId)
                putInt(KEY_TOOLBAR, toolbarId)
            }

        }
    }

    fun popToRoot() {

        val navController = requireActivity().findNavController(navHostId)
        try {

            navController.popBackStack(navController.graph.startDestination, false)
        }catch (e: IllegalArgumentException) {
            Log.i("stacktrace", e.printStackTrace().toString())
        }
    }

    fun handleDeepLink(intent: Intent): Boolean =
        requireActivity().findNavController(navHostId)
            .handleDeepLink(intent)

}
