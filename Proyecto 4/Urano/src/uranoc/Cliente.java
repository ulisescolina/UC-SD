package uranoc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.*;

public class Cliente {
    
    private static final int TAMANIO_BLOQUE = 4194304;
    
    /**
     * Obtiene un archivo, y lo separa en partes de igual tamanio
     * @param archivo Ruta y nombre del archivo a particionar
     * @return HashMap con (SHA-256, bloque del archivo)
     */
    public LinkedHashMap<String,byte[]> particionador (String archivo){
        File ifile = new File(archivo);
        //HashMap<String,byte[]> listaBloques = new HashMap<>();
        LinkedHashMap<String,byte[]> listaBloques = new LinkedHashMap<>();
        if (ifile.exists()){
            FileInputStream fis;
            int fileSize = (int) ifile.length();
            int read = 0, readLength = Cliente.TAMANIO_BLOQUE;
            String parte = "";
            int cont = 0;
            byte[] byteChunk;
            try{
                fis = new FileInputStream(ifile);
                 // Mientras no haya llegado al final del archivo
                while (fileSize > 0) {
                    cont++;
                    // Ultimo bloque del archivo
                    if (fileSize <= Cliente.TAMANIO_BLOQUE) {
                        readLength = fileSize;
                    }
                    byteChunk = new byte[readLength];
                    read = fis.read(byteChunk, 0, readLength); // Leo el tamanio definido
                    listaBloques.put(Cliente.hasheador(byteChunk, fileSize),byteChunk);
                    //parte = String.valueOf(cont);
                    //listaBloques.put(parte,byteChunk);
                    fileSize -= read;
                    byteChunk = null;
                }
                fis.close();
            }catch(Exception e){
                e.printStackTrace();
            }
                    
        }else{
            System.err.println("Archivo inexistente, o path incorrecto");
        }
        return listaBloques;
    }
    
    public byte[] juntador (LinkedHashMap<String,byte[]> listaBloques){
        // Corregir el tamanio del archivo final.
        // Habría que sumar el lenght de cada elemento del Hash para obtener el tamanio final del archivo
        Iterator<Entry<String, byte[]>> it = listaBloques.entrySet().iterator();
        int tamanio = 0;
        while (it.hasNext()){
            tamanio += it.next().getValue().length;
        }
        //byte[] compilado = new byte[TAMANIO_BLOQUE * listaBloques.size()];
        byte[] compilado = new byte[tamanio];
        it = listaBloques.entrySet().iterator();
        int indice = 0;
        while (it.hasNext()) {
            Entry<String, byte[]> e = it.next();
            byte[] a = new byte[e.getValue().length];
            a = e.getValue();
            System.arraycopy(a, 0, compilado, indice, a.length);
            indice = indice + a.length;
            //compilado = ArrayUtils.addAll(compilado, e.getValue());
        }
        return compilado;
    }
    
    public static String hasheador(byte[] bloque, int i) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(bloque);
            String sha256hex = DigestUtils.sha256Hex(encodedhash);
            //System.out.println("hash: " + sha256hex);
            return sha256hex;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    public String obtenerNombre(String archivo) {
        String[] path = archivo.split("\\" + File.separator);
        return path[path.length-1];
    }
    
}
