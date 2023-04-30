package org.example;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author kzsbv
 */
public class JFrameProgram extends javax.swing.JFrame {

    MathManipulation mm = new MathManipulation();
    public JFrameProgram() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel = new javax.swing.JPanel();
        jButtonImport = new javax.swing.JButton();
        jButtonExport = new javax.swing.JButton();
        jButtonExit = new javax.swing.JButton();
        jLabelImport = new javax.swing.JLabel();
        jLabelExport = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButtonImport.setText("Импорт данных");
        jButtonImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportActionPerformed(evt);
            }
        });

        jButtonExport.setText("Экспорт данных");
        jButtonExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportActionPerformed(evt);
            }
        });

        jButtonExit.setText("Выйти");
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        jLabelImport.setText("Данные не загружены");
        jLabelImport.setEnabled(false);

        jLabelExport.setText("Данные не выгружены");
        jLabelExport.setEnabled(false);

        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonImport)
                    .addComponent(jButtonExport))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelImport)
                    .addComponent(jLabelExport))
                .addGap(72, 72, 72))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonExit)
                .addGap(159, 159, 159))
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonImport)
                    .addComponent(jLabelImport))
                .addGap(58, 58, 58)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonExport)
                    .addComponent(jLabelExport))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addComponent(jButtonExit)
                .addGap(59, 59, 59))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportActionPerformed
        if (jLabelImport.getText().equals("Данные не загружены")) JOptionPane.showMessageDialog (null, "Данные еще не получены!", "Oшибка", JOptionPane.ERROR_MESSAGE);
        else if (jLabelExport.getText().equals("Данные выгружены")||jLabelExport.getText().equals("Данные выгружены частично")) JOptionPane.showMessageDialog (null, "Данные уже выгружены!", "Oшибка", JOptionPane.ERROR_MESSAGE);
            else {
                try {
                    mm.writeResultsToExcel("./Result.xlsx");
                    //mm.writeResultsToExcel(".\\src\\main\\resources\\data\\Result.xlsx");
                    jLabelExport.setText("Данные выгружены");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog (null, e.getMessage(), "Oшибка", JOptionPane.ERROR_MESSAGE);
                    if (e.getMessage().equals("Неправильные данные для рассчета ковариации")) jLabelExport.setText("Данные выгружены частично");
                }
            }

    }//GEN-LAST:event_jButtonExportActionPerformed

    private void jButtonImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportActionPerformed
        if (jLabelImport.getText().equals("Данные загружены")) JOptionPane.showMessageDialog (null, "Данные уже получены!", "Oшибка", JOptionPane.ERROR_MESSAGE);
        else {
            try {
                mm.setData("/ДЗ4.xlsx");
                jLabelImport.setText("Данные загружены");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Oшибка", JOptionPane.ERROR_MESSAGE);
                jLabelImport.setText("Данные не загружены");
            }
        }

    }//GEN-LAST:event_jButtonImportActionPerformed

    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButtonExitActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonExport;
    private javax.swing.JButton jButtonImport;
    private javax.swing.JLabel jLabelExport;
    private javax.swing.JLabel jLabelImport;
    private javax.swing.JPanel jPanel;
    // End of variables declaration//GEN-END:variables
}
