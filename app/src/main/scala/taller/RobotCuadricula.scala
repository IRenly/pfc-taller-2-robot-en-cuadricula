package taller

/**
 * Taller 2 — órdenes para un robot en una cuadrícula infinita.
 *
 * El tipo `Orden` es recursivo: `Repetir` y `Secuencia` contienen otras
 * órdenes. Toda función que lo recorra lo es también, con un caso por cada
 * alternativa del tipo.
 *
 * Tal como está, el proyecto compila y las pruebas quedan en rojo.
 */
class RobotCuadricula {

  /** El robot empieza en el origen mirando al norte. */
  val inicial = Estado(0, 0, Norte)

  // Punto 1 -------------------------------------------------------------------

  /** La dirección que queda tras girar noventa grados hacia el sentido dado. */
  def girar(d: Direccion, s: Sentido): Direccion = {

    (d, s) match{

      case (Norte, Izquierda) => Oeste
      case (Norte, Derecha) => Este
      case (Este, Izquierda) => Norte
      case (Este, Derecha) => Sur
      case (Sur, Izquierda) => Este
      case (Sur, Derecha) => Oeste
      case (Oeste, Izquierda) => Sur
      case (Oeste, Derecha) => Norte
    }
  }

  // Punto 2 -------------------------------------------------------------------

  /** El estado que resulta de aplicarle la orden al estado dado. */
  def ejecutar(e: Estado, o: Orden): Estado = {

    def runSecuencia(e: Estado, l: List[Orden]): Estado = {
      l match{
        case Nil => e
        case x :: Nil => ejecutar(e,x)
        case x :: xs => runSecuencia(ejecutar(e,x),xs)
      }
    }

    def runRepetir(e: Estado, o: Orden, n: Int): Estado = {
      if (n == 0) e
      else runRepetir(ejecutar(e,o),o,n-1)
    }

    (e, o) match{
      case (Estado(x,y,dir),Avanzar(n)) if dir == Norte => Estado(x,y+n,dir)
      case (Estado(x,y,dir),Avanzar(n)) if dir == Sur => Estado(x,y-n,dir)
      case (Estado(x,y,dir),Avanzar(n)) if dir == Este => Estado(x+n,y,dir)
      case (Estado(x,y,dir),Avanzar(n)) if dir == Oeste => Estado(x-n,y,dir)
      case (Estado(x,y,dir),Girar(sentido)) => Estado(x,y,girar(dir,sentido))
      case (e,Secuencia(List())) => e
      case (e,Secuencia(lista)) => runSecuencia(e,lista)
      case (e,Repetir(n,_)) if n < 1 => e
      case (e,Repetir(n,orden)) => runRepetir(e,orden,n)
    }
  }

  // Punto 3 -------------------------------------------------------------------

  /** Cuántos Avanzar y Girar llega a ejecutar la orden, contando repeticiones. */
  def cuantasBasicas(o: Orden): Int = ???

  /** Niveles de anidamiento: una básica tiene 1, una compuesta uno más que la más honda. */
  def profundidad(o: Orden): Int = ???

  // Punto 4 -------------------------------------------------------------------

  /** La secuencia de las dos órdenes. */
  def componer(o1: Orden, o2: Orden): Orden = {
    Secuencia(List(o1,o2))
  }

  /** Currificada: fijados los pasos y el giro, queda una función de las veces. */
  def rutina(pasos: Int)(giro: Sentido)(veces: Int): Orden = {
    Repetir(veces, Secuencia(List(Avanzar(pasos),Girar(giro))))
  }

  /** La misma orden ejecutada sobre cada uno de los estados. */
  def aplicarA(estados: List[Estado], o: Orden): List[Estado] = {
    estados match {
      case Nil => Nil
      case x :: xs => ejecutar(x, o) :: aplicarA(xs, o)
    }
  }

  /** Reemplaza cada orden básica por f(orden), respetando la estructura de las compuestas. */
  def transformar(o: Orden, f: Orden => Orden): Orden = {

    def transformarLista(lista: List[Orden]): List[Orden] = {
      lista match {
        case Nil => Nil
        case x :: xs => transformar(x, f) :: transformarLista(xs)
      }
    }

    o match {
      case a: Avanzar => f(a)
      case g: Girar   => f(g)
      case Repetir(veces, subOrden) =>
        Repetir(veces, transformar(subOrden, f))
      case Secuencia(lista) =>
        Secuencia(transformarLista(lista))
    }
  }

  // Punto 5 -------------------------------------------------------------------

  /** Una orden equivalente más corta, con las reglas del enunciado. */
  def simplificar(o: Orden): Orden = {

    def limpiarLista(l: List[Orden]): List[Orden] =
      l match {
      case Nil => Nil
      case Avanzar(0) :: xs => limpiarLista(xs)
      case Secuencia(Nil) :: xs => limpiarLista(xs)
      case Repetir(0, _) :: xs  => limpiarLista(xs)

      case Avanzar(n) :: Avanzar(m) :: xs =>
        if (n + m == 0) limpiarLista(xs)
        else limpiarLista(Avanzar(n + m) :: xs)
      case x :: xs => x :: limpiarLista(xs)
    }

    def simplificarElementos(l: List[Orden]): List[Orden] =
      l match {
      case Nil => Nil
      case x :: xs => paso(x) :: simplificarElementos(xs)
    }

    def paso(ord: Orden): Orden =
      ord match {
      case Girar(s) => Girar(s)
      case Avanzar(0) => Secuencia(Nil)
      case Avanzar(n) => Avanzar(n)
      case Repetir(0, _) => Secuencia(Nil)
      case Repetir(1, sub) => paso(sub)
      case Repetir(n, sub) => Repetir(n, paso(sub))
      case Secuencia(lista) =>
        val simplificados = simplificarElementos(lista)
        val limpios = limpiarLista(simplificados)
        limpios match {
          case Nil => Secuencia(Nil)
          case soloUno :: Nil => soloUno
          case varios => Secuencia(varios)
        }
    }

    val s = paso(o)
    if (s == o) s
    else simplificar(s)
  }
}
