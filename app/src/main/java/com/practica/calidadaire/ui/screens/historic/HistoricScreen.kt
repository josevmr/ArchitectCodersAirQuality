package com.practica.calidadaire.ui.screens.historic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practica.calidadaire.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricScreen(
    vm: HistoricScreenViewModel = viewModel(),
    location: String?,
    onBack: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (location != null) {
            vm.onUiReady(location)
        }
        Scaffold(
           topBar = {
               TopAppBar(
                   navigationIcon = {
                       IconButton(onClick = onBack ) {
                           Icon(
                               imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                               contentDescription = stringResource(R.string.back)
                           )
                       }
                   },
                   title = { Text(text = stringResource(R.string.last_7_days)) }
               )
           }
        ) { padding ->
            ShowHistoricScreen(modifier = Modifier.padding(padding), vm)
        }
    }
}

@Composable
private fun ShowHistoricScreen(modifier: Modifier, vm: HistoricScreenViewModel) {
    val itemList = vm.calculateAverageByParameter(vm.state.data)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(itemList.toList()){ item ->
                CustomItem(item)
            }
        }
    }
}

@Composable
private fun CustomItem(item: Pair<String, Map<String, Double>>) {

    Text (
        text = stringResource(R.string.date, item.first),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )

    item.second.forEach{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            horizontalArrangement = Arrangement.Start
        ){
            Text (
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                        append(stringResource(R.string.parameter))
                    }
                    append(it.key)
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text (
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                        append(stringResource(R.string.average))
                    }
                    append("%.2f µg/m³".format(it.value))
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

        }
    }

}


