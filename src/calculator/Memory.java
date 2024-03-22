package calculator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Memory {

    public static void setData(String d) throws IOException{
        FileOutputStream fout = new FileOutputStream("mr.bin");
        DataOutputStream dout = new DataOutputStream(fout);

        dout.writeUTF(d);

        fout.close();
        dout.close();

        System.out.println("Data saved successfully");
    }

    public static String getData() throws IOException{
        FileInputStream fin = new FileInputStream("mr.bin");
        DataInputStream din = new DataInputStream(fin);
        
        String d = din.readUTF();

        fin.close();
        din.close();
        
        return d;
    }
}

