package com.example.fibra_labeling.data.local.mapper

import com.example.fibra_labeling.data.local.entity.fibrafil.FilUserEntity
import com.example.fibra_labeling.data.model.fibrafil.users.FilUserResponse

fun FilUserResponse.toEntity(): FilUserEntity? =
    userid?.let {
        FilUserEntity(
            userid = it,
            uNAME = uNAME,
            useRCODE = useRCODE
        )
    }