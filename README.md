# CyclesFinder

## Taller de Java - Proyecto Final 
## Ciclos, ciclos y más ciclos! 

### Facultad de Cs. Exactas

INTEGRANTES:
Gerardi, Gregorio Nicolas [gregorionicolasgerardi@gmail.com]
Marcos, Roberto David [roberto.dmarcos@hotmail.com]




------------

Para encontrar los ciclos en un grafo incluimos dos estrategias distintas, cada una de las cuales será mas o menos eficiente dependiendo de la configuracion del grafo en el que buscar dependencias. Estos algoritmos son Johnson, y una busqueda DFS con set blancos gris y negro para marcar los nodos al visitarlos

Para grafos pequeños (menos de 50 arcos) No se aprecia una diferencia conciderable en los tiempos de ejecucion de ambos algoritmos.

En cambio para grafos extensos, la decicion de uno un otro algoritmo dependera del maximo largo de los ciclos que querramos buscar.
Cuando la longitud de los ciclos sea pequeña en relacion al numero de nodos del grafo, recomienda el uso de DFS: sets black-grey-white.
Cuando la longitud de los ciclos a buscar sea grande (aproximadamente 10 o mas en grafos de 50 o mas arcos), se recomienda el uso de Johnson.

La complejidad temporal del algoritmo de Jonhson para un caso sin longitud maxima es O(nc(n + e)) donde n es la cantidad de nodos, e la cantidad de arcos, c la cantidad de ciclos.

La complejidad temporal del algoritmo de DFS para un caso sin longitud maxima es O(n^n)

En casos donde uno especifica una cota maxima 'm' al largo de los ciclos las complejidades cambian a 
Johnson = O(mc(n + e)) 
DFS = O(n^m)

por esto en grafos donde el maximo m es pequeño y la cantidad de ciclos o la cantidad de arcos es alta el algoritmo de DFS pasa a tener un mejor desempeño.

se aprecia que para maximos pequeños, la complejidad de DFS es menor. Por ejemplo para un maximo de 4, conciderando que la cantidad de ciclos sea la de un grafo completamente conexo donde c = n^4 y e n^n
Johnson = O(4c(n + e)) = O(4cn+4ce) = O(4n^5+4(n^4)n^n) = O(n^n)
DFS = O(n^4)

Se entrega tambien una clase a modo de Benchmark que permite encontrar la curva a partir de la cual un grafo tiene demasiados nodos, demasiados arcos, o la extension del ciclo a buscar es demasiado grande para utilizar el algoritmo de DFS y se debe optar por el de Johnson.

las clases java cuentan con documentación javaDoc correspondiente cuando se concidero necesaria, los pseudocodigos de los algoritmos o referencias a donde fueron consultados, y clases de Test para las clases implementantes de los algoritmos.

-------------
##BONUS TRACK 
Para averiguar si algun ciclo pasa por dos vertices dados, la clase CycleInDirectedGraph dispone de un metodo findCycleThrough a tal fin, y en la carpeta de test se comprueba su funcionalidad.
