package com.example.aula.data; // Declaración del paquete de datos

import android.content.Context; // Importación del contexto de la aplicación
import android.content.SharedPreferences; // Importación para el almacenamiento de preferencias

public class SettingsRepository { // Clase repositorio para gestionar la configuración de la aplicación

    private static final String PREFS_NAME = "settings"; // Nombre del archivo de preferencias
    private static final String KEY_DARK_MODE = "dark_mode"; // Clave para almacenar el estado del modo oscuro

    private final SharedPreferences prefs; // Variable para manejar las preferencias compartidas

    public SettingsRepository(Context context) { // Constructor que recibe el contexto
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); // Inicializa las preferencias en modo privado
    }

    public void setDarkMode(boolean enabled) { // Método para guardar el estado del modo oscuro
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply(); // Guarda el valor de forma asíncrona
    }

    public boolean isDarkModeEnabled() { // Método para obtener si el modo oscuro está activado
        return prefs.getBoolean(KEY_DARK_MODE, false); // Retorna el valor guardado, por defecto falso
    }
}
