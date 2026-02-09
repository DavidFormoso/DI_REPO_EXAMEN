package com.example.aula.data; // Declaración del paquete de datos

import com.google.firebase.auth.FirebaseAuth; // Importación de Firebase Auth para la autenticación

public class AuthRepository { // Clase repositorio para gestionar la autenticación

    public interface AuthCallback { // Interfaz para manejar las respuestas de las operaciones de autenticación
        void onSuccess(); // Método que se ejecuta en caso de éxito
        void onError(String message); // Método que se ejecuta en caso de error con un mensaje
    }

    private final FirebaseAuth auth = FirebaseAuth.getInstance(); // Instancia de FirebaseAuth para interactuar con Firebase

    public void login(String email, String pass, AuthCallback cb) { // Método para iniciar sesión con email y contraseña
        auth.signInWithEmailAndPassword(email, pass) // Llama al método de Firebase para iniciar sesión
                .addOnCompleteListener(task -> { // Añade un escuchador para cuando se complete la tarea
                    if (task.isSuccessful()) cb.onSuccess(); // Si fue exitoso, llama al callback de éxito
                    else cb.onError(parseAuthError(task.getException())); // Si falló, procesa el error y llama al callback de error
                });
    }

    public void register(String email, String pass, AuthCallback cb) { // Método para registrar un nuevo usuario
        auth.createUserWithEmailAndPassword(email, pass) // Llama al método de Firebase para crear un usuario
                .addOnCompleteListener(task -> { // Añade un escuchador para cuando se complete la tarea
                    if (task.isSuccessful()) cb.onSuccess(); // Si fue exitoso, llama al callback de éxito
                    else cb.onError(parseAuthError(task.getException())); // Si falló, procesa el error y llama al callback de error
                });

    }

    public boolean isLoggedIn() { // Método para comprobar si hay un usuario autenticado
        return auth.getCurrentUser() != null; // Retorna verdadero si el usuario actual no es nulo
    }

    public void logout() { // Método para cerrar la sesión del usuario
        auth.signOut(); // Llama al método de Firebase para cerrar sesión
    }

    private String parseAuthError(Exception e) { // Método privado para traducir los errores de Firebase a mensajes legibles
        if (e == null) return "Error desconocido"; // Si no hay excepción, retorna un error genérico
        String msg = e.getMessage(); // Obtiene el mensaje de la excepción
        if (msg != null) { // Si el mensaje no es nulo, busca patrones conocidos
            if (msg.contains("password") && msg.contains("invalid")) return "Credenciales inválidas"; // Error en contraseña
            if (msg.contains("no user record")) return "No existe una cuenta con ese email"; // Usuario no encontrado
            if (msg.contains("email address is already in use")) return "Ese email ya está registrado"; // Email duplicado
            if (msg.contains("network error")) return "Error de red. Revisa tu conexión"; // Error de conexión
        }
        return "No se pudo completar la operación"; // Mensaje por defecto para otros errores
    }

    public String getCurrentUserEmail() { // Método para obtener el email del usuario actual
        if (auth.getCurrentUser() != null) { // Si hay un usuario autenticado
            return auth.getCurrentUser().getEmail(); // Retorna su dirección de email
        }
        return "Usuario no identificado"; // Retorna un mensaje si no hay usuario
    }
}
