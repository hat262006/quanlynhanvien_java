package form;

import service.ChamCong;
import dao.TaiKhoan_Dao;
import dao.QLNV_DAO;
import dao.ChamCong_DAO;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Hat262006
 */
public class Cham_Cong extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Cham_Cong.class.getName());

    public Cham_Cong() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH); //Toàn màn hình 
        loadDataToTable();
        themSuKienChonDong();
    }

    //    Hàm sắp xếp và đưa thông tin ra bảng
    private void loadDataToTable() {
        // Lấy giá trị từ ComboBox và RadioButton để xác định cách sắp xếp
        String sapXepTheo = comboBoxSapXep.getSelectedItem().toString(); // ComboBox chọn tiêu chí sắp xếp
        String thuTu = radioButtonTangDan.isSelected() ? "Tăng dần"
                : (radioButtonGiamDan.isSelected() ? "Giảm dần" : "Tăng dần");
        //Mặc định là tăng dần
        String keyWord = txtTimKiem.getText().trim();
        // Khởi tạo tên các cột trong bảng
        String[] columnNames = {
            "id", "Mã nhân viên", "Họ và tên", "Chức vụ", "Ngày",
            "Số giờ làm", "Giờ tăng ca", "Hệ số tăng ca", "Trạng thái"
        };

        // Tạo DefaultTableModel để cập nhật bảng
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Tạo DAO và lấy danh sách nhân viên đã sắp xếp
        ChamCong_DAO dao = new ChamCong_DAO();
        List<ChamCong> list = dao.getAll(keyWord, sapXepTheo, thuTu);
        // Gọi phương thức getAll với tiêu chí và thứ tự sắp xếp

        // Duyệt qua danh sách nhân viên và thêm từng dòng vào bảng
        for (ChamCong cc : list) {
            Object[] row = {
                cc.getId(),
                cc.getMaNV(),
                cc.getTenNV(),
                cc.getChucVu(),
                cc.getNgay(),
                cc.getGioLam(),
                cc.getGioTangCa(),
                cc.getHsTangCa(),
                cc.getTrangThai()
            };
            model.addRow(row);
        }

        // Cập nhật bảng với dữ liệu đã sắp xếp
        jTableNs.setModel(model); // 
        anid();
    }

    //    Hàm lấy ngày - tháng - năm
    public Date getDateCombo(JComboBox<String> cbNgay, JComboBox<String> cbThang, JComboBox<String> cbNam) {
        String ngay = (String) cbNgay.getSelectedItem();
        String thang = (String) cbThang.getSelectedItem();
        String nam = (String) cbNam.getSelectedItem();

        // Chuyển sang số để kiểm tra logic
        int d = Integer.parseInt(ngay);
        int m = Integer.parseInt(thang);
        int y = Integer.parseInt(nam);

        // Tìm số ngày tối đa của tháng đó
        int maxDay = 31;
        switch (m) {
            case 4:
            case 6:
            case 9:
            case 11:
                maxDay = 30;
                break;
            case 2:
                // Kiểm tra năm nhuận
                if ((y % 400 == 0) || (y % 4 == 0 && y % 100 != 0)) {
                    maxDay = 29;
                } else {
                    maxDay = 28;
                }
                break;
        }

        if (d > maxDay) {
            JOptionPane.showMessageDialog(null, "Ngày không hợp lệ! Tháng " + m + " chỉ có tối đa " + maxDay + " ngày.");
            return null; // trả về null nếu sai
        }

        // Tạo LocalDate rồi convert sang java.sql.Date
        LocalDate localDate = LocalDate.of(y, m, d);
        return java.sql.Date.valueOf(localDate);
    }

    //ẩn cột  id  
    private void anid() {
        jTableNs.getColumnModel().getColumn(0).setMinWidth(0);
        jTableNs.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableNs.getColumnModel().getColumn(0).setWidth(0);
    }

    // Phương thức thêm sự kiện cho JTable
    void themSuKienChonDong() {
        jTableNs.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Kiểm tra nếu có dòng được chọn
                int dongDuocChon = jTableNs.getSelectedRow();
                if (dongDuocChon != -1) {
                    // Gọi phương thức để cập nhật thông tin vào các JTextField
                    capNhatThongTinTuDongDuocChon(dongDuocChon);
                }
            }
        });
    }

    // Lấy thông tin các dòng từ bảng chấm công
    private void capNhatThongTinTuDongDuocChon(int chiSoDong) {
        String maNV = jTableNs.getValueAt(chiSoDong, 1).toString(); // Mã nhân viên
        float gioLam = Float.parseFloat(jTableNs.getValueAt(chiSoDong, 5).toString());
        float gioTangCa = Float.parseFloat(jTableNs.getValueAt(chiSoDong, 6).toString());
        float hsTangCa = Float.parseFloat(jTableNs.getValueAt(chiSoDong, 7).toString());

        String trangThai = jTableNs.getValueAt(chiSoDong, 5).toString(); // Trạng thái
        // Cập nhật vào các ô text field tương ứng
        txtMaNhanVien.setText(maNV);
        txtGioLam.setText(String.valueOf(gioLam));
        txtGioTangCa.setText(String.valueOf(gioTangCa));
        txtHeSoTangCa.setText(String.valueOf(hsTangCa));
    }

    // Hàm lấy dữ liệu - Kiểm tra - Tạo object Chấm công
    private ChamCong validateChamCong() {
        try {
            // Lấy dữ liệu từ form
            String maNv = txtMaNhanVien.getText().trim();
            String trangThai = cbTrangThai.getSelectedItem().toString();
            java.sql.Date ngay = getDateCombo(cbNgay1, cbThang1, cbNam1);
            String gioLam_str = txtGioLam.getText().trim();
            String gioTangCa_str = txtGioTangCa.getText().trim();
            String hsTangCa_str = txtHeSoTangCa.getText().trim();

            // Kiểm tra rỗng cơ bản
            if (gioLam_str.isEmpty() || gioTangCa_str.isEmpty() || hsTangCa_str.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return null;
            }

            // Chuyển đổi dữ liệu số
            float gioLam = Float.parseFloat(gioLam_str);
            float gioTangCa = Float.parseFloat(gioTangCa_str);
            float hsTangCa = Float.parseFloat(hsTangCa_str);

            // Tạo object ChamCong
            ChamCong cc = new ChamCong("",
                    maNv, "", "", gioLam, gioTangCa, hsTangCa,
                    ngay, trangThai);

            return cc;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giờ làm, giờ tăng ca, hệ số phải là số hợp lệ!");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        Cham_Cong = new javax.swing.JPanel();
        header2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        btnThem10 = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnThem12 = new javax.swing.JButton();
        btnThem14 = new javax.swing.JButton();
        txtTimKiem = new javax.swing.JTextField();
        btnLuu = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        Body = new javax.swing.JPanel();
        txtMaNhanVien = new javax.swing.JTextField();
        txtma_nhan_vien1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbThang1 = new javax.swing.JComboBox<>();
        txtso_dien_thoai2 = new javax.swing.JLabel();
        cbNgay1 = new javax.swing.JComboBox<>();
        cbNam1 = new javax.swing.JComboBox<>();
        txtngay_sinh4 = new javax.swing.JLabel();
        txtghi_chu1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtHeSoTangCa = new javax.swing.JTextField();
        txtngay_sinh5 = new javax.swing.JLabel();
        cbTrangThai = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        txtGioTangCa = new javax.swing.JTextField();
        txtque_quan2 = new javax.swing.JLabel();
        txtGioLam = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableNs = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtma_nhan_vien2 = new javax.swing.JLabel();
        comboBoxSapXep = new javax.swing.JComboBox<>();
        radioButtonTangDan = new javax.swing.JRadioButton();
        radioButtonGiamDan = new javax.swing.JRadioButton();
        btnThem13 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Cham_Cong.setBackground(new java.awt.Color(102, 204, 255));
        Cham_Cong.setPreferredSize(new java.awt.Dimension(1550, 1000));

        header2.setBackground(new java.awt.Color(0, 102, 204));
        header2.setPreferredSize(new java.awt.Dimension(1296, 110));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(0, 50));

        btnThem10.setBackground(new java.awt.Color(204, 204, 255));
        btnThem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/images__1_-removebg-preview.png"))); // NOI18N
        btnThem10.setText("Xóa");
        btnThem10.setFocusPainted(false);
        btnThem10.setPreferredSize(new java.awt.Dimension(110, 25));
        btnThem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem10ActionPerformed(evt);
            }
        });

        btnReset.setBackground(new java.awt.Color(204, 204, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/rs.png"))); // NOI18N
        btnReset.setText("Làm mới");
        btnReset.setFocusPainted(false);
        btnReset.setPreferredSize(new java.awt.Dimension(110, 25));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnThem12.setBackground(new java.awt.Color(204, 204, 255));
        btnThem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/them-removebg-preview.png"))); // NOI18N
        btnThem12.setText("Thêm");
        btnThem12.setFocusPainted(false);
        btnThem12.setPreferredSize(new java.awt.Dimension(110, 25));
        btnThem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem12ActionPerformed(evt);
            }
        });

        btnThem14.setBackground(new java.awt.Color(204, 204, 255));
        btnThem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/861627.png"))); // NOI18N
        btnThem14.setText("Tìm");
        btnThem14.setFocusPainted(false);
        btnThem14.setPreferredSize(new java.awt.Dimension(80, 25));
        btnThem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem14ActionPerformed(evt);
            }
        });

        txtTimKiem.setPreferredSize(new java.awt.Dimension(200, 25));

        btnLuu.setBackground(new java.awt.Color(204, 204, 255));
        btnLuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/4856668.png"))); // NOI18N
        btnLuu.setText("Lưu");
        btnLuu.setFocusPainted(false);
        btnLuu.setPreferredSize(new java.awt.Dimension(110, 25));
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(btnThem12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(btnThem10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68)
                .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(btnThem14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("BẢNG CHẤM CÔNG");

        javax.swing.GroupLayout header2Layout = new javax.swing.GroupLayout(header2);
        header2.setLayout(header2Layout);
        header2Layout.setHorizontalGroup(
            header2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(header2Layout.createSequentialGroup()
                .addGap(517, 517, 517)
                .addComponent(jLabel4)
                .addContainerGap(556, Short.MAX_VALUE))
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 1297, Short.MAX_VALUE)
        );
        header2Layout.setVerticalGroup(
            header2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(header2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        jPanel1.setBackground(new java.awt.Color(0, 102, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/logo.png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("BẢNG CHẤM CÔNG");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(54, 54, 54))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel8.setBackground(new java.awt.Color(0, 102, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton3.setBackground(new java.awt.Color(0, 102, 204));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/dx.png"))); // NOI18N
        jButton3.setText("ĐĂNG XUẤT");
        jButton3.setActionCommand("ĐĂNG XUẤT          ");
        jButton3.setBorder(null);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton3.setIconTextGap(15);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        Body.setBackground(new java.awt.Color(102, 204, 255));
        Body.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Body.setMinimumSize(new java.awt.Dimension(1492, 712));
        Body.setPreferredSize(new java.awt.Dimension(1400, 350));

        txtMaNhanVien.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtMaNhanVien.setPreferredSize(new java.awt.Dimension(64, 25));
        txtMaNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaNhanVienActionPerformed(evt);
            }
        });

        txtma_nhan_vien1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtma_nhan_vien1.setText("Mã nhân viên");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        cbThang1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cbThang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbThang1ActionPerformed(evt);
            }
        });

        txtso_dien_thoai2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtso_dien_thoai2.setText("Tăng ca (giờ)");

        cbNgay1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        cbNgay1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNgay1ActionPerformed(evt);
            }
        });

        cbNam1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2025", "2024", "2023" }));
        cbNam1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNam1ActionPerformed(evt);
            }
        });

        txtngay_sinh4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtngay_sinh4.setText("Ngày ");

        txtghi_chu1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtghi_chu1.setText("Trạng thái");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        txtHeSoTangCa.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtHeSoTangCa.setPreferredSize(new java.awt.Dimension(64, 25));
        txtHeSoTangCa.setVerifyInputWhenFocusTarget(false);

        txtngay_sinh5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtngay_sinh5.setText("Hệ số tăng ca");

        cbTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đi làm", "Nghỉ phép", "Nghỉ không phép" }));
        cbTrangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTrangThaiActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        txtGioTangCa.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtGioTangCa.setPreferredSize(new java.awt.Dimension(64, 25));

        txtque_quan2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtque_quan2.setText("Giờ làm");

        txtGioLam.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtGioLam.setPreferredSize(new java.awt.Dimension(64, 25));
        txtGioLam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGioLamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BodyLayout = new javax.swing.GroupLayout(Body);
        Body.setLayout(BodyLayout);
        BodyLayout.setHorizontalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BodyLayout.createSequentialGroup()
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7))
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BodyLayout.createSequentialGroup()
                            .addGap(53, 53, 53)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtngay_sinh4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtma_nhan_vien1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtque_quan2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addComponent(cbNgay1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(cbThang1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbNam1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtGioLam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMaNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtso_dien_thoai2)
                    .addComponent(txtghi_chu1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtngay_sinh5, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGioTangCa, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHeSoTangCa, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(162, 162, 162))
        );
        BodyLayout.setVerticalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BodyLayout.createSequentialGroup()
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtghi_chu1)))
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtma_nhan_vien1)
                            .addComponent(txtMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtso_dien_thoai2)
                            .addComponent(txtGioTangCa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtque_quan2)
                            .addComponent(txtGioLam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtngay_sinh5)
                            .addComponent(txtHeSoTangCa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbNgay1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbNam1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbThang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtngay_sinh4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(102, 204, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel9.setPreferredSize(new java.awt.Dimension(1200, 480));

        jTableNs.setBackground(new java.awt.Color(204, 255, 255));
        jTableNs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Họ và tên", "Chức vụ", "Ngày", "Số giờ làm", "Giờ tăng ca", "Hệ số tăng ca", "Trạng thái"
            }
        ));
        jTableNs.setPreferredSize(new java.awt.Dimension(330, 330));
        jScrollPane2.setViewportView(jTableNs);

        txtma_nhan_vien2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtma_nhan_vien2.setText("Sắp xếp theo :");

        comboBoxSapXep.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã nhân viên", "Tên nhân viên", "Chức vụ", "Số giờ tăng ca", "Hệ số tăng ca", "Giờ làm", "Ngày làm", "Trạng thái" }));
        comboBoxSapXep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxSapXepActionPerformed(evt);
            }
        });

        buttonGroup1.add(radioButtonTangDan);
        radioButtonTangDan.setText("Tăng dần");
        radioButtonTangDan.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        radioButtonTangDan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonTangDanActionPerformed(evt);
            }
        });

        buttonGroup1.add(radioButtonGiamDan);
        radioButtonGiamDan.setText("Giảm dần");
        radioButtonGiamDan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonGiamDanActionPerformed(evt);
            }
        });

        btnThem13.setBackground(new java.awt.Color(204, 204, 255));
        btnThem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/sm_5b33460f04516-removebg-preview.png"))); // NOI18N
        btnThem13.setText("Xuất File");
        btnThem13.setFocusPainted(false);
        btnThem13.setPreferredSize(new java.awt.Dimension(70, 25));
        btnThem13.setVerifyInputWhenFocusTarget(false);
        btnThem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(txtma_nhan_vien2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(comboBoxSapXep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125)
                .addComponent(radioButtonTangDan, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(radioButtonGiamDan, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 412, Short.MAX_VALUE)
                .addComponent(btnThem13, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtma_nhan_vien2)
                            .addComponent(comboBoxSapXep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radioButtonTangDan)
                            .addComponent(radioButtonGiamDan)
                            .addComponent(btnThem13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(178, 178, 178))
        );

        jPanel2.setBackground(new java.awt.Color(102, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton1.setBackground(new java.awt.Color(102, 204, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cv.png"))); // NOI18N
        jButton1.setText("Thông Tin Cá Nhân");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.setFocusPainted(false);
        jButton1.setPreferredSize(new java.awt.Dimension(75, 80));

        jButton2.setBackground(new java.awt.Color(102, 204, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/ns.png"))); // NOI18N
        jButton2.setText("Quản Lý Nhân Sự");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.setFocusPainted(false);
        jButton2.setPreferredSize(new java.awt.Dimension(75, 80));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 204, 153));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Luong.png"))); // NOI18N
        jButton4.setText("Bảng Chấm Công");
        jButton4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.setFocusPainted(false);
        jButton4.setPreferredSize(new java.awt.Dimension(75, 80));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(102, 204, 255));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/L.png"))); // NOI18N
        jButton5.setText("Hệ Thống Lương");
        jButton5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.setFocusPainted(false);
        jButton5.setPreferredSize(new java.awt.Dimension(75, 80));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Cham_CongLayout = new javax.swing.GroupLayout(Cham_Cong);
        Cham_Cong.setLayout(Cham_CongLayout);
        Cham_CongLayout.setHorizontalGroup(
            Cham_CongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Cham_CongLayout.createSequentialGroup()
                .addGroup(Cham_CongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Cham_CongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(header2, javax.swing.GroupLayout.PREFERRED_SIZE, 1297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Body, javax.swing.GroupLayout.PREFERRED_SIZE, 1280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 1280, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        Cham_CongLayout.setVerticalGroup(
            Cham_CongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Cham_CongLayout.createSequentialGroup()
                .addGroup(Cham_CongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(header2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Cham_CongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(Cham_CongLayout.createSequentialGroup()
                        .addComponent(Body, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Cham_CongLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(Cham_Cong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Cham_Cong, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem10ActionPerformed
        // Xóa
        try {
            int dong = jTableNs.getSelectedRow();  // Lấy chỉ số dòng
            if (dong != -1) {
                // Lấy mã nhân viên từ cột "Mã nhân viên" (cột 0 chứa id)
                String id = (String) jTableNs.getValueAt(dong, 0);

                // Gọi phương thức xóa nhân viên từ DAO
                QLNV_DAO xoa = new QLNV_DAO();
                String form = "Cham_Cong"; // Xác định form để xóa 
                boolean success = xoa.xoaNhanVien(id, form);

                if (success) {
                    // Nếu xóa thành công, xóa dòng trong JTable và hiển thị thông báo
                    ((DefaultTableModel) jTableNs.getModel()).removeRow(dong);
                    JOptionPane.showMessageDialog(this, "Nhân viên đã được xóa!");
                    loadDataToTable();
                    lamMoi();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để xóa.");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key")) {
                JOptionPane.showMessageDialog(this,
                        "❌ Không thể xóa nhân viên này vì vẫn còn dữ liệu chấm công!",
                        "Lỗi ràng buộc dữ liệu",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Có lỗi SQL xảy ra: " + e.getMessage(),
                        "Lỗi SQL",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnThem10ActionPerformed
    public void lamMoi() {
        txtMaNhanVien.setText("");
        txtGioLam.setText("");
        txtGioTangCa.setText("");
        txtHeSoTangCa.setText("");
        txtTimKiem.setText("");
    }
    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        lamMoi();
        loadDataToTable();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnThem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem12ActionPerformed
        // THÊM
        ChamCong_DAO cham_cong = new ChamCong_DAO();
        ChamCong cc = validateChamCong();
        if (cc == null) {
            return;
        }
        String loi = cham_cong.kiemTraTrung(cc.getMaNV(), cc.getNgay());
        if (!loi.isEmpty()) {
            JOptionPane.showMessageDialog(this, loi);
            return;
        }
        if (cham_cong.themChamCong(cc)) {
            JOptionPane.showMessageDialog(this, "Chấm công thành công!");
            loadDataToTable();
        } else {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không đúng!");
        }

    }//GEN-LAST:event_btnThem12ActionPerformed

    private void btnThem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem14ActionPerformed
        // Tìm kiếm
        loadDataToTable();
    }//GEN-LAST:event_btnThem14ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Đăng Xuất
        TaiKhoan_Dao.dangXuat(this);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtMaNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaNhanVienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaNhanVienActionPerformed

    private void cbThang1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbThang1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbThang1ActionPerformed

    private void cbNgay1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNgay1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbNgay1ActionPerformed

    private void cbNam1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNam1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbNam1ActionPerformed

    private void cbTrangThaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTrangThaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTrangThaiActionPerformed

    private void comboBoxSapXepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxSapXepActionPerformed
        // Sắp xếp
        loadDataToTable();

    }//GEN-LAST:event_comboBoxSapXepActionPerformed

    private void radioButtonTangDanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonTangDanActionPerformed
        // Tăng Dần
        loadDataToTable();
    }//GEN-LAST:event_radioButtonTangDanActionPerformed

    private void radioButtonGiamDanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonGiamDanActionPerformed
        // Giảm dần
        loadDataToTable();
    }//GEN-LAST:event_radioButtonGiamDanActionPerformed

    private void btnThem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem13ActionPerformed
        // Xuất excel
    }//GEN-LAST:event_btnThem13ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtGioLamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGioLamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGioLamActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Nhấp vào chuyển sang trang Quản Lí nhân sự
        QL_NS QL_NS = new QL_NS();
        QL_NS.show();
        this.dispose();//Tắt form 
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        // Lưu

        // Lấy dữ liệu từ các JTextField
        ChamCong cc = validateChamCong();

        if (cc != null) {
            // Gọi DAO để cập nhật thông tin nhân viên
            ChamCong_DAO dao = new ChamCong_DAO();
            boolean success = dao.capNhatNhanVien(cc);

            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadDataToTable();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Mã nhân viên không chính xác");
            }
        }
    }//GEN-LAST:event_btnLuuActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Nhấp vào chuyển sang trang Hệ Thống Lương
        Tinh_Luong Tinh_Luong = new Tinh_Luong();
        Tinh_Luong.show();
        this.dispose();//Tắt Form 
    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Cham_Cong().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Body;
    private javax.swing.JPanel Cham_Cong;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnThem10;
    private javax.swing.JButton btnThem12;
    private javax.swing.JButton btnThem13;
    private javax.swing.JButton btnThem14;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbNam1;
    private javax.swing.JComboBox<String> cbNgay1;
    private javax.swing.JComboBox<String> cbThang1;
    private javax.swing.JComboBox<String> cbTrangThai;
    private javax.swing.JComboBox<String> comboBoxSapXep;
    private javax.swing.JPanel header2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableNs;
    private javax.swing.JRadioButton radioButtonGiamDan;
    private javax.swing.JRadioButton radioButtonTangDan;
    private javax.swing.JTextField txtGioLam;
    private javax.swing.JTextField txtGioTangCa;
    private javax.swing.JTextField txtHeSoTangCa;
    private javax.swing.JTextField txtMaNhanVien;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JLabel txtghi_chu1;
    private javax.swing.JLabel txtma_nhan_vien1;
    private javax.swing.JLabel txtma_nhan_vien2;
    private javax.swing.JLabel txtngay_sinh4;
    private javax.swing.JLabel txtngay_sinh5;
    private javax.swing.JLabel txtque_quan2;
    private javax.swing.JLabel txtso_dien_thoai2;
    // End of variables declaration//GEN-END:variables
}
