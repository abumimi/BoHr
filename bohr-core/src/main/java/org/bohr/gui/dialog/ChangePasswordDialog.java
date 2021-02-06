/**
 * Copyright (c) 2019 The Bohr Developers
 *
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.bohr.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import org.bohr.core.Wallet;
import org.bohr.gui.Action;
import org.bohr.gui.BohrGui;
import org.bohr.gui.SwingUtil;
import org.bohr.message.GuiMessages;

public class ChangePasswordDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final transient BohrGui gui;

    private final JPasswordField oldPasswordField;
    private final JPasswordField passwordField;
    private final JPasswordField repeatField;

    public ChangePasswordDialog(BohrGui gui, JFrame parent) {
        super(parent, GuiMessages.get("ChangePassword"));
        this.gui = gui;

        JLabel lblOldPassword = new JLabel(GuiMessages.get("OldPassword") + ":");
        JLabel lblPassword = new JLabel(GuiMessages.get("Password") + ":");
        JLabel lblRepeat = new JLabel(GuiMessages.get("RepeatPassword") + ":");

        oldPasswordField = new JPasswordField();
        passwordField = new JPasswordField();
        repeatField = new JPasswordField();

        JButton btnOk = SwingUtil.createDefaultButton(GuiMessages.get("OK"), this, Action.OK);

        JButton btnCancel = SwingUtil.createDefaultButton(GuiMessages.get("Cancel"), this, Action.CANCEL);

        // @formatter:off
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(20)
                    .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(lblPassword)
                        .addComponent(lblOldPassword)
                        .addComponent(lblRepeat))
                    .addGap(18)
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(btnCancel)
                            .addGap(18)
                            .addComponent(btnOk))
                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                            .addComponent(repeatField, GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                            .addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                            .addComponent(oldPasswordField, GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)))
                    .addGap(23))
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(32)
                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblOldPassword)
                        .addComponent(oldPasswordField, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblPassword)
                        .addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblRepeat)
                        .addComponent(repeatField, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(btnCancel)
                        .addComponent(btnOk))
                    .addContainerGap(40, Short.MAX_VALUE))
        );
        getContentPane().setLayout(groupLayout);
        // @formatter:on

        this.setModal(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setIconImage(SwingUtil.loadImage("logo", 128, 128).getImage());
        this.pack();
        this.setLocationRelativeTo(parent);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Action action = Action.valueOf(e.getActionCommand());

        switch (action) {
        case OK: {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(passwordField.getPassword());
            String newPasswordRepeat = new String(repeatField.getPassword());

            Wallet wallet = gui.getKernel().getWallet();

            if (!newPassword.equals(newPasswordRepeat)) {
                JOptionPane.showMessageDialog(this, GuiMessages.get("RepeatPasswordError"));
            } else if (!wallet.unlock(oldPassword)) {
                JOptionPane.showMessageDialog(this, GuiMessages.get("IncorrectPassword"));
            } else {
                wallet.changePassword(newPassword);
                wallet.flush();
                JOptionPane.showMessageDialog(this, GuiMessages.get("PasswordChanged"));
                this.dispose();
            }
            break;
        }
        case CANCEL: {
            this.dispose();
            break;
        }
        default:
            break;
        }
    }
}