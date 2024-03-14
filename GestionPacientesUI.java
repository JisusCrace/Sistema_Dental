// Importaciones de paquetes necesarios
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

// Declaración de la clase principal que representa la interfaz de usuario
public class GestionPacientesUI extends JFrame {

    // Declaración de componentes de la interfaz de usuario
    private JTextField nombreField, edadField, direccionField, telefonoField;
    private JButton eliminarButton, modificarButton, agregarButton;
    private ArrayList<Paciente> listaDePacientes; // Lista para almacenar objetos Paciente
    private final String rutaArchivo = "C:\\Users\\terea\\OneDrive\\Escritorio\\gestionpacientesjess\\gestionpacientesjess\\src\\pacientes.txt"; // Ruta absoluta del archivo

    // Constructor de la clase
    public GestionPacientesUI() {
        super("Gestión de Pacientes - Clínica Dental"); // Título de la ventana

        // Crear la lista de pacientes
        listaDePacientes = new ArrayList<>();

        // Crear componentes de la interfaz de usuario
        nombreField = new JTextField(20);
        edadField = new JTextField(5);
        direccionField = new JTextField(20);
        telefonoField = new JTextField(10);

        eliminarButton = new JButton("Eliminar Paciente");
        modificarButton = new JButton("Modificar Paciente");
        agregarButton = new JButton("Agregar Paciente");

        // Crear el menú principal
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opciones");
        JMenuItem agregarMenuItem = new JMenuItem("Agregar Paciente");
        JMenuItem eliminarMenuItem = new JMenuItem("Eliminar Paciente");
        JMenuItem modificarMenuItem = new JMenuItem("Modificar Paciente");

        // Agregar acciones a los elementos del menú
        agregarMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioAgregar();
            }
        });

        eliminarMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPaciente("eliminar");
            }
        });

        modificarMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPaciente("modificar");
            }
        });

        // Agregar elementos al menú
        menu.add(agregarMenuItem);
        menu.add(eliminarMenuItem);
        menu.add(modificarMenuItem);
        menuBar.add(menu);

        // Configurar la barra de menú
        setJMenuBar(menuBar);

        // Configurar el panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel);

        // Agregar componentes al panel principal
        mainPanel.add(new JLabel("Bienvenido a la Gestión de Pacientes - Clínica Dental Akira "));
        mainPanel.add(new JLabel("Seleccione una opción del menú."));

        // Agregar botones al panel principal
        mainPanel.add(agregarButton);
        mainPanel.add(eliminarButton);
        mainPanel.add(modificarButton);

        // Agregar acciones a los botones
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPaciente("eliminar");
            }
        });

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioAgregar();
            }
        });

        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPaciente("modificar");
            }
        });

        // Cargar pacientes desde el archivo al iniciar la aplicación
        cargarPacientesDesdeArchivo();

        // Configuración de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // La aplicación se cierra al cerrar la ventana
        pack(); // Ajusta el tamaño de la ventana automáticamente
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setVisible(true); // Hacer visible la ventana
    }

    // Método para mostrar formulario de agregar paciente
    private void mostrarFormularioAgregar() {
        // Crear panel para el formulario de agregar paciente
        JPanel addPanel = new JPanel(new GridLayout(4, 2));
        addPanel.add(new JLabel("Nombre:"));
        addPanel.add(nombreField);
        addPanel.add(new JLabel("Edad:"));
        addPanel.add(edadField);
        addPanel.add(new JLabel("Dirección:"));
        addPanel.add(direccionField);
        addPanel.add(new JLabel("Teléfono:"));
        addPanel.add(telefonoField);

        // Mostrar el formulario en un cuadro de diálogo
        int result = JOptionPane.showConfirmDialog(this, addPanel, "Agregar Paciente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            agregarPaciente(); // Llamar al método para agregar un nuevo paciente
        }
    }

    // Método para agregar paciente a la lista
    private void agregarPaciente() {
        // Obtener información del paciente de los campos de texto
        String nombre = nombreField.getText();
        int edad = Integer.parseInt(edadField.getText());
        String direccion = direccionField.getText();
        String telefono = telefonoField.getText();

        // Crear un nuevo objeto Paciente con la información ingresada
        Paciente nuevoPaciente = new Paciente(nombre, edad, direccion, telefono);
        listaDePacientes.add(nuevoPaciente); // Agregar el nuevo paciente a la lista

        // Mostrar mensaje de confirmación
        JOptionPane.showMessageDialog(this, "Paciente agregado correctamente.");

        // Limpiar los campos de texto después de agregar un paciente
        limpiarCampos();

        // Guardar la lista de pacientes en el archivo de texto
        guardarPacientesEnArchivo();
    }

    // Método para buscar paciente por nombre y realizar operaciones (eliminar, modificar)
    private void buscarPaciente(String accion) {
        // Solicitar al usuario que ingrese el nombre del paciente
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del paciente:");

        // Buscar el paciente en la lista por su nombre
        for (Paciente paciente : listaDePacientes) {
            if (paciente.getNombre().equalsIgnoreCase(nombre)) {
                if (accion.equals("eliminar")) {
                    eliminarPaciente(paciente); // Llamar al método para eliminar un paciente
                } else if (accion.equals("modificar")) {
                    mostrarFormularioModificar(paciente); // Llamar al método para modificar un paciente
                }
                return;
            }
        }
        // Mostrar mensaje si no se encuentra el paciente
        JOptionPane.showMessageDialog(this, "No se encontró ningún paciente con ese nombre.");
    }

    // Método para eliminar paciente de la lista
    private void eliminarPaciente(Paciente paciente) {
        listaDePacientes.remove(paciente); // Eliminar el paciente de la lista
        JOptionPane.showMessageDialog(this, "Paciente eliminado correctamente.");

        // Guardar la lista de pacientes actualizada en el archivo de texto
        guardarPacientesEnArchivo();
    }

    // Método para mostrar formulario de modificar paciente
    private void mostrarFormularioModificar(Paciente paciente) {
        // Llenar los campos de texto con la información del paciente seleccionado
        nombreField.setText(paciente.getNombre());
        edadField.setText(String.valueOf(paciente.getEdad()));
        direccionField.setText(paciente.getDireccion());
        telefonoField.setText(paciente.getTelefono());

        // Crear panel para el formulario de modificar paciente
        JPanel modifyPanel = new JPanel(new GridLayout(4, 2));
        modifyPanel.add(new JLabel("Nombre:"));
        modifyPanel.add(nombreField);
        modifyPanel.add(new JLabel("Edad:"));
        modifyPanel.add(edadField);
        modifyPanel.add(new JLabel("Dirección:"));
        modifyPanel.add(direccionField);
        modifyPanel.add(new JLabel("Teléfono:"));
        modifyPanel.add(telefonoField);

        // Mostrar el formulario en un cuadro de diálogo
        int result = JOptionPane.showConfirmDialog(this, modifyPanel, "Modificar Paciente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            // Actualizar la información del paciente con los datos ingresados
            paciente.setNombre(nombreField.getText());
            paciente.setEdad(Integer.parseInt(edadField.getText()));
            paciente.setDireccion(direccionField.getText());
            paciente.setTelefono(telefonoField.getText());

            // Mostrar mensaje de confirmación
            JOptionPane.showMessageDialog(this, "Paciente modificado correctamente.");
            limpiarCampos(); // Limpiar los campos de texto

            // Guardar la lista de pacientes actualizada en el archivo de texto
            guardarPacientesEnArchivo();
        }
    }

    // Método para limpiar los campos de texto
    private void limpiarCampos() {
        nombreField.setText("");
        edadField.setText("");
        direccionField.setText("");
        telefonoField.setText("");
    }

    // Método para cargar pacientes desde el archivo
    private void cargarPacientesDesdeArchivo() {
        // Crear objeto File para representar el archivo de texto
        File archivo = new File(rutaArchivo);

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            // Leer cada línea del archivo y crear objetos Paciente con la información
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                String nombre = partes[0];
                int edad = Integer.parseInt(partes[1]);
                String direccion = partes[2];
                String telefono = partes[3];
                Paciente paciente = new Paciente(nombre, edad, direccion, telefono);
                listaDePacientes.add(paciente); // Agregar el paciente a la lista
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Archivo no encontrado.");
        } catch (IOException e) {
            System.err.println("Error de lectura del archivo.");
        }
    }

    // Método para guardar pacientes en el archivo
    private void guardarPacientesEnArchivo() {
        // Crear objeto File para representar el archivo de texto
        File archivo = new File(rutaArchivo);

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            // Escribir la información de cada paciente en una línea del archivo
            for (Paciente paciente : listaDePacientes) {
                pw.println(paciente.getNombre() + "," + paciente.getEdad() + "," + paciente.getDireccion() + "," + paciente.getTelefono());
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo.");
        }
    }

    // Método principal que inicia la aplicación
    public static void main(String[] args) {
        // Crear y mostrar la interfaz de usuario en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> new GestionPacientesUI());
    }
}

// Clase que representa a un paciente
class Paciente {
    // Atributos del paciente: nombre, edad, dirección y teléfono
    private String nombre;
    private int edad;
    private String direccion;
    private String telefono;

    // Constructor de la clase Paciente
    public Paciente(String nombre, int edad, String direccion, String telefono) {
        this.nombre = nombre;
        this.edad = edad;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // Getters y setters para acceder y modificar los atributos del paciente
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}

