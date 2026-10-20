# Ejemplo de informe de corrección

Fundamentos de Programación Funcional y Concurrente.
Documento realizado por el docente Juan Francisco Díaz.

## 1. Argumentar la corrección de programas recursivos

Sea $f : A \to B$ una función, y $A$ un conjunto definido recursivamente
(recordar la definición de Matemáticas Discretas I), como por ejemplo los
naturales o las listas.

Sea $P_f$ un programa recursivo (lineal o en árbol) desarrollado en Scala (o en
cualquier lenguaje de programación) hecho para calcular $f$:

```scala
def Pf(a: A): B = { // Pf recibe a de tipo A, y devuelve f(a) de tipo B
  ...
}
```

¿Cómo argumentar que $P_f(a)$ siempre devuelve $f(a)$ como respuesta? Es decir,
¿cómo argumentar que $P_f$ es correcto con respecto a su especificación?

La respuesta es sencilla: demostrando el siguiente teorema.

```math
\forall a \in A : P_f(a) == f(a)
```

Cuando uno tiene que demostrar que algo se cumple para todos los elementos de
un conjunto definido recursivamente, es natural usar inducción estructural. En
términos prácticos, esto significa demostrar que:

- Para cada valor básico $a$ de $A$, se tiene que $P_f(a) == f(a)$.
- Para cada valor $a \in A$ construido recursivamente a partir de otro(s)
  valor(es) $a' \in A$, se tiene que
  $P_f(a') == f(a') \rightarrow P_f(a) == f(a)$. (Esta es la hipótesis de
  inducción).

### Ejemplo: factorial recursivo

Sea $f : \mathbb{N} \to \mathbb{N}$ la función que calcula el factorial de un
número natural, es decir, $f(n) = n!$. Y sea $P_f$ el siguiente programa en
Scala:

```scala
def Pf(n: Int): Int = { // Pf recibe n de tipo Int, y devuelve n! de tipo Int
  if (n == 0) 1 else n * Pf(n - 1)
}
```

Vamos a demostrar que $\forall n \in \mathbb{N} : P_f(n) == n!$

**Caso base:** $n = 0$

```math
P_f(0) \rightarrow \text{if } (0 == 0)\ 1 \text{ else } 0 \ast P_f(-1) \rightarrow 1
```

Por otro lado, $f(0) = 0! = 1$. Entonces $P_f(0) == f(0)$.

**Caso de inducción:** $n = k + 1$, $k \geq 0$. Hay que demostrar:
$P_f(k) == f(k) \rightarrow P_f(k + 1) == f(k + 1)$

```math
P_f(k+1) \rightarrow \text{if } (k+1 == 0)\ 1 \text{ else } (k+1) \ast P_f(k) \rightarrow (k+1) \ast P_f(k)
```

Usando la hipótesis de inducción (HI):

```math
\rightarrow (k+1) \ast k! = (k+1)!
```

Por lo tanto, $P_f(k + 1) == f(k + 1)$.

Concluimos por inducción que $\forall n \in \mathbb{N} : P_f(n) == n!$

### Ejemplo: el máximo de una lista

Sea $f : \text{List}[\mathbb{N}] \to \mathbb{N}$ la función que calcula el
máximo de una lista de enteros positivos, no vacía. Y sea $P_f$ el siguiente
programa en Scala:

```scala
def maxLin(l: List[Int]): Int = {
  if (l.tail.isEmpty) l.head
  else math.max(maxLin(l.tail), l.head)
}
```

Demostraremos que:

```math
\forall n \in \mathbb{N} \setminus \{0\} : P_f(\text{List}(a_1, a_2, \ldots, a_n)) == f(\text{List}(a_1, a_2, \ldots, a_n))
```

**Caso base:** $n = 1$

```math
P_f(\text{List}(a_1)) \rightarrow \text{if } \text{List}(a_1).\text{tail.isEmpty then } \text{List}(a_1).\text{head else } \ldots \rightarrow \text{List}(a_1).\text{head} \rightarrow a_1
```

Por otro lado, $f(\text{List}(a_1)) = a_1$. Entonces
$P_f(\text{List}(a_1)) == f(\text{List}(a_1))$.

