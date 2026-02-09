package com.example.aula.ui; // Declaración del paquete de la interfaz de usuario

import android.os.Bundle; // Importación para el manejo del estado de la instancia

import androidx.annotation.NonNull; // Anotación para parámetros no nulos
import androidx.annotation.Nullable; // Anotación para parámetros que pueden ser nulos
import androidx.fragment.app.Fragment; // Clase base para los fragmentos
import androidx.lifecycle.ViewModelProvider; // Proveedor para la gestión de ViewModels
import androidx.navigation.fragment.NavHostFragment; // Herramienta para la navegación entre fragmentos

import android.os.Handler; // Importación para el manejo de hilos y retrasos
import android.os.Looper; // Importación para el manejo del bucle de mensajes del hilo principal
import android.view.LayoutInflater; // Clase para inflar diseños XML
import android.view.View; // Clase base para componentes visuales
import android.view.ViewGroup; // Clase base para contenedores de vistas

import com.example.aula.R; // Referencia a los recursos del proyecto
import com.example.aula.data.AuthRepository; // Repositorio de datos de autenticación
import com.example.aula.viewmodel.AuthViewModel; // ViewModel para la lógica de autenticación
import com.example.aula.viewmodel.AuthViewModelFactory; // Factoría para instanciar el ViewModel con dependencias
import com.google.firebase.auth.FirebaseAuth; // Clase para la autenticación con Firebase


public class AuthGateFragment extends Fragment { // Fragmento que actúa como puerta de enlace según el estado de la sesión
    private AuthViewModel vm; // Declaración de la variable para el ViewModel
    public AuthGateFragment() { // Constructor del fragmento
        super(R.layout.fragment_auth_gate); // Asocia el diseño XML correspondiente
    }

    @Override // Indica que se sobrescribe un método de la clase padre
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { // Se ejecuta tras crear la vista
        AuthRepository repo = new AuthRepository(); // Crea una instancia del repositorio de autenticación
        AuthViewModelFactory factory = new AuthViewModelFactory(repo); // Crea la factoría necesaria para el ViewModel
        vm = new ViewModelProvider(this, factory).get(AuthViewModel.class); // Obtiene el ViewModel para este fragmento

        vm.getNavEvent().observe(getViewLifecycleOwner(), event -> { // Observa eventos de navegación desde el ViewModel
            if (event == null) return; // Si no hay evento, no hace nada

            if ("HOME".equals(event)) { // Si el evento indica que el usuario está logueado (HOME)
                NavHostFragment.findNavController(this) // Obtiene el controlador de navegación
                        .navigate(R.id.action_gate_to_home); // Navega a la pantalla principal

            } else if ("LOGIN".equals(event)) { // Si el evento indica que no hay sesión (LOGIN)
                NavHostFragment.findNavController(this) // Obtiene el controlador de navegación
                        .navigate(R.id.action_gate_to_login); // Navega a la pantalla de inicio de sesión
            }

            vm.consumeNavEvent(); // Notifica al ViewModel que el evento ya ha sido procesado
        });

        vm.checkSession(); // Solicita al ViewModel que compruebe si existe una sesión activa
    }
}
