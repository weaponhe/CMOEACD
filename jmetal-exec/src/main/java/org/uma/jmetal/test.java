package org.uma.jmetal;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by weaponhe on 2017/11/25.
 */
public class test {
    public static void main(String[] args) {
        try {
            FileWriter writer = new FileWriter("test.txt", true);
            writer.write("asd\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
