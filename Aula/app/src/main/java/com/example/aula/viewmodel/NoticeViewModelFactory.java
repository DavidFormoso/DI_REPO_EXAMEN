package com.example.aula.viewmodel; // Declaración del paquete de ViewModels

import androidx.annotation.NonNull; // Anotación para parámetros que no pueden ser nulos
import androidx.lifecycle.ViewModel; // Clase base para los ViewModels
import androidx.lifecycle.ViewModelProvider; // Interfaz para la creación de ViewModels

import com.example.aula.data.InMemoryNoticeRepository; // Repositorio de avisos en memoria

public class NoticeViewModelFactory implements ViewModelProvider.Factory { // Clase factoría para instanciar NoticeViewModel con parámetros

    private final InMemoryNoticeRepository repo; // Referencia al repositorio que se inyectará en el ViewModel

    public NoticeViewModelFactory(InMemoryNoticeRepository repo) { // Constructor que recibe el repositorio
        this.repo = repo;
    }

    @NonNull // Indica que el objeto retornado no será nulo
    @Override // Sobrescribe el método de creación de la factoría
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) { // Método para crear la instancia del ViewModel
        if (modelClass.isAssignableFrom(NoticeViewModel.class)) { // Comprueba si la clase solicitada es NoticeViewModel
            return (T) new NoticeViewModel(repo); // Retorna una nueva instancia pasando el repositorio
        }
        throw new IllegalArgumentException("Unknown ViewModel class"); // Lanza una excepción si la clase no es la esperada
    }
}
