/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package logisticaigu;

import Controladoras.ControladoraCliente;
import Controladoras.ControladoraPaquete;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import logisticalogica.Cliente;
import logisticalogica.Paquete;

/**
 *
 * @author ULTRA
 */
public class BuscarClienteReceptor extends javax.swing.JFrame {
     private ControladoraCliente controladoraCliente;
    private DefaultTableModel tableModel;
    private Paquete paquete; 
    private Paquete paqueteTemporal;
    private ControladoraPaquete controladoraPaquete;


    /**
     * Creates new form BuscarClienteReceptor
     */
    public BuscarClienteReceptor() {
        initComponents();
        tableModel = (DefaultTableModel) jTable1.getModel();
        controladoraCliente = new ControladoraCliente();
        cargarClientesEnTabla();
        inicializarVentana();
        botonAceptar.setEnabled(false); 
        paquete = new Paquete(); // Inicializar el paquete


        
    }
    
    
     public void setPaqueteTemporal(Paquete paqueteTemporal) {
        this.paqueteTemporal = paqueteTemporal;
        if (paqueteTemporal != null) {
            Codpaquete.setText("Cod paquete: " + String.valueOf(paqueteTemporal.getCodigo_paquete()));
        }
    }
     
      private void cargarClientesEnTabla() {
    // Limpiar la tabla antes de cargar nuevos datos
    tableModel.setRowCount(0);

    List<Cliente> clientes = controladoraCliente.obtenerTodosLosClientes();
    if (tableModel == null) {
            System.out.println("tableModel es null. Algo está mal.");
            return;
        }
    // Llenar la tabla con los datos de los clientes
    for (Cliente cliente : clientes) {
        String nombreCompleto = cliente.getNombre() + " " + cliente.getApellido();

        Object[] rowData = {
            nombreCompleto,
            cliente.getNro_documento(),
            cliente.getNro_telefono()
        };
        tableModel.addRow(rowData);
    }
}
       private void inicializarVentana() {
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = jTable1.getSelectedRow();
                    if (selectedRow >= 0) {
                        // Habilitar el botón "Aceptar" cuando se selecciona una fila en la tabla
                        botonAceptar.setEnabled(true);
                    } else {
                        // Deshabilitar el botón "Aceptar" si no hay fila seleccionada
                        botonAceptar.setEnabled(false);
                    }
                }
            }
        });
    }
       private Cliente obtenerClienteDesdeFilaSeleccionada(int filaSeleccionada) {
        // Asegúrate de tener la lista de clientes actualizada
        List<Cliente> clientes = controladoraCliente.obtenerTodosLosClientes();
        if (filaSeleccionada >= 0 && filaSeleccionada < clientes.size()) {
            return clientes.get(filaSeleccionada);
        } else {
            return null;
        }
    }
        private void filtrarClientes() {
    String numeroDocumentoStr = ingresardocumento.getText().trim();

    if (numeroDocumentoStr.isEmpty()) {
        cargarClientesEnTabla(); // Si el campo de búsqueda está vacío, muestra todos los clientes
    } else {
        try {
            int numeroDocumento = Integer.parseInt(numeroDocumentoStr);

            // Realizar la búsqueda de clientes según los valores ingresados
            List<Cliente> clientes = controladoraCliente.filtrarClientesPorNumeroDocumento(numeroDocumento);

            // Limpiar la tabla antes de cargar nuevos datos
            tableModel.setRowCount(0);

            for (Cliente cliente : clientes) {
                String nombreCompleto = cliente.getNombre() + " " + cliente.getApellido();

                Object[] rowData = {
                    nombreCompleto,
                    cliente.getNro_documento(),
                    cliente.getNro_telefono()
                };
                tableModel.addRow(rowData);
            }
        } catch (NumberFormatException e) {
            // Manejo de error si el texto no es un número válido
            JOptionPane.showMessageDialog(null, "Ingrese un número válido para el documento.");
        }
    }
}
        
         private void filtrarClientespornombreyape() {
    String nombre = jTextField1.getText().trim(); // Obtener el nombre desde el campo correspondiente
    String apellido = ingresardocumento.getText().trim(); // Obtener el apellido desde el campo de número de documento

    if (nombre.isEmpty() && apellido.isEmpty()) {
        cargarClientesEnTabla(); // Si ambos campos están vacíos, muestra todos los clientes
    } else {
        // Realizar la búsqueda de clientes según los valores ingresados
        List<Cliente> clientes = controladoraCliente.filtrarNombreyApellido(nombre + " " + apellido);

        // Limpiar la tabla antes de cargar nuevos datos
        tableModel.setRowCount(0);

        for (Cliente cliente : clientes) {
            String nombreCompleto = cliente.getNombre() + " " + cliente.getApellido();

            Object[] rowData = {
                nombreCompleto,
                cliente.getNro_documento(),
                cliente.getNro_telefono()
            };
            tableModel.addRow(rowData);
        }
    }

    
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
         
         private void persistirPaqueteEnBD() {
    if (paqueteTemporal != null) {
        controladoraPaquete = new ControladoraPaquete(); // Instancia de la controladora de paquetes
        
        // Obtener los clientes del paquete
        Cliente clienteEmisor = paqueteTemporal.getEmisor();
        Cliente clienteReceptor = paqueteTemporal.getReceptor();
        
        // Verificar si los clientes son nulos antes de acceder a sus métodos
        if (clienteEmisor != null && clienteReceptor != null) {
            // Obtener los ID de los clientes seleccionados
            int idClienteEmisor = clienteEmisor.getClienteID();
            int idClienteReceptor = clienteReceptor.getClienteID();
        
            // Establecer los ID de los clientes en el paquete
            paqueteTemporal.getEmisor().setClienteID(idClienteEmisor);
            paqueteTemporal.getReceptor().setClienteID(idClienteReceptor);
            
            // Guardar el paquete en la base de datos
            controladoraPaquete.crearpaquete(paqueteTemporal);
            
            // Notificar al usuario que el paquete se ha guardado
            JOptionPane.showMessageDialog(null, "El paquete se ha guardado en la base de datos");
        } else {
            // Notificar al usuario si alguno de los clientes es nulo
            JOptionPane.showMessageDialog(null, "Cliente Emisor o Receptor es nulo.");
        }
    } else {
        // Notificar al usuario si no se ha seleccionado ningún paquete
        JOptionPane.showMessageDialog(null, "No se ha seleccionado ningún paquete para guardar");
    }
}
    private void seleccionarClienteReceptor() {
    int filaSeleccionada = jTable1.getSelectedRow();
    if (filaSeleccionada >= 0) {
        Cliente clienteSeleccionado = obtenerClienteDesdeFilaSeleccionada(filaSeleccionada);
        
        // Para depuración
        System.out.println("Cliente seleccionado: " + clienteSeleccionado);

        if (clienteSeleccionado != null) {
            paquete.setReceptor(clienteSeleccionado); // Asignar el cliente receptor al paquete

            // Para depuración
            System.out.println("Cliente receptor asignado: " + paquete.getReceptor());
            
            persistirPaqueteEnBD(); // Persistir el paquete actualizado en la base de datos
        } else {
            JOptionPane.showMessageDialog(null, "El cliente seleccionado es nulo.");
        }
    } else {
        JOptionPane.showMessageDialog(null, "Por favor, seleccione un cliente.");
    }
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        numerodocclienteemisor = new javax.swing.JLabel();
        ingresardocumento = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        Codpaquete = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        botonAceptar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nombre y Apellido", "N° documento", "N° Telefono"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        numerodocclienteemisor.setText("Numero documento:");

        ingresardocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ingresardocumentoActionPerformed(evt);
            }
        });

        jLabel4.setText("Nombre y Apellido");

        Codpaquete.setText("Cod paquete:");

        jButton3.setText("Registrar");

        botonAceptar.setText("Aceptar");
        botonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAceptarActionPerformed(evt);
            }
        });

        jButton1.setText("Cancelar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Buscar Cliente receptor");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(numerodocclienteemisor)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ingresardocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Codpaquete)
                                .addGap(30, 30, 30))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(botonAceptar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1)
                                .addContainerGap())))))
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(ingresardocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numerodocclienteemisor)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Codpaquete))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(botonAceptar)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ingresardocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ingresardocumentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ingresardocumentoActionPerformed

    private void botonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAceptarActionPerformed
        seleccionarClienteReceptor();
        persistirPaqueteEnBD();

    }//GEN-LAST:event_botonAceptarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    // ... (otros métodos de la clase)

    private Cliente obtenerClienteSeleccionadoEnTabla() {
    int filaSeleccionada = jTable1.getSelectedRow();

    if (filaSeleccionada >= 0) {
        return obtenerClienteDesdeFilaSeleccionada(filaSeleccionada);
    } else {
        return null;
    }
}

// Resto de tus métodos de la clase



       
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Codpaquete;
    private javax.swing.JButton botonAceptar;
    private javax.swing.JTextField ingresardocumento;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel numerodocclienteemisor;
    // End of variables declaration//GEN-END:variables
}
