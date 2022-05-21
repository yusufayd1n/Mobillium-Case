package com.yusufaydin.mobilliumcase.view


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.skydoves.landscapist.glide.GlideImage
import com.yusufaydin.mobilliumcase.R
import com.yusufaydin.mobilliumcase.model.Doctor
import com.yusufaydin.mobilliumcase.viewmodel.DoctorListViewModel
import kotlinx.coroutines.coroutineScope


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
                if(viewModel.initialDoctorList.isEmpty()){

                }
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
fun CheckGender(modifier: Modifier = Modifier) {
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
                    checkedStateMale.value = !it
                },
                modifier = Modifier.padding(10.dp)
            )

            Text(
                text = "Kadın",
                modifier = Modifier.padding(top = 12.dp, start = 5.dp),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(65.dp))

            Checkbox(
                checked = checkedStateMale.value,
                onCheckedChange = {
                    checkedStateMale.value = it
                    checkedStateFemale.value = !it
                },
                modifier = Modifier.padding(10.dp)
            )

            Text(
                text = "Erkek",
                modifier = Modifier.padding(top = 12.dp, start = 5.dp),
                fontSize = 16.sp
            )
        }
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

    DoctorListView(doctors = doctorList, navController = navController)

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.Blue)
        }
        if (errorMessage.isNotEmpty()) {
            RetryView(error = errorMessage) {
                //viewModel.downloadDoctors()
            }
        }
    }
}

@Composable
fun DoctorListView(doctors: List<Doctor>, navController: NavController) {
    LazyColumn(contentPadding = PaddingValues(5.dp)) {
        items(doctors) { doctor ->
            DoctorRow(navController = navController, doctor = doctor)
        }
    }
}

@Composable
fun DoctorRow(navController: NavController, doctor: Doctor) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .clickable {
                navController.navigate(
                    "doctor_detail_screen/${doctor.fullName}/${doctor.userStatus}/${doctor.image}"
                )
            }
    ) {
        GlideImage(
            imageModel = doctor.image.url
        )

        Text(
            text = doctor.fullName,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(2.dp),
            color = MaterialTheme.colors.primary
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
        }, modifier = Modifier.align(Alignment.CenterHorizontally)){
            Text(text = "Retry")
        }
    }
}



