# Informe de Corrección 

Fundamentos de Programación Funcional y Concurrente  
Universidad del Valle

---

## Punto 3: corrección de `cuantasBasicas` y `profundidad`

Como `Orden` está definido por sus cuatro alternativas, la inducción es
estructural: hay un caso base por cada orden básica (`Avanzar`, `Girar`) y un
caso de inducción por cada compuesta (`Repetir`, `Secuencia`), en los que se
supone la propiedad de las órdenes contenidas. Se supone $k \geq 0$ en
`Repetir(k, o)`.

### 3.1 `cuantasBasicas`

**Especificación.** Sea $B(o)$ el número de órdenes básicas que se ejecutan al
correr $o$, contando cada repetición. Por cómo se ejecuta cada orden (`Repetir`
ejecuta $k$ veces la orden que contiene, `Secuencia` ejecuta cada orden en
turno):

```math
\begin{aligned}
B(\text{Avanzar}(n)) &= 1 \\
B(\text{Girar}(s)) &= 1 \\
B(\text{Repetir}(k, o')) &= k \cdot B(o') \\
B(\text{Secuencia}(o_1, \ldots, o_n)) &= \sum_{i=1}^{n} B(o_i)
\end{aligned}
```

**Teorema.** $\forall o \in \text{Orden} : \text{cuantasBasicas}(o) == B(o)$

**Casos base.**

```math
\text{cuantasBasicas}(\text{Avanzar}(n)) \rightarrow 1 = B(\text{Avanzar}(n))
```

```math
\text{cuantasBasicas}(\text{Girar}(s)) \rightarrow 1 = B(\text{Girar}(s))
```

**Caso `Repetir`.** Hipótesis de inducción (HI): $\text{cuantasBasicas}(o') == B(o')$.

```math
\text{cuantasBasicas}(\text{Repetir}(k, o')) \rightarrow k \cdot \text{cuantasBasicas}(o')
\overset{HI}{=} k \cdot B(o') = B(\text{Repetir}(k, o'))
```

**Caso `Secuencia`.** HI: $\text{cuantasBasicas}(o_i) == B(o_i)$ para cada $o_i$ de la lista.

```math
\text{cuantasBasicas}(\text{Secuencia}(o_1, \ldots, o_n)) \rightarrow
\text{List}(\text{cuantasBasicas}(o_1), \ldots, \text{cuantasBasicas}(o_n)).\text{sum}
\overset{HI}{=} \sum_{i=1}^{n} B(o_i) = B(\text{Secuencia}(o_1, \ldots, o_n))
```

Si $n = 0$, la suma de la lista vacía es $0$, que coincide con la suma vacía
$B(\text{Secuencia}(\text{Nil})) = 0$.

**Terminación.** Cada llamada recursiva se hace sobre una orden contenida en la
actual, con menos constructores. Como toda orden es finita, se llega a una
orden básica y el proceso termina.

Concluimos por inducción que $\forall o \in \text{Orden} : \text{cuantasBasicas}(o) == B(o)$.

### 3.2 `profundidad`

**Especificación.** Sea $P(o)$ la profundidad de anidamiento de $o$, con
$\max \emptyset = 0$:

```math
\begin{aligned}
P(\text{Avanzar}(n)) &= P(\text{Girar}(s)) = 1 \\
P(\text{Repetir}(k, o')) &= 1 + P(o') \\
P(\text{Secuencia}(o_1, \ldots, o_n)) &= 1 + \max\{0, P(o_1), \ldots, P(o_n)\}
\end{aligned}
```

**Lema.** Si $l = \text{List}(a_1, \ldots, a_n)$ con $a_i \in \mathbb{N}$, entonces
$l.\text{foldLeft}(0)(\text{math.max}) = \max\{0, a_1, \ldots, a_n\}$.

Se demuestra con el método de los programas iterativos:

