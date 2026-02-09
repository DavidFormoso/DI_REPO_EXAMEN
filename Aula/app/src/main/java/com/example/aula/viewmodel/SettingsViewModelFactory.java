package com.example.aula.viewmodel; // Declaración del paquete de ViewModels

import android.content.Context; // Importación del contexto de Android

import androidx.lifecycle.ViewModel; // Clase base para los ViewModels
import androidx.lifecycle.ViewModelProvider; // Interfaz para la creación de ViewModels

import com.example.aula.data.SettingsRepository; // Repositorio de configuración

public class SettingsViewModelFactory implements ViewModelProvider.Factory { // Clase factoría para instanciar SettingsViewModel

    private final Context context; // Variable para almacenar el contexto de la aplicación

    public SettingsViewModelFactory(Context context) { // Constructor que recibe el contexto
        this.context = context.getApplicationContext(); // Guarda el contexto de la aplicación para evitar fugas de memoria
    }

    @Override // Sobrescribe el método de creación de la factoría
    public <T extends ViewModel> T create(Class<T> modelClass) { // Método para crear la instancia del ViewModel
        if (modelClass.isAssignableFrom(SettingsViewModel.class)) { // Comprueba si la clase solicitada es SettingsViewModel
            return (T) new SettingsViewModel(new SettingsRepository(context)); // Retorna una nueva instancia pasando el repositorio inicializado
        }
        throw new IllegalArgumentException("Unknown ViewModel class"); // Lanza una excepción si la clase no es la esperada
    }
}
