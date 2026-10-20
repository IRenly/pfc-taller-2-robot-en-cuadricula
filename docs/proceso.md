# Algoritmo Factorial con Recursión de Cola

## Definición del Algoritmo

```Scala
def factorial(n: Int): BigInt = {
  @annotation.tailrec
  def loop(x: Int, acumulador: BigInt): BigInt = {
    if (x <= 1) acumulador
    else loop(x - 1, acumulador * x)
  }
  loop(n, 1)
}
```

* La función `factorial` calcula el factorial de un número `n` utilizando **recursión de cola**.
* La función interna `loop` es la que hace la recursión:

  * Recibe dos parámetros:

    * `x`: el valor actual decreciente hasta llegar a 1.
    * `acumulador`: donde se guarda el resultado parcial en cada paso.
* El decorador `@annotation.tailrec` obliga a que la función sea optimizada como recursión de cola, es decir, **no se acumulan llamados en la pila**.

## Explicación paso a paso

### Caso base

```Scala
if (x <= 1) acumulador
```

Cuando `x` llega a `1`, la función retorna directamente el valor acumulado, evitando más llamadas.

### Caso recursivo

```Scala
loop(x - 1, acumulador * x)
```

En cada llamada:

* Se reduce el valor de `x` en 1.
* Se multiplica el acumulador por `x` y se pasa a la siguiente iteración.
* Como es recursión de cola, la llamada recursiva es la **última instrucción** en ejecutarse, lo que permite a Scala optimizar la pila.

---

## Llamados de pila en recursión de cola

Ejemplo:

```Scala
factorial(5)
```

### Paso 1: Llamada inicial

```Scala
loop(5, 1)
```

### Paso 2: Primera iteración

```Scala
loop(4, 5)   // acumulador = 1 * 5
```

### Paso 3: Segunda iteración

```Scala
loop(3, 20)  // acumulador = 5 * 4
```

### Paso 4: Tercera iteración

```Scala
loop(2, 60)  // acumulador = 20 * 3
```

### Paso 5: Cuarta iteración

```Scala
loop(1, 120) // acumulador = 60 * 2
```

### Paso 6: Caso base

```Scala
return 120
```

---

## Diferencia con recursión normal

* En **recursión normal** cada llamada queda en la pila esperando a que termine la siguiente, lo que puede causar desbordamiento si `n` es muy grande.
* En **recursión de cola**, el compilador transforma el proceso en un **bucle optimizado**, por lo que no se guarda cada llamada en la pila y el algoritmo puede ejecutarse para valores muy grandes sin problema.

---

## Ejemplo de uso

```Scala
val resultado = factorial(5)
println(resultado)  // 120
```

El resultado de `factorial(5)` es `120`.


## Diagrama de llamados de pila con recursión de cola

```mermaid
sequenceDiagram
    participant Main as factorial(5)
    participant L1 as loop(5, 1)
    participant L2 as loop(4, 5)
    participant L3 as loop(3, 20)
    participant L4 as loop(2, 60)
    participant L5 as loop(1, 120)

    Main->>L1: llamada inicial
    L1->>L2: tail call con (4, 5)
    L2->>L3: tail call con (3, 20)
    L3->>L4: tail call con (2, 60)
    L4->>L5: tail call con (1, 120)
    L5-->>Main: return 120
```
## Punto 3: `cuantasBasicas` y `profundidad`

### Definición

```scala
def cuantasBasicas(o: Orden): Int = o match {
  case Avanzar(_) => 1
  case Girar(_) => 1
  case Repetir(veces, orden) => veces * cuantasBasicas(orden)
  case Secuencia(ordenes)    => ordenes.map(cuantasBasicas).sum
}

def profundidad(o: Orden): Int = o match {
  case Avanzar(_) => 1
  case Girar(_) => 1
  case Repetir(_, orden)  => 1 + profundidad(orden)
  case Secuencia(ordenes) => 1 + ordenes.map(profundidad).foldLeft(0)((acu, actual) => math.max(acu, actual))
}
```

* Ambas son recursivas sobre la estructura de `Orden`: cada llamada recursiva se
  hace sobre una orden contenida en la actual, que es más pequeña, así que el
  proceso termina.
* `cuantasBasicas` cuenta las órdenes `Avanzar` y `Girar` que se ejecutan,
  contando cada repetición: `Repetir` multiplica y `Secuencia` suma.
* `profundidad` cuenta los niveles de anidamiento: una orden básica vale 1 y una
  compuesta vale uno más que la más honda de las que contiene.
* No usan `var`, bucles ni `return`.

### Traza de `cuantasBasicas` sobre el cuadrado

Sea `c = Repetir(4, Secuencia(List(Avanzar(1), Girar(Derecha))))`.

```text
cuantasBasicas(c)
= 4 * cuantasBasicas(Secuencia(List(Avanzar(1), Girar(Derecha))))              // caso Repetir
= 4 * List(cuantasBasicas(Avanzar(1)), cuantasBasicas(Girar(Derecha))).sum     // caso Secuencia
= 4 * List(1, 1).sum                                                           // casos básicos
= 4 * 2
= 8
```

### Traza de `profundidad` sobre el cuadrado

```text
profundidad(c)
= 1 + profundidad(Secuencia(List(Avanzar(1), Girar(Derecha))))                 // caso Repetir
= 1 + (1 + List(profundidad(Avanzar(1)), profundidad(Girar(Derecha))).foldLeft(0)(max))
= 1 + (1 + List(1, 1).foldLeft(0)(max))                                        // casos básicos
```

El `foldLeft` avanza así:

| Paso | `a` (acumulador) | `b` (elemento) | `max(a, b)` |
|---|---|---|---|
| 1 | 0 | 1 | 1 |
| 2 | 1 | 1 | 1 |

```text
= 1 + (1 + 1)
= 3
```

### Casos borde

* `cuantasBasicas(Secuencia(Nil)) = Nil.sum = 0`.
* `profundidad(Secuencia(Nil)) = 1 + Nil.foldLeft(0)(max) = 1 + 0 = 1`.
* `cuantasBasicas(Repetir(0, o)) = 0 * cuantasBasicas(o) = 0`.

### Diagrama: valores de cada nodo del cuadrado

```mermaid
graph TD
    A["Repetir(4, ...)<br/>cuantasBasicas = 8<br/>profundidad = 3"] --> B["Secuencia(...)<br/>cuantasBasicas = 2<br/>profundidad = 2"]
    B --> C["Avanzar(1)<br/>cuantasBasicas = 1<br/>profundidad = 1"]
    B --> D["Girar(Derecha)<br/>cuantasBasicas = 1<br/>profundidad = 1"]
```

