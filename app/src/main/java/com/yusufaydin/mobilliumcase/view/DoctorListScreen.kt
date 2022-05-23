package com.yusufaydin.mobilliumcase.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.yusufaydin.mobilliumcase.R
import com.yusufaydin.mobilliumcase.model.Doctor
import com.yusufaydin.mobilliumcase.viewmodel.DoctorListViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun DoctorListScreen(
    navController: NavController,
    viewModel: DoctorListViewModel = hiltViewModel()
) {
    Surface(
        color = Color.LightGray,
        modifier = Modifier.fillMaxSize(1f)
    ) {
        Column {
            Spacer(modifier = Modifier.padding(top = 15.dp))
            SearchBar(
                hint = "Klinik ara..", modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                viewModel.searchDoctorList(it)
            }
            CheckGender(modifier = Modifier.padding(15.dp))
            DoctorList(navController = navController)

        }
    }
}


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {},
) {

    var text by remember {
        mutableStateOf("")
    }

    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }
    Box(modifier = modifier) {
        TextField(value = text, onValueChange = {
            text = it
            onSearch(it)
        }, leadingIcon = {
            Icon(painter = painterResource(id = R.drawable.search), contentDescription = null)
        }, maxLines = 1,
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, shape = RoundedCornerShape(10.dp))
                .onFocusChanged {
                    isHintDisplayed = it.isFocused != true && text.isEmpty()
                })
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 50.dp, vertical = 16.dp)
            )
        }
    }
}

@Composable
fun CheckGender(modifier: Modifier = Modifier, viewModel: DoctorListViewModel = hiltViewModel()) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, shape = RoundedCornerShape(10.dp))
                .background(Color.White, shape = RoundedCornerShape(10.dp))
        ) {
            val checkedStateFemale = remember {
                mutableStateOf(false)
            }

            val checkedStateMale = remember {
                mutableStateOf(false)
            }
            Checkbox(
                checked = checkedStateFemale.value,
                onCheckedChange = {
                    checkedStateFemale.value = it
                    viewModel.filterDoctorList("female")
                    if (checkedStateFemale.value) {
                        checkedStateMale.value = !it
                    }
                    if (!checkedStateFemale.value && !checkedStateMale.value) {
                        viewModel.filterDoctorList("")
                    }
                },
                modifier = Modifier.padding(10.dp)
            )

            Text(
                text = "Kadın",
                modifier = Modifier
                    .padding(top = 12.dp, start = 5.dp)
                    .weight(1f),
                fontSize = 16.sp
            )

            Checkbox(
                checked = checkedStateMale.value,
                onCheckedChange = {
                    checkedStateMale.value = it
                    viewModel.filterDoctorList("male")
                    if (checkedStateMale.value) {
                        checkedStateFemale.value = !it
                    }
                    if (!checkedStateFemale.value && !checkedStateMale.value) {
                        viewModel.filterDoctorList("")
                    }

                },
                modifier = Modifier.padding(10.dp)
            )

            Text(
                text = "Erkek",
                modifier = Modifier
                    .padding(top = 12.dp, start = 5.dp)
                    .weight(1f),
                fontSize = 16.sp
            )

        }
    }
}


@Composable
fun EmptyList() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_person), contentDescription = null)
        Text(text = "Kullanıcı Bulunamadı.", fontSize = 20.sp)
    }

}

@Composable
fun DoctorList(
    navController: NavController,
    viewModel: DoctorListViewModel = hiltViewModel()
) {
    val doctorList by remember { viewModel.doctorList }
    val errorMessage by remember { viewModel.errorMessage }
    val isLoading by remember { viewModel.isLoading }

    if (viewModel.doctorList.value.isEmpty()) {
        if (!isLoading) {
            EmptyList()
        }
    } else {
        DoctorListView(doctors = doctorList, navController = navController)
    }


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.Blue)
        }
        if (errorMessage.isNotEmpty()) {
            RetryView(error = errorMessage) {
                viewModel.downloadDoctors()
            }
        }
    }
}

@Composable
fun DoctorListView(doctors: List<Doctor>, navController: NavController) {
    LazyColumn(contentPadding = PaddingValues(5.dp)) {
        items(doctors) { doctor ->
            DoctorRow(
                navController = navController,
                doctor = doctor,
                modifier = Modifier.padding(start = 6.dp, end = 6.dp)
            )
        }
    }
}


@Composable
fun DoctorRow(
    navController: NavController, doctor: Doctor,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White)
                .clickable {
                    navController.navigate(
                        "doctor_detail_screen/${doctor.fullName}/${doctor.userStatus}/${
                            URLEncoder.encode(
                                doctor.image.url,
                                StandardCharsets.UTF_8.toString()
                            )
                        }"
                    )
                }
        ) {
            Image(
                painter = rememberImagePainter(data = doctor.image.url),
                contentDescription = doctor.fullName,
                modifier = modifier
                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                    .size(75.dp, 75.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )

            Text(
                text = doctor.fullName,
                modifier = modifier
                    .padding(2.dp)
                    .weight(1f),
                color = Color.Gray,
                fontSize = 20.sp
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_forward), contentDescription = null,
                modifier = modifier.padding(end = 5.dp)
            )
        }
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = modifier.padding(start = 5.dp, end = 5.dp)
        )
    }
}


@Composable
fun RetryView(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(text = error, color = Color.Red, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            onRetry
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Retry")
        }
    }
}



