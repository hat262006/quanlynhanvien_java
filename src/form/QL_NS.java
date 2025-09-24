package form;

import form.Tinh_Luong;
import form.Cham_Cong;
import dao.TaiKhoan_Dao;
import dao.QLNV_DAO;
import model.nv_VanPhong;
import model.nhan_vien;
import java.awt.Image;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import model.nv_May;
import model.nv_ThoiVu;


/**
 *
 * @author
 */
public class QL_NS extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(QL_NS.class.getName());

    public QL_NS() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH); //Toàn màn hình 
        loadDataToTable();
        themSuKienChonDong();

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
        String maNV = (String) jTableNs.getValueAt(chiSoDong, 0); // Mã nhân viên
        String tenNV = (String) jTableNs.getValueAt(chiSoDong, 1); // Họ tên
        String cccd = (String) jTableNs.getValueAt(chiSoDong, 5); // CCCD
        String trinhDo = (String) jTableNs.getValueAt(chiSoDong, 7); // Trình Độ
        String soDienThoai = (String) jTableNs.getValueAt(chiSoDong, 8); // Số điện thoại
        String trangThai = (String) jTableNs.getValueAt(chiSoDong, 9); // Trạng Thái
        String anh = (String) jTableNs.getValueAt(chiSoDong, 10); //anh
        // Cập nhật vào các ô text field tương ứng
        txtMaNhanVien.setText(maNV);
        txtHoTen.setText(tenNV);
        txtCCCD.setText(cccd);
        txtTrinhDo.setText(trinhDo);
        txtSoDienThoai.setText(soDienThoai);
        txtTrangThai.setText(trangThai);
        setAnhNhanVien(labelAnh, anh);
        duongDanAnh = anh;
    }

    //Hàm sắp xếp và đổ thông tin ra bảng 
    private void loadDataToTable() {
        // Lấy giá trị từ ComboBox và RadioButton để xác định cách sắp xếp
        String sapXepTheo = comboBoxSapXep.getSelectedItem().toString(); // ComboBox chọn tiêu chí sắp xếp
        String thuTu = radioButtonTangDan.isSelected() ? "Tăng dần"
                : (radioButtonGiamDan.isSelected() ? "Giảm dần" : "Tăng dần");
        //Mặc định là tăng dần
        String keyWord = txtTimKiem.getText().trim();
        // Khởi tạo tên các cột trong bảng
        String[] columnNames = {
            "Mã NV", "Họ tên", "Giới tính", "Chức vụ", "Ngày sinh",
            "CCCD", "Ngày vào làm", "Trình độ", "SĐT", "Trạng thái", "Ảnh"
        };

        // Tạo DefaultTableModel để cập nhật bảng
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Tạo DAO và lấy danh sách nhân viên đã sắp xếp
        QLNV_DAO dao = new QLNV_DAO();
        List<nhan_vien> list = dao.getAll(keyWord,sapXepTheo, thuTu); 
        // Gọi phương thức getAll với tiêu chí và thứ tự sắp xếp

        // Duyệt qua danh sách nhân viên và thêm từng dòng vào bảng
        for (nhan_vien nv : list) {
            Object[] row = {
                nv.getMaNV(),
                nv.getTenNV(),
                nv.getGioiTinh(),
                nv.getChucVu(),
                nv.getNgaySinh(),
                nv.getCccd(),
                nv.getNgayVaoLam(),
                nv.getTrinhDo(),
                nv.getSoDienThoai(),
                nv.getTrangThai(),
                nv.getAnh()
            };
            model.addRow(row);
        }
        // Cập nhật bảng với dữ liệu đã sắp xếp
        jTableNs.setModel(model); // 
        // ẩn cột ảnh
        anCotAnh();
    }

    //    Hàm lấy ngày - tháng -năm
    public String getDateCombo(JComboBox<String> cbNgay, JComboBox<String> cbThang, JComboBox<String> cbNam) {
        String ngay = (String) cbNgay.getSelectedItem();
        String thang = (String) cbThang.getSelectedItem();
        String nam = (String) cbNam.getSelectedItem();

        if (ngay.length() == 1) {
            ngay = "0" + ngay;
        }
        if (thang.length() == 1) {
            thang = "0" + thang;
        }

        return ngay + "/" + thang + "/" + nam; // hoặc: return nam + "-" + thang + "-" + ngay;
    }

    // Hàm lấy dữ liệu - Kiểm tra - Tạo object
    private nhan_vien validateNhanVien() {
        // LẤY DỮ LIỆU TỪ FORM 
        String maNv = txtMaNhanVien.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String gioiTinh = cbGioiTinh.getSelectedItem().toString();
        String chucVu = cbChucVu.getSelectedItem().toString();
        String ngaySinh = getDateCombo(cbNgay1, cbThang1, cbNam1);
        String ngayVaoLam = getDateCombo(cbNgay2, cbThang2, cbNam2);
        String cccd = txtCCCD.getText().trim();
        String trinhDo = txtTrinhDo.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String trangThai = cbTrangThai.getSelectedItem().toString();

        // Kiểm tra rỗng cơ bản
        if (maNv.isEmpty() || hoTen.isEmpty() || cccd.isEmpty()
                || trinhDo.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return null;
        }
        // Kiểm tra họ tên
        if (!hoTen.matches("^[\\p{L}\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Họ tên chỉ được chứa chữ cái, không được có số hoặc ký tự đặc biệt!");
            return null;
        }
        // Đường dẫn ảnh
        if (duongDanAnh == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ảnh !");
            return null;
        }

        // Kiểm tra định dạng số điện thoại
        if (!sdt.matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ (phải 10-11 chữ số)!");
            return null;
        }

        // Kiểm tra CCCD (12 số)
        if (!cccd.matches("\\d{12}")) {
            JOptionPane.showMessageDialog(this, "CCCD phải gồm đúng 12 chữ số!");
            return null;
        }

        // 
        nhan_vien nv = null;

        switch (chucVu) {
            case "Công nhân may":
                nv = new nv_May(
                        0, 0, 0, 0, maNv, hoTen, gioiTinh, chucVu, ngaySinh,
                        cccd, ngayVaoLam, trinhDo, sdt, trangThai, duongDanAnh
                );
                break;

            case "Nhân viên thời vụ":
                nv = new nv_ThoiVu(
                        0, maNv, hoTen, gioiTinh, chucVu, ngaySinh,
                        cccd, ngayVaoLam, trinhDo, sdt, trangThai, duongDanAnh);
                break;

            case "Nhân viên văn phòng":
                nv = new nv_VanPhong(
                        0, 0, 0, maNv, hoTen, gioiTinh, chucVu, ngaySinh,
                        cccd, ngayVaoLam, trinhDo, sdt, trangThai, duongDanAnh);
                break;

            default:
                nv = new nv_May(
                        0, 0, 0, 0, maNv, hoTen, gioiTinh, chucVu, ngaySinh,
                        cccd, ngayVaoLam, trinhDo, sdt, trangThai, duongDanAnh
                );
        }
        return nv;
    }

    // Lấy ảnh chân dung 
    public void setAnhNhanVien(JLabel label, String duongDanAnh) {
        if (duongDanAnh == null || duongDanAnh.isEmpty()) {
            label.setIcon(null);
            return;
        }

        try {
            // Tạo ImageIcon từ đường dẫn
            ImageIcon icon = new ImageIcon(duongDanAnh);

            // Scale ảnh cho vừa JLabel
            Image img = icon.getImage().getScaledInstance(
                    label.getWidth(), label.getHeight(),
                    Image.SCALE_SMOOTH);

            // Set ảnh vào JLabel
            label.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            e.printStackTrace();
            label.setIcon(null);
        }
    }

    //ẩn cột  ảnh  
    private void anCotAnh() {
        jTableNs.getColumnModel().getColumn(10).setMinWidth(0);
        jTableNs.getColumnModel().getColumn(10).setMaxWidth(0);
        jTableNs.getColumnModel().getColumn(10).setWidth(0);
    }
