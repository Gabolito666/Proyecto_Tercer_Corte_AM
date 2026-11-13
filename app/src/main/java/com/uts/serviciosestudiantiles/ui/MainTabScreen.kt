package com.uts.serviciosestudiantiles.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uts.serviciosestudiantiles.viewmodel.ServiciosViewModel

/**
 * MainTabScreen migrado a MainDrawerScreen con ModalNavigationDrawer y navegación por roles.
 */
@Composable
fun MainTabScreen(
    viewModel: ServiciosViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    MainDrawerScreen(
        viewModel = viewModel,
        modifier = modifier
    )
}
