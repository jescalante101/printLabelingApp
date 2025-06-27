package com.example.fibra_labeling.data.local.mapper.fibraprint

import com.example.fibra_labeling.data.local.entity.fibraprint.POcrdEntity
import com.example.fibra_labeling.data.model.fibraprint.OcrdResponse

fun OcrdResponse.toEntity(): POcrdEntity=
    POcrdEntity(
        cardCode=cardCode,
        cardName = cardName.orEmpty(),
    )

fun POcrdEntity.toOcrdResponse():OcrdResponse=
    OcrdResponse(
        cardCode=cardCode,
        cardName = cardName
    )