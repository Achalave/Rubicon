/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PlayerSelect.java
 *
 * Created on Feb 14, 2013, 12:26:40 PM
 */
package rubicon.menus;

import java.awt.Image;
import javax.swing.ImageIcon;
import rubicon.Data;
import rubicon.Vars;
import rubicon.players.Player;

/**
 *
 * @author michaelhaertling
 */
public class PlayerSelect extends javax.swing.JPanel {

    /** Creates new form PlayerSelect */
    int index = 0;
    Data data;
    
    public PlayerSelect() {
        initComponents();
    }
    
    public void setData(Data d){
        data = d;
        refreshPlayer();
    }
    
    private void refreshPlayer(){
        if(index == 0)
            back.setEnabled(false);
        else
            back.setEnabled(true);
        if(index == Vars.players.length-1)
            forward.setEnabled(false);
        else
            forward.setEnabled(true);
        Player p = Vars.players[index];
        Image i = p.getImage();
        image.setIcon(new ImageIcon(i));
        description.setText(p.getDescription());
        if(data.isPlayerUnlocked(index)){
            if(data.getPlayerIndex() == index)
                select.setEnabled(false);
            else
                select.setEnabled(true);
            select.setText("Select");
        }
        else{
            select.setText("Purchase("+p.getCost()+")");
            if(p.getCost() <= data.getMoney())
                select.setEnabled(true);
            else
                select.setEnabled(false);
        }
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        back = new javax.swing.JButton();
        forward = new javax.swing.JButton();
        select = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        description = new javax.swing.JTextArea();
        image = new javax.swing.JLabel();

        setOpaque(false);

        back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LeftArrow.png"))); // NOI18N
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        forward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/RightArrow.png"))); // NOI18N
        forward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forwardActionPerformed(evt);
            }
        });

        select.setText("Select");
        select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectActionPerformed(evt);
            }
        });

        description.setColumns(20);
        description.setEditable(false);
        description.setLineWrap(true);
        description.setRows(4);
        description.setWrapStyleWord(true);
        description.setDragEnabled(false);
        jScrollPane1.setViewportView(description);

        image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(image, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(select, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(forward, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(select, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(forward, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        index--;
        refreshPlayer();
    }//GEN-LAST:event_backActionPerformed

    private void forwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forwardActionPerformed
        index++;
        refreshPlayer();
    }//GEN-LAST:event_forwardActionPerformed

    private void selectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectActionPerformed
        if(data.isPlayerUnlocked(index)){
            data.setPlayerIndex(index);
            select.setEnabled(false);
        }else{
            data.setMoney(data.getMoney()-Vars.players[index].getCost());
            select.setText("Select");
            data.setPlayerIndex(index);
            select.setEnabled(false);
            data.unlockPlayer(index);
        }
    }//GEN-LAST:event_selectActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JTextArea description;
    private javax.swing.JButton forward;
    private javax.swing.JLabel image;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton select;
    // End of variables declaration//GEN-END:variables
}