- Estado $s = (acum, resto)$, con estado inicial $s_0 = (0, l)$.
- $s$ es final si $resto$ es vacía.
- $\text{transformar}((acum, h :: t)) = (\text{math.max}(acum, h), t)$.
- Invariante: $\text{Inv}(acum, resto) \equiv resto = \text{List}(a_i, \ldots, a_n) \land acum = \max\{0, a_1, \ldots, a_{i-1}\}$.

**1.** $\text{Inv}(s_0)$: con $i = 1$, $acum = 0 = \max\{0\}$.

**2.** Si $\text{Inv}(acum, resto)$ y $resto \neq \text{Nil}$, entonces el nuevo acumulador es
$\text{math.max}(\max\{0, a_1, \ldots, a_{i-1}\}, a_i) = \max\{0, a_1, \ldots, a_i\}$
y el nuevo resto es $\text{List}(a_{i+1}, \ldots, a_n)$. Se mantiene $\text{Inv}$.

**3.** En el estado final, $resto$ es vacía ($i = n + 1$), así que $acum = \max\{0, a_1, \ldots, a_n\}$.

**4.** En cada paso $resto$ pierde un elemento; tras $n$ pasos queda vacía, y el proceso termina.

**Teorema.** $\forall o \in \text{Orden} : \text{profundidad}(o) == P(o)$

**Casos base.** $\text{profundidad}(\text{Avanzar}(n)) \rightarrow 1 = P(\text{Avanzar}(n))$, e igual para `Girar`.

**Caso `Repetir`.** HI: $\text{profundidad}(o') == P(o')$.

```math
\text{profundidad}(\text{Repetir}(k, o')) \rightarrow 1 + \text{profundidad}(o')
\overset{HI}{=} 1 + P(o') = P(\text{Repetir}(k, o'))
```

**Caso `Secuencia`.** HI: $\text{profundidad}(o_i) == P(o_i)$ para cada $o_i$.

```math
\text{profundidad}(\text{Secuencia}(o_1, \ldots, o_n)) \rightarrow
1 + \text{List}(\text{profundidad}(o_1), \ldots, \text{profundidad}(o_n)).\text{foldLeft}(0)(\text{math.max})
```

```math
\overset{HI}{=} 1 + \text{List}(P(o_1), \ldots, P(o_n)).\text{foldLeft}(0)(\text{math.max})
\overset{Lema}{=} 1 + \max\{0, P(o_1), \ldots, P(o_n)\} = P(\text{Secuencia}(o_1, \ldots, o_n))
```

Con $n = 0$ el resultado es $1 + 0 = 1$, que coincide con la especificación.

**Terminación.** Igual que en `cuantasBasicas`: cada llamada recursiva es sobre una orden contenida en la actual.

Concluimos por inducción que $\forall o \in \text{Orden} : \text{profundidad}(o) == P(o)$.

---

## Punto 4: Rutinas con Funciones de Alto Orden

### 1. Composición y Currificación (`componer` y `rutina`)
- **`componer(o1, o2)`:** Combina dos órdenes asociándolas secuencialmente en `Secuencia(List(o1, o2))`. Por definición de `ejecutar`, la secuencia evalúa $o_1$ sobre el estado inicial y el estado resultante es inmediatamente procesado por $o_2$.
- **`rutina(pasos)(giro)(veces)`:** Utiliza **currificación** (técnica de alto orden) dividiendo la recepción de parámetros en múltiples listas de argumentos:
  $$\text{rutina} : \text{Int} \to \text{Sentido} \to \text{Int} \to \text{Orden}$$
  Esto permite la aplicación parcial (fijar los pasos y el sentido de giro para generar una plantilla reutilizable de orden, completándola luego con el número de repeticiones). Retorna `Repetir(veces, Secuencia(List(Avanzar(pasos), Girar(giro))))`.

### 2. Corrección de `aplicarA` (Inducción Estructural sobre Listas)

**Especificación:** Sea $L = [e_1, e_2, \ldots, e_n]$ una lista de estados de robots y $o$ una orden. Queremos demostrar que:
$$\text{aplicarA}(L, o) = [\text{ejecutar}(e_1, o), \text{ejecutar}(e_2, o), \ldots, \text{ejecutar}(e_n, o)]$$