**Caso de inducción:** $n = k + 1$, $k \geq 1$. Se debe demostrar:

```math
P_f(\text{List}(b_1, b_2, \ldots, b_k)) == f(\text{List}(b_1, b_2, \ldots, b_k)) \rightarrow P_f(\text{List}(a_1, a_2, \ldots, a_{k+1})) == f(\text{List}(a_1, a_2, \ldots, a_{k+1}))
```

Empecemos por calcular qué devuelve $P_f$ usando el modelo de sustitución:

```math
P_f(L) \rightarrow \text{if } L.\text{tail.isEmpty then } L.\text{head else math.max}(P_f(L.\text{tail}), L.\text{head})
```

```math
\rightarrow \text{math.max}(P_f(\text{List}(a_2, \ldots, a_{k+1})), a_1)
```

Sea $b = P_f(\text{List}(a_2, \ldots, a_{k+1}))$; por la hipótesis de
inducción, $b = f(\text{List}(a_2, \ldots, a_{k+1}))$. Hay dos posibilidades:

- Si $\text{math.max}(b, a_1) = b$, entonces $b \geq a_1$ y
 $b == f(\text{List}(a_1, a_2, \ldots, a_{k+1}))$.
- Si $\text{math.max}(b, a_1) = a_1$, entonces $a_1 \geq b$ y
 $a_1 == f(\text{List}(a_1, a_2, \ldots, a_{k+1}))$.

Por lo tanto, $P_f(L) == f(L)$.

Concluimos por inducción que:

```math
\forall n \in \mathbb{N} \setminus \{0\} : P_f(\text{List}(a_1, a_2, \ldots, a_n)) == f(\text{List}(a_1, a_2, \ldots, a_n))
```

## 2. Argumentar la corrección de programas iterativos

Para argumentar la corrección de programas iterativos, se debe formalizar cómo
es la iteración. Esto implica definir:

- Cómo se representa un estado de la iteración, $s$.
- Cuál es el estado inicial, $s_0$.
- Cuál es el estado final (o cómo se reconoce que un estado es final): $s_f$.
- Qué condición (o predicado) cumple todo estado: $\text{Inv}(s)$ (invariante
  de la iteración).
- El mecanismo para pasar de un estado al siguiente: $\text{transformar}(s)$.
  Si $s_i$ es el estado $i$, entonces $\text{transformar}(s_i) = s_{i+1}$.

Un programa iterativo tiene la siguiente forma:

```scala
def Pf(a: A): B = { // Pf recibe a de tipo A, y devuelve f(a) de tipo B
  def Pf_iter(s: Estado): B =
    if (esFinal(s)) respuesta(s) else Pf_iter(transformar(s))
  Pf_iter(s0)
}
```

Demostración de corrección:

- $\text{Inv}(s_0)$: el estado inicial cumple la condición invariante.
- Si $(s_i \neq s_f \land \text{Inv}(s_i)) \rightarrow \text{Inv}(\text{transformar}(s_i))$:
  el nuevo estado cumple la condición invariante si el estado anterior la
  cumplía.
- De lo anterior se concluye $\text{Inv}(s_f)$, es decir, el estado final
  cumple la condición invariante. Luego,
  $\text{Inv}(s_f) \rightarrow \text{respuesta}(s_f) == f(a)$.
- Finalmente, demostrar que siempre se llega al estado final $s_f$. Esto
  implica que
  $P_f(a) == \text{iter}(s_0) == \text{respuesta}(s_f) == f(a)$.

### Ejemplo: factorial iterativo

Considere el siguiente programa iterativo en Scala para calcular la función
factorial:

```scala
def Pf(n: Int): Int = { // Pf recibe n de tipo Int, y devuelve n! de tipo Int
  def Pf_iter(i: Int, n: Int, ac: Int): Int =
    if (i > n) ac else Pf_iter(i + 1, n, i * ac)
  Pf_iter(1, n, 1)
}
```

Este programa implementa el siguiente proceso iterativo:

- Un estado $s = (i, n, ac)$.
- El estado inicial es $s_0 = (1, n, 1)$.
- $(i, n, ac)$ es final si $i > n$, o lo que es lo mismo, si $i = n + 1$.
- La invariante de ciclo es
  $\text{Inv}(i, n, ac) \equiv i \leq n + 1 \land ac = (i-1)!$.
  La invariante de ciclo es una relación que SIEMPRE se cumple en el ciclo.
