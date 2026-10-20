package com.example.libraryreg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.libraryreg.data.MemberViewModel
import com.example.libraryreg.ui.navigation.Routes
import com.example.libraryreg.ui.screens.memberdetail.MemberDetailScreen
import com.example.libraryreg.ui.screens.memberform.MemberFormScreen
import com.example.libraryreg.ui.screens.memberlist.MemberListScreen
import com.example.libraryreg.ui.screens.splash.SplashScreen
import com.example.libraryreg.ui.theme.LibraryRegTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibraryRegTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LibraryRegApp()
                }
            }
        }
    }
}

@Composable
fun LibraryRegApp() {
    val navController = rememberNavController()
    val memberViewModel: MemberViewModel = viewModel()
    val memberIdArgument = listOf(navArgument("memberId") { type = NavType.StringType })

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(onTimeout = {
                navController.navigate(Routes.MEMBER_LIST) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }
        composable(Routes.MEMBER_LIST) {
            MemberListScreen(
                viewModel = memberViewModel,
                onRegisterMember = { navController.navigate(Routes.ADD_MEMBER) },
                onOpenMember = { memberId -> navController.navigate(Routes.memberDetail(memberId)) },
                onEditMember = { memberId -> navController.navigate(Routes.editMember(memberId)) }
            )
        }
        composable(Routes.ADD_MEMBER) {
            MemberFormScreen(
                viewModel = memberViewModel,
                memberId = null,
                onDone = { navController.popBackStack() }
            )
        }
        composable(route = Routes.MEMBER_DETAIL, arguments = memberIdArgument) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getString("memberId").orEmpty()
            MemberDetailScreen(
                viewModel = memberViewModel,
                memberId = memberId,
                onEdit = { navController.navigate(Routes.editMember(memberId)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = Routes.EDIT_MEMBER, arguments = memberIdArgument) { backStackEntry ->
            MemberFormScreen(
                viewModel = memberViewModel,
                memberId = backStackEntry.arguments?.getString("memberId"),
                onDone = { navController.popBackStack() }
            )
        }
    }
}
