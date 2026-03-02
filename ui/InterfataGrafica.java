package com.serviceauto.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.serviceauto.models.Client;
import com.serviceauto.models.Masina;
import com.serviceauto.services.ClientService;
import com.serviceauto.services.MasinaService;
import com.serviceauto.services.OpenAIService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class InterfataGrafica extends JFrame {

    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    // Culori & Stil
    private final Color SIDEBAR_COLOR = new Color(33, 37, 41);
    private final Color ACCENT_COLOR = new Color(13, 110, 253);
    private final Color TEXT_COLOR = new Color(248, 249, 250);

    public InterfataGrafica() {
        setTitle("Service Auto Manager v3.0 AI Edition");
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // 2. Continut
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- ECRANE ---
        mainContentPanel.add(new DashboardHomePanel(), "HOME");
        mainContentPanel.add(new ClientiMasiniPanel(), "DATA");
        // Aici este noul panou AI modificat
        mainContentPanel.add(new AIDevizPanel(), "AI");

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel titleLabel = new JLabel("MechanicSHOP");
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(titleLabel);
        sidebar.add(Box.createVerticalStrut(40));

        sidebar.add(createNavButton("📊 Dashboard", "HOME"));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createNavButton("👥 Clienți & Mașini", "DATA"));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createNavButton("🤖 Estimator AI", "AI")); // Redenumit

        sidebar.add(Box.createVerticalGlue());
        JLabel footer = new JLabel("v3.0 AI");
        footer.setForeground(Color.GRAY);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(footer);

        return sidebar;
    }

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setForeground(TEXT_COLOR);
        btn.setBackground(SIDEBAR_COLOR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(ACCENT_COLOR); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(SIDEBAR_COLOR); }
        });

        btn.addActionListener(e -> cardLayout.show(mainContentPanel, cardName));
        return btn;
    }

    // =================================================================================
    // PANOU 1: DASHBOARD
    // =================================================================================
    class DashboardHomePanel extends JPanel {
        public DashboardHomePanel() {
            setLayout(new GridLayout(2, 2, 20, 20));
            int nrClienti = 0, nrMasini = 0;
            try {
                nrClienti = ClientService.getTotiClientii().size();
                nrMasini = MasinaService.getToateMasinile().size();
            } catch (Exception e) {}

            add(createStatCard("Total Clienți", String.valueOf(nrClienti), new Color(25, 135, 84)));
            add(createStatCard("Mașini în Service", String.valueOf(nrMasini), new Color(13, 202, 240)));
            add(createStatCard("Devize Generate", "AI Powered", new Color(255, 193, 7)));
            add(createStatCard("Status Sistem", "Online", new Color(220, 53, 69)));
        }

        private JPanel createStatCard(String title, String value, Color color) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(new Color(45, 45, 45));
            card.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, color));
            JLabel lblTitle = new JLabel(title);
            lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblTitle.setForeground(Color.LIGHT_GRAY);
            lblTitle.setBorder(new EmptyBorder(15, 20, 5, 20));
            JLabel lblValue = new JLabel(value);
            lblValue.setFont(new Font("Segoe UI", Font.BOLD, 36));
            lblValue.setForeground(Color.WHITE);
            lblValue.setBorder(new EmptyBorder(0, 20, 15, 20));
            card.add(lblTitle, BorderLayout.NORTH);
            card.add(lblValue, BorderLayout.CENTER);
            return card;
        }
    }

    // =================================================================================
    // PANOU 2: CLIENTI & MASINI
    // =================================================================================
    class ClientiMasiniPanel extends JPanel {
        private JTable tableClienti, tableMasini;
        private DefaultTableModel modelClienti, modelMasini;

        public ClientiMasiniPanel() {
            setLayout(new GridLayout(1, 2, 20, 0));

            // Stanga (Clienti)
            JPanel panelStanga = new JPanel(new BorderLayout());
            panelStanga.add(new JLabel("Lista Clienți"), BorderLayout.NORTH);
            String[] colClienti = {"ID", "Nume", "Prenume", "Email"};
            modelClienti = new DefaultTableModel(colClienti, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
            tableClienti = styleTable(new JTable(modelClienti));
            panelStanga.add(new JScrollPane(tableClienti), BorderLayout.CENTER);

            JPanel btnPanelC = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton btnAddC = new JButton("➕ Adaugă Client");
            JButton btnRefreshC = new JButton("↻");
            btnAddC.setBackground(new Color(25, 135, 84)); btnAddC.setForeground(Color.WHITE);
            btnPanelC.add(btnAddC); btnPanelC.add(btnRefreshC);
            panelStanga.add(btnPanelC, BorderLayout.SOUTH);

            // Dreapta (Masini)
            JPanel panelDreapta = new JPanel(new BorderLayout());
            panelDreapta.add(new JLabel("Parc Auto"), BorderLayout.NORTH);
            String[] colMasini = {"ID", "Marca", "Model", "Nr.", "Proprietar ID"};
            modelMasini = new DefaultTableModel(colMasini, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
            tableMasini = styleTable(new JTable(modelMasini));
            panelDreapta.add(new JScrollPane(tableMasini), BorderLayout.CENTER);

            JPanel btnPanelM = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton btnAddM = new JButton("➕ Adaugă Mașină");
            JButton btnRefreshM = new JButton("↻");
            btnAddM.setBackground(ACCENT_COLOR); btnAddM.setForeground(Color.WHITE);
            btnPanelM.add(btnAddM); btnPanelM.add(btnRefreshM);
            panelDreapta.add(btnPanelM, BorderLayout.SOUTH);

            add(panelStanga); add(panelDreapta);

            // Functionalitate
            Runnable refreshAll = () -> {
                modelClienti.setRowCount(0);
                modelMasini.setRowCount(0);
                try {
                    for(Client c : ClientService.getTotiClientii())
                        modelClienti.addRow(new Object[]{c.getId(), c.getNume(), c.getPrenume(), c.getEmail()});
                    for(Masina m : MasinaService.getToateMasinile())
                        modelMasini.addRow(new Object[]{m.getId(), m.getMarca(), m.getModel(), m.getNR(), m.getProprietarId()});
                } catch(Exception ex){}
            };

            btnRefreshC.addActionListener(e -> refreshAll.run());
            btnRefreshM.addActionListener(e -> refreshAll.run());
            btnAddC.addActionListener(e -> { showAddClientDialog(); refreshAll.run(); });
            btnAddM.addActionListener(e -> { showAddMasinaDialog(); refreshAll.run(); });

            refreshAll.run();
        }

        // Dialoguri Adaugare (Condensed)
        private void showAddClientDialog() {
            JDialog d = new JDialog(InterfataGrafica.this, "Client Nou", true); d.setSize(300, 250); d.setLocationRelativeTo(this);
            d.setLayout(new GridLayout(4, 2));
            JTextField t1 = new JTextField(), t2 = new JTextField(), t3 = new JTextField();
            d.add(new JLabel("Nume:")); d.add(t1); d.add(new JLabel("Prenume:")); d.add(t2); d.add(new JLabel("Email:")); d.add(t3);
            JButton b = new JButton("Salvează"); d.add(new JLabel("")); d.add(b);
            b.addActionListener(e -> {
                ClientService.adaugaClientInDB(new Client(0, t1.getText(), t2.getText(), t3.getText()));
                d.dispose();
            });
            d.setVisible(true);
        }

        private void showAddMasinaDialog() {
            JDialog d = new JDialog(InterfataGrafica.this, "Masina Noua", true); d.setSize(400, 300); d.setLocationRelativeTo(this);
            d.setLayout(new GridLayout(5, 2));
            JTextField t1 = new JTextField(), t2 = new JTextField(), t3 = new JTextField();
            JComboBox<String> combo = new JComboBox<>();
            ClientService.getTotiClientii().forEach(c -> combo.addItem(c.getId() + " - " + c.getNume()));
            d.add(new JLabel("Marca:")); d.add(t1); d.add(new JLabel("Model:")); d.add(t2); d.add(new JLabel("Numar:")); d.add(t3);
            d.add(new JLabel("Proprietar:")); d.add(combo); JButton b = new JButton("Salvează"); d.add(new JLabel("")); d.add(b);
            b.addActionListener(e -> {
                int pid = Integer.parseInt(((String)combo.getSelectedItem()).split(" - ")[0]);
                MasinaService.adaugaMasinaInDB(new Masina(0, t1.getText(), t2.getText(), pid, t3.getText()));
                d.dispose();
            });
            d.setVisible(true);
        }

        private JTable styleTable(JTable table) {
            table.setRowHeight(30); table.setShowVerticalLines(false);
            table.getTableHeader().setBackground(new Color(40, 40, 40));
            table.getTableHeader().setForeground(Color.WHITE);
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
                public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c){
                    Component comp = super.getTableCellRendererComponent(t,v,s,f,r,c);
                    if(!s) comp.setBackground(r%2==0?new Color(60,63,65):new Color(50,50,50));
                    return comp;
                }
            });
            return table;
        }
    }

    // =================================================================================
    // PANOU 3: AI DEVIZIER SI ESTIMATOR (Nou si Complet Refacut)
    // =================================================================================
    class AIDevizPanel extends JPanel {
        private JComboBox<String> comboMasini;
        private JTextField txtManopera;
        private JTextArea txtPiese;
        private JTextArea txtRezultatAI;
        private JButton btnCalculeaza;

        public AIDevizPanel() {
            setLayout(new GridLayout(1, 2, 20, 20)); // Impartim ecranul in 2: Formular si Rezultat

            // --- 1. PARTEA STANGA (Formular Input) ---
            JPanel panelInput = new JPanel(new GridBagLayout());
            panelInput.setBorder(new TitledBorder("📝 Detalii Reparație"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            // Selectie Masina
            gbc.gridx = 0; gbc.gridy = 0;
            panelInput.add(new JLabel("Alege Mașina din Service:"), gbc);

            comboMasini = new JComboBox<>();
            populeazaComboMasini();
            gbc.gridy = 1;
            panelInput.add(comboMasini, gbc);

            JButton btnRefreshMasini = new JButton("↻ Reîncarcă lista mașini");
            btnRefreshMasini.addActionListener(e -> populeazaComboMasini());
            gbc.gridy = 2;
            panelInput.add(btnRefreshMasini, gbc);

            // Descriere Manopera
            gbc.gridy = 3;
            panelInput.add(new JLabel("Descriere Manoperă (ex: Schimb distribuție):"), gbc);
            txtManopera = new JTextField();
            txtManopera.putClientProperty("JTextField.placeholderText", "Ce operațiune se efectuează?");
            gbc.gridy = 4;
            panelInput.add(txtManopera, gbc);

            // Piese Folosite
            gbc.gridy = 5;
            panelInput.add(new JLabel("Piese & Consumabile (ex: Kit curea, pompă apă):"), gbc);
            txtPiese = new JTextArea(4, 20);
            txtPiese.setLineWrap(true);
            txtPiese.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            gbc.gridy = 6;
            panelInput.add(new JScrollPane(txtPiese), gbc);

            // Buton Actiune
            btnCalculeaza = new JButton("✨ Generează Deviz Estimativ cu AI");
            btnCalculeaza.setBackground(ACCENT_COLOR);
            btnCalculeaza.setForeground(Color.WHITE);
            btnCalculeaza.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnCalculeaza.setCursor(new Cursor(Cursor.HAND_CURSOR));
            gbc.gridy = 7;
            gbc.ipady = 20; // Buton mai inalt
            panelInput.add(btnCalculeaza, gbc);


            // --- 2. PARTEA DREAPTA (Rezultat AI) ---
            JPanel panelOutput = new JPanel(new BorderLayout());
            panelOutput.setBorder(new TitledBorder("🤖 Deviz Generat & Estimare Preț"));

            txtRezultatAI = new JTextArea();
            txtRezultatAI.setEditable(false);
            txtRezultatAI.setFont(new Font("Consolas", Font.PLAIN, 14));
            txtRezultatAI.setLineWrap(true);
            txtRezultatAI.setWrapStyleWord(true);
            txtRezultatAI.setText("Completează formularul din stânga și apasă butonul pentru a genera un deviz...");

            panelOutput.add(new JScrollPane(txtRezultatAI), BorderLayout.CENTER);

            add(panelInput);
            add(panelOutput);

            // --- ACTIUNE BUTON ---
            btnCalculeaza.addActionListener(e -> genereazaDeviz());
        }

        private void populeazaComboMasini() {
            comboMasini.removeAllItems();
            try {
                List<Masina> masini = MasinaService.getToateMasinile();
                if (masini.isEmpty()) {
                    comboMasini.addItem("Nu există mașini în DB");
                } else {
                    for (Masina m : masini) {
                        comboMasini.addItem(m.getId() + " - " + m.getMarca() + " " + m.getModel() + " (" + m.getNR() + ")");
                    }
                }
            } catch (Exception e) {
                comboMasini.addItem("Eroare la încărcare DB");
            }
        }

        private void genereazaDeviz() {
            String masinaSelectata = (String) comboMasini.getSelectedItem();
            String manopera = txtManopera.getText().trim();
            String piese = txtPiese.getText().trim();

            if (manopera.isEmpty() || piese.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Te rog completează Manopera și Piesele!");
                return;
            }

            // --- CONSTRUIRE PROMPT INTELIGENT (MODIFICAT PENTRU MATEMATICA) ---
            String systemPrompt =
                    "Ești un calculator de devize auto expert și PRECIS.\n" +
                            "TARIF ORAR SERVICE: 150 RON.\n\n" +
                            "SARCINA TA:\n" +
                            "Analizează cererea și generează un deviz. Trebuie să respecți următorii pași logici și matematici:\n\n" +
                            "PASUL 1: Estimează costul pieselor (scrie doar valoarea, ex: 500 RON).\n" +
                            "PASUL 2: Estimează timpul de lucru în ore.\n" +
                            "PASUL 3: Calculează Cost Manoperă = Ore * 150 RON.\n" +
                            "PASUL 4: Calculează TOTAL = Cost Piese + Cost Manoperă. (Verifică adunarea de două ori!).\n\n" +
                            "FORMATUL FINAL AL RĂSPUNSULUI (Te rog respectă structura):\n" +
                            "--------------------------------------------------\n" +
                            "🛠️ DEVIZ ESTIMATIV: [Mașina]\n" +
                            "--------------------------------------------------\n" +
                            "1. DESCRIERE TEHNICĂ:\n" +
                            "[Descriere scurtă a problemei]\n\n" +
                            "2. PIESE NECESARE:\n" +
                            "[Lista piese] -> Estimat: [Preț Piese] RON\n\n" +
                            "3. MANOPERĂ:\n" +
                            "[Nr Ore] ore x 150 RON/oră = [Cost Manoperă] RON\n\n" +
                            "==================================================\n" +
                            "💰 TOTAL ESTIMAT: [Suma Exactă] RON\n" +
                            "==================================================\n" +
                            "[Mesaj scurt de încheiere]\n\n" +
                            "DATE INTRARE:\n" +
                            "Mașina: " + masinaSelectata + "\n" +
                            "Operațiune: " + manopera + "\n" +
                            "Piese: " + piese + "\n";

            // UI Loading state
            txtRezultatAI.setText("⏳ AI-ul calculează costurile și verifică totalul... Te rog așteaptă...");
            btnCalculeaza.setEnabled(false);

            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() {
                    return OpenAIService.cereAI(systemPrompt);
                }

                @Override
                protected void done() {
                    try {
                        String raspuns = get();
                        txtRezultatAI.setText(raspuns);
                        txtRezultatAI.setCaretPosition(0); // Scroll sus
                    } catch (Exception ex) {
                        txtRezultatAI.setText("Eroare: Nu am putut genera devizul.");
                    } finally {
                        btnCalculeaza.setEnabled(true);
                    }
                }
            }.execute();
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(new FlatDarkLaf()); } catch (Exception ex) {}
        SwingUtilities.invokeLater(() -> new InterfataGrafica().setVisible(true));
    }
}