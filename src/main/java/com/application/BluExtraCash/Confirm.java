package com.application.BluExtraCash;

import javax.swing.*;
import java.util.Scanner;

public class Confirm implements Runnable{
    public void run() {
        //Show BoxDialog
        Object[] options = {"Iya", "Tidak"};
        int confirm = JOptionPane.showOptionDialog(null, "Apakah Anda yakin ingin melanjutkan?", "Konfirmasi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (confirm == JOptionPane.YES_OPTION) {
            System.out.println("Melanjutkan...");
        }else{
            System.out.println("Batal");
            System.exit(0);
        }
    }


    public void confirmDialog(){
        //Show confirm in Console
        Scanner scanner = new Scanner(System.in);
        System.out.println("Apakah Anda yakin ingin melanjutkan? (y/n)");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("y")) {
            System.out.println("Melanjutkan...");
        } else {
            System.out.println("Batal");
            System.exit(0);
        }
        scanner.close();
    }
}
