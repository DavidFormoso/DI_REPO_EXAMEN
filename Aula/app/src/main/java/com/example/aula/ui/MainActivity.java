package com.example.aula.ui; // Declaración del paquete de la interfaz de usuario

import android.os.Bundle; // Importación para el manejo del estado de la instancia
import androidx.appcompat.app.AppCompatActivity; // Clase base para actividades compatibles con versiones anteriores
import androidx.appcompat.app.AppCompatDelegate; // Delegado para gestionar el tema de la aplicación (modo oscuro)
import androidx.core.splashscreen.SplashScreen; // API para el manejo de la pantalla de carga (Splash Screen)

import com.example.aula.R; // Referencia a los recursos del proyecto
import com.example.aula.data.SettingsRepository; // Repositorio de configuración

public class MainActivity extends AppCompatActivity { // Definición de la actividad principal de la aplicación
    @Override // Indica que se sobrescribe un método de la clase padre
    protected void onCreate(Bundle savedInstanceState) { // Se ejecuta al crear la actividad
        SettingsRepository repo = new SettingsRepository(this); // Crea una instancia del repositorio de ajustes

        AppCompatDelegate.setDefaultNightMode( // Establece el tema nocturno global antes de cargar la interfaz
                repo.isDarkModeEnabled() // Comprueba si el modo oscuro está activado en las preferencias
                        ? AppCompatDelegate.MODE_NIGHT_YES // Si es verdadero, activa el modo noche
                        : AppCompatDelegate.MODE_NIGHT_NO // Si es falso, activa el modo día
        );
        SplashScreen.installSplashScreen(this); // Instala y gestiona la pantalla de bienvenida de Android
        super.onCreate(savedInstanceState); // Llama al constructor de la clase padre

        setContentView(R.layout.activity_main); // Establece el diseño XML de la actividad principal
    }
}
