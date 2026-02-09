package com.example.aula.ui; // Declaración del paquete de la interfaz de usuario

import android.os.Bundle; // Importación para el manejo del estado de la instancia
import android.view.View; // Importación para la gestión de vistas
import android.widget.TextView; // Importación para componentes de texto

import androidx.annotation.NonNull; // Anotación para parámetros no nulos
import androidx.annotation.Nullable; // Anotación para parámetros que pueden ser nulos
import androidx.fragment.app.Fragment; // Clase base para los fragmentos
import androidx.lifecycle.ViewModelProvider; // Proveedor para la gestión de ViewModels
import androidx.navigation.fragment.NavHostFragment; // Herramienta para la navegación entre fragmentos

import com.example.aula.R; // Referencia a los recursos del proyecto
import com.example.aula.data.AuthRepository; // Repositorio de datos de autenticación
import com.example.aula.viewmodel.AuthViewModel; // ViewModel para la lógica de autenticación
import com.example.aula.viewmodel.AuthViewModelFactory; // Factoría para instanciar el ViewModel con dependencias
import com.google.android.material.button.MaterialButton; // Botón con estilo Material Design

public class ProfileFragment extends Fragment { // Definición de la clase del fragmento de perfil

    private AuthViewModel vm; // Declaración de la variable para el ViewModel

    public ProfileFragment(){ // Constructor del fragmento
        super(R.layout.fragment_profile); // Asocia el diseño XML correspondiente
    }

    // ¡OJO! Aquí estaba el fallo: "onViewCreated" tiene que ser en MINÚSCULA
    // Además ponemos @Override para que el compilador nos ayude
    @Override // Indica que se sobrescribe un método de la clase padre
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){ // Se ejecuta tras crear la vista
        super.onViewCreated(view, savedInstanceState); // Llama a la implementación de la clase padre

        // 1. Buscamos los elementos del XML
        TextView Username = view.findViewById(R.id.textViewUsername); // Vincula el texto para mostrar el nombre de usuario
        MaterialButton botonLogout = view.findViewById(R.id.btnLogout); // Vincula el botón de cierre de sesión

        // 2. Configuramos el Mayordomo (ViewModel)
        AuthRepository repo = new AuthRepository(); // Crea una instancia del repositorio de autenticación
        AuthViewModelFactory factory = new AuthViewModelFactory(repo); // Crea la factoría necesaria para el ViewModel

        // Usamos requireActivity() para compartir el mismo ViewModel que el Login/Registro
        vm = new ViewModelProvider(requireActivity(), factory).get(AuthViewModel.class); // Obtiene el ViewModel compartido

        // 3. Observamos el Email para ponerlo en el texto
        vm.getUserEmail().observe(getViewLifecycleOwner(), email -> { // Observa cambios en el email del usuario
            if (email != null) { // Si el email no es nulo
                Username.setText(email); // Lo muestra en la pantalla
            }
        });

        // 4. Observamos la navegación (para saber cuándo irnos al Login)
        vm.getNavEvent().observe(getViewLifecycleOwner(), event -> { // Observa eventos de navegación
            if ("LOGIN".equals(event)) { // Si el evento indica ir al login
                NavHostFragment.findNavController(this).navigate(R.id.action_fragment_profile_to_loginFragment); // Navega al fragmento de login
                vm.consumeNavEvent(); // Notifica que el evento ha sido procesado
            }
        });

        // 5. El clic del botón: Llama a la función de logout del ViewModel
        botonLogout.setOnClickListener(v -> vm.logout()); // Cierra la sesión al pulsar el botón

        // 6. Pedimos cargar el email al entrar
        vm.loadUserEmail(); // Solicita la carga de la información del usuario
    }
}
