package com.iliasg.startrekfleetcommand.buildings.presentation.detailed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.iliasg.startrekfleetcommand.buildings.databinding.FragDetailedBinding
import com.iliasg.startrekfleetcommand.buildings.domain.models.Building
import com.iliasg.startrekfleetcommand.buildings.presentation.utils.buildingId
import com.iliasg.startrekfleetcommand.buildings.presentation.utils.buildingLevel
import com.iliasg.startrekfleetcommand.core.domain.models.BonusMerge
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailedFragment: Fragment() {

    private var _binding: FragDetailedBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.building?.let { updateLayout(it, state.currentUserLevel) }
                }
            }
        }

        viewModel.setUp(requireArguments().buildingId, requireArguments().buildingLevel)
    }

    private fun updateLayout(building: Building, currentLevel: Int) {
        with(binding) {
            binding.data = building
            binding.upgradeable = (building.levelDetails.level == currentLevel + 1)

            vlRequirements.setItems(building.levelDetails.requirements)
            building.levelDetails.buildCosts.let {
                vlBuildCosts.setItems(it.resources + it.materials + it.others)
            }
            building.levelDetails.rewards?.let {
                vlRewards.setItems(it.resources + it.materials + it.others)
            }
            building.bonuses?.let { headers ->
                building.levelDetails.bonuses?.let { values ->
                    val merges = headers.map { (idx, bonus) ->
                        BonusMerge(idx, bonus, values[idx]!!)
                    }
                    vlBonuses.setItems(merges)
                }
            }

            btnPrevious.setOnClickListener { viewModel.onEvent(DetailedEvent.OnPreviousClick) }
            btnNext.setOnClickListener { viewModel.onEvent(DetailedEvent.OnNextClick) }
            btnUpgrade.setOnClickListener { viewModel.onEvent(DetailedEvent.OnUpgradeClick) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}