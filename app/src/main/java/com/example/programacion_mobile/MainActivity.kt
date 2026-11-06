package com.example.programacion_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.programacion_mobile.ui.PantallaRecordatorios
import com.example.programacion_mobile.ui.theme.ProgramacionMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProgramacionMobileTheme {
                PantallaRecordatorios()
            }
        }
    }
}
