package com.mathroda.common.state

sealed class DialogState {
    object Open: DialogState()
    object Close: DialogState()
}
