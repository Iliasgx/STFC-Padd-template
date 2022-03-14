package com.iliasg.startrekfleetcommand.buildings.presentation.detailed

internal sealed class DetailedEvent {
    object OnPreviousClick : DetailedEvent()
    object OnNextClick : DetailedEvent()
    object OnUpgradeClick : DetailedEvent()
}