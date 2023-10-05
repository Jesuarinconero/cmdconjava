package help;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class Help extends JFrame {

    private String baseHelpPath = "/html/";

    /** Constructor */
    public Help() {
        initComponents();
        //Icono de la aplicacion
        setIconImage(new ImageIcon(getClass().getResource("LogoIcono.png")).getImage());

        setTitle("BonoBus Conecta");
        //propiedades de splipanel
        jSplitPane1.setOneTouchExpandable(true);
        jSplitPane1.setDividerLocation(250);
        jSplitPane1.setDividerSize(10);        
        //propiedades de editorpanel
        jEditorPane1.setEditable(false);        
        jEditorPane1.setContentType( "text/html" );
        jEditorPane1.getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        jEditorPane1.setText( readFile("archivo1") );//lee el primer archivo
        jEditorPane1.setCaretPosition(0);
        
        //jtree                
        jTree1.setModel(createTreeModel());       
        //listener
        jTree1.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {             
                //cuando se realice un clic sobre algun item, se carga el archivo HTML correspondiente
                TreePath path = jTree1.getSelectionPath();
                if( path!=null )
                {
                    DefaultMutableTreeNode NodoSeleccionado = (DefaultMutableTreeNode)path.getLastPathComponent();              
                    //Obtiene el nombre del archivo HTML correspondiente al item seleccionado
                    String archivo = ((HelpArchivo) NodoSeleccionado.getUserObject()).getArchivo();
                    jEditorPane1.setText( readFile(archivo) );
                    jEditorPane1.setCaretPosition(0);
                }
            }            
        }); 
        //iconos del jtree
        DefaultTreeCellRenderer render= (DefaultTreeCellRenderer)jTree1.getCellRenderer();
        render.setLeafIcon(new ImageIcon(getClass().getResource("sheet.jpg")));        
        render.setOpenIcon(new ImageIcon(getClass().getResource("open.jpg")));
        render.setClosedIcon(new ImageIcon(getClass().getResource("close.jpg")));
    }

    /**
     * Metodo para leer un archivo HTML
     * @param String fileName
     * @return String 
     */
    private String readFile(String fileName) {
        StringBuilder result = new StringBuilder("");
        // Utiliza la ruta base para construir la ruta completa del archivo HTML
        String filePath = baseHelpPath + fileName + ".html";
        InputStream inputStream = getClass().getResourceAsStream(filePath);

        if (inputStream != null) {
            try (Scanner scanner = new Scanner(inputStream)) {
                while (scanner.hasNextLine()) {
                    String line = replaceSRC(scanner.nextLine().trim());
                    result.append(line);
                }
            }
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "El archivo [" + fileName + ".html] no existe.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return result.toString();
    }
    
    /**
     * Metodo que lee el archivo HELP para crear el arbol de ayuda
     * @return DefaultTreeModel
     */
    private DefaultTreeModel createTreeModel()
    {
       DefaultMutableTreeNode root  = new DefaultMutableTreeNode();   
       DefaultMutableTreeNode hoja = new DefaultMutableTreeNode();           
       //carga archivo help       
	try {
            InputStream input = getClass().getResourceAsStream("/help/indice.txt");            
            Scanner scanner = new Scanner(input);
            //lee archivo linea por linea
	    while (scanner.hasNextLine()) {
	    String line = scanner.nextLine().trim();                        
                if( line.substring(0, 1).equals(">") )//es la raiz del arbol
                {
                    root = new DefaultMutableTreeNode( createHelpArchivo(line.substring(1, line.length())) );         
                }
                else if( line.substring(0, 1).equals("|") )//es una rama u hoja
                {
                    hoja = new DefaultMutableTreeNode( createHelpArchivo(line.substring(1, line.length())) );    
                    root.add(hoja);
                }
                else if( line.substring(0, 1).equals("-") )//es una hoja
                {
                    hoja.add(new DefaultMutableTreeNode( createHelpArchivo(line.substring(1, line.length())) ));
                }
            } 
            scanner.close(); 
            //se añade arbol al modelo
            DefaultTreeModel modelo = new DefaultTreeModel(root);
            return modelo; 
	} catch (NullPointerException e) {            
             JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
	} 
	return null;
    }    
    
    /**
     * Dado un valor string "value" crea una clase HelpArchivo
     * @param String value de la forma
     * Ej. [texto][archivo html]
     * @return HelpArchivo
     */
    private HelpArchivo createHelpArchivo( String value )
    {
       HelpArchivo helpArchivo = new HelpArchivo();
       //System.out.println( value );
       Pattern patron = Pattern.compile("\\[(.*)\\]\\[(\\w+)\\]");
       Matcher matcher = patron.matcher(value);       
       matcher.find();//busca cadenas
       helpArchivo.setTexto(matcher.group(1));
       helpArchivo.setArchivo(matcher.group(2));        
       return helpArchivo;
    }
    
    /**
     * Metodo que reemplaza la ruta de imagen 
     * Antes src="imagen.jpg"
     * despues src="file:\\\c:\carpeta\carpeta\appHelp\help\imagen.jpg"
     * @param String value
     * @return String
     */
    private String replaceSRC(String value)
    {
        // Si existe una imagen en la etiqueta "img src", reemplaza su ruta
        if (value.contains("src=\"")) {
            // Patrón para encontrar la ruta de la imagen
            Pattern pattern = Pattern.compile("src=\"(.*?)\"");
            Matcher matcher = pattern.matcher(value);

            while (matcher.find()) {
                String imagePath = matcher.group(1);
                // Reemplaza la ruta relativa con una ruta de recurso en el JAR
                String resourcePath = baseHelpPath + imagePath;
                // Carga la imagen como recurso desde el JAR
                ImageIcon imageIcon = new ImageIcon(getClass().getResource(resourcePath));
                // Actualiza la etiqueta "src" con la ruta del recurso
                value = value.replace(matcher.group(1), imageIcon.toString());
            }
        }

        return value;
    }

    
    //-------------------END ---------------------
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new JSplitPane();
        jScrollPane2 = new JScrollPane();
        jEditorPane1 = new JEditorPane();
        jScrollPane3 = new JScrollPane();
        jTree1 = new JTree();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(900, 500));
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));

        jScrollPane2.setViewportView(jEditorPane1);

        jSplitPane1.setRightComponent(jScrollPane2);

        jScrollPane3.setMinimumSize(new java.awt.Dimension(200, 23));
        jScrollPane3.setViewportView(jTree1);

        jSplitPane1.setLeftComponent(jScrollPane3);

        getContentPane().add(jSplitPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Help.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Help.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Help.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Help.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Help().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JEditorPane jEditorPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JSplitPane jSplitPane1;
    private JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
