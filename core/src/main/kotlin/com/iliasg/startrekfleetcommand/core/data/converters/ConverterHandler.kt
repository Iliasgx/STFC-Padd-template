package com.iliasg.startrekfleetcommand.core.data.converters

@ExperimentalStdlibApi
data class ConverterHandler(
    val bonusHeaderConverter: BonusHeaderConverter,
    val bonusValuesConverter: BonusValuesConverter,
    val requirementListConverter: RequirementListConverter,
    val itemsConverter: ItemsConverter
)