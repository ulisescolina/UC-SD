/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author urc
 */
public class Proceso implements Serializable{

    private static final int CANTIDAD_PREDECESORES = 2;
    private static final int CANTIDAD_SUCECESORES = 2;
    /**
     * Tiempo en milisegundos que el hilo que controla los latidos va a tolerar para determinar que un proceso fallo
     */
    private static final long TOLERANCIA_VIDA_PROCESO = 3000;
    private int id;
    private TipoProceso tp;
    private Inet4Address ipPropia;
    private int puertoPropio;
    private Inet4Address ipIntroductor;
    private int puertoIntroductor;
    private TreeMap<Integer,Proceso> procesos;
    private TreeMap<Integer,Proceso> vecinos;
    private TreeMap<Integer, Long> temporizadores;
    
    /**
     * Constructor para proceso de tipo Introductor
     * @param id identificador del proceso
     * @param puerto puerto en el cual va a escuchar
     */
    public Proceso(int id, int puerto){
        this.tp = TipoProceso.INTRODUCTOR;
        // Inicializo vecinos y procesos.
        this.procesos = new TreeMap<>();
        this.vecinos = new TreeMap<>();
        this.temporizadores = new TreeMap<>();
        // Inicializacion de otros datos concernientes con el introductor
        try {
            this.id = id;
            this.ipPropia = (Inet4Address) Inet4Address.getLocalHost();
            this.puertoPropio = puerto;
        } catch (UnknownHostException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.agregarProceso(this);
    }
    
    /**
     * Constructor para proceso de tipo Introductor
     * @param id identificador del proceso
     * @param puertopropio puerto en el cual va a escuchar
     * @param ipintroductor
     * @param puertointroductor
     */
    public Proceso(int id, int puertopropio, String ipintroductor, int puertointroductor){
        this.tp = TipoProceso.SIMPLE;
        // Inicializo vecinos y procesos.
        this.procesos = new TreeMap<>();
        this.vecinos = new TreeMap<>();
        this.temporizadores = new TreeMap<>();
        // Inicializacion de otros datos concernientes con cada proceso
        try {
            this.id = id;
            this.ipPropia = (Inet4Address) Inet4Address.getLocalHost();
            this.puertoPropio = puertopropio;
            this.ipIntroductor = (Inet4Address) Inet4Address.getByName(ipintroductor);
            this.puertoIntroductor = puertointroductor;
        } catch (UnknownHostException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void enviarProceso(HashMap<String, Proceso> hmp, InetAddress ip, int puerto){
        DatagramSocket ds = null;
        ObjectOutputStream os = null;
        try {
            ds = new DatagramSocket();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            os = new ObjectOutputStream(outputStream);
            os.writeObject(hmp);
            byte[] data = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, ip, puerto);
            ds.send(sendPacket);
        } catch (SocketException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ds.close();
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(MarcaPaso.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Proceso para establecer comunicacion con el introductor, con el fin de solicitar ingresar al grupo de Procesos que conforman el anillo.
     * @param p Proceso que solicita acceso al anillo.
     * @return Lista de procesos de los cuales está al tanto el introductor.
     */
    public void solicitarAcceso () {
        try {
            HashMap<String, Proceso> hmp = new HashMap<>();
            hmp.put("IN", this);
            // Este no tiene el bloque sincronizado dado a que solo se lo llama una vez
            // por proceso.
            this.enviarProceso(hmp, this.ipIntroductor, this.puertoIntroductor);
            DatagramSocket ds = new DatagramSocket(this.puertoPropio);
            this.esperarMensaje(ds);
        } catch (SocketException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Recibe el mensaje mediante el socket y crea un hilo para que maneje el mismo.
     * @param dSocket socket por el cual recibe el mensaje.
     */
    private void esperarMensaje(DatagramSocket dSocket) {
        try {
            byte[] buffer = new byte[4096];
            DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
            dSocket.receive(incomingPacket);
            byte[] data = incomingPacket.getData();
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            try (ObjectInputStream is = new ObjectInputStream(in)) {
                this.setProcesos((TreeMap<Integer, Proceso>) is.readObject());
            }
            this.actualizarVecinos();
        } catch (IOException ex) {
            Logger.getLogger(Escuchador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dSocket.close();
        }
    }
    
    /**
     * Devuelve el resultado de evaluar la existencia de un proceso introductor en la lista de procesos.
     * @return 
     */
    private boolean existeProcesoIntroductor(){
        boolean existencia = false;
        if (!this.procesos.isEmpty()) {
            Iterator<Entry<Integer, Proceso>> it = this.procesos.entrySet().iterator();
            while (it.hasNext()) {
                Entry<Integer, Proceso> e = it.next();
                Proceso p = e.getValue();
                if (p.tp == TipoProceso.INTRODUCTOR) {
                    existencia = true;
                    break;
                }
            }
        }
        return existencia;
    }
    
    public synchronized void actualizarVecinos() {
        Proceso pivot = this;
        TreeMap<Integer, Proceso> vAux = new TreeMap<>();
        for (int i = 0 ; i < Proceso.CANTIDAD_PREDECESORES; i++) { 
            if (this.procesos.lowerEntry(pivot.id) != null && this.procesos.lowerEntry(pivot.id).getValue().getId() != this.getId()) {
                pivot = this.procesos.lowerEntry(pivot.id).getValue();
            } else {
                pivot = this.procesos.lastEntry().getValue();    
            }
            // Voy a ir creando una lista auxiliar con los predecesores
            if (pivot.getId() != this.id) { // Consulto si el pivot no es el proceso que esta actualizando los vecinos, de otra manera este proceso se agrega como vecino de sí mismo
                vAux.put(pivot.id, pivot);
            }
        }
        
        pivot = this;
        for (int i = 0 ; i < Proceso.CANTIDAD_SUCECESORES; i++) {
            if (this.procesos.higherEntry(pivot.id) != null) {
                pivot = this.procesos.higherEntry(pivot.id).getValue();
            } else {
                pivot = this.procesos.firstEntry().getValue();    
            }
            // Voy a ir creando una lista auxiliar con los sucecesores
            if (pivot.getId() != this.id) {
                vAux.put(pivot.id, pivot);
            }
        }
        // seteo la lista auxiliar con sucesores y predecesores como la nueva lista de vecinos
        this.setVecinos(vAux);
        if (this.temporizadores.isEmpty()) {
            this.inicializarTemporizadores(vAux);
        }
    }
    
    private void inicializarTemporizadores(TreeMap<Integer, Proceso> v) {
        long tSistema = System.currentTimeMillis();
        Iterator<Entry<Integer, Proceso>> vecinos = v.entrySet().iterator();
        TreeMap<Integer, Long> tAux = new TreeMap<>();
        while(vecinos.hasNext()){
            Proceso vecino = vecinos.next().getValue();
            tAux.put(vecino.getId(), tSistema);
        }
        this.setTemporizadores(tAux);
    }
    
    /**
     * Envia a los vecinos un mensaje con el Proceso y la accion a ser realizado sobre el mismo
     * @param p proceso que se envia a los vecinos
     * @param e indica que tipo de mensaje será enviado a los vecinos (IN, EXIT, DEATH, LIVE)
     */
    public void comunicarEstadoProcesoVecinos(Proceso p, String e){
        HashMap<String, Proceso> mensaje = new HashMap<>();
        Iterator<Entry<Integer, Proceso>> it = this.getVecinos().entrySet().iterator();
        // Armar mensaje a ser enviado
        mensaje.put(e, p);
        while (it.hasNext()) {
            Proceso vecino = it.next().getValue();
            synchronized (mensaje) {
                    this.enviarProceso(mensaje, vecino.getIpPropia(), vecino.getPuertoPropio());
            }
        }
    }
    
    // Getter y Setter

    public synchronized int getId() {
        return id;
    }

    public synchronized void setId(int id) {
        this.id = id;
    }

    public synchronized Inet4Address getIpPropia() {
        return ipPropia;
    }

    public synchronized void setIpPropia(Inet4Address ipPropia) {
        this.ipPropia = ipPropia;
    }

    public synchronized int getPuertoPropio() {
        return puertoPropio;
    }

    public synchronized void setPuertoPropio(int puertoPropio) {
        this.puertoPropio = puertoPropio;
    }

    public synchronized Inet4Address getIpIntroductor() {
        return ipIntroductor;
    }

    public synchronized void setIpIntroductor(Inet4Address ipIntroductor) {
        this.ipIntroductor = ipIntroductor;
    }

    public synchronized int getPuertoIntroductor() {
        return puertoIntroductor;
    }

    public synchronized void setPuertoIntroductor(int puertoIntroductor) {
        this.puertoIntroductor = puertoIntroductor;
    }

    public synchronized TreeMap<Integer, Proceso> getProcesos() {
        return new TreeMap<>(this.procesos);
    }

    public synchronized void setProcesos(TreeMap<Integer, Proceso> procesos) {
        this.procesos = procesos;
    }
    
    public synchronized void agregarProceso(Proceso p){
        this.procesos.put(p.getId(), p);
    }
    
    public synchronized void agregarProceso(Map<Integer, Proceso> procesos){
        this.procesos.putAll(procesos);        
    }
    
    public synchronized void quitarProceso(Proceso p) {
        if (this.procesos.containsKey(p.id)) {
            this.procesos.remove(p.id);
        }
    }
    
    public synchronized void quitarProceso(Map<Integer, Proceso> procesos) {
        Map<Integer, Proceso> aux = this.procesos;
    }
    
    public synchronized TreeMap<Integer, Proceso> getVecinos() {
        return new TreeMap<>(this.vecinos);
    }

    public synchronized void setVecinos(TreeMap<Integer, Proceso> vecinos) {
        this.vecinos = vecinos;
    }
    
    public synchronized void agregarVecino(Proceso p) {
        this.vecinos.put(p.id, p);
    }
    
    public synchronized void quitarVecino(Proceso p) {
        if (this.vecinos.containsKey(p.id)) {
            this.vecinos.remove(p.id);
        }
    }

    public synchronized TipoProceso getTipoProceso() {
        return this.tp;
    }

    public synchronized TreeMap<Integer, Long> getTemporizadores() {
        return new TreeMap<>(this.temporizadores);
    }

    public synchronized void setTemporizadorProceso(int id, long t) {
        if (this.temporizadores.containsKey(id)) {
            this.temporizadores.remove(id);
        }
        this.temporizadores.put(id, t);
    }
    
    public synchronized long getToleranciaVidaProceso(){
        return Proceso.TOLERANCIA_VIDA_PROCESO;
    }

    private synchronized void setTemporizadores(TreeMap<Integer, Long> tAux) {
        this.temporizadores = tAux;
    }
    
    public synchronized String imprimirProcesos(){
        String procesos = "";
        Iterator<Entry<Integer, Proceso>> it = this.procesos.entrySet().iterator();
        while (it.hasNext()) {
            Proceso p = it.next().getValue();
            procesos += "ID: "+p.getId()+", Tipo: "+p.getTipoProceso()+", IP:PUERTO: "+p.getIpPropia()+":"+p.getPuertoPropio()+"\n";
        }
        return procesos;
    }
    
    public synchronized String imprimirVecinos(){
        String vecinos = "";
        Iterator<Entry<Integer, Proceso>> it = this.vecinos.entrySet().iterator();
        
        while (it.hasNext()) {
            Proceso p = it.next().getValue();
            vecinos += "ID: "+p.getId()+", Tipo: "+p.getTipoProceso()+", IP:PUERTO: "+p.getIpPropia()+":"+p.getPuertoPropio()+"\n";
        }
        return vecinos;
    }
}
