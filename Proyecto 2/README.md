# Objetivo
El objetivo de este proyecto es implementar un sistema de consultas de Logs distribuido. Los depuradores (Ej.: Gdb, IDEs) funcionan bien principalmente en programas con un único hilo. En la industria, el enfoque más popular para depurar sistemas distribuidos es el uso de registros (logs). Esto significa que cada nodo crea uno o más archivos locales en los que se acumulan mensajes de estado, mensajes de error y, en general, cualquier cosa que se desee registrar. Estos registros se pueden consultar de forma remota.

# Funcionmiento
Se debe escribir un programa que permita consultar archivos de registro distribuidos en varios nodos, desde cualquiera de estos nodos.

Como prueba trabaje con N (>= 5) nodos, cada una de las cuales genera un archivo de registro denominado nodo.i.log.

El sistema debe funcionar desde una terminal en cualquiera de esos N nodos. La funcionalidad del sistema es permitir la ejecución de un comando grep que se ejecute sobre el archivo de registro en todos los nodos y produzca resultados en la terminal de consulta (con las líneas apropiadas para designar de qué entradas de registro provienen).

El programa de consulta de logs debe ser tolerante a fallas, es decir, debe obtener respuestas de todos los nodos que no han fallado. Está bien inicializar las instancias con un estado definidio, por ejemplo, nombres de máquina o direcciones IP (usando un arhivo de nodos como en el proyecto 1). También está bien suponer que la máquina de consultas no fallará (¡pero las otras máquinas pueden fallar!). Puede suponer que las máquinas son de tipo fail-stop, es decir, una vez que fallaron, no vuelven a estar en línea.

Debe implementar dos tipos de procesos, uno cliente que envia la consulta a los nodos servidores y uno servidor que realiza la búsqueda sobre el archivo de log asociado y devuelve el resultado al cliente.

La consulta a los servidores por parte del cliente debe ser concurrente. Los servidores deben ser concurrentes.

# Archivo de Logs
Puede generar sus propios archivos de logs usando [Apache Commons Logging](http://commons.apache.org/proper/commons-logging/guide.html#Quick_Start).

O usar el [siguiente archivo de logs](http://ita.ee.lbl.gov/html/contrib/SDSC-HTTP.html). Tome parte de este archivo y distribuyalas en los distintos nodos, no realize replicación.

# Bibliotecas de Terceros y Restricciones #
La aplicación debe implementarse utilizando los paquetes java.net.*, java.util.* y java.io.*, también pueden usar otros paquetes de utilidad del paquete central de Java siempre que no esté usando paquetes relacionados con RPC o frameworks de objetos distribuidos. Tampoco está permitido el uso de archivos jar externos.

Si usa objetos distribuidos, RPC o archivos jar externos para desarrollar la aplicación, tendra un puntaje de 0 (cero).

Puede discutir el proyecto con sus compañeros a nivel arquitectónico, pero la implementación del proyecto es un esfuerzo de cada grupo.

# Forma de lanzar el proyecto
## Servidor(es)
Se distribuyo el [siguiente archivo de logs](http://ita.ee.lbl.gov/html/contrib/SDSC-HTTP.html) en archivos de logs que lee cada uno de los servidores que estén levantados, estos leeran el archivo de logs que concuerde con su IO.

Para iniciar un servidor debe Abrir un terminal y escribir el siguiente comando:

```sh
$ cd Ejecutables/
$ java -jar ServidorLOGS.jar
```
El servidor procederá a solicitarle el **Puerto** en el cual este va a estar escuchando y luego el **ID** que tendrá el mismo, cuando realice estos dos pasos, el servidor se pondrá a escuchar por conexiones entrantes.

`Esto lo debe repetir en diferentes terminales por la cantidad de servidores que tenga registrados en el archivo **servers.txt**`

## Cliente(s)
Para iniciar el cliente debe ejecutar el siguiente comando:

```sh
$ cd Ejecutables/
$ java -jar ClienteLOGS.jar
```
El cliente procederá a solicitarle una cadena de texto que va a representar la busqueda que desea realizar en los servidores que se encuentren corriendo.

### Opcional
Si desea puede correr el comando para el cliente de la siguiente manera:

```sh
$ cd Ejecutables/
$ java -jar ClienteLOGS.jar > resultados.txt
```
Aqui el cliente va a esperar que ingrese la cadena a buscar y cuando la ingrese este procedera a guardar toda la consulta en el archivo `resultados.txt`
y en la misma terminal le avisará si hubiern servidores a los cuales no se pudo conectar indicando la direccion del mismo y el puerto.
