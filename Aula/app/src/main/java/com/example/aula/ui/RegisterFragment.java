package com.example.aula.ui; // Declaración del paquete de la interfaz de usuario

// Importaciones para el manejo de componentes de Android, navegación y lógica de negocio
import android.os.Bundle; // Manejo del estado de la instancia
import android.text.Editable; // Clase para manejar texto editable
import android.text.TextWatcher; // Interfaz para observar cambios en campos de texto
import android.view.View; // Clase base para componentes visuales
import androidx.annotation.NonNull; // Anotación para parámetros no nulos
import androidx.annotation.Nullable; // Anotación para parámetros que pueden ser nulos
import androidx.fragment.app.Fragment; // Clase base para fragmentos
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

public class RegisterFragment extends Fragment { // Definición de la clase del fragmento de registro

    private AuthViewModel vm; // Declaración de la variable para el ViewModel

    public RegisterFragment() { // Constructor del fragmento
        super(R.layout.fragment_register); // Asocia el diseño XML correspondiente
    }

    @Override // Indica que se sobrescribe un método de la clase padre
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { // Se ejecuta tras crear la vista

        // --- IDENTIFICACIÓN DE ELEMENTOS VISUALES ---
        TextInputLayout tilEmail = view.findViewById(R.id.tilEmail); // Vincula el contenedor del campo email
        TextInputLayout tilPass = view.findViewById(R.id.tilPass); // Vincula el contenedor del campo contraseña
        TextInputLayout tilPass2 = view.findViewById(R.id.tilPass2); // Vincula el contenedor de confirmación de contraseña

        TextInputEditText etEmail = view.findViewById(R.id.etEmail); // Vincula el editor de texto del email
        TextInputEditText etPass = view.findViewById(R.id.etPass); // Vincula el editor de texto de la contraseña
        TextInputEditText etPass2 = view.findViewById(R.id.etPass2); // Vincula el editor de texto de confirmación

        MaterialButton btnRegister = view.findViewById(R.id.btnRegister); // Vincula el botón de registro
        MaterialButton btnGoLogin = view.findViewById(R.id.btnGoLogin); // Vincula el botón para volver al login

        CircularProgressIndicator progress = view.findViewById(R.id.progress); // Vincula el indicador de progreso

        // --- CONFIGURACIÓN DEL CEREBRO (VIEWMODEL) ---
        AuthRepository repo = new AuthRepository(); // Crea una instancia del repositorio de autenticación
        AuthViewModelFactory factory = new AuthViewModelFactory(repo); // Crea la factoría para el ViewModel
        vm = new ViewModelProvider(requireActivity(), factory).get(AuthViewModel.class); // Obtiene el ViewModel compartido

        // --- LIMPIADOR DE ERRORES AUTOMÁTICO ---
        TextWatcher clearErrorsWatcher = new TextWatcher() { // Define un observador para los cambios de texto
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {} // Método previo al cambio
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { // Método durante el cambio
                tilEmail.setError(null); // Limpia el error del email al escribir
                tilPass.setError(null); // Limpia el error de la contraseña al escribir
                tilPass2.setError(null); // Limpia el error de confirmación al escribir
            }
            @Override public void afterTextChanged(Editable s) {} // Método posterior al cambio
        };
        etEmail.addTextChangedListener(clearErrorsWatcher); // Asocia el vigilante al campo email
        etPass.addTextChangedListener(clearErrorsWatcher); // Asocia el vigilante al campo contraseña
        etPass2.addTextChangedListener(clearErrorsWatcher); // Asocia el vigilante al campo de confirmación

        // --- OBSERVADORES (ESTAR ATENTO A CAMBIOS) ---

        vm.getLoading().observe(getViewLifecycleOwner(), isLoading -> { // Observa el estado de carga
            boolean loading = Boolean.TRUE.equals(isLoading); // Determina si se está cargando
            btnRegister.setEnabled(!loading); // Deshabilita el botón de registro si está cargando
            btnGoLogin.setEnabled(!loading); // Deshabilita el botón de login si está cargando
            tilEmail.setEnabled(!loading); // Deshabilita el campo email si está cargando
            tilPass.setEnabled(!loading); // Deshabilita el campo contraseña si está cargando
            tilPass2.setEnabled(!loading); // Deshabilita el campo confirmación si está cargando

            if (loading) progress.show(); // Muestra la rueda de progreso si está cargando
            else progress.hide(); // Oculta la rueda de progreso si no está cargando
        });

        vm.getErrorMessage().observe(getViewLifecycleOwner(), msg -> { // Observa los mensajes de error
            if (msg != null) { // Si hay un mensaje
                Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show(); // Lo muestra en un Snackbar
            }
        });

        vm.getNavEvent().observe(getViewLifecycleOwner(), event -> { // Observa eventos de navegación
            if (event == null) return; // Si no hay evento, no hace nada

            if ("HOME".equals(event)) { // Si el evento es navegar a HOME
                NavHostFragment.findNavController(this).navigate(R.id.action_register_to_home); // Realiza la navegación
            }

            vm.consumeNavEvent(); // Notifica que el evento ha sido procesado
        });

        // --- CONFIGURACIÓN DE CLICS ---

        btnGoLogin.setOnClickListener(v -> // Acción al pulsar volver al login
                NavHostFragment.findNavController(this).navigate(R.id.action_register_to_login) // Navega al fragmento de login
        );

        btnRegister.setOnClickListener(v -> { // Acción al pulsar el botón de registro

            tilEmail.setError(null); // Resetea error del email
            tilPass.setError(null); // Resetea error de contraseña
            tilPass2.setError(null); // Resetea error de confirmación

            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : ""; // Obtiene el email
            String pass = etPass.getText() != null ? etPass.getText().toString() : ""; // Obtiene la contraseña
            String pass2 = etPass2.getText() != null ? etPass2.getText().toString() : ""; // Obtiene la confirmación

            boolean ok = true; // Variable de validación
            if (email.isEmpty()) { // Valida si el email está vacío
                tilEmail.setError("Email obligatorio"); // Muestra mensaje de error
                ok = false;
            } else if (!email.contains("@")) { // Valida el formato del email
                tilEmail.setError("Email no válido"); // Muestra mensaje de error
                ok = false;
            }

            if (pass.isEmpty()) { // Valida si la contraseña está vacía
                tilPass.setError("Contraseña obligatoria"); // Muestra mensaje de error
                ok = false;
            } else if (pass.length() < 6) { // Valida la longitud mínima
                tilPass.setError("Mínimo 6 caracteres"); // Muestra mensaje de error
                ok = false;
            }

            if (pass2.isEmpty()) { // Valida si la confirmación está vacía
                tilPass2.setError("Repite la contraseña"); // Muestra mensaje de error
                ok = false;
            } else if (!pass2.equals(pass)) { // Valida que ambas coincidan
                tilPass2.setError("Las contraseñas no coinciden"); // Muestra mensaje de error
                ok = false;
            }

            if (!ok) return; // Si hay errores, detiene el proceso

            vm.register(email, pass); // Solicita el registro al ViewModel
        });
    }
}
