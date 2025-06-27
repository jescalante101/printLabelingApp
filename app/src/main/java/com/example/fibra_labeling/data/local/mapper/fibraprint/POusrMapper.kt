package com.example.fibra_labeling.data.local.mapper.fibraprint

import com.example.fibra_labeling.data.local.entity.fibraprint.POusrEntity
import com.example.fibra_labeling.data.model.fibrafil.users.FilUserResponse

fun FilUserResponse.toEntity(): POusrEntity? =
    userid?.let {
        POusrEntity(
            userid=userid,
            uNAME=uNAME,
            useRCODE=useRCODE
        )
    }