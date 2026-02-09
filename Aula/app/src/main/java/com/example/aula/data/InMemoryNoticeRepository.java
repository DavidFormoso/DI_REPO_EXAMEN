package com.example.aula.data; // Declaración del paquete de datos


import java.util.ArrayList; // Importación para el uso de listas dinámicas
import java.util.List; // Interfaz para el manejo de colecciones de tipo lista

public class InMemoryNoticeRepository { // Clase repositorio para gestionar avisos en memoria


    private static InMemoryNoticeRepository instance; // Instancia única para el patrón Singleton
    private final List<String> database = new ArrayList<>(); // Lista que actúa como base de datos temporal

    public InMemoryNoticeRepository() { // Constructor de la clase
        database.add("DI | Entrega README"); // Añade un aviso inicial por defecto
        database.add("SI | Repasar permisos Linux"); // Añade otro aviso inicial por defecto
    }

    public List<String> getAll() { // Método para obtener todos los avisos
        return new ArrayList<>(database); // Retorna una copia de la lista actual
    }
    public static synchronized InMemoryNoticeRepository getInstance() { // Método estático para obtener la instancia única
        if (instance == null) instance = new InMemoryNoticeRepository(); // Crea la instancia si no existe
        return instance; // Retorna la instancia compartida
    }
    public void add(String noticeText) { // Método para añadir un nuevo aviso
        database.add(0, noticeText); // Inserta el aviso al principio de la lista
    }

    public boolean removeLast() { // Método para eliminar el último aviso
        if (database.isEmpty()) return false; // Si la lista está vacía, no hace nada y retorna falso
        database.remove(database.size() - 1); // Elimina el elemento en la última posición
        return true; // Retorna verdadero tras la eliminación
    }
}
