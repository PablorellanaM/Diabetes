import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VentanaPrincipal extends JFrame {

    private JTextField txtNombre = new JTextField(20);
    private JTextField txtEdad = new JTextField(20);
    private JTextField txtNivelGlucosa = new JTextField(20);
    private JComboBox<String> cbTipoDiabetes = new JComboBox<>(new String[] { "Tipo 1", "Tipo 2", "Gestacional" });
    private JButton btnGuardar = new JButton("Guardar");

    private Map<String, String> recomendaciones;

    {
        recomendaciones = new HashMap<>();
        recomendaciones.put("Sed excesiva", "Aumenta la ingesta de agua y evita bebidas azucaradas.");
        recomendaciones.put("Fatiga", "Descansa y consulta a tu médico. Considera ajustar tus medicamentos.");
        recomendaciones.put("Visión borrosa",
                "Consulta inmediatamente a un oftalmólogo. Asegúrate de controlar tus niveles de glucosa.");
        recomendaciones.put("Pérdida de peso repentina",
                "Consulta a tu médico. Revisa tu dieta y tu régimen de medicamentos.");
        recomendaciones.put("Hambre constante", "Come alimentos saludables en porciones controladas.");
    }

    public VentanaPrincipal() {
        super("Control de Diabetes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Personalización de los componentes
        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblEdad = new JLabel("Edad:");
        JLabel lblNivelGlucosa = new JLabel("Nivel de glucosa:");
        JLabel lblTipoDiabetes = new JLabel("Tipo de diabetes:");

        // Tooltips para proporcionar más información
        txtNombre.setToolTipText("Introduce tu nombre completo");
        txtEdad.setToolTipText("Introduce tu edad");
        txtNivelGlucosa.setToolTipText("Introduce tu nivel de glucosa en sangre");
        cbTipoDiabetes.setToolTipText("Selecciona tu tipo de diabetes");

        // Personalización de botones
        btnGuardar.setBackground(new Color(153, 204, 255));
        btnGuardar.setForeground(Color.BLACK);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 12));

        // Añadir componentes al layout
        add(lblNombre, gbc);
        add(txtNombre, gbc);
        add(lblEdad, gbc);
        add(txtEdad, gbc);
        add(lblNivelGlucosa, gbc);
        add(txtNivelGlucosa, gbc);
        add(lblTipoDiabetes, gbc);
        add(cbTipoDiabetes, gbc);
        add(btnGuardar, gbc);

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double nivelGlucosa = Double.parseDouble(txtNivelGlucosa.getText());
                    if (nivelGlucosa > 140) { // Asumiendo 140 como nivel alto de glucosa
                        JOptionPane.showMessageDialog(null, "No puedes seguir acá, ve al médico.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Por favor, introduce un número válido para el nivel de glucosa.");
                    return;
                }

                int respuesta = JOptionPane.showOptionDialog(null, "¿Cómo te sientes?", "Estado de salud",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                        new Object[] { "Bien", "Mal" }, "Bien");

                if (respuesta == 0) {
                    guardarDatos("Bien", new ArrayList<>());
                    JOptionPane.showMessageDialog(null, "Datos guardados con éxito.");
                } else if (respuesta == 1) {
                    ArrayList<String> sintomas = mostrarDialogoSintomas();
                    StringBuilder consejos = new StringBuilder("Recomendaciones:\n");
                    for (String sintoma : sintomas) {
                        consejos.append("- ").append(recomendaciones.get(sintoma)).append("\n");
                    }
                    guardarDatos("Mal", sintomas);
                    JOptionPane.showMessageDialog(null, consejos.toString());
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private ArrayList<String> mostrarDialogoSintomas() {
        ArrayList<String> sintomasSeleccionados = new ArrayList<>();

        JCheckBox chkSintoma1 = new JCheckBox("Sed excesiva");
        JCheckBox chkSintoma2 = new JCheckBox("Fatiga");
        JCheckBox chkSintoma3 = new JCheckBox("Visión borrosa");
        JCheckBox chkSintoma4 = new JCheckBox("Pérdida de peso repentina");
        JCheckBox chkSintoma5 = new JCheckBox("Hambre constante");

        Object[] message = {
                "Selecciona tus síntomas:",
                chkSintoma1,
                chkSintoma2,
                chkSintoma3,
                chkSintoma4,
                chkSintoma5
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Síntomas", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (chkSintoma1.isSelected())
                sintomasSeleccionados.add(chkSintoma1.getText());
            if (chkSintoma2.isSelected())
                sintomasSeleccionados.add(chkSintoma2.getText());
            if (chkSintoma3.isSelected())
                sintomasSeleccionados.add(chkSintoma3.getText());
            if (chkSintoma4.isSelected())
                sintomasSeleccionados.add(chkSintoma4.getText());
            if (chkSintoma5.isSelected())
                sintomasSeleccionados.add(chkSintoma5.getText());
        }
        return sintomasSeleccionados;
    }

    private void guardarDatos(String estado, ArrayList<String> sintomas) {
        String datos = txtNombre.getText() + "," + txtEdad.getText() + "," + txtNivelGlucosa.getText() + ","
                + cbTipoDiabetes.getSelectedItem().toString() + "," + estado;

        if (estado.equals("Mal")) {
            for (String sintoma : sintomas) {
                datos += "," + sintoma;
            }
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("usuarios.csv", true)))) {
            out.println(datos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RegistroVentana().setVisible(true);
            }
        });
    }
}

