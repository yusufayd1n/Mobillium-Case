package com.yusufaydin.mobilliumcase.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AppointmentScreen(
    userStatus: String
) {
    Surface(
        color = Color.LightGray,
        modifier = Modifier.fillMaxSize(1f)
    ) {
        CheckUserStatus(userStatus = userStatus)
    }
}

@Composable
fun CheckUserStatus(userStatus: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (userStatus.equals("premium")) {
            Text(
                text = "Randevu Ekranı", fontSize = 30.sp
            )
        } else {
            Text(
                text = "Ödeme Ekranı", fontSize = 30.sp
            )
        }
    }

}