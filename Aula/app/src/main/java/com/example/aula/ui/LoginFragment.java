package com.example.aula.ui; // Declaración del paquete de la interfaz de usuario

import android.os.Bundle; // Importación para el manejo del estado de la instancia
import android.view.View; // Importación para la gestión de vistas

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
import com.google.android.material.progressindicator.CircularProgressIndicator; // Indicador de carga circular
import com.google.android.material.snackbar.Snackbar; // Mensajes emergentes en la parte inferior
import com.google.android.material.textfield.TextInputEditText; // Campo de entrada de texto
import com.google.android.material.textfield.TextInputLayout; // Contenedor para el campo de texto con soporte de errores

public class LoginFragment extends Fragment { // Definición de la clase del fragmento de inicio de sesión

    private AuthViewModel vm; // Declaración de la variable para el ViewModel

    public LoginFragment() { super(R.layout.fragment_login); } // Constructor que vincula el diseño XML del fragmento

    @Override // Indica que se sobrescribe un método de la clase padre
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { // Se ejecuta tras crear la vista

        TextInputLayout tilEmail = view.findViewById(R.id.tilEmail); // Vincula el contenedor del campo email
        TextInputLayout tilPass = view.findViewById(R.id.tilPass); // Vincula el contenedor del campo contraseña
        TextInputEditText etEmail = view.findViewById(R.id.etEmail); // Vincula el editor de texto del email
        TextInputEditText etPass = view.findViewById(R.id.etPass); // Vincula el editor de texto de la contraseña

        MaterialButton btnLogin = view.findViewById(R.id.btnLogin); // Vincula el botón de inicio de sesión
        MaterialButton btnGoRegister = view.findViewById(R.id.btnGoRegister); // Vincula el botón para ir al registro
        CircularProgressIndicator progress = view.findViewById(R.id.progress); // Vincula el indicador de progreso

        AuthRepository repo = new AuthRepository(); // Crea una instancia del repositorio de autenticación
        AuthViewModelFactory factory = new AuthViewModelFactory(repo); // Crea la factoría necesaria para el ViewModel
        vm = new ViewModelProvider(requireActivity(), factory).get(AuthViewModel.class); // Obtiene el ViewModel compartido con la actividad

        vm.getLoading().observe(getViewLifecycleOwner(), isLoading -> { // Observa el estado de carga desde el ViewModel
            boolean loading = Boolean.TRUE.equals(isLoading); // Verifica si el estado es cargando
            btnLogin.setEnabled(!loading); // Deshabilita el botón de login si está cargando
            btnGoRegister.setEnabled(!loading); // Deshabilita el botón de registro si está cargando
            if (loading) { // Si se está realizando una operación
                progress.show(); // Muestra el indicador visual de progreso
            } else { // Si la operación ha terminado
                progress.hide(); // Oculta el indicador visual de progreso
            }
        });

        vm.getErrorMessage().observe(getViewLifecycleOwner(), msg -> { // Observa los mensajes de error
            if (msg != null) { // Si existe un mensaje de error
                Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show(); // Muestra el error mediante un Snackbar
            }
        });

        vm.getNavEvent().observe(getViewLifecycleOwner(), event -> { // Observa los eventos de navegación
            if (event == null) return; // Si no hay evento, sale de la función
            if (event.equals("HOME")) { // Si el evento indica ir a la pantalla de inicio
                NavHostFragment.findNavController(this) // Obtiene el controlador de navegación
                        .navigate(R.id.action_login_to_home); // Realiza la transición a la pantalla Home
            }
            vm.consumeNavEvent(); // Notifica al ViewModel que el evento ya ha sido procesado
        });

        btnGoRegister.setOnClickListener(v -> // Configura la acción al pulsar el botón de registro
                NavHostFragment.findNavController(this) // Obtiene el controlador de navegación
                        .navigate(R.id.action_login_to_register) // Navega hacia el fragmento de registro
        );

        btnLogin.setOnClickListener(v -> { // Configura la acción al pulsar el botón de login
            tilEmail.setError(null); // Resetea cualquier error previo en el email
            tilPass.setError(null); // Resetea cualquier error previo en la contraseña

            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : ""; // Obtiene y limpia el texto del email
            String pass = etPass.getText() != null ? etPass.getText().toString() : ""; // Obtiene el texto de la contraseña

            boolean ok = true; // Variable de control para la validación
            if (email.isEmpty()) { tilEmail.setError("Email obligatorio"); ok = false; } // Valida que el email no esté vacío
            else if (!email.contains("@")) { tilEmail.setError("Email no válido"); ok = false; } // Valida el formato del email

            if (pass.isEmpty()) { tilPass.setError("Contraseña obligatoria"); ok = false; } // Valida que la contraseña no esté vacía
            else if (pass.length() < 6) { tilPass.setError("Mínimo 6 caracteres"); ok = false; } // Valida la longitud de la contraseña

            if (!ok) return; // Si alguna validación falló, detiene la ejecución

            vm.login(email, pass); // Solicita al ViewModel que realice el inicio de sesión
        });
    }
}
