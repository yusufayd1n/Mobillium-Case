package com.yusufaydin.mobilliumcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.yusufaydin.mobilliumcase.ui.theme.MobilliumCaseTheme
import com.yusufaydin.mobilliumcase.view.AppointmentScreen
import com.yusufaydin.mobilliumcase.view.DoctorDetailScreen
import com.yusufaydin.mobilliumcase.view.DoctorListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobilliumCaseTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "doctor_list_screen") {

                    composable("doctor_list_screen") {
                        DoctorListScreen(navController = navController)
                    }

                    composable("doctor_detail_screen/{doctorName}/{userStatus}/{imageUrl}",
                        arguments = listOf(
                            navArgument("doctorName") {
                                type = NavType.StringType
                            },
                            navArgument("userStatus") {
                                type = NavType.StringType
                            },
                            navArgument("imageUrl") {
                                type = NavType.StringType
                            }
                        )) {
                        val doctorName = remember {
                            it.arguments?.getString("doctorName")
                        }

                        val userStatus = remember {
                            it.arguments?.getString("userStatus")
                        }

                        val imageUrl = remember {
                            it.arguments?.getString("imageUrl")
                        }

                        DoctorDetailScreen(
                            navController = navController,
                            doctorName = doctorName ?: "",
                            userStatus = userStatus ?: "",
                            imageUrl = imageUrl ?: ""
                        )
                    }
                    composable("appointment_screen/{userStatus}",
                        arguments = listOf(
                            navArgument("userStatus") {
                                type = NavType.StringType
                            }
                        )) {
                        val userStatus = remember {
                            it.arguments?.getString("userStatus")
                        }
                        AppointmentScreen(
                            navController = navController,
                            userStatus = userStatus ?: ""
                        )

                    }
                }
            }
        }
    }
}
