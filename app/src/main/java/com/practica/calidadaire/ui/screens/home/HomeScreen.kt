package com.practica.calidadaire.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practica.calidadaire.R
import com.practica.calidadaire.data.model.AirParameter
import com.practica.calidadaire.ui.utils.QualityColorBuilders
import kotlinx.coroutines.delay


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeScreen(
    onClick: (String) -> Unit,
    vm: HomeViewModel = viewModel()
) {
    val state by vm.state.collectAsState()
    val homeState = rememberHomeState()

    homeState.AskCoordinatesEffect { coordinates ->
        if (coordinates != null) vm.onUiReady(coordinates) else vm.onPermissionDenied()
    }
    if (state.message.isNotEmpty()) {
        homeState.ShowMessageEffect(message = state.message) {
            vm.onMessageShown()
        }

    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = homeState.snackbarHostState) }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                if (state.loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Screen(
                        vm = vm,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 48.dp),
                        onHistoricClick = { onClick(state.data.name) }
                    )
                }
                if (state.isPermissionDeniedVisible){
                    PermissionDeniedCard(
                        onOpenSettings = {
                            vm.onSettingsButtonClicked()
                            homeState.openAppSettings()
                        },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
private fun Screen(
    vm: HomeViewModel,
    modifier: Modifier,
    onHistoricClick: () -> Unit
) {

    val state by vm.state.collectAsState()
    val airQualityIndex = vm.calculateAirQualityIndex()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val qualityColorModel = QualityColorBuilders.getQualityColorModel(airQualityIndex)

            Text(
                text = stringResource(R.string.air_quality),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(36.dp))

            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(colorResource(id = qualityColorModel.imageColor))
                )

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .border(4.dp, colorResource(id = R.color.borderColor), CircleShape)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    ShowContent(qualityColorModel.image, airQualityIndex)
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = state.data.name.substringBefore(" ").trim(),
                fontSize = 24.sp
            )
            Text(
                text = state.data.name.substringAfter(" ").trim(),
                fontSize = 20.sp
            )

            Text(
                text = vm.parseDate(state.data.lastUpdated),
                fontSize = 16.sp
            )
            Spacer(Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.parameters),
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.padding(vertical = 6.dp),
                content = {
                    items(state.data.parameters) { item ->
                        ParameterItemList(vm, item)
                    }
                }
            )

            Spacer(Modifier.height(32.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.see_historic_7_days),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    )
                    IconButton(onClick = onHistoricClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "historic Icon"
                        )
                    }
                }
            }
        }
        ExpandCard(vm)
    }
}

@Composable
private fun ShowContent(image: Int, average: Double) {
    var showText by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000L)
            showText = !showText
        }
    }

    when (showText) {
        true -> Text(
            text = String.format("%.2f", average),
            fontSize = 30.sp
        )

        else -> Icon(
            painter = painterResource(id = image),
            contentDescription = "icon",
            modifier = Modifier
                .size(60.dp)
        )
    }
}

@Composable
private fun ExpandCard(vm: HomeViewModel) {
    var isExpanded by remember { mutableStateOf(false) }

    val cardHeight by animateDpAsState(
        targetValue = if (isExpanded) 500.dp else 100.dp,
        animationSpec = tween(durationMillis = 300),
        label = "Animation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                    contentDescription = "Expandable card"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (isExpanded) {
                AirQualityDescriptions(vm)
            }
        }
    }
}

@Composable
fun AirQualityDescriptions(vm: HomeViewModel) {
    val context = LocalContext.current

    val descriptions = vm.getDescriptions()

    Text(
        text = stringResource(R.string.gas_particles_information),
        fontWeight = FontWeight.Bold
    )

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {


        items(descriptions.toList()) { (first, second) ->
            val title = context.getString(first)
            val desc = context.getString(second)

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(title)
                    }
                    append(desc)
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun ParameterItemList(vm: HomeViewModel, parameter: AirParameter) {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = vm.getParameterTitle(parameter.parameter, context),
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "  ${parameter.lastValue}"
        )
    }
}

@Composable
fun PermissionDeniedCard(onOpenSettings: () -> Unit, modifier: Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.settings_text)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onOpenSettings) {
                Text(text = stringResource(R.string.open_settings))
            }
        }
    }
}
