package com.iliasg.startrekfleetcommand.buildings.presentation.levels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iliasg.startrekfleetcommand.buildings.R
import com.iliasg.startrekfleetcommand.buildings.databinding.FragLevelsBinding
import com.iliasg.startrekfleetcommand.buildings.presentation.utils.GridSpacingItemDecoration
import com.iliasg.startrekfleetcommand.buildings.presentation.utils.buildingId
import com.iliasg.startrekfleetcommand.buildings.presentation.utils.buildingLevel
import com.iliasg.startrekfleetcommand.buildings.presentation.utils.buildingLevelCount
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LevelsFragment: Fragment() {

    private var _binding: FragLevelsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragLevelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val adapter = LevelsGridAdapter(requireArguments().buildingLevelCount) { level ->
            findNavController().navigate(
                resId = R.id.action_levels_to_detailed,
                args = Bundle().apply {
                    buildingId = requireArguments().buildingId
                    buildingLevel = level
                }
            )
        }

        binding.rvLevels.apply {
            addItemDecoration(GridSpacingItemDecoration(5, 0, true))
            setHasFixedSize(true)
            setAdapter(adapter)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}