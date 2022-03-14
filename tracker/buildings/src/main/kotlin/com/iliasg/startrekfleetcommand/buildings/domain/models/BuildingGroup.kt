package com.iliasg.startrekfleetcommand.buildings.domain.models

import androidx.annotation.StringRes
import com.iliasg.startrekfleetcommand.buildings.R

enum class BuildingGroup(@StringRes val groupName: Int? = null) {
    NONE                (null),
    RESEARCH            (R.string.building_group_research),
    SHIP                (R.string.building_group_ship),
    OFFICER             (R.string.building_group_officer),
    DRYDOCK             (R.string.building_group_drydock),
    DEFENSE_PLATFORM    (R.string.building_group_defense_platform),
    PARSTEEL            (R.string.building_group_parsteel),
    TRITANIUM           (R.string.building_group_tritanium),
    DILITHIUM           (R.string.building_group_dilithium)
}