package com.example.aula.ui; // Declaración del paquete de la interfaz de usuario

import android.os.Bundle; // Importación para el manejo del estado de la instancia
import android.view.View; // Importación para la gestión de vistas
import android.widget.TextView; // Importación para componentes de texto

import androidx.annotation.NonNull; // Anotación para parámetros no nulos
import androidx.annotation.Nullable; // Anotación para parámetros que pueden ser nulos
import androidx.appcompat.app.AppCompatDelegate; // Delegado para gestionar el tema de la aplicación (modo oscuro)
import androidx.fragment.app.Fragment; // Clase base para los fragmentos
import androidx.lifecycle.ViewModelProvider; // Proveedor para la gestión de ViewModels
import androidx.navigation.fragment.NavHostFragment; // Herramienta para la navegación entre fragmentos

import com.example.aula.R; // Referencia a los recursos del proyecto
import com.example.aula.data.InMemoryNoticeRepository; // Repositorio de avisos en memoria
import com.example.aula.viewmodel.NoticeViewModel; // ViewModel para la lógica de avisos
import com.example.aula.viewmodel.NoticeViewModelFactory; // Factoría para el ViewModel de avisos
import com.example.aula.viewmodel.SettingsViewModel; // ViewModel para la configuración (modo oscuro)
import com.example.aula.viewmodel.SettingsViewModelFactory; // Factoría para el ViewModel de configuración
import com.google.android.material.button.MaterialButton; // Botón con estilo Material Design
import com.google.android.material.snackbar.Snackbar; // Mensajes emergentes en la parte inferior
import com.google.android.material.materialswitch.MaterialSwitch; // Interruptor con estilo Material Design
import com.google.android.material.textfield.TextInputLayout; // Contenedor para el campo de texto con soporte de errores
import com.google.firebase.auth.FirebaseAuth; // Clase para la autenticación con Firebase

public class HomeFragment extends Fragment { // Definición de la clase del fragmento de inicio

    public HomeFragment() { // Constructor del fragmento
        super(R.layout.fragment_home); // Asocia el diseño XML correspondiente
    }

    @Override // Indica que se sobrescribe un método de la clase padre
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { // Se ejecuta tras crear la vista

        TextInputLayout tilTitle = view.findViewById(R.id.tilTitle); // Vincula el contenedor del título del aviso
        TextInputLayout tilSubject = view.findViewById(R.id.tilSubject); // Vincula el contenedor de la asignatura

        MaterialButton btnProfile = view.findViewById(R.id.btnProfile); // Vincula el botón para ir al perfil
        MaterialButton btnAdd = view.findViewById(R.id.btnAdd); // Vincula el botón para añadir aviso
        MaterialButton btnDeleteLast = view.findViewById(R.id.btnDeleteLast); // Vincula el botón para borrar el último aviso
        MaterialButton btnLogout = view.findViewById(R.id.btnLogout); // Vincula el botón para cerrar sesión

        TextView tvList = view.findViewById(R.id.tvList); // Vincula el texto donde se muestra el listado de avisos

        MaterialSwitch switchDark = view.findViewById(R.id.switchDarkMode); // Vincula el interruptor de modo oscuro

        SettingsViewModelFactory settingsFactory = new SettingsViewModelFactory(requireContext()); // Crea la factoría para el ViewModel de ajustes
        SettingsViewModel settingsVm = new ViewModelProvider(this, settingsFactory) // Obtiene el ViewModel de ajustes
                .get(SettingsViewModel.class);

        settingsVm.getDarkMode().observe(getViewLifecycleOwner(), enabled -> { // Observa cambios en la preferencia del modo oscuro
            if (enabled == null) return; // Si el valor es nulo, no hace nada

            if (switchDark.isChecked() != enabled) { // Sincroniza el interruptor si su estado no coincide con el guardado
                switchDark.setChecked(enabled);
            }

            AppCompatDelegate.setDefaultNightMode( // Cambia el tema de la aplicación según el valor
                    enabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        switchDark.setOnCheckedChangeListener((buttonView, isChecked) -> { // Configura la acción al cambiar el interruptor
            settingsVm.setDarkMode(isChecked); // Guarda la nueva preferencia en el ViewModel
        });

        InMemoryNoticeRepository repo = InMemoryNoticeRepository.getInstance(); // Obtiene la instancia del repositorio de avisos
        NoticeViewModelFactory noticeFactory = new NoticeViewModelFactory(repo); // Crea la factoría para el ViewModel de avisos
        NoticeViewModel noticeVm = new ViewModelProvider(this, noticeFactory) // Obtiene el ViewModel de avisos
                .get(NoticeViewModel.class);

        noticeVm.getListado().observe(getViewLifecycleOwner(), tvList::setText); // Actualiza el TextView con el listado de avisos

        noticeVm.getError().observe(getViewLifecycleOwner(), err -> { // Observa errores en la gestión de avisos
            if (err != null) Snackbar.make(view, err, Snackbar.LENGTH_LONG).show(); // Muestra el error en un Snackbar
        });

        noticeVm.getEventoToast().observe(getViewLifecycleOwner(), msg -> { // Observa eventos informativos (añadido/borrado)
            if (msg != null) { // Si hay un mensaje
                Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show(); // Muestra el mensaje informativo
                noticeVm.consumirEventoToast(); // Notifica al ViewModel que el evento ya se mostró
            }
        });

        btnAdd.setOnClickListener(v -> { // Configura la acción al pulsar el botón de añadir
            tilTitle.setError(null); // Limpia errores previos del título

            if (tilTitle.getEditText() == null || tilSubject.getEditText() == null) { // Seguridad: comprueba que los campos existen
                Snackbar.make(view, "Error en el layout: falta EditText dentro del TextInputLayout", Snackbar.LENGTH_LONG).show();
                return;
            }

            String title = tilTitle.getEditText().getText().toString().trim(); // Obtiene el texto del título
            String subject = tilSubject.getEditText().getText().toString().trim(); // Obtiene el texto de la asignatura

            noticeVm.addNotice(title, subject); // Envía los datos al ViewModel para añadir el aviso
            tilTitle.getEditText().setText(""); // Limpia el campo del título tras añadir
        });

        btnDeleteLast.setOnClickListener(v -> noticeVm.deleteLast()); // Configura la acción para borrar el último aviso

        btnLogout.setOnClickListener(v -> { // Configura la acción para cerrar sesión
            FirebaseAuth.getInstance().signOut(); // Cierra sesión en Firebase
            NavHostFragment.findNavController(this) // Obtiene el controlador de navegación
                    .navigate(R.id.action_home_to_authGate); // Vuelve a la pantalla de validación inicial
        });

        btnProfile.setOnClickListener(v -> { // Configura la acción para ver el perfil
            NavHostFragment.findNavController(this) // Obtiene el controlador de navegación
                    .navigate(R.id.action_homeFragment_to_fragment_profile); // Navega al fragmento de perfil

        });
    }
}
