package edu.david.dmp.servicios; // Paquete que agrupa las clases relacionadas con los servicios.

import java.sql.Connection; // Importa la clase Connection para manejar conexiones a la base de datos.
import java.sql.PreparedStatement; // Importa la clase PreparedStatement para ejecutar sentencias SQL.
import java.sql.SQLException; // Importa la clase SQLException para manejar errores de SQL.
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner; // Importa la clase Scanner para leer entradas del usuario.

import edu.david.dmp.dtos.ClubDto;
import edu.david.dmp.dtos.UsuarioDto;

public class ConsultasPostgresqlImplementacion implements ConsultasPostgresqlInterfaz { // Clase que implementa la interfaz ConsultasPostgresqlInterfaz.

    Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario.

    // Método privado para insertar un nuevo usuario en la base de datos.
    private void insertarUsuario(Connection conexion, List<UsuarioDto> usuarios) {
       
    	
    	
    	String sql = "INSERT INTO dlk.\"Usuarios\" (id_usuario, nombre_usuario, email_usuario, telefono_usuario, club_usuario, moto_usuario) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            for (UsuarioDto usuario : usuarios) {
                // Establece los parámetros de la sentencia.
                pstmt.setLong(1, usuario.getId_usuario());
                pstmt.setString(2, usuario.getNombre_usuario());
                pstmt.setString(3, usuario.getEmail_usuario());
                pstmt.setString(4, usuario.getTelefono_usuario());
                pstmt.setString(5, usuario.getClub_usuario());
                pstmt.setString(6, usuario.getMoto_usuario());
                // Agrega la consulta a la batch
                pstmt.addBatch();
            }
            // Ejecuta el batch
            int[] filasInsertadas = pstmt.executeBatch();
            System.out.println(filasInsertadas.length + " usuarios fueron insertados exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al insertar los usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método público para solicitar datos del usuario y llamarlo para insertar en la base de datos.
    public void solicitarDatosYInsertarUsuario(Connection conexion, List<UsuarioDto> usuarios) {
        
        
        // Se utiliza un bucle para permitir la inserción de múltiples usuarios
        boolean continuar = true;
        
        while (continuar) {
            System.out.println("Introduce el ID del nuevo usuario:");
            long idNuevo = Long.parseLong(scanner.nextLine());

            System.out.println("Introduce el nombre del nuevo usuario:");
            String nombreNuevo = scanner.nextLine();

            System.out.println("Introduce el email del nuevo usuario:");
            String emailNuevo = scanner.nextLine();

            System.out.println("Introduce el teléfono del nuevo usuario:");
            String telefonoNuevo = scanner.nextLine();

            System.out.println("Introduce el club del nuevo usuario:");
            String clubNuevo = scanner.nextLine();

            System.out.println("Introduce la moto del nuevo usuario:");
            String motoNueva = scanner.nextLine();

            // Agrega el nuevo usuario a la lista
            usuarios.add(new UsuarioDto(idNuevo, nombreNuevo, emailNuevo, telefonoNuevo, clubNuevo, motoNueva));

            // Pregunta si quiere agregar otro usuario
            System.out.println("¿Deseas agregar otro usuario? (s/n):");
            String respuesta = scanner.nextLine();
            continuar = respuesta.equalsIgnoreCase("s");
        }

        // Llamada al método para insertar la lista de usuarios en la base de datos
        insertarUsuario(conexion, usuarios);
    }

    // Método privado para modificar un usuario existente en la base de datos.
    private void modificarUsuarios(Connection conexion, List<UsuarioDto> listaUsu) throws SQLException {
        // SQL para actualizar un registro existente en la tabla Usuarios.
        String sql = "UPDATE dlk.\"Usuarios\" SET nombre_usuario = ?, email_usuario = ?, telefono_usuario = ?, club_usuario = ?, moto_usuario = ? WHERE id_usuario = ?";

        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            // Recorre la lista de usuarios.
            for (UsuarioDto usuario : listaUsu) {
                // Establece los parámetros de la sentencia para cada usuario.
                pstmt.setString(1, usuario.getNombre_usuario());
                pstmt.setString(2, usuario.getEmail_usuario());
                pstmt.setString(3, usuario.getTelefono_usuario());
                pstmt.setString(4, usuario.getClub_usuario());
                pstmt.setString(5, usuario.getMoto_usuario());
                pstmt.setLong(6, usuario.getId_usuario());

                // Ejecuta la actualización y obtiene el número de filas actualizadas.
                int filasActualizadas = pstmt.executeUpdate();

                if (filasActualizadas > 0) {
                    System.out.println("El usuario con ID " + usuario.getId_usuario() + " fue actualizado exitosamente.");
                } else {
                    System.out.println("No se encontró el usuario con el ID " + usuario.getId_usuario() + ".");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar los usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
      public void respuestaModificarUsu(Connection conexion, List<UsuarioDto> listaUsu ) {
       

        boolean continuar = true;
        
        while (continuar) {
            // Solicita el ID del usuario que se desea modificar.
            System.out.println("Introduce el ID del usuario que deseas modificar:");
            long idModificar = Long.parseLong(scanner.nextLine());

            // Solicita nuevos datos del usuario.
            System.out.println("Introduce el nuevo nombre del usuario:");
            String nombreModificar = scanner.nextLine();

            System.out.println("Introduce el nuevo email del usuario:");
            String emailModificar = scanner.nextLine();

            System.out.println("Introduce el nuevo teléfono del usuario:");
            String telefonoModificar = scanner.nextLine();

            System.out.println("Introduce el nuevo club del usuario:");
            String clubModificar = scanner.nextLine();

            System.out.println("Introduce la nueva moto del usuario:");
            String motoModificar = scanner.nextLine();

            // Crea un nuevo objeto UsuarioDto con los datos modificados.
            UsuarioDto usuarioModificado = new UsuarioDto(idModificar, nombreModificar, emailModificar, telefonoModificar, clubModificar, motoModificar);

            // Agrega el usuario modificado a la lista.
            listaUsu.add(usuarioModificado);

            // Pregunta si quiere modificar otro usuario.
            System.out.println("¿Deseas modificar otro usuario? (s/n):");
            String respuesta = scanner.nextLine();
            continuar = respuesta.equalsIgnoreCase("s");
        }

        // Llama al método para modificar los usuarios en la base de datos.
        try {
            modificarUsuarios(conexion, listaUsu);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void eliminarUsuarios(Connection conexion, List<UsuarioDto> listaUsu) throws SQLException {
        // SQL para eliminar un registro en la tabla Usuarios.
        String sql = "DELETE FROM dlk.\"Usuarios\" WHERE id_usuario = ?";

        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            // Recorre la lista de usuarios usando un bucle for.
            for (int i = 0; i < listaUsu.size(); i++) {
                UsuarioDto usuario = listaUsu.get(i);
                pstmt.setLong(1, usuario.getId_usuario()); // Establece el ID del usuario a eliminar.

                // Ejecuta la eliminación y obtiene el número de filas eliminadas.
                int filasEliminadas = pstmt.executeUpdate();
                if (filasEliminadas > 0) {
                    // Elimina el usuario de la lista
                    listaUsu.remove(i); 
                    i--; // Ajusta el índice porque hemos modificado la lista
                    System.out.println("El usuario con ID " + usuario.getId_usuario() + " fue eliminado exitosamente de la base de datos.");
                } else {
                    System.out.println("No se encontró el usuario con ID " + usuario.getId_usuario() + ".");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar los usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarUsu(Connection conexion, List<UsuarioDto> usuariosParaEliminar) {
        boolean continuar = true;

        while (continuar) {
            // Solicita el ID del usuario que se desea eliminar.
            System.out.println("Introduce el ID del usuario que deseas eliminar:");
            long idEliminar = Long.parseLong(scanner.nextLine());

            // Crea un objeto UsuarioDto con el ID proporcionado (sin necesidad de los demás datos).
            UsuarioDto usuarioAEliminar = new UsuarioDto(idEliminar, null, null, null, null, null);

            // Agrega el usuario a eliminar a la lista.
            usuariosParaEliminar.add(usuarioAEliminar);

            // Pregunta si quiere eliminar otro usuario.
            System.out.println("¿Deseas eliminar otro usuario? (s/n):");
            String respuesta = scanner.nextLine();
            continuar = respuesta.equalsIgnoreCase("s");
        }

        // Llama al método para eliminar los usuarios de la base de datos.
        try {
            eliminarUsuarios(conexion, usuariosParaEliminar);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void insertarClubs(Connection conexion, List<ClubDto> listaClubs) throws SQLException {
        // SQL para insertar un nuevo registro en la tabla Clubs.
        String sql = "INSERT INTO dlk.\"Clubs\" (id_club, nombre_club, color_club, id_usuario) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            // Recorre la lista de clubes.
            for (ClubDto club : listaClubs) {
                // Establece los parámetros de la sentencia.
                pstmt.setLong(1, club.getId_club());
                pstmt.setString(2, club.getNombre_club());
                pstmt.setString(3, club.getColor_club());
                pstmt.setLong(4, club.getId_usuario());

                // Ejecuta la inserción y obtiene el número de filas insertadas.
                int filasInsertadas = pstmt.executeUpdate();
                if (filasInsertadas > 0) {
                    System.out.println("El club con ID " + club.getId_club() + " fue insertado exitosamente.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar los clubs: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void solicitarDatosYInsertarClub(Connection conexion, List<ClubDto> listaClubs) {
      

        boolean continuar = true;

        while (continuar) {
            // Solicita el ID del nuevo club.
            System.out.println("Introduce el ID del nuevo club:");
            long idNuevoClub = Long.parseLong(scanner.nextLine());

            // Solicita otros datos del nuevo club.
            System.out.println("Introduce el nombre del nuevo club:");
            String nombreNuevoClub = scanner.nextLine();

            System.out.println("Introduce el color del nuevo club:");
            String colorNuevoClub = scanner.nextLine();

            System.out.println("Introduce el ID del usuario del nuevo club:");
            long idUsuarioClub = Long.parseLong(scanner.nextLine());

            // Crea un nuevo objeto ClubDto con los datos recopilados.
            ClubDto nuevoClub = new ClubDto(idNuevoClub, nombreNuevoClub, colorNuevoClub, idUsuarioClub);

            // Agrega el club a la lista.
            listaClubs.add(nuevoClub);

            // Pregunta si quiere agregar otro club.
            System.out.println("¿Deseas agregar otro club? (s/n):");
            String respuesta = scanner.nextLine();
            continuar = respuesta.equalsIgnoreCase("s");
        }

        // Llama al método para insertar los clubes en la base de datos.
        try {
            insertarClubs(conexion, listaClubs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Método privado para modificar un club existente en la base de datos.
    private void modificarClub(Connection conexion, List<ClubDto> listaClubs) throws SQLException {
        // SQL para actualizar un registro existente en la tabla Clubs.
        String sql = "UPDATE dlk.\"Clubs\" SET nombre_club = ?, color_club = ?, id_usuario = ? WHERE id_club = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) { // Prepara la sentencia SQL.
            
        	for (ClubDto clubDto : listaClubs) {
				
			
        	// Establece los parámetros de la sentencia.
            pstmt.setString(1, clubDto.getNombre_club());
            pstmt.setString(2, clubDto.getColor_club());
            pstmt.setLong(3, clubDto.getId_usuario());
            pstmt.setLong(4, clubDto.getId_club());
            // Ejecuta la actualización y obtiene el número de filas actualizadas.
            int filasActualizadas = pstmt.executeUpdate();
        	
            if (filasActualizadas > 0) { // Verifica si se actualizó al menos una fila.
                System.out.println("El club fue actualizado exitosamente."); // Mensaje de éxito.
            } else {
                System.out.println("No se encontró el club con el ID proporcionado."); // Mensaje si no se encontró el club.
            }
        	}
        }
    }

    @Override
    public void respuestaModificarClub(Connection conexion, List<ClubDto> listaClubs) {
        // Solicita el ID del club que se desea modificar.
        System.out.println("Introduce el ID del club que deseas modificar:");
        long idModificarClub = Long.parseLong(scanner.nextLine()); // Lee el ID.

        // Solicita nuevos datos del club.
        System.out.println("Introduce el nuevo nombre del club:");
        String nombreModificarClub = scanner.nextLine();

        System.out.println("Introduce el nuevo color del club:");
        String colorModificarClub = scanner.nextLine();

        System.out.println("Introduce el nuevo ID de usuario del club:");
        long idUsuarioModificarClub = Long.parseLong(scanner.nextLine()); // Lee el nuevo ID del usuario.
        
        ClubDto nuevoClub = new ClubDto(idModificarClub, nombreModificarClub, colorModificarClub, idUsuarioModificarClub);
        
        listaClubs.add(nuevoClub);
        // Llamada a la función para modificar el club.
        try {
             modificarClub(conexion, listaClubs);
        } catch (SQLException e) { // Captura excepciones de SQL.
            e.printStackTrace(); // Imprime el stack trace para depuración.
        }
    }

    private void eliminarClubs(Connection conexion, List<ClubDto> listaClubs) throws SQLException {
        // SQL para eliminar un registro en la tabla Clubs.
        String sql = "DELETE FROM dlk.\"Clubs\" WHERE id_club = ?";

        // Para almacenar los IDs de los clubes que realmente se eliminarán.
        List<Long> idsAEliminar = new ArrayList<>();

        // Solicita el ID del club a eliminar.
        System.out.println("Introduce el ID del club que deseas eliminar:");
        long idEliminar = Long.parseLong(scanner.nextLine());

        // Verifica si el ID está en la lista.
        boolean existeClub = listaClubs.stream().anyMatch(club -> club.getId_club() == idEliminar);
        
        if (!existeClub) {
            System.out.println("No se encontró el club con ID " + idEliminar + ".");
            return; // Salir si el club no existe en la lista.
        }

        // Añade el ID a la lista de IDs a eliminar.
        idsAEliminar.add(idEliminar);

        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            for (Long id : idsAEliminar) {
                pstmt.setLong(1, id); // Establece el ID del club a eliminar.

                // Ejecuta la eliminación y obtiene el número de filas eliminadas.
                int filasEliminadas = pstmt.executeUpdate();
                if (filasEliminadas > 0) {
                    System.out.println("El club con ID " + id + " fue eliminado exitosamente de la base de datos.");
                    
                    // Elimina el club de la lista
                    listaClubs.removeIf(club -> club.getId_club() == id);
                } else {
                    System.out.println("No se encontró el club con ID " + id + ".");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar los clubes: " + e.getMessage());
            e.printStackTrace();
        }
    }



    @Override
    public void eliminarClub(Connection conexion, List<ClubDto> clubesParaEliminar) {
        boolean continuar = true;

        while (continuar) {
            // Solicita el ID del club que se desea eliminar.
            System.out.println("Introduce el ID del club que deseas eliminar:");
            long idEliminar = Long.parseLong(scanner.nextLine());

            // Crea un objeto ClubDto con el ID proporcionado (sin necesidad de los demás datos).
            ClubDto clubAEliminar = new ClubDto(idEliminar, null, null, 0);

            // Agrega el club a eliminar a la lista.
            clubesParaEliminar.add(clubAEliminar);

            // Pregunta si quiere eliminar otro club.
            System.out.println("¿Deseas eliminar otro club? (s/n):");
            String respuesta = scanner.nextLine();
            continuar = respuesta.equalsIgnoreCase("s");
        }

        // Llama al método para eliminar los clubes de la base de datos.
        try {
            eliminarClubs(conexion, clubesParaEliminar);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