- $\text{transformar}((i, n, ac)) = (i+1, n, i \ast ac)$.

Ahora, demostramos los puntos mencionados:

**1.** $\text{Inv}(s_0)$: el estado inicial cumple la condición invariante.

```math
s_0 = (1, n, 1) \implies 1 \leq n + 1 \land 1 = 0!
```

**2.** La invariante se mantiene con la transformación de estados,
$(s_i \neq s_f \land \text{Inv}(s_i)) \rightarrow \text{Inv}(\text{transformar}(s_i))$:

1. Primer cambio, $i = i + 1$, lo que implica $ac = ((i+1) - 1)! = i!$.
2. Segundo cambio, $ac = i \ast ac$, entonces $ac = (i - 1)! \ast i = i!$.
3. Como se puede ver en ambos cambios indicados en la transformación, la
   invariante se mantiene.

**3.** $\text{Inv}(s_f) \rightarrow \text{respuesta}(s_f) == f(a)$

```math
(n + 1 \leq n + 1) \land ac = ((n+1)-1)! \rightarrow ac == n!
```

**4.** En cada paso, la componente $i$ del estado incrementa, acercándose a $n+1$.
Después de $n$ iteraciones, se alcanza $n+1$.

Esto implica que $P_f(n) == \text{iter}(1, n, 1) == n!$

### Ejemplo: el máximo de una lista

Se desea calcular el máximo de una lista de enteros positivos, no vacía. Sea
$f : \text{List}[\mathbb{N}] \to \mathbb{N}$ la función que calcula ese valor.
Y sea $P_f$ el siguiente programa en Scala:

```scala
def maxIt(l: List[Int]): Int = {
  def maxAux(max: Int, l: List[Int]): Int = {
    if (l.isEmpty) max
    else maxAux(math.max(max, l.head), l.tail)
  }
  maxAux(l.head, l.tail)
}
```

Este programa implementa el siguiente proceso iterativo:

- Un estado $s = (max, l)$ donde $l = \text{List}(a_i, a_{i+1}, \ldots, a_k)$
  es una cola de $L$.
- El estado inicial es
  $s_0 = (L.\text{head}, L.\text{tail}) = (a_1, \text{List}(a_2, \ldots, a_k))$.
- $s = (max, l)$ es final si $l$ es vacía.
- $\text{Inv}(max, l) \equiv l = \text{List}(a_i, a_{i+1}, \ldots, a_k) \land max = f(\text{List}(a_1, a_2, \ldots, a_{i-1}))$.
- $\text{transformar}((max, l)) = (nmax, l.\text{tail})$ donde $nmax = max$ si
  $max \geq l.\text{head}$, y $nmax = l.\text{head}$ si no.

Demostración de los puntos:

**1.** $\text{Inv}(s_0)$: el estado inicial cumple la condición invariante.

```math
s_0 = (a_1, \text{List}(a_2, \ldots, a_k)) \implies a_1 = f(\text{List}(a_1))
```

**2.** $(s_i \neq s_f \land \text{Inv}(s_i)) \rightarrow \text{Inv}(\text{transformar}(s_i))$

```math
\neg\, l.\text{isEmpty} \land l = \text{List}(a_i, a_{i+1}, \ldots, a_k) \land max = f(\text{List}(a_1, a_2, \ldots, a_{i-1}))
```

```math
\rightarrow l.\text{tail} = \text{List}(a_{i+1}, \ldots, a_k) \land nmax = f(\text{List}(a_1, \ldots, a_i))
```

**3.** $\text{Inv}(s_f) \rightarrow \text{respuesta}(s_f) == f(a)$

```math
\text{Inv}((max, \text{List}())) \rightarrow max = f(\text{List}(a_1, \ldots, a_k))
```

**4.** En cada paso, la lista $l$ se reduce, acercándose a ser vacía. Después de
$k$ iteraciones, $l = \text{List}()$.

Esto implica que $P_f(L) == \text{maxAux}(L.\text{head}, L.\text{tail}) == f(L)$

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
