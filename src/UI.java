
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import help.Help;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
//import com.github.weisj.darklaf.LafManager;
//import com.github.weisj.darklaf.components.uiresource.JButtonUIResource;
//import com.github.weisj.darklaf.theme.*;
public class UI extends JFrame {
    private JPanel panelMain, estancia,colectivo,propuesta,suBillete;
    private JLabel dias,viajes;
    private JLabel holder;
    private JSpinner ndias;
    private JSpinner slViajes;
    private JButton aceptar, cancelar, help;
    private int dias_selec=1;
    private int viajes_selec=1;
    private String colect_selec;
    private Boolean flag=false;
    private JRadioButton sDescuento,jubilado,discapacitado,parado,estudiante;
    private ButtonGroup tipoBonos;
    private final JLabel imgfin = new JLabel();
    private int cuentaAtras = 7;
    private final ActionListener ti = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(cuentaAtras>1){
                cuentaAtras--;
                holder.setText("Su billete se confirmará en "+cuentaAtras+"s");
            }
            else {
                reiniciar();
            }
        }
    };
    private final Timer tiempo = new Timer(1000,ti);
    private GridLayout gridLayout = new GridLayout();
    private JTextArea objetoFinal= new JTextArea();
    public void IniciarVentana() {
        Color azulito = new Color(173, 216, 230);
        Color c = new Color(34, 72, 88);
        UIManager.put("Panel.background", azulito);
        UIManager.put("TitledBorder.font", new Font("Arial", Font.BOLD, 25));
        UIManager.put("TitledBorder.titleColor", c);
        UIManager.put("Label.font", new Font("Arial",Font.PLAIN,20));
        UIManager.put("RadioButton.font", new Font("Arial",Font.PLAIN,20));
        ImageIcon logo = new ImageIcon("Iconos/autobus.png");
        this.setIconImage(logo.getImage());
        setTitle("Aplicación movilidad");
        holder = new JLabel();
        holder.setFont(new Font("Arial",Font.PLAIN,20));
        holder.setBorder(BorderFactory.createEmptyBorder(15,0,0,0));
        setLocation(500, 150);
        gridLayout.setHgap(30);
        gridLayout.setVgap(40);
        gridLayout.setRows(1);
        gridLayout.setColumns(2);
        panelMain = new JPanel(gridLayout);
        //para separarlo de los bordes que no esté tan pegado
        panelMain.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(panelMain);

        Estancia();
        Colectivo();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(765,313);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    public void AnadirHelp() {
        help = new JButton();
        help.setToolTipText("Ayuda");
        JPanel helpPanel = new JPanel();
        ImageIcon imageIcon = new ImageIcon("src/iconodepregunta.png");
        Image originalImage = imageIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(60, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        help.setIcon(scaledIcon);
        help.setBorderPainted(false);
        help.setContentAreaFilled(false);
        help.setFocusPainted(false);
        help.addActionListener(this::jButton1ActionPerformed);
        helpPanel.setOpaque(false);
        helpPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        helpPanel.add(help);
        estancia.add(helpPanel);

    }
    private void jButton1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Help help = new Help();
        help.setVisible(true);
    }
    public void tiempo(){
        tiempo.restart();
    }
    public void Estancia(){
        //panel estancia
        estancia= new JPanelConFondo();
        estancia.setLayout(new GridLayout(3,1,10,10));
        estancia.setBorder(BorderFactory.createTitledBorder(" Estancia "));
        estancia.setBackground(new Color(45,67,23));
        //fila 1: dias y spinner----------------
        JPanel paneldias= new JPanel(new FlowLayout(FlowLayout.LEFT));
        dias= new JLabel("Días");
        //añadimos
        ndias = new JSpinner(new SpinnerNumberModel(1,1,999,1));
        ndias.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                dias_selec = (int) ndias.getValue();
                if (flag){
                    CalculadorMovilidad c = new CalculadorMovilidad(dias_selec,viajes_selec,colect_selec);
                    ArrayList<Double> arr = c.calculaPreciosViaje();
                    objetoFinal.setText(c.mejorOpcion(arr));
                }
            }
        });
        AnadirHelp();
        paneldias.setLayout(new FlowLayout(FlowLayout.LEFT));
        dias.setBorder(BorderFactory.createEmptyBorder(0,10,0,40));
        paneldias.add(dias);
        paneldias.add(ndias);
        paneldias.setOpaque(false);
        estancia.add(paneldias);
        //FIlA 2 slider-------------------
        slViajes = new JSpinner(new SpinnerNumberModel(1,1,999,1));
        //damos funcionalidad a los nViaj con respecto al slider cuand la barra se mueve
        //los números de viajes aumentan
        slViajes.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                viajes_selec= (int )slViajes.getValue();
                if (flag){
                    CalculadorMovilidad c = new CalculadorMovilidad(dias_selec,viajes_selec,colect_selec);
                    ArrayList<Double> arr = c.calculaPreciosViaje();
                    objetoFinal.setText(c.mejorOpcion(arr));
                }
            }
        });
        //FILA 3 viajes-nviajes
        JPanel panelviajes=new JPanel();
        viajes= new JLabel("Viajes");
        panelviajes.setLayout(new FlowLayout(FlowLayout.LEFT));
        viajes.setBorder(BorderFactory.createEmptyBorder(0,10,0,27));
        panelviajes.add(viajes);
        panelviajes.add(slViajes);
        panelviajes.setOpaque(false);
        estancia.add(panelviajes);
        //añadimos al panel central
        panelMain.add(estancia);
    }
    public void Colectivo(){
        //panel colectivo
        colectivo=new JPanel(new GridLayout(5,1));
        colectivo.setBorder(BorderFactory.createTitledBorder(" Colectivo "));
        //creamos el grupo para los botones radios
        sDescuento= new JRadioButton("Sin Descuento");
        jubilado= new JRadioButton("Jubilado");
        discapacitado= new JRadioButton("Discapacitado");
        parado= new JRadioButton("Parado");
        estudiante= new JRadioButton("Estudiante");
        tipoBonos= new ButtonGroup();
        //agregamos al grupo
        tipoBonos.add(sDescuento);
        tipoBonos.add(jubilado);
        tipoBonos.add(discapacitado);
        tipoBonos.add(parado);
        tipoBonos.add(estudiante);
        //agregamos la escucha
        ActionListener escucha_radio = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractButton aButton = (AbstractButton) e.getSource();
                switch (aButton.getText()) {
                    case "Sin Descuento" -> colect_selec = "1";
                    case "Jubilado" -> colect_selec = "2";
                    case "Discapacitado" -> colect_selec = "4";
                    case "Parado" -> colect_selec = "3";
                    case "Estudiante" -> colect_selec = "5";
                }
                if(!flag) {
                    gridLayout.setRows(2);
                    Propuesta();
                    SuBillete();
                    panelMain.repaint();
                    panelMain.revalidate();
                    setSize(765,590);
                    flag=true;
                }
                CalculadorMovilidad c = new CalculadorMovilidad(dias_selec,viajes_selec,colect_selec);
                ArrayList<Double> arr = c.calculaPreciosViaje();
                objetoFinal.setText(c.mejorOpcion(arr));
                holder.setText("Su billete aparecerá aquí");
            }
        };
        sDescuento.addActionListener(escucha_radio);
        jubilado.addActionListener(escucha_radio);
        discapacitado.addActionListener(escucha_radio);
        parado.addActionListener(escucha_radio);
        estudiante.addActionListener(escucha_radio);
        //agregamos al panel panel de colectivos
        colectivo.add(sDescuento);
        colectivo.add(jubilado);
        colectivo.add(discapacitado);
        colectivo.add(parado);
        colectivo.add(estudiante);
        //añadimos al panel central
        panelMain.add(colectivo);
    }
    public void Propuesta(){
        propuesta= new JPanel(new GridLayout(2,1,20,40));
        propuesta.setBorder(BorderFactory.createTitledBorder(" Propuesta "));
        //FILA 1 texto objeto final
        objetoFinal.setBorder(BorderFactory.createEmptyBorder(20,20,0,0));
        objetoFinal.setLineWrap(true);
        objetoFinal.setWrapStyleWord(true);
        objetoFinal.setEditable(false);
        Font tipoFuente = new Font("Arial",Font.BOLD,19);
        objetoFinal.setFont(tipoFuente);
        propuesta.add(objetoFinal,BorderLayout.NORTH);

        //FILA 2  Botones
        JPanel botones= new JPanel();
        //boton aceptar
        aceptar= new JButton(new ImageIcon("Iconos/aceptar.png"));
        aceptar.setBackground(new Color(144,238,144));
        aceptar.setToolTipText("Aceptar");
        aceptar.setLayout(new FlowLayout(FlowLayout.CENTER));


        //accion al aceptar nos lleva a que se muestre el billete del descuento que se ha elegido.
        aceptar.addActionListener(e -> {
            holder.setText("Su billete se confirmará en "+cuentaAtras+"s");
            aceptar.setEnabled(false);
            sDescuento.setEnabled(false);
            jubilado.setEnabled(false);
            discapacitado.setEnabled(false);
            parado.setEnabled(false);
            estudiante.setEnabled(false);
            ndias.setEnabled(false);
            slViajes.setEnabled(false);
            CalculadorMovilidad c = new CalculadorMovilidad(dias_selec,viajes_selec,colect_selec);
            ArrayList<Double> arr = c.calculaPreciosViaje();
            objetoFinal.setText(c.mejorOpcion(arr));
            ImageIcon ico = null;
            Image img;
            Image img2;
            switch (colect_selec) {
                case "1" -> {
                    ico = new ImageIcon("Iconos/sindesc.png");
                    img = ico.getImage();
                    img2 = img.getScaledInstance(110, 110, Image.SCALE_SMOOTH);
                    ico.setImage(img2);
                }
                case "2" -> {
                    ico = new ImageIcon("Iconos/jubil.png");
                    img = ico.getImage();
                    img2 = img.getScaledInstance(110, 110, Image.SCALE_SMOOTH);
                    ico.setImage(img2);
                }
                case "4" -> {
                    ico = new ImageIcon("Iconos/disc.png");
                    img = ico.getImage();
                    img2 = img.getScaledInstance(110, 110, Image.SCALE_SMOOTH);
                    ico.setImage(img2);
                }
                case "3" -> {
                    ico = new ImageIcon("Iconos/par.png");
                    img = ico.getImage();
                    img2 = img.getScaledInstance(110, 110, Image.SCALE_SMOOTH);
                    ico.setImage(img2);
                }
                case "5" -> {
                    ico = new ImageIcon("Iconos/estu.png");
                    img = ico.getImage();
                    img2 = img.getScaledInstance(110, 110, Image.SCALE_SMOOTH);
                    ico.setImage(img2);
                }
            }
            imgfin.setIcon(ico);
            suBillete.add(imgfin);
            suBillete.repaint();
            tiempo();
        });
        //boton cancelar
        cancelar= new JButton(new ImageIcon("Iconos/rechazar.png"));
        cancelar.setBackground(new Color(255,192,203));
        cancelar.setToolTipText("Cancelar");

        //acción al cancelar reinicia a como estaba antes de empezar el programa
        cancelar.addActionListener(e->{
            reiniciar();
        });
        botones.add(aceptar);
        botones.add(cancelar);
        propuesta.setVisible(true);
        propuesta.add(botones);
        panelMain.add(propuesta);
    }
    public void SuBillete(){
        suBillete=new JPanel();
        ImageIcon ico = new ImageIcon("Iconos/bus.gif");
        suBillete.add(holder);
        imgfin.setIcon(ico);
        suBillete.add(imgfin);
        suBillete.setBorder(BorderFactory.createTitledBorder(" Su Billete "));
        suBillete.setVisible(true);
        panelMain.add(suBillete);
    }
    public void reiniciar(){
        aceptar.setEnabled(true);
        sDescuento.setEnabled(true);
        jubilado.setEnabled(true);
        discapacitado.setEnabled(true);
        parado.setEnabled(true);
        estudiante.setEnabled(true);
        ndias.setEnabled(true);
        slViajes.setEnabled(true);
        ndias.setValue(1);
        slViajes.setValue(1);
        tipoBonos.clearSelection();
        panelMain.remove(propuesta);
        panelMain.remove(suBillete);
        panelMain.revalidate();
        panelMain.repaint();
        flag = false;
        cuentaAtras = 7;
        gridLayout.setRows(1);
        setSize(765,313);
        tiempo.stop();

    }
    public static void main(String[] args) throws UnsupportedLookAndFeelException {

                UIManager.setLookAndFeel(new FlatLightLaf());

        SwingUtilities.invokeLater(() -> new UI());
        UI u=new UI();
        u.IniciarVentana();
    }
}