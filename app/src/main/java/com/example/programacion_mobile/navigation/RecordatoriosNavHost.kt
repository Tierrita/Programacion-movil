package com.example.programacion_mobile.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.programacion_mobile.ui.PantallaRecordatorios
import com.example.programacion_mobile.ui.PantallaCrearRecordatorio
import com.example.programacion_mobile.ui.PantallaDetalleRecordatorio
import com.example.programacion_mobile.viewmodel.RecordatoriosViewModel

object Rutas {
    const val LISTADO = "recordatorios"
    const val CREAR = "recordatorios/crear"
    const val DETALLE = "recordatorios/detalle/{id}"

    fun detalle(id: Int): String =
        "recordatorios/detalle/$id"
}

@Composable
fun RecordatoriosNavHost(
    modifier: Modifier = Modifier,
    vm: RecordatoriosViewModel = viewModel(),
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.LISTADO,
        modifier = modifier,
    ) {
        composable(Rutas.LISTADO) {
            PantallaRecordatorios(
                vm = vm,
                onCrearRecordatorio = {
                    vm.limpiarFormulario()
                    navController.navigate(Rutas.CREAR) {
                        launchSingleTop = true
                    }
                },
                onRecordatorioClick = { id ->
                    navController.navigate(Rutas.detalle(id)) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(Rutas.CREAR) {
            PantallaCrearRecordatorio(
                vm = vm,
                onGuardar = {
                    navController.popBackStack()
                },
                onCancelar = {
                    navController.popBackStack()
                },
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            ),
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: -1

            PantallaDetalleRecordatorio(
                id = id,
                vm = vm,
                onVolver = {
                    navController.popBackStack()
                },
            )
        }
    }
}
