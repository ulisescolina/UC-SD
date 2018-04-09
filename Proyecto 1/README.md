# Objetivo
El objetivo de esta parte del proyecto es lograr que se sienta cómodo codificando en una configuración distribuida donde necesita administrar las comunicaciones subyacentes entre los nodos. La tarea requiere que verifique la corrección de la misma al asegurarse que:
1. el número de mensajes que envía y recibe dentro del sistema coinciden, y
2. estos mensajes no han sido corrompidos en tránsito hacia el destinatario deseado.

# Protocolo
Se debe crear un conjunto de procesos **P** que se inician en diferentes combinaciones de host y puerto. Cada proceso, leerá información sobre este conjunto de procesos desde un archivo de texto `hosts.txt`, cada línea en el archivo contendrá la información del host y del puerto de cada proceso que comprende este conjunto. Un proceso nunca debe intentar conectarse consigo mismo.

Cada proceso participa en un conjunto de rondas. Cada ronda implica que un proceso se conecta a un proceso elegido al azar en el conjunto de procesos **P**. Todas las comunicaciones en el sistema se basarán en TCP. Una vez que se establece una conexión con un nodo aleatorio, el proceso emisor envía 5 mensajes al proceso al que se ha conectado. En cada mensaje se envía un entero aleatorio (positivo o negativo). Al final de cada ronda, la conexión del socket se cierra y el proceso repite la ronda eligiendo otro nodo al azar del conjunto **P**. Cada proceso iniciará 5000 de dichas rondas. El número de procesos se fijará al inicio del experimento. Es probable que se utilize entre 5-9 procesos para el entorno de prueba durante la calificación de la tarea.

Cada proceso mantendrá dos atributos enteros que se inicializan a cero: `controlEnvios` y `controlRecepciones`. El atributo `controlEnvios` representa la cantidad de mensajes enviados por un proceso y `controlRecepciones` mantiene información sobre la cantidad de mensajes que recibió ese proceso.

Cada proceso mantendrá dos atributos enteros adicionales que se inicializan a cero: `sumaEnvios` y `sumaRecepciones`. El atributo `sumaEnvios`, suma continuamente los valores de los números aleatorios que se envían como parte de cada mensaje, mientras que `sumaRecepciones` suma los valores enteros que se reciben. Los valores de `sumaEnvios` y `sumaRecepciones` en un proceso pueden ser positivos o negativos.

El número de rondas y la cantidad de mensajes enviados por ronda se deben pasar como argumentos al programa y deben ser atributos del mismo. Lo mismo aplica para los datos de host y puerto asociado al proceso y al procesode clasificación. Cada proceso debe implementar un hilo encargado del envío de mensajes e hilos que manejen las recepciones de mensajes de cada ronda (un hilo por ronda).

# Verificación de Correctitud #
El número total de mensajes que fueron enviados y recibidos por el conjunto de procesos **P** debe coincidir, es decir, la suma acumulada de `controlRecepciones` en cada proceso debe coincidir con la suma acumulada de `controlEnvios` en cada proceso. Se comprobará que los mensajes enviados no estén dañados, debido a un error en el código, al verificar que los valores acumulados de `sumaEnvios` coincidirá exactamente con los valores acumulados de `sumaRecepciones`.

# Clasificador de Salidas #
Para ayudar en el control, también se requiere que implemente un proceso de clasificación. Cada proceso enviará metadatos sobre la cantidad de mensajes recibidos y enviados junto con los totales de suma correspondientes. Los totales finales deben imprimirse en el colector de salidas como se muestra a continuación. 

*Ejemplo de salida*:

| Nodo | Nro. Mensajes Enviados | Nro. Mensajes Recibidos | Suma Mensajes Enviados | Suma Mensajes Recibidos |
|------|------------------------|-------------------------------|------------------------|----------------------------|
|host1:puerto1|..|..|..|..|
|host2:puerto2|..|..|..|..|
|..|..|..|..|..|
|hostN:puertoN|..|..|..|..|

Piense como puede implementar un totalizador general en el proceso de clasificación

# Bibliotecas de Terceros y Restricciones #
La aplicación debe implementarse utilizando los paquetes java.net.*, java.util.* y java.io.*, también pueden usar otros paquetes de utilidad del paquete central de Java siempre que no esté usando paquetes relacionados con RPC o frameworks de objetos distribuidos. Tampoco está permitido el uso de archivos jar externos.

Si usa objetos distribuidos, RPC o archivos jar externos para desarrollar la aplicación, tendra un puntaje de 0 (cero).

Puede discutir el proyecto con sus compañeros a nivel arquitectónico, pero la implementación del proyecto es un esfuerzo de cada grupo.
