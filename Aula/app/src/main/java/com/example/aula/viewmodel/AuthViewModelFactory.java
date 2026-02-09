package com.example.aula.viewmodel; // Declaración del paquete de ViewModels

import androidx.lifecycle.ViewModel; // Importación de la clase base ViewModel
import androidx.lifecycle.ViewModelProvider; // Interfaz para la creación de ViewModels

import com.example.aula.data.AuthRepository; // Repositorio de autenticación

public class AuthViewModelFactory implements ViewModelProvider.Factory { // Clase factoría para instanciar AuthViewModel con parámetros

    private final AuthRepository repo; // Referencia al repositorio que se inyectará en el ViewModel

    public AuthViewModelFactory(AuthRepository repo) { // Constructor que recibe el repositorio
        this.repo = repo;
    }

    @Override // Sobrescribe el método de creación de la factoría
    public <T extends ViewModel> T create(Class<T> modelClass) { // Método para crear la instancia del ViewModel
        if (modelClass.isAssignableFrom(AuthViewModel.class)) { // Comprueba si la clase solicitada es AuthViewModel
            return (T) new AuthViewModel(repo); // Retorna una nueva instancia pasando el repositorio
        }
        throw new IllegalArgumentException("Unknown ViewModel class"); // Lanza una excepción si la clase no es la esperada
    }
}