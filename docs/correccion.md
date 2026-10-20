# Informe de Corrección — Puntos 4 y 5

Fundamentos de Programación Funcional y Concurrente  
Universidad del Valle

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

**Teorema:** Para todo estado $e$ y toda orden $o$, ejecutar la orden original produce el mismo estado final que ejecutar la orden simplificada:
$$\text{ejecutar}(e, o) = \text{ejecutar}(e, \text{simplificar}(o))$$

### Demostración regla por regla:

1. **Regla 1 (`Avanzar(0)` y `Repetir(0, _)` $\to$ `Secuencia(Nil)`):**  
   Avanzar $0$ pasos deja las coordenadas $(x, y)$ intactas ($x \pm 0 = x, y \pm 0 = y$). Repetir $0$ veces no realiza acciones. Ambas dejan el estado sin alterar ($e$), exactamente igual que `Secuencia(Nil)`.

2. **Regla 2 (Dos `Avanzar` seguidos se juntan):**  
   Avanzar $n$ pasos y luego $m$ pasos en la misma dirección suma algebraicamente los desplazamientos: $y + n + m = y + (n + m)$. Por asociatividad de la suma, equivale a `Avanzar(n + m)`. Si $n + m = 0$, el desplazamiento neto es cero.

3. **Regla 3 (`Repetir(1, o)` $\to$ `o`):**  
   Repetir una vez un bloque de órdenes equivale a ejecutar dicho bloque una única vez.

4. **Regla 4 (`Secuencia(List(o))` $\to$ `o`):**  
   Una secuencia de un único elemento ejecuta exclusivamente esa orden; desenvolverla no altera el resultado.

5. **Regla 5 (Eliminar órdenes nulas de secuencias):**  
   Como las órdenes nulas actúan como la función identidad sobre el estado ($\text{ejecutar}(e, \text{noOp}) = e$), quitarlas de una secuencia no altera el estado final.

**Conclusión:** Cada regla individual preserva la equivalencia de ejecución. Por lo tanto, aplicarlas sucesivamente hasta el punto fijo garantiza que $\text{ejecutar}(e, o) = \text{ejecutar}(e, \text{simplificar}(o))$.