class RegistroVentana extends JFrame {
    private JTextField txtUsuario = new JTextField(20);
    private JPasswordField txtPassword = new JPasswordField(20);
    private JButton btnRegistrar = new JButton("Registrar");
    private JButton btnIniciarSesion = new JButton("Iniciar Sesión");

    private static final String USUARIOS_ARCHIVO = "usuarios.txt";

    public RegistroVentana() {
        super("Registro / Inicio de Sesión");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Personalización de los componentes
        JLabel lblUsuario = new JLabel("Usuario:");
        JLabel lblPassword = new JLabel("Contraseña:");

        // Tooltips para proporcionar más información
        txtUsuario.setToolTipText("Crea un nombre de usuario");
        txtPassword.setToolTipText("Crea una contraseña");

        // Personalización de botones
        btnRegistrar.setBackground(new Color(153, 204, 255));
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 12));

        btnIniciarSesion.setBackground(new Color(153, 204, 255));
        btnIniciarSesion.setForeground(Color.BLACK);
        btnIniciarSesion.setFocusPainted(false);
        btnIniciarSesion.setFont(new Font("Arial", Font.BOLD, 12));

        // Añadir componentes al layout
        add(lblUsuario, gbc);
        add(txtUsuario, gbc);
        add(lblPassword, gbc);
        add(txtPassword, gbc);
        add(btnRegistrar, gbc);
        add(btnIniciarSesion, gbc);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = txtUsuario.getText();
                String password = new String(txtPassword.getPassword());
                if (registrarUsuario(usuario, password)) {
                    JOptionPane.showMessageDialog(null, "Usuario registrado con éxito.");
                } else {
                    JOptionPane.showMessageDialog(null, "El nombre de usuario ya existe. Por favor, elige otro.");
                }
            }
        });

        btnIniciarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = txtUsuario.getText();
                String password = new String(txtPassword.getPassword());
                if (verificarCredenciales(usuario, password)) {
                    JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso.");
                    dispose();
                    new VentanaPrincipal().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas.");
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private boolean registrarUsuario(String usuario, String password) {
        // Verificar si el usuario ya existe
        if (usuarioExiste(usuario)) {
            return false;
        }

        // Registrar el nuevo usuario
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(USUARIOS_ARCHIVO, true)))) {
            out.println(usuario + ":" + password);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean usuarioExiste(String usuario) {
        try (Scanner scanner = new Scanner(new File(USUARIOS_ARCHIVO))) {
            while (scanner.hasNextLine()) {
                String[] credenciales = scanner.nextLine().split(":");
                if (credenciales[0].equals(usuario)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            // Si el archivo no existe, no hay usuarios registrados aún
            return false;
        }
        return false;
    }

    private boolean verificarCredenciales(String usuario, String password) {
        try (Scanner scanner = new Scanner(new File(USUARIOS_ARCHIVO))) {
            while (scanner.hasNextLine()) {
                String[] credenciales = scanner.nextLine().split(":");
                if (credenciales[0].equals(usuario) && credenciales[1].equals(password)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
