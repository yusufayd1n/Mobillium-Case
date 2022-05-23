package com.yusufaydin.mobilliumcase.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.yusufaydin.mobilliumcase.R
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun DoctorDetailScreen(
    navController: NavController,
    doctorName: String,
    userStatus: String,
    imageUrl: String
) {
    Surface(
        color = Color.LightGray,
        modifier = Modifier.fillMaxSize(1f)
    ) {
        Column {
            DoctorBox(
                imageUrl = imageUrl,
                doctorName = doctorName, userStatus = userStatus,
                modifier = Modifier.padding(top = 15.dp, start = 15.dp, end = 15.dp, bottom = 10.dp)
            )
            ActionRow(
                navController = navController, userStatus = userStatus,
                modifier = Modifier.padding(top = 10.dp, start = 15.dp, end = 15.dp, bottom = 15.dp)
            )
        }
    }
}

@Composable
fun DoctorBox(
    modifier: Modifier = Modifier,
    imageUrl: String,
    doctorName: String,
    userStatus: String
) {
    Box(modifier = modifier) {
        Row(
            modifier
                .background(color = Color.White)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = rememberImagePainter(data = imageUrl),
                    contentDescription = null,
                    modifier = modifier
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                        .size(125.dp, 125.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                )
                Text(text = doctorName, fontSize = 20.sp)
                if (userStatus.equals("premium")) {
                    Text(text = userStatus, fontSize = 20.sp, color = Color.Yellow)
                }
                Spacer(modifier = modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun ActionRow(
    navController: NavController,
    modifier: Modifier = Modifier,
    userStatus: String
) {
    Row(
        modifier = modifier
            .padding(15.dp)
            .background(color = Color.White)
            .fillMaxWidth().clickable {
            navController.navigate(
                "appointment_screen/${userStatus}"
            )
        }
    ) {
        if (userStatus.equals("premium")) {
            Text(
                text = "Randevu Al", fontSize = 20.sp, color = Color.Green,
                modifier = modifier.weight(1f)
            )
        } else {
            Text(
                text = "Premium Paket Al", fontSize = 20.sp, color = Color.Green,
                modifier = modifier.weight(1f)
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_forward), contentDescription = null,
            tint = Color.Green, modifier = modifier.padding(end = 5.dp, top = 2.dp)
        )
    }
}