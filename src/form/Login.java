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
                .addGap(0, 0, 0)
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
    private javax.swing.JPanel Phai19;
    private javax.swing.JPanel Trai20;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPasswordField txtPassLog;
    private javax.swing.JTextField txtUserLog;
    // End of variables declaration//GEN-END:variables
}
