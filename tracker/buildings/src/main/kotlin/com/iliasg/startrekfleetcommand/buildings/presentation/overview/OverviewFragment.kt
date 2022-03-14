package com.iliasg.startrekfleetcommand.buildings.presentation.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.iliasg.startrekfleetcommand.buildings.R
import com.iliasg.startrekfleetcommand.buildings.databinding.FragOverviewBinding
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingGroup
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingListItem
import com.iliasg.startrekfleetcommand.buildings.presentation.overview.OverviewEvent.*
import com.iliasg.startrekfleetcommand.buildings.presentation.overview.adapter.OverviewAdapterEvent
import com.iliasg.startrekfleetcommand.buildings.presentation.overview.adapter.OverviewAdapterEvent.*
import com.iliasg.startrekfleetcommand.buildings.presentation.overview.adapter.OverviewListAdapter
import com.iliasg.startrekfleetcommand.buildings.presentation.utils.buildingId
import com.iliasg.startrekfleetcommand.buildings.presentation.utils.buildingLevel
import com.iliasg.startrekfleetcommand.buildings.presentation.utils.buildingLevelCount
import com.iliasg.startrekfleetcommand.core.R.string.cancel
import com.iliasg.startrekfleetcommand.core.R.string.confirm
import com.iliasg.startrekfleetcommand.core.data.datasources.preferences.LocalPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OverviewFragment : Fragment() {

    private var _binding: FragOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: OverviewListAdapter

    private val viewModel by viewModels<OverviewViewModel>()

    private var rvJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        binding.btnBackMain.setOnClickListener {
            viewModel.onEvent(RemoveGroupFilter)
            setGroupHeaderVisibility()
        }
    }

    private fun setUpRecyclerView() {
        adapter = OverviewListAdapter { onAdapterEvent(it) }
        binding.rvBuildings.adapter = adapter

        rvJob?.cancel()
        rvJob = lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    adapter.setItems(state.buildings)
                }
            }
        }
    }

    private fun onAdapterEvent(event: OverviewAdapterEvent) {
        when(event) {
            is OnBuildingClick -> {
                findNavController().navigate(
                    resId = R.id.action_overview_to_levels,
                    args = Bundle().apply {
                        buildingId = event.buildingId
                        buildingLevelCount = event.levelCount
                    }
                )
            }
            is OnBuildingUpgrade -> {
                findNavController().navigate(
                    resId = R.id.action_overview_to_detailed,
                    args = Bundle().apply {
                        val item = event.building as BuildingListItem.SingleItem
                        buildingId = item.id
                        buildingLevel = item.currentLevel + 1
                    }
                )
            }
            is OnGroupClick -> {
                viewModel.onEvent(FilterOnGroup(event.group))
                setGroupHeaderVisibility(event.group)
            }
            is OnBuildingDowngrade -> {
                requestDowngradeConfirmation(event.building)
            }
        }
    }

    private fun setGroupHeaderVisibility(group: BuildingGroup? = null) {
        if (group != null) {
            binding.tvGroup.text = getString(group.groupName!!)
            binding.sublistHeader.visibility = View.VISIBLE
        } else {
            binding.tvGroup.text = ""
            binding.sublistHeader.visibility = View.GONE
        }
    }

    private fun requestDowngradeConfirmation(data: BuildingListItem.SingleItem) {
        val isOperations = data.id == LocalPreferences.PREF_OPERATIONS_KEY

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_confirmation_title, data.name))
            .setMessage(getString(
                if (isOperations) R.string.dialog_confirmation_message_operations
                else R.string.dialog_confirmation_message
            ))
            .setIcon(
                if (isOperations) ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_warning,
                    requireContext().theme
                )
                else null
            )
            .setPositiveButton(getString(confirm)) { _, _ ->
                viewModel.onEvent(DowngradeBuilding(data))
            }
            .setNegativeButton(getString(cancel)) { _, _ -> /* No action */ }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}