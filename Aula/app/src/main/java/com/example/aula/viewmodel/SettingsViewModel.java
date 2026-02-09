package com.example.aula.viewmodel; // Declaración del paquete de ViewModels

import androidx.lifecycle.LiveData; // Importación para el manejo de datos observables inmutables
import androidx.lifecycle.MutableLiveData; // Importación para el manejo de datos observables mutables
import androidx.lifecycle.ViewModel; // Clase base para los ViewModels

import com.example.aula.data.SettingsRepository; // Repositorio de configuración

public class SettingsViewModel extends ViewModel { // Clase ViewModel para gestionar la configuración de la aplicación

    private final SettingsRepository repo; // Referencia al repositorio de configuración
    private final MutableLiveData<Boolean> _darkMode = new MutableLiveData<>(); // Estado del modo oscuro (mutable)

    public SettingsViewModel(SettingsRepository repo) { // Constructor que recibe el repositorio
        this.repo = repo;
        _darkMode.setValue(repo.isDarkModeEnabled()); // Inicializa el estado con el valor guardado en el repositorio
    }

    public LiveData<Boolean> getDarkMode() { // Expone el estado del modo oscuro como LiveData inmutable
        return _darkMode;
    }

    public void setDarkMode(boolean enabled) { // Método para cambiar el estado del modo oscuro
        repo.setDarkMode(enabled); // Guarda la nueva configuración en el repositorio
        _darkMode.setValue(enabled); // Actualiza el LiveData para notificar a los observadores
    }
}