- **Caso Base ($L = \text{Nil}$):**  
  Por definición de la función:
  $$\text{aplicarA}(\text{Nil}, o) \to \text{Nil}$$
  Una flota sin robots produce una lista vacía, cumpliendo directamente la especificación para $n = 0$.

- **Hipótesis de Inducción (HI):**  
  Suponemos que para una lista de estados $xs$ de longitud $k$, $\text{aplicarA}(xs, o)$ aplica correctamente la orden $o$ a cada estado conservando su cardinalidad y orden posicional.

- **Paso Inductivo ($L = x :: xs$):**  
  Aplicando la definición recursiva del programa:
  $$\text{aplicarA}(x :: xs, o) \to \text{ejecutar}(x, o) :: \text{aplicarA}(xs, o)$$
  Por la HI, $\text{aplicarA}(xs, o)$ evalúa la cola $xs$ transformando cada uno de sus elementos. Al anteponer con `::` el estado resultante de transformar la cabeza $\text{ejecutar}(x, o)$, la lista resultante tiene longitud $k + 1$, preserva el orden estricto de los robots y aplica $o$ sobre cada uno de ellos.

- **Terminación:**  
  En cada paso recursivo se pasa la cola de la lista (`xs`), reduciendo la longitud de la entrada en 1. Como toda lista en Scala es de longitud finita, el algoritmo alcanza necesariamente el caso base `Nil` en exactamente $n$ pasos.

### 3. Corrección de `transformar` (Inducción Estructural sobre `Orden`)

**Especificación:** La función $\text{transformar}(o, f)$ es un homomorfismo sobre el tipo de datos algebraico inductivo `Orden`. Aplica una función transformadora $f : \text{Orden} \to \text{Orden}$ exclusivamente sobre las órdenes básicas (hojas del árbol sintáctico), conservando la estructura y parámetros de las órdenes compuestas (`Repetir` y `Secuencia`).

- **Casos Base (Órdenes Básicas):**
  - Para $\text{Avanzar}(n)$: al ser básica, el pattern matching aplica $f$ directamente retornando $f(\text{Avanzar}(n))$.
  - Para $\text{Girar}(s)$: análogamente, se evalúa $f(\text{Girar}(s))$. En ambos casos base la propiedad se satisface de forma inmediata.

- **Hipótesis de Inducción (HI):**  
  Para cualquier suborden $o'$ cuya profundidad o número de nodos sea menor que el de $o$, suponemos que $\text{transformar}(o', f)$ aplica correctamente $f$ a todas sus órdenes básicas internas respetando su jerarquía.

- **Paso Inductivo (Órdenes Compuestas):**
  - **Caso $\text{Repetir}(k, sub)$:**  
    Por definición:
    $$\text{transformar}(\text{Repetir}(k, sub), f) \to \text{Repetir}(k, \text{transformar}(sub, f))$$
    El número de iteraciones $k$ se preserva y, por la HI, la suborden $sub$ queda transformada correctamente en su interior.
  - **Caso $\text{Secuencia}(lista)$:**  
    Por definición:
    $$\text{transformar}(\text{Secuencia}(lista), f) \to \text{Secuencia}(\text{transformarLista}(lista))$$
    La función auxiliar interna `transformarLista` aplica inducción estructural sobre listas:
    $$\text{transformarLista}(x :: xs) \to \text{transformar}(x, f) :: \text{transformarLista}(xs)$$
    Por la HI, cada orden $x$ de la secuencia es procesada por `transformar` y recombinada en el mismo orden temporal, conservando la lista completa dentro del constructor `Secuencia`.

- **Terminación:**  
  Cada llamada recursiva se realiza sobre subórdenes contenidas con menor número de constructores y menor profundidad. Dado que toda `Orden` es un árbol sintáctico finito, el proceso desciende hasta las hojas básicas y termina deterministamente.

---

## Punto 5: Teorema de Corrección de `simplificar`

