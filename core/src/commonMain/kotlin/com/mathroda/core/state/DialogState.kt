package com.mathroda.core.state

sealed class DialogState {
    data object Open: DialogState()
    data object Close: DialogState()
}
