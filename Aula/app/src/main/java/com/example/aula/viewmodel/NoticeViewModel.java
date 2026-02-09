package com.example.aula.viewmodel; // Declaración del paquete de ViewModels


import androidx.lifecycle.LiveData; // Importación para el manejo de datos observables inmutables
import androidx.lifecycle.MutableLiveData; // Importación para el manejo de datos observables mutables
import androidx.lifecycle.ViewModel; // Clase base para los ViewModels

import com.example.aula.data.InMemoryNoticeRepository; // Repositorio de avisos en memoria

import java.util.List; // Interfaz para el manejo de colecciones de tipo lista

public class NoticeViewModel extends ViewModel { // Clase ViewModel para gestionar la lógica de los avisos

    private final InMemoryNoticeRepository repo; // Referencia al repositorio de datos

    private final MutableLiveData<String> _listado = new MutableLiveData<>(); // Lista de avisos formateada como texto (mutable)
    private final MutableLiveData<String> _error = new MutableLiveData<>(); // Mensaje de error (mutable)
    private final MutableLiveData<String> _eventoToast = new MutableLiveData<>(null); // Evento para mostrar notificaciones Toast (mutable)

    public NoticeViewModel(InMemoryNoticeRepository repo) { // Constructor que recibe el repositorio
        this.repo = repo;
        refresh(); // Carga inicial de los datos
    }

    public LiveData<String> getListado() { return _listado; } // Expone el listado formateado como LiveData inmutable
    public LiveData<String> getError() { return _error; } // Expone los errores como LiveData inmutable
    public LiveData<String> getEventoToast() { return _eventoToast; } // Expone los eventos de Toast como LiveData inmutable

    public void consumirEventoToast() { // Método para resetear el evento del Toast tras mostrarlo
        _eventoToast.setValue(null);
    }

    public void addNotice(String title, String subject) { // Método para añadir un nuevo aviso
        if (title == null || title.trim().isEmpty()) { // Valida que el título no esté vacío
            _error.setValue("El título no puede estar vacío"); // Notifica error si el título es inválido
            return;
        }
        if (subject == null || subject.trim().isEmpty()) subject = "General"; // Asigna un tema por defecto si está vacío

        repo.add(subject.trim() + " | " + title.trim()); // Añade el aviso formateado al repositorio
        refresh(); // Actualiza la vista con los nuevos datos
        _eventoToast.setValue("Aviso añadido"); // Dispara el evento de confirmación
    }

    public void deleteLast() { // Método para borrar el último aviso añadido
        boolean ok = repo.removeLast(); // Llama al repositorio para borrar
        if (!ok) { // Si no se pudo borrar (estaba vacío)
            _error.setValue("No hay avisos para borrar"); // Notifica el error
            return;
        }
        refresh(); // Actualiza la vista
        _eventoToast.setValue("Último aviso borrado"); // Dispara el evento de confirmación
    }

    private void refresh() { // Método privado para actualizar el listado formateado
        List<String> data = repo.getAll(); // Obtiene todos los avisos del repositorio
        if (data.isEmpty()) { // Si no hay avisos
            _listado.setValue("Crea un aviso para que se muestre aquí"); // Muestra un mensaje de ayuda
            return;
        }
        StringBuilder sb = new StringBuilder(); // Acumulador para formatear la lista
        for (String s : data) sb.append("• ").append(s).append("\n"); // Añade viñetas y saltos de línea
        _listado.setValue(sb.toString()); // Actualiza el LiveData con el texto formateado
    }
}