**Teorema:** Para todo estado $e \in \text{Estado}$ y para toda orden $o \in \text{Orden}$, ejecutar la orden original produce exactamente el mismo estado final que ejecutar la orden simplificada:
$$\text{ejecutar}(e, o) = \text{ejecutar}(e, \text{simplificar}(o))$$

### 1. Estrategia de la demostración
La función `simplificar` aplica pasadas de optimización sucesivas hasta alcanzar un **punto fijo** (es decir, una orden irreducible donde $\text{pasada}(o) = o$). 

Por la transitividad de la igualdad, si cada regla de reducción local $o \to o'$ preserva la semántica del estado final ($\text{ejecutar}(e, o) = \text{ejecutar}(e, o')$), entonces cualquier secuencia finita de transformaciones preserva el resultado hasta alcanzar el punto fijo.

### 2. Demostración por familias de reglas

1. **Eliminación de órdenes neutras:**
   - Para $\text{Avanzar}(0)$: por definición de `ejecutar`, avanzar $0$ pasos suma algebraicamente $0$ a las coordenadas actuales ($x \pm 0 = x, y \pm 0 = y$), manteniendo el estado inalterado:
     $$\text{ejecutar}((x, y, d), \text{Avanzar}(0)) = (x, y, d)$$
   - Para $\text{Repetir}(0, o)$: repetir cero veces no realiza ninguna acción y retorna el estado intacto $e$.
   - Ambas órdenes actúan como la función identidad sobre el estado ($\text{ejecutar}(e, \text{noOp}) = e$). Por lo tanto, eliminarlas de una secuencia no altera el resultado acumulado del pliegue de ejecución.

2. **Fusión y cancelación de avances consecutivos:**
   - Cuando aparecen dos órdenes consecutivas $\text{Avanzar}(n)$ y $\text{Avanzar}(m)$ dentro de una secuencia, ambas se ejecutan en la misma orientación actual $d$. Por asociatividad de la suma en las coordenadas cartesianas:
     $$(y + n) + m = y + (n + m)$$
   - En consecuencia, ejecutar ambas órdenes en secuencia produce el mismo efecto que una única orden $\text{Avanzar}(n + m)$:
     $$\text{ejecutar}(e, \text{Secuencia}(\text{List}(\text{Avanzar}(n), \text{Avanzar}(m)))) = \text{ejecutar}(e, \text{Avanzar}(n + m))$$
   - En el caso particular donde $n + m = 0$ (avances opuestos que se cancelan), la orden resultante es $\text{Avanzar}(0)$, que se anula por la regla de órdenes neutras sin alterar la posición final.

3. **Desenvolvimiento de estructuras unitarias:**
   - $\text{Repetir}(1, o)$: ejecutar un ciclo de una sola iteración consiste exactamente en evaluar $o$ una vez sobre el estado actual, por lo que $\text{ejecutar}(e, \text{Repetir}(1, o)) = \text{ejecutar}(e, o)$.
   - $\text{Secuencia}(\text{List}(o))$: una secuencia con un único elemento ejecuta el pliegue sobre una lista unitaria, lo cual evalúa directamente $\text{ejecutar}(e, o)$. Desenvolver la lista no modifica la trayectoria ni el estado final.

4. **Preservación en estructuras compuestas:**
   - Para $\text{Repetir}(k, o)$ y $\text{Secuencia}(ordenes)$, la simplificación desciende recursivamente a sus subórdenes. Por hipótesis de inducción estructural, si cada suborden interna simplificada preserva su semántica de ejecución individual, la composición secuencial o iterativa de las mismas produce el mismo estado global.

### 3. Terminación y Convergencia
Cada pasada de simplificación reduce la longitud de las listas en secuencias, elimina constructores redundantes o fusiona nodos contiguos. Como toda orden $o$ es un árbol sintáctico finito, la cantidad de reducciones posibles es estrictamente finita. El algoritmo converge de manera determinista hacia una orden canónica irreducible $o^*$, garantizando que:
$$\text{ejecutar}(e, o) = \text{ejecutar}(e, o^*)$$
