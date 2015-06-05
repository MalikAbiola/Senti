

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Doctormaliko
 */
public class Normalisations {

    public Normalisations() {
    }

    public String normalise(String text, int allowedRepition) {
        String outt = "";
        for (int i = 0; i < text.length(); i++) {
            int count = 0;
            Character c = text.charAt(i);

            if ((i + 1) < text.length()) {
                if (c != text.charAt(i + 1)) {
                    count++;
                } else {
                    while ((i + 1 < text.length()) && c == text.charAt(i + 1)) {
                        count++;
                        i++;
                    }
                }
            } else {
                if (c != text.charAt(i - 1)) {
                    count++;
                }
            }

            if (count >= allowedRepition) {
                for (int j = 0; j < allowedRepition; j++) {
                    outt += String.valueOf(c);
                }
            } else {
                outt += c;
            }
        }

        return outt;
    }

    public static void main(String[] args) {
        System.out.println(new Normalisations().normalise("ggggggggggggggggooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaalllllllllllllllllllllll", 1));
    }
}
