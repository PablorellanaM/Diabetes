import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    // Componentes de la interfaz
    private JTextField nombreTextField;
    private JTextField edadTextField;
    private JComboBox<String> sintomasComboBox;
    private JButton ingresarDatosButton;
    private List<String> sintomasSeleccionados;

    public VentanaPrincipal() {
        // Configurar la ventana principal
        setTitle("Diabetes Control App");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Inicializar componentes
        nombreTextField = new JTextField(20);
        edadTextField = new JTextField(5);
        String[] sintomasOptions = {"Bien", "Mal"};
        sintomasComboBox = new JComboBox<>(sintomasOptions);
        ingresarDatosButton = new JButton("Ingresar Datos");
        sintomasSeleccionados = new ArrayList<>();

        // Configurar el diseño de la interfaz
        setLayout(new BorderLayout());

        // Panel para los campos de texto y el combo
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Nombre: "));
        inputPanel.add(nombreTextField);
        inputPanel.add(new JLabel("Edad: "));
        inputPanel.add(edadTextField);
        inputPanel.add(new JLabel("¿Cómo te sientes?"));
        inputPanel.add(sintomasComboBox);

        // Panel para el botón de registro
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(ingresarDatosButton);

        // Agregar componentes a la ventana
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Configurar acción para el botón de registro
        ingresarDatosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener datos ingresados por el usuario
                String nombre = nombreTextField.getText();
                int edad = Integer.parseInt(edadTextField.getText());
                String sintomas = (String) sintomasComboBox.getSelectedItem();

                if ("Mal".equals(sintomas)) {
                    // Si el usuario se siente mal, preguntar por síntomas adicionales
                    preguntarSintomas();
                } else {
                    // Generar recomendaciones generales
                    String recomendaciones = generarRecomendacionesGenerales();
                    JOptionPane.showMessageDialog(null, "Recomendaciones:\n" + recomendaciones);
                }
            }
        });
    }

    private void preguntarSintomas() {
        String[] opcionesSintomas = {"Presión baja", "Fatiga", "Visión borrosa", "Orina frecuente", "Sed excesiva"};

        // Mostrar diálogo de selección múltiple de síntomas
        Object seleccionados = JOptionPane.showInputDialog(
                null,
                "Selecciona los síntomas que tienes (puedes seleccionar varios):\n",
                "Síntomas",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesSintomas,
                opcionesSintomas[0]
        );

        if (seleccionados != null) {
            sintomasSeleccionados.clear(); // Limpiar la lista de síntomas seleccionados

            // Verificar si se seleccionaron síntomas
            for (String sintoma : opcionesSintomas) {
                if (seleccionados.equals(sintoma)) {
                    sintomasSeleccionados.add(sintoma);
                }
            }

            // Generar recomendaciones basadas en los síntomas seleccionados
            String recomendaciones = generarRecomendacionesEspecificas();
            JOptionPane.showMessageDialog(null, "Recomendaciones:\n" + recomendaciones);
        } else {
            JOptionPane.showMessageDialog(null, "No se seleccionaron síntomas.");
        }
    }

    private String generarRecomendacionesGenerales() {
        // Generar recomendaciones generales relacionadas con la diabetes Mellitus
        StringBuilder recomendaciones = new StringBuilder();
        recomendaciones.append("Recomendaciones generales para controlar la diabetes:\n");
        recomendaciones.append(" - Mantenga una dieta equilibrada.\n");
        recomendaciones.append(" - Realice ejercicio regularmente.\n");
        recomendaciones.append(" - Controle su nivel de glucosa según las indicaciones médicas.\n");
        return recomendaciones.toString();
    }

    private String generarRecomendacionesEspecificas() {
        // Generar recomendaciones basadas en los síntomas seleccionados
        StringBuilder recomendaciones = new StringBuilder();
        recomendaciones.append("Recomendaciones basadas en los síntomas seleccionados:\n");

        for (String sintoma : sintomasSeleccionados) {
            if ("Presión baja".equals(sintoma)) {
                recomendaciones.append(" - Beba líquidos y descanse.\n");
            } else if ("Fatiga".equals(sintoma)) {
                recomendaciones.append(" - Descanse y mantenga una dieta equilibrada.\n");
            } else if ("Visión borrosa".equals(sintoma)) {
                recomendaciones.append(" - Evite actividades que requieran enfoque visual.\n");
            } else if ("Orina frecuente".equals(sintoma)) {
                recomendaciones.append(" - Mantenga una hidratación adecuada.\n");
            } else if ("Sed excesiva".equals(sintoma)) {
                recomendaciones.append(" - Controle su ingesta de líquidos.\n");
            }
        }

        return recomendaciones.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
                ventanaPrincipal.setVisible(true);
            }
        });
    }
}
