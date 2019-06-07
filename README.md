# CyclesFinder

## Taller de Java - Proyecto Final 
## Ciclos, ciclos y más ciclos! 

### Facultad de Cs. Exactas

INTEGRANTES:
Gerardi, Gregorio Nicolas [gregorionicolasgerardi@gmail.com]
Marcos, Roberto David [roberto.dmarcos@hotmail.com]




------------

Para encontrar los ciclos en un grafo incluimos dos estrategias distintas, cada una de las cuales será mas o menos eficiente dependiendo de la configuracion del grafo en el que buscar dependencias. 

Para grafos pequeños (aproximadamente menos de 80 arcos) se recomienda utilizar el algoritmo de Jonhson. 
En cambio para grafos extensos se recomienda el uso de DFS: sets black-grey-white
La eleccion de que algoritmo es mejor no depende unicamente de la cantidad de arcos, sino tambien de la longitud maxima deseada para los ciclos a encontrar y la relacion de este valor con el maximo largo posible de un ciclo (numero de nodos).

Se entrega tambien una clase a modo de Benchmark que permite encontrar la curva a partir de la cual un grafo tiene demasiados nodos, demasiados arcos, o la extension del ciclo a buscar es demasiado grande para utilizar el algoritmo de johnson y se debe optar por el DFS.

las clases java cuentan con documentación javaDoc correspondiente cuando se concidero necesaria, los pseudocodigos de los algoritmos o referencias a donde fueron consultados, y clases de Test para las clases implementantes de los algoritmos.

-------------

Para averiguar si algun ciclo pasa por dos vertices dados, la clase CycleInDirectedGraph dispone de un metodo findCycleThrough a tal fin, y en la carpeta de test se comprueba su funcionalidad.
