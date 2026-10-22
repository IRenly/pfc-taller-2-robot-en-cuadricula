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

### 1. Corrección de `aplicarA` (Inducción Estructural sobre Listas)
Demostramos que `aplicarA(estados, o)` ejecuta la orden $o$ en cada robot preservando el orden de la lista:

- **Caso Base (`estados = Nil`):**  
  `aplicarA(Nil, o)` retorna `Nil`. Una lista vacía produce una lista vacía. Se cumple directamente.
- **Hipótesis de Inducción (HI):**  
  Suponemos que `aplicarA(xs, o)` ejecuta correctamente $o$ sobre la cola `xs`.
- **Paso Inductivo (`estados = x :: xs`):**  
  La función calcula `ejecutar(x, o) :: aplicarA(xs, o)`. Por la HI, la cola `xs` ya fue transformada; al anteponerle el estado procesado del primer robot `x`, la lista completa queda transformada en el orden correcto.

### 2. Corrección de `transformar` (Inducción Estructural sobre `Orden`)
Demostramos que `transformar(o, f)` aplica $f$ a las órdenes básicas respetando la estructura de las compuestas:

- **Casos Base (`Avanzar` y `Girar`):**  
  Al ser órdenes básicas, la función les aplica directamente $f$: retorna $f(o)$.
- **Hipótesis de Inducción (HI):**  
  Suponemos que `transformar` procesa correctamente las subórdenes internas de cualquier compuesta.
- **Paso Inductivo:**  
  - Para `Repetir(veces, sub)`: conserva el número de repeticiones y transforma `sub` por HI.
  - Para `Secuencia(lista)`: transforma cada elemento de la lista por HI y reconstruye la `Secuencia`.

---

## Punto 5: Teorema de Corrección de `simplificar`

**Teorema:** $\forall e \in \text{Estado}, \forall o \in \text{Orden} : \text{ejecutar}(e, o) = \text{ejecutar}(e, \text{simplificar}(o))$

**Demostración:** Cada regla de simplificación preserva el estado final:
- **Órdenes nulas:** `Avanzar(0)` y `Repetir(0, _)` no modifican posición ni dirección ($\text{ejecutar}(e, \text{noOp}) = e$).
- **Fusión de avances:** Por asociatividad de la suma, desplazamientos consecutivos se reducen a $\text{Avanzar}(n + m)$. Si $n + m = 0$, se anulan.
- **Desenvolvimiento:** `Repetir(1, o)` y `Secuencia(List(o))` ejecutan exactamente $o$ una sola vez.
- **Punto fijo:** Al aplicar sucesivamente transformaciones que preservan el estado hasta que no haya más cambios, la orden resultante produce el mismo estado que la original.
