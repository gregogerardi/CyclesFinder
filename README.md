# CyclesFinder

## Taller de Java - Proyecto Final 
## Ciclos, ciclos y más ciclos! 

### Facultad de Cs. Exactas

INTEGRANTES:
Gerardi, Gregorio Nicolas [gregorionicolasgerardi@gmail.com]
Marcos, Roberto David [roberto.dmarcos@hotmail.com]




------------

La ejecucion del .jar debe seguir la siguiente estructura: java -jar nombreDelEjecutable rutaDelXml maxNumeroDeCiclos rutaSalida1 rutaSalida2 [algoritmoAEjecutar]

Donde nombreDelEjecutable es el nombre del .jar, rutaDelXml es la ruta donde se encuentra el archivo con las dependencias a analizar, maxNumeroDeCiclos es la cantidad máxima de ciclos que se desea analizar (como mínimo 3), rutaSalida1 es la ruta donde se almacenara el archivo de salida con todos los ciclos encontrados, rutaSalida2 es la ruta donde se almacenara el archivo de salida con la cantidad de ciclos para cada longitud analizada, y AlgoritmoAEjecutar da la posibilidad de elegir el algoritmo a utilizar.

Para encontrar los ciclos en un grafo incluimos dos estrategias distintas, cada una de las cuales será más o menos eficiente dependiendo de la configuración del grafo en el que buscar dependencias. Estos algoritmos son Johnson, y una búsqueda DFS con set blancos gris y negro para marcar los nodos al visitarlos

Para grafos pequeños (menos de 50 arcos) No se aprecia una diferencia considerable en los tiempos de ejecución de ambos algoritmos.
En cambio para grafos extensos, la decisión de uno u otro algoritmo dependerá del máximo largo de los ciclos que queramos buscar. Cuando la longitud de los ciclos sea pequeña en relación al número de nodos del grafo, recomienda el uso de DFS: sets black-grey-white. Cuando la longitud de los ciclos a buscar sea grande (aproximadamente 10 o más en grafos de 50 o más arcos), se recomienda el uso de Johnson.
La complejidad temporal del algoritmo de Jonhson para un caso sin longitud máxima es O(nc(n + e)) donde n es la cantidad de nodos, e la cantidad de arcos, c la cantidad de ciclos.

La complejidad temporal del algoritmo de DFS para un caso sin longitud máxima es O(n^n)

En casos donde uno especifica una cota máxima 'm' al largo de los ciclos las complejidades cambian a Johnson = O(mc(n + e)) DFS = O(n^m)
por esto en grafos donde el máximo m es pequeño y la cantidad de ciclos o la cantidad de arcos es alta el algoritmo de DFS pasa a tener un mejor desempeño.

se aprecia que para máximos pequeños, la complejidad de DFS es menor. Por ejemplo para un máximo de 4, considerando que la cantidad de ciclos sea la de un grafo completamente conexo donde c = n^4 y e n^n Johnson = O(4c(n + e)) = O(4cn+4ce) = O(4n^5+4(n^4)n^n) = O(n^n) DFS = O(n^4)

Se entrega también una clase a modo de Benchmark que permite encontrar la curva a partir de la cual un grafo tiene demasiados nodos, demasiados arcos, o la extensión del ciclo a buscar es demasiado grande para utilizar el algoritmo de DFS y se debe optar por el de Johnson.

Las clases java cuentan con documentación javaDoc correspondiente cuando se consideró necesaria, los pseudocodigos de los algoritmos o referencias a donde fueron consultados, y clases de Test para las clases que implementan dichos algoritmos.

-------------
## BONUS TRACK 
Para averiguar si algún ciclo pasa por dos vértices dados, la clase CycleInDirectedGraph dispone de un método findCycleThrough a tal fin, y en la carpeta de test se comprueba su funcionalidad.
