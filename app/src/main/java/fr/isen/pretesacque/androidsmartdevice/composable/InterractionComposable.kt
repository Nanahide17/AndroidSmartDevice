package fr.isen.pretesacque.androidsmartdevice.composable

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.isen.pretesacque.androidsmartdevice.LED
import fr.isen.pretesacque.androidsmartdevice.R

@Composable
fun InterractionScreen(innerPadding : PaddingValues,
                       ledInterraction: (Int) -> Unit,
                       listLeds: List<LED>){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text="Lumière")
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Image(modifier = Modifier
                .size(100.dp)
                .padding(20.dp, 0.dp)
                .clickable {
                    ledInterraction(1)
                },
                painter = painterResource(if (!listLeds[0].isOn) R.drawable.baseline_lightbulb_outline_off else R.drawable.baseline_lightbulb_outline_on),//TODO : mettre variable condition
                contentDescription = "lumière 1")
            Image(modifier = Modifier
                .size(100.dp)
                .padding(20.dp, 0.dp)
                .clickable {
                    ledInterraction(2)
                },
                painter = painterResource(if (!listLeds[1].isOn) R.drawable.baseline_lightbulb_outline_off else R.drawable.baseline_lightbulb_outline_on),//TODO : mettre variable condition
                contentDescription = "lumière 2")
            Image(modifier = Modifier
                .size(100.dp)
                .padding(20.dp, 0.dp)
                .clickable {
                    ledInterraction(3)
                },
                painter = painterResource(if (!listLeds[2].isOn) R.drawable.baseline_lightbulb_outline_off else R.drawable.baseline_lightbulb_outline_on),//TODO : mettre variable condition
                contentDescription = "lumière 3")
        }
    }
}