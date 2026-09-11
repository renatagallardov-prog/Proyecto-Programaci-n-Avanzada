package com.parquetematico.ui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedHashMap;
import java.util.Map;

public class PanelGraficoBarras extends JPanel {

    private Map<String, Double> datos = new LinkedHashMap<>();

    public void actualizarDatos(Map<String, Double> nuevosDatos) {
        this.datos = new LinkedHashMap<>(nuevosDatos);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();
        int margenIzquierdo = 40;
        int margenInferior = 60;
        int margenSuperior = 20;

        g2.setColor(Color.GRAY);
        g2.drawLine(margenIzquierdo, margenSuperior, margenIzquierdo, alto - margenInferior);
        g2.drawLine(margenIzquierdo, alto - margenInferior, ancho - 10, alto - margenInferior);

        if (datos.isEmpty()) {
            g2.drawString("No hay datos para graficar en este rango de fechas.", margenIzquierdo + 10, alto / 2);
            return;
        }

        int cantidadBarras = datos.size();
        int anchoDisponible = ancho - margenIzquierdo - 30;
        int anchoBarra = anchoDisponible / cantidadBarras;
        int altoMaximo = alto - margenSuperior - margenInferior;

        double escalaMaxima = 100.0;

        int i = 0;
        for (Map.Entry<String, Double> entrada : datos.entrySet()) {
            double porcentaje = entrada.getValue();
            if (porcentaje > escalaMaxima) {
                escalaMaxima = porcentaje;
            }
            i++;
        }

        i = 0;
        for (Map.Entry<String, Double> entrada : datos.entrySet()) {
            String nombre = entrada.getKey();
            double porcentaje = entrada.getValue();

            int alturaBarra = (int) ((porcentaje / escalaMaxima) * altoMaximo);
            int x = margenIzquierdo + (i * anchoBarra) + 10;
            int y = alto - margenInferior - alturaBarra;

            g2.setColor(colorSegunOcupacion(porcentaje));
            g2.fillRect(x, y, anchoBarra - 20, alturaBarra);
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(x, y, anchoBarra - 20, alturaBarra);

            String textoPorcentaje = String.format("%.0f%%", porcentaje);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString(textoPorcentaje, x, y - 5);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            String nombreCorto = nombre.length() > 12 ? nombre.substring(0, 12) + "." : nombre;
            FontMetrics fm = g2.getFontMetrics();
            int anchoTexto = fm.stringWidth(nombreCorto);
            g2.drawString(nombreCorto, x + (anchoBarra - 20 - anchoTexto) / 2, alto - margenInferior + 15);

            i++;
        }
    }

    private Color colorSegunOcupacion(double porcentaje) {
        if (porcentaje < 50) {
            return new Color(76, 175, 80);
        } else if (porcentaje < 85) {
            return new Color(255, 152, 0);
        } else {
            return new Color(211, 47, 47);
        }
    }
}
