package com.iliasg.startrekfleetcommand.stfcpadd.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iliasg.startrekfleetcommand.stfcpadd.R
import com.iliasg.startrekfleetcommand.stfcpadd.databinding.FragHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvBuildings.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_buildings)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}