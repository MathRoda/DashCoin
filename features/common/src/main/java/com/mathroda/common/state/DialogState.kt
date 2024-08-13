package com.mathroda.common.state

sealed class DialogState {
    data object Open: DialogState()
    data object Close: DialogState()
}
