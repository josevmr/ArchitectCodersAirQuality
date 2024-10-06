package com.practica.calidadaire.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.practica.calidadaire.ui.models.QualityColorModel
import com.practica.calidadaire.R

object QualityColorBuilders {

    fun getQualityColorModel(value: Double): QualityColorModel {
        return when {
            value <= 50 -> goodQualityBuilder()
            value <=100 -> moderateQualityBuilder()
            value <=150 -> unhealthySensibleQualityBuilder()
            value <=200 -> unhealthyQualityBuilder()
            value <=300 -> veryUnhealthyQualityBuilder()
            value >300 -> hazardousQualityBuilder()
            else -> defaultQualityBuilder()
        }
    }
    private fun defaultQualityBuilder(): QualityColorModel {
        return QualityColorModel(
            imageColor = R.color.defaultColor,
            image = R.drawable.gris
        )
    }

    private fun goodQualityBuilder(): QualityColorModel {
        return QualityColorModel(
            imageColor = R.color.goodColor,
            image = R.drawable.verde
        )
    }
    private fun moderateQualityBuilder(): QualityColorModel {
        return QualityColorModel(
            imageColor = R.color.moderateColor,
            image = R.drawable.amarillo
        )
    }
    private fun unhealthySensibleQualityBuilder(): QualityColorModel {
        return QualityColorModel(
            imageColor = R.color.unhealthySensibleColor,
            image = R.drawable.naranja
        )
    }
    private fun unhealthyQualityBuilder(): QualityColorModel {
        return QualityColorModel(
            imageColor = R.color.unhealthyColor,
            image = R.drawable.amarillo
        )
    }
    private fun veryUnhealthyQualityBuilder(): QualityColorModel {
        return QualityColorModel(
            imageColor = R.color.veryUnhealthyColor,
            image = R.drawable.morado
        )
    }
    private fun hazardousQualityBuilder(): QualityColorModel {
        return QualityColorModel(
            imageColor = R.color.hazardousColor,
            image = R.drawable.granate
        )
    }
}