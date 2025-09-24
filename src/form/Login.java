package form;

import dao.TaiKhoan_Dao;
import javax.swing.JOptionPane;

/**
 *
 * @author x
 */
public class Login extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Login.class.getName());

    public Login() {
        initComponents();
        setLocationRelativeTo(null); // Căn giữa form 
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Phai19 = new javax.swing.JPanel();
        jLabel98 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        txtUserLog = new javax.swing.JTextField();
        jLabel100 = new javax.swing.JLabel();
        txtPassLog = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jLabel101 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        Trai20 = new javax.swing.JPanel();
        jLabel102 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Phai19.setBackground(new java.awt.Color(255, 255, 255));
        Phai19.setMinimumSize(new java.awt.Dimension(400, 500));

        jLabel98.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel98.setForeground(new java.awt.Color(0, 102, 102));
        jLabel98.setText("ĐĂNG NHẬP");

        jLabel99.setBackground(new java.awt.Color(102, 102, 102));
        jLabel99.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel99.setText("Tài khoản ");

        txtUserLog.setForeground(new java.awt.Color(102, 102, 102));

        jLabel100.setBackground(new java.awt.Color(102, 102, 102));
        jLabel100.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel100.setText("Mật khẩu");

        txtPassLog.setPreferredSize(new java.awt.Dimension(90, 40));
        txtPassLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPassLogActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Đăng nhập ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel101.setText("Bạn chưa có tài khoản ?");

        jButton2.setBackground(new java.awt.Color(0, 102, 102));
        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Đăng ký");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Phai19Layout = new javax.swing.GroupLayout(Phai19);
        Phai19.setLayout(Phai19Layout);
        Phai19Layout.setHorizontalGroup(
            Phai19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Phai19Layout.createSequentialGroup()
                .addGroup(Phai19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Phai19Layout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(jLabel98))
                    .addGroup(Phai19Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(Phai19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addGroup(Phai19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel99)
                                .addComponent(txtUserLog)
                                .addComponent(jLabel100)
                                .addComponent(txtPassLog, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))
                            .addGroup(Phai19Layout.createSequentialGroup()
                                .addComponent(jLabel101)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2)))))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        Phai19Layout.setVerticalGroup(
            Phai19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Phai19Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel98)
                .addGap(28, 28, 28)
                .addComponent(jLabel99)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtUserLog, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel100)
                .addGap(18, 18, 18)
                .addComponent(txtPassLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(Phai19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel101)
                    .addComponent(jButton2))
                .addContainerGap(99, Short.MAX_VALUE))
        );

        Trai20.setBackground(new java.awt.Color(0, 102, 102));
        Trai20.setPreferredSize(new java.awt.Dimension(400, 500));

        jLabel102.setIcon(new javax.swing.ImageIcon("C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\icon\\logo.png")); // NOI18N

        jLabel103.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel103.setForeground(new java.awt.Color(255, 255, 255));
        jLabel103.setText("HÃY CỨ LÀM THẬT ");

        javax.swing.GroupLayout Trai20Layout = new javax.swing.GroupLayout(Trai20);
        Trai20.setLayout(Trai20Layout);
        Trai20Layout.setHorizontalGroup(
            Trai20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Trai20Layout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addGroup(Trai20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel103)
                    .addComponent(jLabel102, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(113, Short.MAX_VALUE))
        );
        Trai20Layout.setVerticalGroup(
            Trai20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Trai20Layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addComponent(jLabel102)
                .addGap(41, 41, 41)
                .addComponent(jLabel103)
                .addContainerGap(231, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(Trai20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Phai19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Phai19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Trai20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPassLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPassLogActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPassLogActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Đăng nhập
        String user = txtUserLog.getText();
        String pass = new String(txtPassLog.getPassword());
        TaiKhoan_Dao dao = new TaiKhoan_Dao();
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu");
            return;
        }
        if (dao.checkLogin(user, pass)) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
            QL_NS QL_NS = new QL_NS();
            QL_NS.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu");
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Nhấp vào chuyển sang trang đăng ký
        signup signup = new signup();
        signup.show();
        this.dispose();//Tắt form
    }//GEN-LAST:event_jButton2ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Login().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Phai;
    private javax.swing.JPanel Phai1;
    private javax.swing.JPanel Phai10;
    private javax.swing.JPanel Phai11;
    private javax.swing.JPanel Phai12;
    private javax.swing.JPanel Phai13;
    private javax.swing.JPanel Phai14;
    private javax.swing.JPanel Phai15;
    private javax.swing.JPanel Phai16;
    private javax.swing.JPanel Phai17;
    private javax.swing.JPanel Phai18;
    private javax.swing.JPanel Phai19;
    private javax.swing.JPanel Phai2;
    private javax.swing.JPanel Phai3;
    private javax.swing.JPanel Phai4;
    private javax.swing.JPanel Phai5;
    private javax.swing.JPanel Phai6;
    private javax.swing.JPanel Phai7;
    private javax.swing.JPanel Phai8;
    private javax.swing.JPanel Phai9;
    private javax.swing.JPanel Trai;
    private javax.swing.JPanel Trai1;
    private javax.swing.JPanel Trai10;
    private javax.swing.JPanel Trai11;
    private javax.swing.JPanel Trai12;
    private javax.swing.JPanel Trai13;
    private javax.swing.JPanel Trai14;
    private javax.swing.JPanel Trai15;
    private javax.swing.JPanel Trai16;
    private javax.swing.JPanel Trai17;
    private javax.swing.JPanel Trai18;
    private javax.swing.JPanel Trai19;
    private javax.swing.JPanel Trai2;
    private javax.swing.JPanel Trai20;
    private javax.swing.JPanel Trai3;
    private javax.swing.JPanel Trai4;
    private javax.swing.JPanel Trai5;
    private javax.swing.JPanel Trai6;
    private javax.swing.JPanel Trai7;
    private javax.swing.JPanel Trai8;
    private javax.swing.JPanel Trai9;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JPasswordField txtPass1;
    private javax.swing.JPasswordField txtPass10;
    private javax.swing.JPasswordField txtPass11;
    private javax.swing.JPasswordField txtPass12;
    private javax.swing.JPasswordField txtPass13;
    private javax.swing.JPasswordField txtPass14;
    private javax.swing.JPasswordField txtPass15;
    private javax.swing.JPasswordField txtPass16;
    private javax.swing.JPasswordField txtPass17;
    private javax.swing.JPasswordField txtPass18;
    private javax.swing.JPasswordField txtPass2;
    private javax.swing.JPasswordField txtPass3;
    private javax.swing.JPasswordField txtPass4;
    private javax.swing.JPasswordField txtPass5;
    private javax.swing.JPasswordField txtPass6;
    private javax.swing.JPasswordField txtPass7;
    private javax.swing.JPasswordField txtPass8;
    private javax.swing.JPasswordField txtPass9;
    private javax.swing.JPasswordField txtPassLog;
    private javax.swing.JTextField txtUser;
    private javax.swing.JTextField txtUser1;
    private javax.swing.JTextField txtUser10;
    private javax.swing.JTextField txtUser11;
    private javax.swing.JTextField txtUser12;
    private javax.swing.JTextField txtUser13;
    private javax.swing.JTextField txtUser14;
    private javax.swing.JTextField txtUser15;
    private javax.swing.JTextField txtUser16;
    private javax.swing.JTextField txtUser17;
    private javax.swing.JTextField txtUser18;
    private javax.swing.JTextField txtUser2;
    private javax.swing.JTextField txtUser3;
    private javax.swing.JTextField txtUser4;
    private javax.swing.JTextField txtUser5;
    private javax.swing.JTextField txtUser6;
    private javax.swing.JTextField txtUser7;
    private javax.swing.JTextField txtUser8;
    private javax.swing.JTextField txtUser9;
    private javax.swing.JTextField txtUserLog;
    // End of variables declaration//GEN-END:variables
}
