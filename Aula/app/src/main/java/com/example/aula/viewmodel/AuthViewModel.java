package com.example.aula.viewmodel; // Declaración del paquete de ViewModels

import androidx.lifecycle.LiveData; // Importación para el manejo de datos observables inmutables
import androidx.lifecycle.MutableLiveData; // Importación para el manejo de datos observables mutables
import androidx.lifecycle.ViewModel; // Clase base para los ViewModels

import com.example.aula.data.AuthRepository; // Repositorio de autenticación

public class AuthViewModel extends ViewModel { // Clase ViewModel para gestionar el estado de la autenticación

    private final AuthRepository repo; // Referencia al repositorio de datos

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false); // Estado de carga (mutable internamente)
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null); // Mensaje de error (mutable internamente)
    private final MutableLiveData<String> _navEvent = new MutableLiveData<>(null); // Evento de navegación (mutable internamente)
    private final MutableLiveData<String> _currentUserEmail = new MutableLiveData<>(null); // Email del usuario actual (mutable internamente)
    
    public AuthViewModel(AuthRepository repo) { // Constructor que recibe el repositorio
        this.repo = repo;
    }

    public LiveData<Boolean> getLoading() { return _loading; } // Expone el estado de carga como LiveData inmutable
    public LiveData<String> getErrorMessage() { return _errorMessage; } // Expone el mensaje de error como LiveData inmutable
    public LiveData<String> getNavEvent() { return _navEvent; } // Expone el evento de navegación como LiveData inmutable
    public LiveData<String> getUserEmail() {return _currentUserEmail;} // Expone el email del usuario como LiveData inmutable


    public void consumeNavEvent() { _navEvent.setValue(null); } // Método para resetear el evento de navegación tras procesarlo

    public void login(String email, String pass) { // Método para realizar el inicio de sesión
        _errorMessage.setValue(null); // Resetea el mensaje de error
        _loading.setValue(true); // Activa el estado de carga

        repo.login(email, pass, new AuthRepository.AuthCallback() { // Llama al repositorio para el login
            @Override public void onSuccess() { // Si tiene éxito
                _loading.postValue(false); // Desactiva la carga (uso de postValue para hilos secundarios)
                _navEvent.postValue("HOME"); // Dispara el evento de navegación a HOME
            }

            @Override public void onError(String message) { // Si hay un error
                _loading.postValue(false); // Desactiva la carga
                _errorMessage.postValue(message); // Publica el mensaje de error
            }
        });
    }

    public void register(String email, String pass) { // Método para realizar el registro de usuario
        _errorMessage.setValue(null); // Resetea el mensaje de error
        _loading.setValue(true); // Activa el estado de carga

        repo.register(email, pass, new AuthRepository.AuthCallback() { // Llama al repositorio para el registro
            @Override public void onSuccess() { // Si tiene éxito
                _loading.postValue(false); // Desactiva la carga
                _navEvent.postValue("HOME"); // Dispara el evento de navegación a HOME
            }

            @Override public void onError(String message) { // Si hay un error
                _loading.postValue(false); // Desactiva la carga
                _errorMessage.postValue(message); // Publica el mensaje de error
            }
        });
    }

    public void checkSession() { // Método para comprobar si hay una sesión activa
        _navEvent.setValue(repo.isLoggedIn() ? "HOME" : "LOGIN"); // Decide a qué pantalla navegar según el estado de sesión
    }

    public void logout() { // Método para cerrar la sesión
        repo.logout(); // Llama al logout del repositorio
        _navEvent.setValue("LOGIN"); // Dispara el evento de navegación a LOGIN
    }

    public void loadUserEmail() { // Método para cargar el email del usuario logueado
        String email = repo.getCurrentUserEmail(); // Obtiene el email del repositorio

        _currentUserEmail.setValue(email); // Actualiza el LiveData con el email obtenido
    }


}
