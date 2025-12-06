package com.agrosense.ui;

import com.agrosense.model.*;
import com.agrosense.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AgroSenseGUI extends JFrame {

    // Modern Color Palette - Nature/Agriculture Theme
    private static final Color PRIMARY_GREEN = new Color(34, 139, 34);
    private static final Color ACCENT_GREEN = new Color(76, 175, 80);
    private static final Color LIGHT_GREEN = new Color(129, 199, 132);
    private static final Color BG_COLOR = new Color(245, 248, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(33, 33, 33);
    private static final Color TEXT_LIGHT = new Color(117, 117, 117);
    private static final Color WARNING_YELLOW = new Color(255, 193, 7);
    private static final Color DANGER_RED = new Color(244, 67, 54);
    private static final Color INFO_BLUE = new Color(33, 150, 243);

    private GestorLotes gestorLotes;
    private SensorService sensorService;
    private AlertaService alertaService;
    private RecomendacionService recomendacionService;
    private DataPersistenceService persistenceService;

    // Components
    private JTabbedPane tabbedPane;
    private JPanel panelLotes;
    private JPanel panelSensores;
    private JPanel panelMonitoreo;
    private JPanel panelAlertas;

    // Lotes Components
    private DefaultTableModel tableModelLotes;
    private JTable tableLotes;
    private JTextField txtLoteId, txtLoteNombre, txtLoteCultivo, txtLoteArea;

    // Sensores Components
    private JComboBox<String> comboLotesSensor;
    private JTextField txtSensorId, txtSensorUbicacion;
    private JComboBox<String> comboSensorTipo;

    // Monitoreo Components
    private DefaultTableModel tableModelMonitoreo;
    private JTable tableMonitoreo;

    // Alertas Components
    private DefaultTableModel tableModelAlertas;
    private JTable tableAlertas;
    private JTextArea txtRecomendaciones;

    public AgroSenseGUI() {
        // Initialize Services
        gestorLotes = new GestorLotes();
        sensorService = new SensorService();
        alertaService = new AlertaService();
        recomendacionService = new RecomendacionService();
        persistenceService = new DataPersistenceService();

        // Setup Window
        setTitle("AgroSense - Sistema de Monitoreo Agrícola");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            customizeUIDefaults();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set background
        getContentPane().setBackground(BG_COLOR);

        // Main Layout with Header
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(CARD_BG);

        initPanelLotes();
        initPanelSensores();
        initPanelMonitoreo();
        initPanelAlertas();

        tabbedPane.addTab("🌱 Gestión de Lotes", panelLotes);
        tabbedPane.addTab("📡 Sensores", panelSensores);
        tabbedPane.addTab("📊 Monitoreo", panelMonitoreo);
        tabbedPane.addTab("⚠️ Alertas", panelAlertas);

        add(tabbedPane, BorderLayout.CENTER);

        // Auto-load data if exists
        cargarDatosAutomaticamente();
    }

    private void customizeUIDefaults() {
        UIManager.put("Button.background", PRIMARY_GREEN);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 12));
        UIManager.put("TabbedPane.selected", ACCENT_GREEN);
        UIManager.put("Table.gridColor", new Color(224, 224, 224));
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBackground(PRIMARY_GREEN);
        header.setPreferredSize(new Dimension(0, 80));

        // Title
        JLabel titleLabel = new JLabel("🌿 AgroSense");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 25, 10, 0));

        JLabel subtitleLabel = new JLabel("Sistema Inteligente de Monitoreo Agrícola");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 230, 201));
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 25, 15, 0));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PRIMARY_GREEN);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        header.add(titlePanel, BorderLayout.WEST);

        // Add Export/Import buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(PRIMARY_GREEN);

        JButton btnExportar = new JButton("💾 Exportar Datos");
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportar.setForeground(PRIMARY_GREEN);
        btnExportar.setBackground(Color.WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExportar.addActionListener(e -> exportarDatos());

        JButton btnImportar = new JButton("📂 Importar Datos");
        btnImportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnImportar.setForeground(PRIMARY_GREEN);
        btnImportar.setBackground(Color.WHITE);
        btnImportar.setFocusPainted(false);
        btnImportar.setBorderPainted(false);
        btnImportar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnImportar.addActionListener(e -> importarDatos());

        buttonPanel.add(btnExportar);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(btnImportar);

        header.add(buttonPanel, BorderLayout.EAST);

        return header;
    }

    private void initPanelLotes() {
        panelLotes = new JPanel(new BorderLayout(15, 15));
        panelLotes.setBackground(BG_COLOR);
        panelLotes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form Panel with Card Style
        JPanel formCard = new JPanel();
        formCard.setLayout(new BorderLayout());
        formCard.setBackground(CARD_BG);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel formTitle = new JLabel("➕ Registrar Nuevo Lote");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(TEXT_DARK);
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form fields
        addFormField(formPanel, gbc, 0, "ID del Lote:", txtLoteId = createStyledTextField());
        addFormField(formPanel, gbc, 1, "Nombre:", txtLoteNombre = createStyledTextField());
        addFormField(formPanel, gbc, 2, "Tipo de Cultivo:", txtLoteCultivo = createStyledTextField());
        addFormField(formPanel, gbc, 3, "Área (hectáreas):", txtLoteArea = createStyledTextField());

        JButton btnAgregar = createStyledButton("Registrar Lote", PRIMARY_GREEN);
        btnAgregar.addActionListener(this::registrarLote);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 8, 8);
        formPanel.add(btnAgregar, gbc);

        formCard.add(formPanel, BorderLayout.CENTER);

        // Table Panel with Card Style
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(CARD_BG);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel tableTitle = new JLabel("📋 Lotes Registrados");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(TEXT_DARK);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        tableCard.add(tableTitle, BorderLayout.NORTH);

        String[] columns = { "ID", "Nombre", "Cultivo", "Área (ha)", "Sensores" };
        tableModelLotes = new DefaultTableModel(columns, 0);
        tableLotes = createStyledTable(tableModelLotes);
        JScrollPane scrollPane = new JScrollPane(tableLotes);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224)));
        tableCard.add(scrollPane, BorderLayout.CENTER);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_COLOR);
        topPanel.add(formCard, BorderLayout.CENTER);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        panelLotes.add(topPanel, BorderLayout.NORTH);
        panelLotes.add(tableCard, BorderLayout.CENTER);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(TEXT_DARK);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(200, 32));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(32);
        table.setShowGrid(true);
        table.setGridColor(new Color(240, 240, 240));
        table.setSelectionBackground(LIGHT_GREEN);
        table.setSelectionForeground(TEXT_DARK);

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(ACCENT_GREEN);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 38));

        // Center alignment for cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        return table;
    }

    private void registrarLote(ActionEvent e) {
        try {
            String id = txtLoteId.getText().trim();
            String nombre = txtLoteNombre.getText().trim();
            String cultivo = txtLoteCultivo.getText().trim();
            String areaStr = txtLoteArea.getText().trim();

            if (id.isEmpty() || nombre.isEmpty()) {
                showStyledMessage("ID y Nombre son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double area = Double.parseDouble(areaStr);

            Lote lote = new Lote(id, nombre, cultivo, area);
            gestorLotes.registrarLote(lote);
            actualizarTablaLotes();
            actualizarCombosLotes();
            limpiarFormularioLotes();
            showStyledMessage("✅ Lote registrado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            showStyledMessage("El área debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showStyledMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    private void actualizarTablaLotes() {
        tableModelLotes.setRowCount(0);
        for (Lote lote : gestorLotes.obtenerTodos()) {
            Object[] row = {
                    lote.getId(),
                    lote.getNombre(),
                    lote.getTipoCultivo(),
                    String.format("%.2f", lote.getArea()),
                    lote.getSensores().size()
            };
            tableModelLotes.addRow(row);
        }
    }

    private void limpiarFormularioLotes() {
        txtLoteId.setText("");
        txtLoteNombre.setText("");
        txtLoteCultivo.setText("");
        txtLoteArea.setText("");
    }

    private void initPanelSensores() {
        panelSensores = new JPanel(new BorderLayout(15, 15));
        panelSensores.setBackground(BG_COLOR);
        panelSensores.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formCard = new JPanel();
        formCard.setLayout(new BorderLayout());
        formCard.setBackground(CARD_BG);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel formTitle = new JLabel("📡 Agregar Sensor a Lote");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(TEXT_DARK);
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        comboLotesSensor = new JComboBox<>();
        comboLotesSensor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboLotesSensor.setPreferredSize(new Dimension(200, 32));
        addFormField(formPanel, gbc, 0, "Seleccionar Lote:", comboLotesSensor);

        addFormField(formPanel, gbc, 1, "ID del Sensor:", txtSensorId = createStyledTextField());

        comboSensorTipo = new JComboBox<>(new String[] { "HUMEDAD", "TEMPERATURA" });
        comboSensorTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboSensorTipo.setPreferredSize(new Dimension(200, 32));
        addFormField(formPanel, gbc, 2, "Tipo de Sensor:", comboSensorTipo);

        addFormField(formPanel, gbc, 3, "Ubicación:", txtSensorUbicacion = createStyledTextField());

        JButton btnAgregar = createStyledButton("Agregar Sensor", ACCENT_GREEN);
        btnAgregar.addActionListener(this::agregarSensor);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 8, 8);
        formPanel.add(btnAgregar, gbc);

        formCard.add(formPanel, BorderLayout.CENTER);

        // Info Panel
        JPanel infoCard = new JPanel();
        infoCard.setLayout(new BorderLayout());
        infoCard.setBackground(new Color(232, 245, 233));
        infoCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREEN, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel infoIcon = new JLabel("💡");
        infoIcon.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        infoCard.add(infoIcon, BorderLayout.WEST);

        JTextArea infoText = new JTextArea(
                "Los sensores IoT monitorean las condiciones del cultivo en tiempo real.\n\n" +
                        "• Sensores de HUMEDAD: Miden el nivel de humedad del suelo (0-100%)\n" +
                        "• Sensores de TEMPERATURA: Registran la temperatura ambiente (°C)\n\n" +
                        "Estos datos permiten tomar decisiones informadas sobre riego y cuidado.");
        infoText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoText.setForeground(TEXT_DARK);
        infoText.setEditable(false);
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);
        infoText.setBackground(new Color(232, 245, 233));
        infoText.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        infoCard.add(infoText, BorderLayout.CENTER);

        panelSensores.add(formCard, BorderLayout.NORTH);
        panelSensores.add(infoCard, BorderLayout.CENTER);
    }

    private void agregarSensor(ActionEvent e) {
        String selectedLote = (String) comboLotesSensor.getSelectedItem();
        if (selectedLote == null) {
            showStyledMessage("Debe seleccionar un lote primero", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String loteId = selectedLote.split(" - ")[0];
        String sensorId = txtSensorId.getText().trim();
        String tipo = (String) comboSensorTipo.getSelectedItem();
        String ubicacion = txtSensorUbicacion.getText().trim();

        if (sensorId.isEmpty()) {
            showStyledMessage("ID del sensor es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        var loteOpt = gestorLotes.buscarPorId(loteId);
        if (loteOpt.isPresent()) {
            Lote lote = loteOpt.get();
            Sensor sensor;
            if ("HUMEDAD".equals(tipo)) {
                sensor = new SensorHumedad(sensorId, ubicacion);
            } else {
                sensor = new SensorTemperatura(sensorId, ubicacion);
            }
            lote.agregarSensor(sensor);
            actualizarTablaLotes();
            showStyledMessage("✅ Sensor " + tipo + " agregado al lote " + lote.getNombre(), "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            txtSensorId.setText("");
            txtSensorUbicacion.setText("");
        }
    }

    private void actualizarCombosLotes() {
        comboLotesSensor.removeAllItems();
        for (Lote lote : gestorLotes.obtenerTodos()) {
            comboLotesSensor.addItem(lote.getId() + " - " + lote.getNombre());
        }
    }

    private void initPanelMonitoreo() {
        panelMonitoreo = new JPanel(new BorderLayout(15, 15));
        panelMonitoreo.setBackground(BG_COLOR);
        panelMonitoreo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BG_COLOR);

        JButton btnSimular = createStyledButton("▶️ Simular Lectura de Sensores", INFO_BLUE);
        btnSimular.setPreferredSize(new Dimension(250, 42));
        btnSimular.addActionListener(this::simularMonitoreo);
        headerPanel.add(btnSimular);

        panelMonitoreo.add(headerPanel, BorderLayout.NORTH);

        // Table Card
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(CARD_BG);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel tableTitle = new JLabel("📊 Lecturas en Tiempo Real");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(TEXT_DARK);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        tableCard.add(tableTitle, BorderLayout.NORTH);

        String[] columns = { "Lote", "Sensor ID", "Tipo", "Ubicación", "Valor", "Estado" };
        tableModelMonitoreo = new DefaultTableModel(columns, 0);
        tableMonitoreo = createStyledTable(tableModelMonitoreo);
        JScrollPane scrollPane = new JScrollPane(tableMonitoreo);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224)));
        tableCard.add(scrollPane, BorderLayout.CENTER);

        panelMonitoreo.add(tableCard, BorderLayout.CENTER);
    }

    private void simularMonitoreo(ActionEvent e) {
        tableModelMonitoreo.setRowCount(0);
        List<Lote> lotes = gestorLotes.obtenerTodos();

        if (lotes.isEmpty()) {
            showStyledMessage("No hay lotes registrados para monitorear", "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Lote lote : lotes) {
            for (Sensor sensor : lote.getSensores()) {
                double valor = sensorService.leerSensor(sensor);
                Medicion medicion = new Medicion(valor, sensor.getId(), sensor.getTipo());

                alertaService.verificarMedicion(medicion, lote.getId());

                String estado = "✅ Normal";
                if (sensor.getTipo().equals("HUMEDAD")) {
                    if (valor < 30)
                        estado = "🔴 CRÍTICO";
                    else if (valor < 50)
                        estado = "⚠️ Bajo";
                } else if (sensor.getTipo().equals("TEMPERATURA")) {
                    if (valor > 35)
                        estado = "🔴 CRÍTICO";
                    else if (valor > 30)
                        estado = "⚠️ Alto";
                }

                String valorStr = String.format("%.1f", valor);
                if (sensor.getTipo().equals("HUMEDAD"))
                    valorStr += "%";
                else
                    valorStr += "°C";

                Object[] row = {
                        lote.getNombre(),
                        sensor.getId(),
                        sensor.getTipo(),
                        sensor.getUbicacion(),
                        valorStr,
                        estado
                };
                tableModelMonitoreo.addRow(row);
            }
        }
        actualizarPanelAlertas();
    }

    private void initPanelAlertas() {
        panelAlertas = new JPanel(new BorderLayout(15, 15));
        panelAlertas.setBackground(BG_COLOR);
        panelAlertas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setBackground(BG_COLOR);
        splitPane.setDividerSize(8);

        // Alerts Card
        JPanel alertsCard = new JPanel(new BorderLayout());
        alertsCard.setBackground(CARD_BG);
        alertsCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel alertsTitle = new JLabel("⚠️ Historial de Alertas");
        alertsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        alertsTitle.setForeground(TEXT_DARK);
        alertsTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        alertsCard.add(alertsTitle, BorderLayout.NORTH);

        String[] columns = { "Fecha/Hora", "Nivel", "Lote", "Mensaje" };
        tableModelAlertas = new DefaultTableModel(columns, 0);
        tableAlertas = createStyledTable(tableModelAlertas);
        JScrollPane scrollAlertas = new JScrollPane(tableAlertas);
        scrollAlertas.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224)));
        alertsCard.add(scrollAlertas, BorderLayout.CENTER);

        // Recommendations Card
        JPanel recomCard = new JPanel(new BorderLayout());
        recomCard.setBackground(CARD_BG);
        recomCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel recomTitle = new JLabel("💡 Recomendaciones Inteligentes");
        recomTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        recomTitle.setForeground(TEXT_DARK);
        recomTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        recomCard.add(recomTitle, BorderLayout.NORTH);

        txtRecomendaciones = new JTextArea();
        txtRecomendaciones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtRecomendaciones.setEditable(false);
        txtRecomendaciones.setLineWrap(true);
        txtRecomendaciones.setWrapStyleWord(true);
        txtRecomendaciones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollRec = new JScrollPane(txtRecomendaciones);
        scrollRec.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224)));
        recomCard.add(scrollRec, BorderLayout.CENTER);

        JButton btnRefrescar = createStyledButton("🔄 Actualizar", ACCENT_GREEN);
        btnRefrescar.addActionListener(e -> actualizarPanelAlertas());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(CARD_BG);
        btnPanel.add(btnRefrescar);
        recomCard.add(btnPanel, BorderLayout.SOUTH);

        splitPane.setTopComponent(alertsCard);
        splitPane.setBottomComponent(recomCard);
        splitPane.setDividerLocation(300);

        panelAlertas.add(splitPane, BorderLayout.CENTER);
    }

    private void actualizarPanelAlertas() {
        tableModelAlertas.setRowCount(0);
        List<Alerta> alertas = alertaService.getHistorialAlertas();
        for (Alerta alerta : alertas) {
            Object[] row = {
                    alerta.getFechaHora().toString().replace("T", " "),
                    alerta.getNivel(),
                    alerta.getLoteId(),
                    alerta.getMensaje()
            };
            tableModelAlertas.addRow(row);
        }

        List<Recomendacion> recomendaciones = recomendacionService.generarRecomendaciones(alertas);
        StringBuilder sb = new StringBuilder();
        if (recomendaciones.isEmpty()) {
            sb.append("✅ No hay recomendaciones pendientes.\n\n");
            sb.append("Sus cultivos están en buen estado. Continue monitoreando regularmente.");
        } else {
            sb.append("Se han generado las siguientes recomendaciones:\n\n");
            int count = 1;
            for (Recomendacion rec : recomendaciones) {
                sb.append(count++).append(". Lote ").append(rec.getLoteId()).append("\n");
                sb.append("   ").append(rec.getMensaje()).append("\n");
                sb.append("   ➜ ACCIÓN: ").append(rec.getAccionSugerida()).append("\n\n");
            }
        }
        txtRecomendaciones.setText(sb.toString());
    }

    private void exportarDatos() {
        try {
            persistenceService.exportarDatos(gestorLotes, alertaService);
            showStyledMessage("✅ Datos exportados exitosamente a data/agrosense_data.json", "Exportación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showStyledMessage("❌ Error al exportar datos: " + e.getMessage(), "Error de Exportación",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void importarDatos() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Desea importar datos? Esto reemplazará los datos actuales.",
                "Confirmar Importación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            DataPersistenceService.AgroSenseData data = persistenceService.importarDatos();

            // Clear current data
            gestorLotes.obtenerTodos().clear();
            alertaService.getHistorialAlertas().clear();

            // Load imported data
            for (Lote lote : data.lotes) {
                gestorLotes.registrarLote(lote);
            }

            for (Alerta alerta : data.alertas) {
                alertaService.getHistorialAlertas().add(alerta);
            }

            // Update all UI components
            actualizarTablaLotes();
            actualizarCombosLotes();
            actualizarPanelAlertas();

            showStyledMessage("✅ Datos importados exitosamente", "Importación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (java.io.FileNotFoundException e) {
            showStyledMessage("❌ No se encontró el archivo de datos.\nAsegúrese de exportar datos primero.",
                    "Error de Importación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            showStyledMessage("❌ Error al importar datos: " + e.getMessage(), "Error de Importación",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void cargarDatosAutomaticamente() {
        try {
            DataPersistenceService.AgroSenseData data = persistenceService.importarDatos();

            // Load imported data
            for (Lote lote : data.lotes) {
                gestorLotes.registrarLote(lote);
            }

            for (Alerta alerta : data.alertas) {
                alertaService.getHistorialAlertas().add(alerta);
            }

            // Update UI
            actualizarTablaLotes();
            actualizarCombosLotes();
            actualizarPanelAlertas();

            System.out.println("✅ Datos cargados automáticamente desde data/agrosense_data.json");
        } catch (java.io.FileNotFoundException e) {
            // Si no existe archivo, no hacer nada (primera ejecución)
            System.out.println("ℹ️ No se encontró archivo de datos previo. Iniciando con datos vacíos.");
        } catch (Exception e) {
            System.err.println("❌ Error al cargar datos automáticamente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AgroSenseGUI().setVisible(true);
        });
    }
}
