import javax.swing.*;
import java.awt.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {

    private JTextField txtNombre = new JTextField(20);
    private JTextField txtEdad = new JTextField(20);
    private JTextField txtNivelGlucosa = new JTextField(20);
    private JComboBox<String> cbTipoDiabetes = new JComboBox<>(new String[] { "Tipo 1", "Tipo 2", "Gestacional"
    });
    private JButton btnGuardar = new JButton("Guardar");

    private Map<String, String> recomendaciones;

    {

    };

    public VentanaPrincipal() {
        setLayout(new FlowLayout());
        recomendaciones = new HashMap<>();
        recomendaciones.put("Sed excesiva", "Aumenta la ingesta de agua y evita bebidas azucaradas.");
        recomendaciones.put("Fatiga", "Descansa y consulta a tu médico. Considera ajustar tus medicamentos.");
        recomendaciones.put("Visión borrosa",
                "Consulta inmediatamente a un oftalmólogo. Asegúrate de controlar tus niveles de glucosa.");
        recomendaciones.put("Pérdida de peso repentina",
                "Consulta a tu médico. Revisa tu dieta y tu régimen de medicamentos.");
        recomendaciones.put("Hambre constante", "Come alimentos saludables en porciones controladas.");

        add(new JLabel("Nombre:"));
        add(txtNombre);
        add(new JLabel("Edad:"));
        add(txtEdad);
        add(new JLabel("Nivel de glucosa:"));
        add(txtNivelGlucosa);
        add(new JLabel("Tipo de diabetes:"));
        add(cbTipoDiabetes);
        add(btnGuardar);

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        setSize(250, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        new VentanaPrincipal();
    }
}
