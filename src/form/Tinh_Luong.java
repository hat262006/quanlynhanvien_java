package form;

import form.Cham_Cong;
import service.ChamCong;
import dao.TinhLuong_DAO;
import dao.TaiKhoan_Dao;
import model.nv_VanPhong;
import model.nhan_vien;
import java.awt.CardLayout;
import java.sql.SQLException;
import java.awt.Color;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import dao.QLNV_DAO;
import model.nv_May;
import model.nv_ThoiVu;
import model.nv_ThoiVu;
import model.nv_ThoiVu;

/**
 *
 * @author Hat262006
 */
public class Tinh_Luong extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Tinh_Luong.class.getName());

    public Tinh_Luong() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH); //Toàn màn hình 
        loadDataToTable();
        themSuKienChonDong();

    }

    // Hàm sắp xếp và đưa thông tin ra bảng
    private void loadDataToTable() {
        // Lấy giá trị từ ComboBox và RadioButton để xác định cách sắp xếp
        String sapXepTheo = comboBoxSapXep.getSelectedItem().toString();
        String thuTu = radioButtonTangDan.isSelected() ? "Tăng dần"
                : (radioButtonGiamDan.isSelected() ? "Giảm dần" : "Tăng dần");
        String chucVu = comboChucVu.getSelectedItem().toString();
        String keyWord = txtTimKiem.getText().trim();
        // Khởi tạo tên các cột trong bảng
        String[] columnNames = {
            "Mã nhân viên", "Họ và tên", "Chức vụ", "Giờ làm",
            "Giờ tăng ca", "Hệ số tăng ca", "Lương"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Tạo DAO và lấy danh sách nhân viên đã sắp xếp
        TinhLuong_DAO dao = new TinhLuong_DAO();
        // Hiển thị theo danh sách
        List<Object[]> list = dao.getAll(keyWord, sapXepTheo, thuTu, chucVu);

        for (Object[] row : list) {
            model.addRow(row);
        }

        jTableNs.setModel(model);
    }

    // HÀM TÍNH LƯƠNG THEO TỪNG LOẠI NHÂN VIÊN
    public void tinhLuong(String maNV,
            String chuc_Vu,
            String luongMotGioStr,
            String luongCoDinhStr,
            String donGiaStr,
            String sanLuongStr,
            String phuCapStr,
            String thuongStr,
            String phatStr) {
        try {
            TinhLuong_DAO dao = new TinhLuong_DAO();
            List<ChamCong> dsChamCong = dao.getChamCongByNV(maNV);

            if (dsChamCong.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy dữ liệu chấm công!");
                return;
            }

            ChamCong cc = dsChamCong.get(0);
            String chucVu = cc.getChucVu().trim();
            // Kiểm tra chức vụ có khớp form không
            if (!chucVu.equals(chuc_Vu)) {
                JOptionPane.showMessageDialog(null,
                        "Mã nhân viên không đúng với chức vụ hiện tại!\n"
                        + "Chức vụ trong hệ thống: " + chucVu,
                        "Sai chức vụ", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (maNV.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Mã nhân viên không được bỏ trống");
                return;
            }
            nhan_vien nv = null;

            switch (chucVu) {
                case "Nhân viên thời vụ":
                    if (luongMotGioStr.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập thông tin");
                        return;
                    }
                    double luongMotGio = Double.parseDouble(luongMotGioStr);
                    if (luongMotGio < 0) {
                        JOptionPane.showMessageDialog(null, "Lương một giờ phải >0 !");
                        return;
                    }

                    nv = new nv_ThoiVu(luongMotGio,
                            cc.getMaNV(),
                            cc.getTenNV(),
                            "",
                            cc.getChucVu(),
                            "", "", "", "", "", "", "");
                    break;

                case "Công nhân may":
                    if (luongCoDinhStr.isEmpty() || donGiaStr.isEmpty() || sanLuongStr.isEmpty() || phuCapStr.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin !");
                        return;
                    }
                    double luongCoDinh = Double.parseDouble(luongCoDinhStr);
                    float donGia = Float.parseFloat(donGiaStr);
                    int sanLuong = Integer.parseInt(sanLuongStr);
                    double phuCap = Double.parseDouble(phuCapStr);
                    if (luongCoDinh < 0 || donGia < 0 || sanLuong < 0 || phuCap < 0) {
                        JOptionPane.showMessageDialog(null, "Chỉ số nhập vào phải > 0 !");
                        return;
                    }
                    nv = new nv_May(sanLuong, donGia, luongCoDinh, phuCap,
                            cc.getMaNV(), cc.getTenNV(), "", chucVu,
                            "", "", "", "", "", "", "");
                    break;

                case "Nhân viên văn phòng":
                    double luongCB = Double.parseDouble(luongCoDinhStr);
                    double thuong = Double.parseDouble(thuongStr);
                    double phat = Double.parseDouble(phatStr);
                    if (luongCB < 0 || thuong < 0 || phat < 0) {
                        JOptionPane.showMessageDialog(null, "Chỉ số nhập vào phải > 0 !");
                        return;
                    }
                    nv = new nv_VanPhong(luongCB, thuong, phat,
                            cc.getMaNV(), cc.getTenNV(), "", chucVu,
                            "", "", "", "", "", "", "");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Chức vụ chưa hỗ trợ tính lương!");
                    return;
            }

            // Tính lương
            DecimalFormat df = new DecimalFormat("#0.0");
            double tongLuong = dao.tinhLuongTheoChamCong(nv, dsChamCong);
            String tongLuongStr = df.format(tongLuong);
            // Xuất ra form
            double tongGioLam = dsChamCong.stream().mapToDouble(ChamCong::getGioLam).sum();
            double tongTangCa = dsChamCong.stream().mapToDouble(ChamCong::getGioTangCa).sum();
            MaNV.setText(cc.getMaNV());
            TenNV.setText(cc.getTenNV());
            ChucVu.setText(chucVu);
            GioLam.setText(String.valueOf(tongGioLam));
            TangCa.setText(String.valueOf(tongTangCa));
            HeSoTangCa.setText(String.valueOf(cc.getHsTangCa()));
            TongLuong.setText(tongLuongStr);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Các giá trị nhập vào phải là số!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
        }
    }

//    Sự kiện cho nút chuyển layout
    private void chuyenLayout(String panelName, JButton activeButton) {
        CardLayout cl = (CardLayout) Layout.getLayout(); // layoutPanel là JPanel chứa CardLayout
        cl.show(Layout, panelName);

        JButton[] buttons = {btnCongNhan, btnThoiVu, btnVanPhong};

        for (JButton btn : buttons) {
            btn.setEnabled(true);
            btn.setBackground(new Color(0, 153, 255));
        }

        activeButton.setEnabled(false);
        activeButton.setBackground(null);

    }

    //Hàm Thêm Thông Tin
    private void themBangLuong() {
        try {
            String maNV = MaNV.getText();
            String tenNV = TenNV.getText();
            String chucVu = ChucVu.getText();
            float gioLam = Float.parseFloat(GioLam.getText());
            float tangCa = Float.parseFloat(TangCa.getText());
            float heSoTangCa = Float.parseFloat(HeSoTangCa.getText());
            double tongLuong = Double.parseDouble(TongLuong.getText());

            // Gọi DAO trực tiếp, không cần tạo đối tượng nhân viên
            TinhLuong_DAO dao = new TinhLuong_DAO();
            String loi = dao.kiemTraTrung(maNV);
            if (!loi.isEmpty()) {
                JOptionPane.showMessageDialog(this, loi);
                return;
            }
            boolean kq = dao.luuBangLuong(maNV, tenNV, chucVu, gioLam, tangCa, heSoTangCa, tongLuong);
            if (kq) {
                JOptionPane.showMessageDialog(null, "Đã thêm lương thành công!");
                loadDataToTable();
            } else {
                JOptionPane.showMessageDialog(null, "Thêm lương thất bại!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Dữ liệu trên form không hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
        }
    }

    // Phương thức thêm sự kiện cho JTable
    public void themSuKienChonDong() {
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

    // Lấy thông tin các dòng 
    private void capNhatThongTinTuDongDuocChon(int chiSoDong) {
        String maNV = (String) jTableNs.getValueAt(chiSoDong, 0);
        String chucVu = (String) jTableNs.getValueAt(chiSoDong, 2);

        switch (chucVu) {
            case "Công nhân may":
                txtMaNhanVien1.setText(maNV);

                break;
            case "Nhân viên văn phòng":
                txtMaNhanVien3.setText(maNV);

                break;
            case "Nhân viên thời vụ":
                txtMaNhanVien2.setText(maNV);

                break;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        Tinh_Luong = new javax.swing.JPanel();
        header3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        btnThem11 = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnTimKiem = new javax.swing.JButton();
        txtTimKiem = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        Body = new javax.swing.JPanel();
        Layout = new javax.swing.JPanel();
        panelMay = new javax.swing.JPanel();
        txtma_nhan_vien9 = new javax.swing.JLabel();
        txtma_nhan_vien10 = new javax.swing.JLabel();
        txtLuongCoDinh = new javax.swing.JTextField();
        btnThem_May1 = new javax.swing.JButton();
        txtMaNhanVien1 = new javax.swing.JTextField();
        btnThemMay1 = new javax.swing.JButton();
        txtma_nhan_vien11 = new javax.swing.JLabel();
        txtma_nhan_vien12 = new javax.swing.JLabel();
        txtma_nhan_vien13 = new javax.swing.JLabel();
        txtDonGia = new javax.swing.JTextField();
        txtSanLuong = new javax.swing.JTextField();
        txtPhuCap = new javax.swing.JTextField();
        panelVanPhong = new javax.swing.JPanel();
        txtma_nhan_vien4 = new javax.swing.JLabel();
        txtma_nhan_vien5 = new javax.swing.JLabel();
        btnThem_May = new javax.swing.JButton();
        txtMaNhanVien3 = new javax.swing.JTextField();
        btnTraCuu_VP = new javax.swing.JButton();
        txtma_nhan_vien6 = new javax.swing.JLabel();
        txtma_nhan_vien8 = new javax.swing.JLabel();
        txtLuongCoBan = new javax.swing.JTextField();
        txtThuong = new javax.swing.JTextField();
        txtPhat = new javax.swing.JTextField();
        panelPartTime = new javax.swing.JPanel();
        txtma_nhan_vien1 = new javax.swing.JLabel();
        txtma_nhan_vien3 = new javax.swing.JLabel();
        txtLuong1h = new javax.swing.JTextField();
        btnThem17 = new javax.swing.JButton();
        txtMaNhanVien2 = new javax.swing.JTextField();
        btnThem18 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        MaNV = new javax.swing.JTextField();
        TenNV = new javax.swing.JTextField();
        ChucVu = new javax.swing.JTextField();
        GioLam = new javax.swing.JTextField();
        TangCa = new javax.swing.JTextField();
        HeSoTangCa = new javax.swing.JTextField();
        TongLuong = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableNs = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtma_nhan_vien2 = new javax.swing.JLabel();
        comboBoxSapXep = new javax.swing.JComboBox<>();
        radioButtonTangDan = new javax.swing.JRadioButton();
        radioButtonGiamDan = new javax.swing.JRadioButton();
        btnThem13 = new javax.swing.JButton();
        txtma_nhan_vien7 = new javax.swing.JLabel();
        comboChucVu = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnVanPhong = new javax.swing.JButton();
        btnCongNhan = new javax.swing.JButton();
        btnThoiVu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Tinh_Luong.setBackground(new java.awt.Color(102, 204, 255));
        Tinh_Luong.setPreferredSize(new java.awt.Dimension(1550, 1000));

        header3.setBackground(new java.awt.Color(0, 102, 204));
        header3.setPreferredSize(new java.awt.Dimension(1296, 110));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setPreferredSize(new java.awt.Dimension(0, 50));

        btnThem11.setBackground(new java.awt.Color(204, 204, 255));
        btnThem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/images__1_-removebg-preview.png"))); // NOI18N
        btnThem11.setText("Xóa");
        btnThem11.setFocusPainted(false);
        btnThem11.setMinimumSize(new java.awt.Dimension(110, 25));
        btnThem11.setPreferredSize(new java.awt.Dimension(110, 25));
        btnThem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem11ActionPerformed(evt);
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

        btnTimKiem.setBackground(new java.awt.Color(204, 204, 255));
        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/861627.png"))); // NOI18N
        btnTimKiem.setText("Tìm");
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.setPreferredSize(new java.awt.Dimension(80, 25));
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        txtTimKiem.setPreferredSize(new java.awt.Dimension(200, 25));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(btnThem11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("HỆ THỐNG LƯƠNG");

        javax.swing.GroupLayout header3Layout = new javax.swing.GroupLayout(header3);
        header3.setLayout(header3Layout);
        header3Layout.setHorizontalGroup(
            header3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(header3Layout.createSequentialGroup()
                .addGap(517, 517, 517)
                .addComponent(jLabel4)
                .addContainerGap(563, Short.MAX_VALUE))
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 1297, Short.MAX_VALUE)
        );
        header3Layout.setVerticalGroup(
            header3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(header3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        jPanel1.setBackground(new java.awt.Color(0, 102, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/logo.png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("HỆ THỐNG LƯƠNG");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
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

        Layout.setBackground(new java.awt.Color(255, 255, 51));
        Layout.setPreferredSize(new java.awt.Dimension(1000, 157));
        Layout.setLayout(new java.awt.CardLayout());

        panelMay.setBackground(new java.awt.Color(102, 204, 255));
        panelMay.setMaximumSize(new java.awt.Dimension(32767, 100));
        panelMay.setPreferredSize(new java.awt.Dimension(1276, 170));

        txtma_nhan_vien9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtma_nhan_vien9.setText("Mã nhân viên");

        txtma_nhan_vien10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtma_nhan_vien10.setText("Lương cơ bản");

        txtLuongCoDinh.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtLuongCoDinh.setPreferredSize(new java.awt.Dimension(130, 25));
        txtLuongCoDinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLuongCoDinhActionPerformed(evt);
            }
        });

        btnThem_May1.setBackground(new java.awt.Color(204, 204, 204));
        btnThem_May1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/them-removebg-preview.png"))); // NOI18N
        btnThem_May1.setText("Thêm");
        btnThem_May1.setPreferredSize(new java.awt.Dimension(100, 25));
        btnThem_May1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem_May1ActionPerformed(evt);
            }
        });

        txtMaNhanVien1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtMaNhanVien1.setPreferredSize(new java.awt.Dimension(130, 25));
        txtMaNhanVien1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaNhanVien1ActionPerformed(evt);
            }
        });

        btnThemMay1.setBackground(new java.awt.Color(204, 204, 204));
        btnThemMay1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/861627.png"))); // NOI18N
        btnThemMay1.setText("Tra cứu");
        btnThemMay1.setPreferredSize(new java.awt.Dimension(100, 25));
        btnThemMay1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemMay1ActionPerformed(evt);
            }
        });

        txtma_nhan_vien11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtma_nhan_vien11.setText("Phụ cấp");

        txtma_nhan_vien12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtma_nhan_vien12.setText("Sản lượng ");

        txtma_nhan_vien13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtma_nhan_vien13.setText("Đơn giá");

        txtDonGia.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtDonGia.setPreferredSize(new java.awt.Dimension(130, 25));
        txtDonGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDonGiaActionPerformed(evt);
            }
        });

        txtSanLuong.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtSanLuong.setPreferredSize(new java.awt.Dimension(130, 25));
        txtSanLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSanLuongActionPerformed(evt);
            }
        });

        txtPhuCap.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPhuCap.setPreferredSize(new java.awt.Dimension(130, 25));
        txtPhuCap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhuCapActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMayLayout = new javax.swing.GroupLayout(panelMay);
        panelMay.setLayout(panelMayLayout);
        panelMayLayout.setHorizontalGroup(
            panelMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMayLayout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(panelMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtma_nhan_vien10, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtma_nhan_vien9, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtma_nhan_vien11, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(77, 77, 77)
                .addGroup(panelMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMayLayout.createSequentialGroup()
                        .addComponent(txtMaNhanVien1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 240, Short.MAX_VALUE)
                        .addComponent(txtma_nhan_vien12, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelMayLayout.createSequentialGroup()
                        .addComponent(txtLuongCoDinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelMayLayout.createSequentialGroup()
                        .addComponent(txtPhuCap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 240, Short.MAX_VALUE)
                        .addComponent(txtma_nhan_vien13, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(panelMayLayout.createSequentialGroup()
                        .addComponent(txtSanLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(129, 129, 129)
                        .addComponent(btnThemMay1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelMayLayout.createSequentialGroup()
                        .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnThem_May1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(117, 117, 117))
        );
        panelMayLayout.setVerticalGroup(
            panelMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMayLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panelMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMayLayout.createSequentialGroup()
                        .addGroup(panelMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaNhanVien1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtma_nhan_vien9))
                        .addGap(21, 21, 21)
                        .addGroup(panelMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtma_nhan_vien10)
                            .addComponent(txtLuongCoDinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSanLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtma_nhan_vien12)
                        .addComponent(btnThemMay1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMayLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(btnThem_May1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMayLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(panelMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtma_nhan_vien11)
                            .addComponent(txtPhuCap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtma_nhan_vien13))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        Layout.add(panelMay, "panelMay");

        panelVanPhong.setBackground(new java.awt.Color(102, 204, 255));
        panelVanPhong.setMaximumSize(new java.awt.Dimension(32767, 100));
        panelVanPhong.setPreferredSize(new java.awt.Dimension(1276, 100));

        txtma_nhan_vien4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtma_nhan_vien4.setText("Mã nhân viên");

        txtma_nhan_vien5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtma_nhan_vien5.setText("Lương cơ bản");

        btnThem_May.setBackground(new java.awt.Color(204, 204, 204));
        btnThem_May.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/them-removebg-preview.png"))); // NOI18N
        btnThem_May.setText("Thêm");
        btnThem_May.setPreferredSize(new java.awt.Dimension(100, 25));
        btnThem_May.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem_MayActionPerformed(evt);
            }
        });

        txtMaNhanVien3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtMaNhanVien3.setPreferredSize(new java.awt.Dimension(130, 25));
        txtMaNhanVien3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaNhanVien3ActionPerformed(evt);
            }
        });

        btnTraCuu_VP.setBackground(new java.awt.Color(204, 204, 204));
        btnTraCuu_VP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/861627.png"))); // NOI18N
        btnTraCuu_VP.setText("Tra cứu");
        btnTraCuu_VP.setPreferredSize(new java.awt.Dimension(100, 25));
        btnTraCuu_VP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraCuu_VPActionPerformed(evt);
            }
        });

        txtma_nhan_vien6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtma_nhan_vien6.setText("Phạt");

        txtma_nhan_vien8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtma_nhan_vien8.setText("Thưởng");

        txtLuongCoBan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtLuongCoBan.setPreferredSize(new java.awt.Dimension(130, 25));
        txtLuongCoBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLuongCoBanActionPerformed(evt);
            }
        });

        txtThuong.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtThuong.setPreferredSize(new java.awt.Dimension(130, 25));
        txtThuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtThuongActionPerformed(evt);
            }
        });

        txtPhat.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPhat.setPreferredSize(new java.awt.Dimension(130, 25));
        txtPhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelVanPhongLayout = new javax.swing.GroupLayout(panelVanPhong);
        panelVanPhong.setLayout(panelVanPhongLayout);
        panelVanPhongLayout.setHorizontalGroup(
            panelVanPhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelVanPhongLayout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(panelVanPhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtma_nhan_vien5, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtma_nhan_vien4, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(83, 83, 83)
                .addGroup(panelVanPhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelVanPhongLayout.createSequentialGroup()
                        .addComponent(txtMaNhanVien3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 184, Short.MAX_VALUE)
                        .addComponent(txtma_nhan_vien8, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelVanPhongLayout.createSequentialGroup()
                        .addComponent(txtLuongCoBan, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtma_nhan_vien6, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelVanPhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelVanPhongLayout.createSequentialGroup()
                        .addComponent(txtPhat, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(117, 117, 117)
                        .addComponent(btnThem_May, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelVanPhongLayout.createSequentialGroup()
                        .addComponent(txtThuong, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(117, 117, 117)
                        .addComponent(btnTraCuu_VP, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(179, 179, 179))
        );
        panelVanPhongLayout.setVerticalGroup(
            panelVanPhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVanPhongLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(panelVanPhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTraCuu_VP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtma_nhan_vien8)
                    .addComponent(txtThuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(panelVanPhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtma_nhan_vien5)
                    .addComponent(txtma_nhan_vien6)
                    .addComponent(btnThem_May, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLuongCoBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
            .addGroup(panelVanPhongLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(panelVanPhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtma_nhan_vien4)
                    .addComponent(txtMaNhanVien3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        Layout.add(panelVanPhong, "panelVanPhong");

        panelPartTime.setBackground(new java.awt.Color(102, 204, 255));
        panelPartTime.setMaximumSize(new java.awt.Dimension(32767, 100));
        panelPartTime.setPreferredSize(new java.awt.Dimension(1276, 100));

        txtma_nhan_vien1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtma_nhan_vien1.setText("Mã nhân viên");

        txtma_nhan_vien3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtma_nhan_vien3.setText("Lương một giờ ");

        txtLuong1h.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtLuong1h.setPreferredSize(new java.awt.Dimension(64, 25));

        btnThem17.setBackground(new java.awt.Color(204, 204, 204));
        btnThem17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/them-removebg-preview.png"))); // NOI18N
        btnThem17.setText("Thêm");
        btnThem17.setPreferredSize(new java.awt.Dimension(110, 25));
        btnThem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem17ActionPerformed(evt);
            }
        });

        txtMaNhanVien2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtMaNhanVien2.setPreferredSize(new java.awt.Dimension(64, 25));

        btnThem18.setBackground(new java.awt.Color(204, 204, 204));
        btnThem18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/861627.png"))); // NOI18N
        btnThem18.setText("Tra cứu");
        btnThem18.setPreferredSize(new java.awt.Dimension(110, 25));
        btnThem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPartTimeLayout = new javax.swing.GroupLayout(panelPartTime);
        panelPartTime.setLayout(panelPartTimeLayout);
        panelPartTimeLayout.setHorizontalGroup(
            panelPartTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPartTimeLayout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addGroup(panelPartTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtma_nhan_vien1)
                    .addComponent(txtma_nhan_vien3))
                .addGap(82, 82, 82)
                .addGroup(panelPartTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaNhanVien2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLuong1h, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(367, 367, 367)
                .addGroup(panelPartTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThem18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(365, Short.MAX_VALUE))
        );
        panelPartTimeLayout.setVerticalGroup(
            panelPartTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPartTimeLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(panelPartTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtma_nhan_vien1)
                    .addComponent(txtMaNhanVien2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(panelPartTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLuong1h, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtma_nhan_vien3))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        Layout.add(panelPartTime, "panelPartTime");

        jPanel12.setBackground(new java.awt.Color(102, 204, 255));

        MaNV.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        MaNV.setEnabled(false);
        MaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MaNVActionPerformed(evt);
            }
        });

        TenNV.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        TenNV.setEnabled(false);
        TenNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TenNVActionPerformed(evt);
            }
        });

        ChucVu.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ChucVu.setEnabled(false);
        ChucVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChucVuActionPerformed(evt);
            }
        });

        GioLam.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        GioLam.setEnabled(false);
        GioLam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GioLamActionPerformed(evt);
            }
        });

        TangCa.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        TangCa.setEnabled(false);
        TangCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TangCaActionPerformed(evt);
            }
        });

        HeSoTangCa.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        HeSoTangCa.setEnabled(false);
        HeSoTangCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HeSoTangCaActionPerformed(evt);
            }
        });

        TongLuong.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        TongLuong.setEnabled(false);
        TongLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TongLuongActionPerformed(evt);
            }
        });

        jLabel5.setText("Mã NV");

        jLabel6.setText("Họ và tên");

        jLabel7.setText("Chức vụ ");

        jLabel8.setText("Giờ làm");

        jLabel9.setText("Giờ tăng ca");

        jLabel10.setText("Hệ số tăng ca");

        jLabel11.setText("Tổng Lương ");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(GioLam, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TangCa, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(HeSoTangCa)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TongLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(GioLam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TangCa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HeSoTangCa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TongLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout BodyLayout = new javax.swing.GroupLayout(Body);
        Body.setLayout(BodyLayout);
        BodyLayout.setHorizontalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Layout, javax.swing.GroupLayout.DEFAULT_SIZE, 1264, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        BodyLayout.setVerticalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Layout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(102, 204, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel9.setPreferredSize(new java.awt.Dimension(1200, 480));

        jTableNs.setBackground(new java.awt.Color(204, 255, 255));
        jTableNs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Họ và tên", "Chức vụ", "Giờ làm", "Giờ tăng ca", "Hệ số tăng ca", "Lương"
            }
        ));
        jTableNs.setPreferredSize(new java.awt.Dimension(330, 330));
        jScrollPane2.setViewportView(jTableNs);

        txtma_nhan_vien2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtma_nhan_vien2.setText("Sắp xếp theo :");

        comboBoxSapXep.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã nhân viên", "Tên nhân viên", "Chức vụ", "Số giờ tăng ca", "Hệ số tăng ca", "Giờ làm", "Tổng lương" }));
        comboBoxSapXep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxSapXepActionPerformed(evt);
            }
        });

        buttonGroup1.add(radioButtonTangDan);
        radioButtonTangDan.setText("Tăng dần");
        radioButtonTangDan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonTangDanActionPerformed(evt);
            }
        });

        buttonGroup1.add(radioButtonGiamDan);
        radioButtonGiamDan.setText("Giảm dần");
        radioButtonGiamDan.setName(""); // NOI18N
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
        btnThem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem13ActionPerformed(evt);
            }
        });

        txtma_nhan_vien7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtma_nhan_vien7.setText("Hiển thị");

        comboChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Công nhân may", "Nhân viên văn phòng", "Nhân viên thời vụ" }));
        comboChucVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboChucVuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(txtma_nhan_vien2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(comboBoxSapXep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62)
                        .addComponent(radioButtonTangDan, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(radioButtonGiamDan, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(173, 173, 173)
                        .addComponent(txtma_nhan_vien7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(btnThem13, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(100, Short.MAX_VALUE))
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
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtma_nhan_vien7)
                                .addComponent(comboChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnThem13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(radioButtonTangDan)
                                .addComponent(radioButtonGiamDan)))))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(178, 178, 178))
        );

        btnThem13.getAccessibleContext().setAccessibleDescription("");

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

        jButton4.setBackground(new java.awt.Color(102, 204, 255));
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

        jButton5.setBackground(new java.awt.Color(0, 204, 153));
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
                .addContainerGap(139, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(102, 204, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(301, 30));

        btnVanPhong.setBackground(new java.awt.Color(0, 153, 255));
        btnVanPhong.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnVanPhong.setText("Văn phòng");
        btnVanPhong.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnVanPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVanPhongActionPerformed(evt);
            }
        });

        btnCongNhan.setBackground(new java.awt.Color(51, 204, 255));
        btnCongNhan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCongNhan.setText("Công nhân");
        btnCongNhan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCongNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCongNhanActionPerformed(evt);
            }
        });

        btnThoiVu.setBackground(new java.awt.Color(0, 153, 255));
        btnThoiVu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThoiVu.setText("Thời vụ");
        btnThoiVu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnThoiVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoiVuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(146, Short.MAX_VALUE)
                .addComponent(btnThoiVu, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnVanPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(32, 32, 32)
                    .addComponent(btnCongNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(250, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThoiVu)
                    .addComponent(btnVanPhong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(9, Short.MAX_VALUE)
                    .addComponent(btnCongNhan)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout Tinh_LuongLayout = new javax.swing.GroupLayout(Tinh_Luong);
        Tinh_Luong.setLayout(Tinh_LuongLayout);
        Tinh_LuongLayout.setHorizontalGroup(
            Tinh_LuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Tinh_LuongLayout.createSequentialGroup()
                .addGroup(Tinh_LuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(Tinh_LuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Tinh_LuongLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Tinh_LuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(header3, javax.swing.GroupLayout.PREFERRED_SIZE, 1297, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Body, javax.swing.GroupLayout.PREFERRED_SIZE, 1280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 1280, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(Tinh_LuongLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        Tinh_LuongLayout.setVerticalGroup(
            Tinh_LuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tinh_LuongLayout.createSequentialGroup()
                .addGroup(Tinh_LuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(header3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Tinh_LuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(Tinh_LuongLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Tinh_LuongLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Body, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(Tinh_Luong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Tinh_Luong, javax.swing.GroupLayout.PREFERRED_SIZE, 849, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem11ActionPerformed
        // Xóa
        try {
            int dong = jTableNs.getSelectedRow();  // Lấy chỉ số dòng
            if (dong != -1) {
                // Lấy mã nhân viên từ cột "Mã nhân viên" (cột 0 chứa mã nhân viên)
                String maNV = (String) jTableNs.getValueAt(dong, 0);
                // Gọi phương thức xóa nhân viên từ DAO
                QLNV_DAO xoa = new QLNV_DAO();
                String form = "Tinh_Luong"; // Xác định form để xóa 
                boolean success = xoa.xoaNhanVien(maNV, form);

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

    }//GEN-LAST:event_btnThem11ActionPerformed
    public void lamMoi() {
        //Nhan vien May
        txtMaNhanVien1.setText("");
        txtLuongCoDinh.setText("");
        txtSanLuong.setText("");
        txtDonGia.setText("");
        txtPhuCap.setText("");

        //Nhan vien thời vụ
        txtMaNhanVien2.setText("");
        txtLuong1h.setText("");

        //Nhan vien văn phòng
        txtMaNhanVien3.setText("");
        txtLuongCoBan.setText("");
        txtThuong.setText("");
        txtPhat.setText("");

        //Thông tin
        MaNV.setText("");
        TenNV.setText("");
        ChucVu.setText("");
        GioLam.setText("");
        TangCa.setText("");
        HeSoTangCa.setText("");
        TongLuong.setText("");

        //tìm kiếm
        txtTimKiem.setText("");
    }
    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        lamMoi();
        loadDataToTable();

    }//GEN-LAST:event_btnResetActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // Nút tìm kiếm
        loadDataToTable();

    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Đăng Xuất
        TaiKhoan_Dao.dangXuat(this);
    }//GEN-LAST:event_jButton3ActionPerformed

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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Nhấp vào chuyển sang trang Quản Lí nhân sự
        QL_NS QL_NS = new QL_NS();
        QL_NS.show();
        this.dispose();//Tắt form
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Nhấp vào chuyển sang trang Bảng Chấm Công
        Cham_Cong Cham_Cong = new Cham_Cong();
        Cham_Cong.show();
        this.dispose();//Tắt form
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnVanPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVanPhongActionPerformed
        // Panel Lương Nhân Viên Văn Phòng 
        chuyenLayout("panelVanPhong", btnVanPhong);
    }//GEN-LAST:event_btnVanPhongActionPerformed

    private void btnCongNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCongNhanActionPerformed
        // Panel Lương Công Nhân May
        chuyenLayout("panelMay", btnCongNhan);

    }//GEN-LAST:event_btnCongNhanActionPerformed

    private void btnThoiVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoiVuActionPerformed
        // Panel Lương Nhân Viên PartTime
        chuyenLayout("panelPartTime", btnThoiVu);
    }//GEN-LAST:event_btnThoiVuActionPerformed

    private void btnThem_MayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem_MayActionPerformed
        themBangLuong();
    }//GEN-LAST:event_btnThem_MayActionPerformed

    private void txtMaNhanVien3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaNhanVien3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaNhanVien3ActionPerformed

    private void btnTraCuu_VPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraCuu_VPActionPerformed
        //Sự kiện nút Tra cứu của Nv Văn Phòng
        String maNv = txtMaNhanVien3.getText().trim();
        String luongCoBan = txtLuongCoBan.getText().trim();
        String thuong = txtThuong.getText().trim();
        String phat = txtPhat.getText().trim();
        String chuc_vu = "Nhân viên văn phòng";
        //Gọi hàm
        tinhLuong(maNv, chuc_vu, null, luongCoBan, null, null, null, thuong, phat);
    }//GEN-LAST:event_btnTraCuu_VPActionPerformed

    private void txtLuongCoDinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLuongCoDinhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLuongCoDinhActionPerformed

    private void btnThem_May1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem_May1ActionPerformed
        //  Thêm bảng lương công nhân

        themBangLuong();
    }//GEN-LAST:event_btnThem_May1ActionPerformed

    private void txtMaNhanVien1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaNhanVien1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaNhanVien1ActionPerformed

    private void btnThemMay1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemMay1ActionPerformed
        //Sự kiện nút Tra cứu của Nv May
        String maNv = txtMaNhanVien1.getText().trim();
        String luongCoDinh = txtLuongCoDinh.getText().trim();
        String donGia = txtDonGia.getText().trim();
        String sanLuong = txtSanLuong.getText().trim();
        String phuCap = txtPhuCap.getText().trim();
        String chuc_vu = "Công nhân may";

        //Gọi hàm
        tinhLuong(maNv, chuc_vu, null, luongCoDinh, donGia, sanLuong, phuCap, null, null);
    }//GEN-LAST:event_btnThemMay1ActionPerformed

    private void txtDonGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDonGiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDonGiaActionPerformed

    private void txtSanLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSanLuongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSanLuongActionPerformed

    private void txtPhuCapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhuCapActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhuCapActionPerformed

    private void MaNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MaNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MaNVActionPerformed

    private void TenNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TenNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TenNVActionPerformed

    private void ChucVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChucVuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChucVuActionPerformed

    private void GioLamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GioLamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GioLamActionPerformed

    private void TangCaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TangCaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TangCaActionPerformed

    private void HeSoTangCaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HeSoTangCaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_HeSoTangCaActionPerformed

    private void TongLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TongLuongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TongLuongActionPerformed

    private void btnThem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem18ActionPerformed
        //Sự kiện nút Tra cứu của Nv parttime
        String maNv = txtMaNhanVien2.getText().trim();
        String luongMotGio = txtLuong1h.getText().trim();
        String chuc_vu = "Nhân viên thời vụ";

        //Gọi hàm
        tinhLuong(maNv, chuc_vu, luongMotGio, null, null, null, null, null, null);

    }//GEN-LAST:event_btnThem18ActionPerformed

    private void txtLuongCoBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLuongCoBanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLuongCoBanActionPerformed

    private void txtThuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtThuongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtThuongActionPerformed

    private void txtPhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhatActionPerformed

    private void btnThem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem17ActionPerformed
        // Sự kiện nút thêm thời vụ
        themBangLuong();
    }//GEN-LAST:event_btnThem17ActionPerformed

    private void comboChucVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboChucVuActionPerformed
        // Lựa chọn hiển thị
        loadDataToTable();
    }//GEN-LAST:event_comboChucVuActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Tinh_Luong().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Body;
    private javax.swing.JTextField ChucVu;
    private javax.swing.JTextField GioLam;
    private javax.swing.JTextField HeSoTangCa;
    private javax.swing.JPanel Layout;
    private javax.swing.JTextField MaNV;
    private javax.swing.JTextField TangCa;
    private javax.swing.JTextField TenNV;
    private javax.swing.JPanel Tinh_Luong;
    private javax.swing.JTextField TongLuong;
    private javax.swing.JButton btnCongNhan;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnThem11;
    private javax.swing.JButton btnThem13;
    private javax.swing.JButton btnThem17;
    private javax.swing.JButton btnThem18;
    private javax.swing.JButton btnThemMay1;
    private javax.swing.JButton btnThem_May;
    private javax.swing.JButton btnThem_May1;
    private javax.swing.JButton btnThoiVu;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnTraCuu_VP;
    private javax.swing.JButton btnVanPhong;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> comboBoxSapXep;
    private javax.swing.JComboBox<String> comboChucVu;
    private javax.swing.JPanel header3;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableNs;
    private javax.swing.JPanel panelMay;
    private javax.swing.JPanel panelPartTime;
    private javax.swing.JPanel panelVanPhong;
    private javax.swing.JRadioButton radioButtonGiamDan;
    private javax.swing.JRadioButton radioButtonTangDan;
    private javax.swing.JTextField txtDonGia;
    private javax.swing.JTextField txtLuong1h;
    private javax.swing.JTextField txtLuongCoBan;
    private javax.swing.JTextField txtLuongCoDinh;
    private javax.swing.JTextField txtMaNhanVien1;
    private javax.swing.JTextField txtMaNhanVien2;
    private javax.swing.JTextField txtMaNhanVien3;
    private javax.swing.JTextField txtPhat;
    private javax.swing.JTextField txtPhuCap;
    private javax.swing.JTextField txtSanLuong;
    private javax.swing.JTextField txtThuong;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JLabel txtma_nhan_vien1;
    private javax.swing.JLabel txtma_nhan_vien10;
    private javax.swing.JLabel txtma_nhan_vien11;
    private javax.swing.JLabel txtma_nhan_vien12;
    private javax.swing.JLabel txtma_nhan_vien13;
    private javax.swing.JLabel txtma_nhan_vien2;
    private javax.swing.JLabel txtma_nhan_vien3;
    private javax.swing.JLabel txtma_nhan_vien4;
    private javax.swing.JLabel txtma_nhan_vien5;
    private javax.swing.JLabel txtma_nhan_vien6;
    private javax.swing.JLabel txtma_nhan_vien7;
    private javax.swing.JLabel txtma_nhan_vien8;
    private javax.swing.JLabel txtma_nhan_vien9;
    // End of variables declaration//GEN-END:variables
}
