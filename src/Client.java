
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws NoSuchAlgorithmException, ClassNotFoundException {

        final Socket clientSocket;
        final DataInputStream myIn;
        final DataOutputStream myOut;
        final Scanner sc = new Scanner(System.in);
        final Key publicKeyRecue;
        final byte[] keySecreteByte;
        Key secretKey = GenerateurKey.generateKeyDES();

        try {
            clientSocket = new Socket("127.0.0.1",1234);
            System.out.println("test1");

            myIn = new DataInputStream(clientSocket.getInputStream());
            myOut = new DataOutputStream(clientSocket.getOutputStream());

            //inkey = new ObjectInputStream(new InputStreamReader(clientSocket.getInputStream()));

            byte[]keybyte = new byte[myIn.read()];
            publicKeyRecue = new SecretKeySpec(keybyte, "RSA");
            System.out.println("test2");
//            Key secretKey = GenerateurKey.generateKeyDES();
            keySecreteByte = Codage.encodeMessage(publicKeyRecue,secretKey.getEncoded());
            myOut.write(keySecreteByte);
            myOut.flush();
            System.out.println("test");
            Thread envoyer = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    while(true){
                        msg = sc.nextLine();
                        byte[] msgEnByte = msg.getBytes();
                        try {
                            byte[] msgEncode = Codage.encodeMessage(publicKeyRecue, msgEnByte);
                            myOut.write(msgEncode);
                            myOut.flush();
//                            out.println(msg);
//                            out.flush();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }
            });
            envoyer.start();

            Thread recevoir = new Thread(new Runnable() {
                byte[] msg;
                @Override
                public void run() {
                    try {
                        msg = new byte[myIn.read()];
                        //tant que le client est connecté
                        while(msg!=null){
                            byte[]msgRecueEncode = msg;
                            byte[]msgRecueDecodede = Decodage.decodeMessage(secretKey, msgRecueEncode);
                            System.out.println("test.Client : "+msgRecueDecodede);
//                            msg = in.readLine();
                        }
                        //sortir de la boucle si le client a déconecté
                        System.out.println("test.Client déconecté");
                        //fermer le flux et la session socket
                        myOut.close();
                        clientSocket.close();
                    } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException  e) {
                        e.printStackTrace();
                    }
                }
            });
            recevoir.start();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
