
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Phanindra V.V.D
 */
public class Student_GUI extends javax.swing.JFrame {

    /**
     * Creates new form Student_GUI
     */
    
    protected String name;
    private String ID;
    protected String ip;
   // private Socket socket;
    static Socket socket;
    static protected DataOutputStream output;
    static protected DataInputStream input;
    private final int port;
    private final S_Connect_GUI connGUI;
    private S_ScrollPane_GUI spGUI;
    protected FileChannel lockChannel;
    protected FileLock lock;
    JFrame frm;
    
    public Student_GUI(){
        initComponents();
        frm=this;
        //Remove current panels
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        
        connGUI = new S_Connect_GUI(this);
        
        //Add new panel        
        mainPanel.add(connGUI.connectPanel);
        mainPanel.repaint();
        mainPanel.revalidate();      
        
        port = 4555;
        output = null;
        
        //For avoiding multiple instances
        /*try {
            lockChannel = new RandomAccessFile("Student_GUI.java", "rw").getChannel();
            lock = lockChannel.tryLock();
            
            if(lock==null){
                JOptionPane.showConfirmDialog(this, "Cannot open multiple instances");
                System.exit(0);
            }
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        */
                
         frm.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
         frm.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent evt){
                int Answer=JOptionPane.showConfirmDialog(frm, "You want to close the connection ?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(Answer==JOptionPane.YES_OPTION){
                    
                    if(output!=null){
                        try {
                            //lock.release();
                            //lockChannel.close();
                            output.writeUTF("exit");
                            Thread.sleep(1000);
                            closeStreams();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    
                    System.exit(0);
                }
            }
            
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        chatPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Student");

        mainPanel.setBackground(new java.awt.Color(51, 0, 51));
        mainPanel.setLayout(new java.awt.CardLayout());

        chatPanel.setBackground(new java.awt.Color(0, 0, 51));

        jPanel1.setLayout(new java.awt.CardLayout());

        jPanel2.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout chatPanelLayout = new javax.swing.GroupLayout(chatPanel);
        chatPanel.setLayout(chatPanelLayout);
        chatPanelLayout.setHorizontalGroup(
            chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chatPanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );
        chatPanelLayout.setVerticalGroup(
            chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chatPanelLayout.createSequentialGroup()
                .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(chatPanelLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(chatPanelLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(84, Short.MAX_VALUE))
        );

        mainPanel.add(chatPanel, "card3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    protected void connect()
    {
                
        try {
            socket = new Socket(ip, port);
            setupStreams();
            //Identification
            output.writeUTF("SCHOLAR");
            output.writeUTF(name);
            //Get ID                       
            ID = (String)input.readUTF();
            
        } catch (IOException ex) {
            connGUI.status.setText("Server offline :(");
            System.out.println(ex);
            return;
        }
        
        spGUI = new S_ScrollPane_GUI(this); 
        new Thread(spGUI).start();
                
        //Remove current panels
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        
        //set title
        this.setTitle(name);
        
        //Add new panel        
        mainPanel.add(chatPanel);
        mainPanel.repaint();
        mainPanel.revalidate();
        
        chatPanel.add(jPanel1);
        chatPanel.add(jPanel2);
        chatPanel.repaint();
        chatPanel.revalidate();
        
        jPanel1.add(spGUI.listPanel);
        jPanel1.repaint();
        jPanel1.revalidate();
        
       
                                     
    }
    
    
            
    void setupStreams() throws IOException {
        //Initialise Streams
        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());
        
    }
    
    
    void closeStreams() throws IOException{
        output.close();
        input.close();
        socket.close();
    }
    
    
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Student_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Student_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Student_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Student_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Student_GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel chatPanel;
    private javax.swing.JPanel jPanel1;
    protected javax.swing.JPanel jPanel2;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables

    

}