//    hàm làm mới 

    public void lamMoi() {
        txtMaNhanVien.setText("");
        txtHoTen.setText("");
        cbGioiTinh.getSelectedItem();
        cbGioiTinh.setSelectedIndex(0);
        cbChucVu.setSelectedIndex(0);
        cbChucVu.getSelectedItem();
        txtCCCD.setText("");
        txtTrinhDo.setText("");
        txtSoDienThoai.setText("");
        txtTimKiem.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        QL_NhanSu = new javax.swing.JPanel();
        header2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        btnThem10 = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        btnThem12 = new javax.swing.JButton();
        btnThem14 = new javax.swing.JButton();
        txtTimKiem = new javax.swing.JTextField();
        btnReset = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        Body = new javax.swing.JPanel();
        txtHoTen = new javax.swing.JTextField();
        txtMaNhanVien = new javax.swing.JTextField();
        txtque_quan1 = new javax.swing.JLabel();
        txtma_nhan_vien1 = new javax.swing.JLabel();
        txtngay_sinh3 = new javax.swing.JLabel();
        cbThang1 = new javax.swing.JComboBox<>();
        txtso_dien_thoai2 = new javax.swing.JLabel();
        txtTrinhDo = new javax.swing.JTextField();
        cbGioiTinh = new javax.swing.JComboBox<>();
        txtho_ten1 = new javax.swing.JLabel();
        cbNgay1 = new javax.swing.JComboBox<>();
        cbNam1 = new javax.swing.JComboBox<>();
        txtngay_sinh4 = new javax.swing.JLabel();
        txtghi_chu1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtSoDienThoai = new javax.swing.JTextField();
        txtngay_sinh5 = new javax.swing.JLabel();
        txtgioi_tinh1 = new javax.swing.JLabel();
        cbTrangThai = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        txtCCCD = new javax.swing.JTextField();
        txtso_dien_thoai3 = new javax.swing.JLabel();
        cbNgay2 = new javax.swing.JComboBox<>();
        cbNam2 = new javax.swing.JComboBox<>();
        cbThang2 = new javax.swing.JComboBox<>();
        cbChucVu = new javax.swing.JComboBox<>();
        btnThem15 = new javax.swing.JButton();
        labelAnh = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableNs = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtma_nhan_vien2 = new javax.swing.JLabel();
        comboBoxSapXep = new javax.swing.JComboBox<>();
        radioButtonTangDan = new javax.swing.JRadioButton();
        radioButtonGiamDan = new javax.swing.JRadioButton();
        txtma_nhan_vien3 = new javax.swing.JLabel();
        txtTrangThai = new javax.swing.JTextField();
        btnThem13 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        QL_NhanSu.setBackground(new java.awt.Color(102, 204, 255));
        QL_NhanSu.setPreferredSize(new java.awt.Dimension(1550, 1000));

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
        btnThem14.setPreferredSize(new java.awt.Dimension(80, 25));
        btnThem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem14ActionPerformed(evt);
            }
        });

        txtTimKiem.setPreferredSize(new java.awt.Dimension(200, 25));
        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(btnThem12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addComponent(btnThem10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
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
                    .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("QUẢN LÝ NHÂN VIÊN");

        javax.swing.GroupLayout header2Layout = new javax.swing.GroupLayout(header2);
        header2.setLayout(header2Layout);
        header2Layout.setHorizontalGroup(
            header2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(header2Layout.createSequentialGroup()
                .addGap(517, 517, 517)
                .addComponent(jLabel4)
                .addContainerGap(533, Short.MAX_VALUE))
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
        jLabel3.setText("QUẢN LÝ NHÂN SỰ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jLabel3)
                .addContainerGap(57, Short.MAX_VALUE))
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

        txtHoTen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtHoTen.setPreferredSize(new java.awt.Dimension(64, 25));
        txtHoTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHoTenActionPerformed(evt);
            }
        });

        txtMaNhanVien.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtMaNhanVien.setPreferredSize(new java.awt.Dimension(64, 25));
        txtMaNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaNhanVienActionPerformed(evt);
            }
        });

        txtque_quan1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtque_quan1.setText("Chức vụ");

        txtma_nhan_vien1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtma_nhan_vien1.setText("Mã nhân viên");

        txtngay_sinh3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtngay_sinh3.setText("Trình độ");

        cbThang1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cbThang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbThang1ActionPerformed(evt);
            }
        });

        txtso_dien_thoai2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtso_dien_thoai2.setText("CCCD");

        txtTrinhDo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTrinhDo.setPreferredSize(new java.awt.Dimension(64, 25));
        txtTrinhDo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTrinhDoActionPerformed(evt);
            }
        });

        cbGioiTinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ", " " }));

        txtho_ten1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtho_ten1.setText("Họ và tên");

        cbNgay1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        cbNgay1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNgay1ActionPerformed(evt);
            }
        });

        cbNam1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000", "1999", "1998", "1997", "1996", "1995", "1994", "1993", "1992", "1991", "1990", "1989", "1988", "1987", "1986", "1985", "1984", "1983", "1982", "1981", "1980", "1979", "1978", "1977", "1976", "1975", "1974", "1973", "1972", "1971", "1970" }));
        cbNam1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNam1ActionPerformed(evt);
            }
        });

        txtngay_sinh4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtngay_sinh4.setText("Ngày sinh");

        txtghi_chu1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtghi_chu1.setText("Trạng thái");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        txtSoDienThoai.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtSoDienThoai.setPreferredSize(new java.awt.Dimension(64, 25));

        txtngay_sinh5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtngay_sinh5.setText("Số điện thoại");

        txtgioi_tinh1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtgioi_tinh1.setText("Giới tính");

        cbTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang làm việc ", "Nghỉ phép", "Thôi việc" }));
        cbTrangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTrangThaiActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        txtCCCD.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtCCCD.setPreferredSize(new java.awt.Dimension(64, 25));
        txtCCCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCCCDActionPerformed(evt);
            }
        });

        txtso_dien_thoai3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtso_dien_thoai3.setText("Ngày vào làm");

        cbNgay2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        cbNgay2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNgay2ActionPerformed(evt);
            }
        });

        cbNam2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2025", "2024", "2023", "2022", "2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007" }));
        cbNam2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNam2ActionPerformed(evt);
            }
        });

        cbThang2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cbThang2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbThang2ActionPerformed(evt);
            }
        });

        cbChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Công nhân may", "Nhân viên văn phòng", "Nhân viên thời vụ" }));

        btnThem15.setBackground(new java.awt.Color(204, 204, 255));
        btnThem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/10024501.png"))); // NOI18N
        btnThem15.setText("Thay ảnh");
        btnThem15.setFocusPainted(false);
        btnThem15.setPreferredSize(new java.awt.Dimension(70, 25));
        btnThem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem15ActionPerformed(evt);
            }
        });

        labelAnh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/avt.jpg"))); // NOI18N

        javax.swing.GroupLayout BodyLayout = new javax.swing.GroupLayout(Body);
        Body.setLayout(BodyLayout);
        BodyLayout.setHorizontalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BodyLayout.createSequentialGroup()
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BodyLayout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7))
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtho_ten1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtngay_sinh4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtgioi_tinh1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtngay_sinh3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtma_nhan_vien1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTrinhDo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addComponent(cbNgay1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(cbThang1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbNam1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtHoTen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMaNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BodyLayout.createSequentialGroup()
                                .addComponent(btnThem15, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(170, 170, 170))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BodyLayout.createSequentialGroup()
                                .addComponent(labelAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(158, 158, 158)))
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtghi_chu1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtque_quan1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtso_dien_thoai2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtngay_sinh5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtso_dien_thoai3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addComponent(cbNgay2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbThang2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbNam2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(83, 83, 83))
        );
        BodyLayout.setVerticalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BodyLayout.createSequentialGroup()
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtque_quan1)
                            .addComponent(cbChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtso_dien_thoai2))
                        .addGap(24, 24, 24)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbNgay2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtso_dien_thoai3)
                            .addComponent(cbThang2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbNam2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtngay_sinh5))
                        .addGap(25, 25, 25)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtghi_chu1)
                            .addComponent(cbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addComponent(labelAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cbNgay1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbNam1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbThang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtngay_sinh4)
                                    .addComponent(btnThem15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtma_nhan_vien1)
                                    .addComponent(txtMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtho_ten1)
                                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtgioi_tinh1)
                                    .addComponent(cbGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTrinhDo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtngay_sinh3))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
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
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã NV", "Họ và tên", "Giới tính", "Chức vụ", "Ngày sinh", "CCCD", "Ngày vào làm", "Trình độ", "SĐT", "Trạng thái"
            }
        ));
        jTableNs.setPreferredSize(new java.awt.Dimension(330, 330));
        jScrollPane2.setViewportView(jTableNs);

        txtma_nhan_vien2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtma_nhan_vien2.setText("Sắp xếp theo :");

        comboBoxSapXep.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã Nhân Viên", "Tên", "Ngày sinh", "Chức vụ", "Ngày vào làm", "Trạng thái" }));
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
        radioButtonGiamDan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonGiamDanActionPerformed(evt);
            }
        });

        txtma_nhan_vien3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtma_nhan_vien3.setText("Trạng thái");

        txtTrangThai.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtTrangThai.setEnabled(false);
        txtTrangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTrangThaiActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1181, Short.MAX_VALUE))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(txtma_nhan_vien2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(comboBoxSapXep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(radioButtonTangDan, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(radioButtonGiamDan, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(txtma_nhan_vien3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnThem13, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98))
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
                            .addComponent(txtma_nhan_vien3)
                            .addComponent(txtTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnThem13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(190, 190, 190))
        );

        jPanel2.setBackground(new java.awt.Color(102, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton2.setBackground(new java.awt.Color(0, 204, 153));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/ns.png"))); // NOI18N
        jButton2.setText("Quản Lý Nhân Sự");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.setFocusPainted(false);
        jButton2.setPreferredSize(new java.awt.Dimension(75, 80));

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
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout QL_NhanSuLayout = new javax.swing.GroupLayout(QL_NhanSu);
        QL_NhanSu.setLayout(QL_NhanSuLayout);
        QL_NhanSuLayout.setHorizontalGroup(
            QL_NhanSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, QL_NhanSuLayout.createSequentialGroup()
                .addGroup(QL_NhanSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(QL_NhanSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(header2, javax.swing.GroupLayout.PREFERRED_SIZE, 1297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Body, javax.swing.GroupLayout.PREFERRED_SIZE, 1280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 1280, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        QL_NhanSuLayout.setVerticalGroup(
            QL_NhanSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QL_NhanSuLayout.createSequentialGroup()
                .addGroup(QL_NhanSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(header2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(QL_NhanSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(QL_NhanSuLayout.createSequentialGroup()
                        .addComponent(Body, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(QL_NhanSuLayout.createSequentialGroup()
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
                .addComponent(QL_NhanSu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(QL_NhanSu, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTrangThaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTrangThaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTrangThaiActionPerformed

    private void radioButtonGiamDanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonGiamDanActionPerformed
        // Giảm dần
        loadDataToTable();
    }//GEN-LAST:event_radioButtonGiamDanActionPerformed

    private void comboBoxSapXepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxSapXepActionPerformed
        // Sắp xếp 
        loadDataToTable();

    }//GEN-LAST:event_comboBoxSapXepActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Đăng Xuất
        TaiKhoan_Dao.dangXuat(this);
    }//GEN-LAST:event_jButton3ActionPerformed
    private String duongDanAnh;
    private void btnThem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem15ActionPerformed
        // Thay Ảnh
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv"));
        fileChooser.setDialogTitle("Chọn ảnh nhân viên");
        // Chỉ cho phép chọn file ảnh
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Hình ảnh", "jpg", "png", "jpeg", "gif"
        ));

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            // Lấy đường dẫn file ảnh
            duongDanAnh = fileChooser.getSelectedFile().getAbsolutePath();

            // Hiện ảnh ra label
            setAnhNhanVien(labelAnh, duongDanAnh);
        }
    }//GEN-LAST:event_btnThem15ActionPerformed

    private void btnThem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem14ActionPerformed
        // Tìm kiếm
        loadDataToTable();
    }//GEN-LAST:event_btnThem14ActionPerformed

    private void btnThem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem12ActionPerformed
        // THÊM
        QLNV_DAO ns = new QLNV_DAO();
        nhan_vien nv = validateNhanVien();
        if (nv == null) {
            return;
        }
        String loi = ns.kiemTraTrung(nv.getMaNV(), nv.getSoDienThoai(), nv.getCccd(), nv.getAnh());
        if (!loi.isEmpty()) {
            JOptionPane.showMessageDialog(this, loi);
            return;
        }
        if (ns.them(nv)) {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
            loadDataToTable();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!");
        }
    }//GEN-LAST:event_btnThem12ActionPerformed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        // LƯU
        // Lấy dữ liệu từ các JTextField
        nhan_vien nv = validateNhanVien();

        if (nv != null) {
            // Gọi DAO để cập nhật thông tin nhân viên
            QLNV_DAO dao = new QLNV_DAO();
            boolean success = dao.capNhatNhanVien(nv);

            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                // Cập nhật lại JTable nếu cần
                loadDataToTable();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        }
    }//GEN-LAST:event_btnLuuActionPerformed

    private void btnThem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem10ActionPerformed
        // Xóa
        try {
            int dong = jTableNs.getSelectedRow();  // Lấy chỉ số dòng 
            if (dong != -1) {
                // Lấy mã nhân viên từ cột "Mã nhân viên" (cột 0 chứa mã nhân viên)
                String maNV = (String) jTableNs.getValueAt(dong, 0);

                // Gọi phương thức xóa nhân viên từ DAO
                QLNV_DAO xoa = new QLNV_DAO();
                String form = "QL_NS"; // Xác định form để xóa 
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
                        "Không thể xóa nhân viên này vì vẫn còn dữ liệu chấm công!",
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

    private void radioButtonTangDanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonTangDanActionPerformed
        // Tăng Dần 
        loadDataToTable();
    }//GEN-LAST:event_radioButtonTangDanActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Nhấp vào chuyển sang trang Chấm Công
        Cham_Cong Cham_Cong = new Cham_Cong();
        Cham_Cong.show();
        this.dispose();//Tắt form 
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
    //làm mới 
        lamMoi();
        loadDataToTable();

    }//GEN-LAST:event_btnResetActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Nhấp vào chuyển sang trang Hệ Thống Lương
        Tinh_Luong Tinh_Luong = new Tinh_Luong();
        Tinh_Luong.show();
        this.dispose();//Tắt form
    }//GEN-LAST:event_jButton5ActionPerformed

    private void cbThang2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbThang2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbThang2ActionPerformed

    private void cbNam2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNam2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbNam2ActionPerformed

    private void cbNgay2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNgay2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbNgay2ActionPerformed

    private void cbTrangThaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTrangThaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTrangThaiActionPerformed

    private void cbNam1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNam1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbNam1ActionPerformed

    private void cbNgay1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNgay1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbNgay1ActionPerformed

    private void txtTrinhDoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTrinhDoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTrinhDoActionPerformed

    private void cbThang1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbThang1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbThang1ActionPerformed

    private void txtMaNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaNhanVienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaNhanVienActionPerformed

    private void txtHoTenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHoTenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoTenActionPerformed

    private void txtCCCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCCCDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCCCDActionPerformed

    private void btnThem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem13ActionPerformed
        // Xuất excel
    }//GEN-LAST:event_btnThem13ActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new QL_NS().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Body;
    private javax.swing.JPanel QL_NhanSu;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnThem10;
    private javax.swing.JButton btnThem12;
    private javax.swing.JButton btnThem13;
    private javax.swing.JButton btnThem14;
    private javax.swing.JButton btnThem15;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbChucVu;
    private javax.swing.JComboBox<String> cbGioiTinh;
    private javax.swing.JComboBox<String> cbNam1;
    private javax.swing.JComboBox<String> cbNam2;
    private javax.swing.JComboBox<String> cbNgay1;
    private javax.swing.JComboBox<String> cbNgay2;
    private javax.swing.JComboBox<String> cbThang1;
    private javax.swing.JComboBox<String> cbThang2;
    private javax.swing.JComboBox<String> cbTrangThai;
    private javax.swing.JComboBox<String> comboBoxSapXep;
    private javax.swing.JPanel header2;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableNs;
    private javax.swing.JLabel labelAnh;
    private javax.swing.JRadioButton radioButtonGiamDan;
    private javax.swing.JRadioButton radioButtonTangDan;
    private javax.swing.JTextField txtCCCD;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaNhanVien;
    private javax.swing.JTextField txtSoDienThoai;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTrangThai;
    private javax.swing.JTextField txtTrinhDo;
    private javax.swing.JLabel txtghi_chu1;
    private javax.swing.JLabel txtgioi_tinh1;
    private javax.swing.JLabel txtho_ten1;
    private javax.swing.JLabel txtma_nhan_vien1;
    private javax.swing.JLabel txtma_nhan_vien2;
    private javax.swing.JLabel txtma_nhan_vien3;
    private javax.swing.JLabel txtngay_sinh3;
    private javax.swing.JLabel txtngay_sinh4;
    private javax.swing.JLabel txtngay_sinh5;
    private javax.swing.JLabel txtque_quan1;
    private javax.swing.JLabel txtso_dien_thoai2;
    private javax.swing.JLabel txtso_dien_thoai3;
    // End of variables declaration//GEN-END:variables
}
