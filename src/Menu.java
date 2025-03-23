import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Menu extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel nimLabel;
    private JLabel namaLabel;
    private JLabel jenisKelaminLabel;
    private JButton UpdateButton;
    private ArrayList<Mahasiswa> listMahasiswa;
    private JTable mahasiswaTable;
    private JTextField nimField, namaField, nilaiField;
    private JComboBox<String> jenisKelaminComboBox;
    private JButton updateButton, cancelButton, deleteButton;
    private int selectedIndex = -1;

    public static void main(String[] args) {
        Menu window = new Menu();
        window.setSize(600, 500);
        window.setLocationRelativeTo(null);
        window.setTitle("Data Mahasiswa");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public Menu() {
        listMahasiswa = new ArrayList<>();
        populateList(); // Call populateList to fill the list
        initializeUI();
    }

    private void initializeUI() {
        // Initialize the mainPanel field instead of creating a local variable
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // NIM
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        nimLabel = new JLabel("NIM");
        formPanel.add(nimLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        nimField = new JTextField(20);
        formPanel.add(nimField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        updateButton = new JButton("Update");
        formPanel.add(updateButton, gbc);

        // Nama
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        namaLabel = new JLabel("Nama");
        formPanel.add(namaLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        namaField = new JTextField(20);
        formPanel.add(namaField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        cancelButton = new JButton("Cancel");
        formPanel.add(cancelButton, gbc);

        // Jenis Kelamin
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        jenisKelaminLabel = new JLabel("Jenis Kelamin");
        formPanel.add(jenisKelaminLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.7;
        jenisKelaminComboBox = new JComboBox<>(new String[]{"Laki-laki", "Perempuan"});
        formPanel.add(jenisKelaminComboBox, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        deleteButton = new JButton("Delete");
        formPanel.add(deleteButton, gbc);

        // Nilai
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.1;
        JLabel nilaiLabel = new JLabel("Nilai");
        formPanel.add(nilaiLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.7;
        nilaiField = new JTextField(20);
        formPanel.add(nilaiField, gbc);

        // Title at the top
        titleLabel = new JLabel("Data Mahasiswa");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        // Table
        mahasiswaTable = new JTable();
        mahasiswaTable.setModel(setTable());
        JScrollPane scrollPane = new JScrollPane(mahasiswaTable);
        scrollPane.setPreferredSize(new Dimension(580, 200)); // Set ukuran minimal agar tabel tetap terlihat

        // JSplitPane untuk form dan tabel
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, scrollPane);
        splitPane.setResizeWeight(0.4); // 40% untuk form, 60% untuk tabel
        splitPane.setOneTouchExpandable(true);

        // Title
        titleLabel = new JLabel("Data Mahasiswa", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Panel utama
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Set content pane
        setContentPane(mainPanel);

        // Add action listeners
        updateButton.addActionListener(e -> {
            if (selectedIndex == -1) {
                insertData();
            } else {
                updateData();
            }
        });

        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Hapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteData();
            }
        });

        cancelButton.addActionListener(e -> clearForm());

        mahasiswaTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && mahasiswaTable.getSelectedRow() != -1) {
                selectedIndex = mahasiswaTable.getSelectedRow();
                Mahasiswa mhs = listMahasiswa.get(selectedIndex);
                nimField.setText(mhs.getNim());
                namaField.setText(mhs.getNama());
                jenisKelaminComboBox.setSelectedItem(mhs.getJenisKelamin());
                nilaiField.setText(mhs.getNilai());
                updateButton.setText("Update");
                deleteButton.setVisible(true);
            }
        });

        // Initially hide delete button
        deleteButton.setVisible(false);

        // Add components to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        // Set content pane
        setContentPane(mainPanel);
    }

    private DefaultTableModel setTable() {
        String[] column = {"No", "NIM", "Nama", "Jenis Kelamin", "Nilai"};
        DefaultTableModel model = new DefaultTableModel(null, column);
        for (int i = 0; i < listMahasiswa.size(); i++) {
            Mahasiswa mhs = listMahasiswa.get(i);
            model.addRow(new Object[]{i + 1, mhs.getNim(), mhs.getNama(), mhs.getJenisKelamin(), mhs.getNilai()});
        }
        return model;
    }

    private void insertData() {
        if (validateInput()) {
            String nim = nimField.getText();
            String nama = namaField.getText();
            String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
            String nilai = nilaiField.getText();

            listMahasiswa.add(new Mahasiswa(nim, nama, jenisKelamin, nilai));
            mahasiswaTable.setModel(setTable());
            clearForm();
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        }
    }

    private void updateData() {
        if (validateInput()) {
            Mahasiswa mhs = listMahasiswa.get(selectedIndex);
            mhs.setNim(nimField.getText());
            mhs.setNama(namaField.getText());
            mhs.setJenisKelamin(jenisKelaminComboBox.getSelectedItem().toString());
            mhs.setNilai(nilaiField.getText());

            mahasiswaTable.setModel(setTable());
            clearForm();
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        }
    }

    private void deleteData() {
        listMahasiswa.remove(selectedIndex);
        mahasiswaTable.setModel(setTable());
        clearForm();
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    }

    private void clearForm() {
        nimField.setText("");
        namaField.setText("");
        nilaiField.setText("");
        jenisKelaminComboBox.setSelectedIndex(0);
        updateButton.setText("Update");
        deleteButton.setVisible(false);
        selectedIndex = -1;
    }

    private boolean validateInput() {
        if (nimField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "NIM tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (namaField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (nilaiField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nilai tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void populateList() {
        listMahasiswa.add(new Mahasiswa("2203999", "Amelia Zalfa Julianti", "Perempuan", "A"));
        listMahasiswa.add(new Mahasiswa("2202292", "Muhammad Iqbal Fadhilah", "Laki-laki", "B"));
        listMahasiswa.add(new Mahasiswa("2202346", "Muhammad Rifky Afandi", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2210239", "Muhammad Hanif Abdillah", "Laki-laki", "B"));
        listMahasiswa.add(new Mahasiswa("2202046", "Nurainun", "Perempuan", "A"));
        listMahasiswa.add(new Mahasiswa("2205101", "Kelvin Julian Putra", "Laki-laki", "B"));
        listMahasiswa.add(new Mahasiswa("2200163", "Rifanny Lysara Annastasya", "Perempuan", "A"));
        listMahasiswa.add(new Mahasiswa("2202869", "Revana Faliha Salma", "Perempuan", "A"));
        listMahasiswa.add(new Mahasiswa("2209489", "Rakha Dhifiargo Hariadi", "Laki-laki", "C"));
        listMahasiswa.add(new Mahasiswa("2203142", "Roshan Syalwan Nurilham", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2200311", "Raden Rahman Ismail", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2200978", "Ratu Syahirah Khairunnisa", "Perempuan", "A"));
        listMahasiswa.add(new Mahasiswa("2204509", "Muhammad Fahreza Fauzan", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2205027", "Muhammad Rizki Revandi", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2203484", "Arya Aydin Margono", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2200481", "Marvel Ravindra Dioputra", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2209889", "Muhammad Fadlul Hafiizh", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2206697", "Rifa Sania", "Perempuan", "A"));
        listMahasiswa.add(new Mahasiswa("2207260", "Imam Chalish Rafidhul Haque", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2204343", "Meiva Labibah Putri", "Perempuan", "A"));
    }
}