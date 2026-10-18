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
  def cuantasBasicas(o: Orden): Int = o match {
    case Avanzar(_) => 1
    case Girar(_) => 1
    case Repetir(veces, orden) => veces*cuantasBasicas(orden)
    case Secuencia(ordenes) =>ordenes.map(cuantasBasicas).sum
  }


  /** Niveles de anidamiento: una básica tiene 1, una compuesta uno más que la más honda. */
  def profundidad(o: Orden): Int = o match {
    case Avanzar(_) => 1
    case Girar(_) => 1
    case Repetir(_, orden) => 1 + profundidad(orden)
    case Secuencia(ordenes) => 1 + ordenes.map(profundidad).foldLeft(0)((acu,actual)=>math.max(acu,actual))
  }

  // Punto 4 -------------------------------------------------------------------

  /** La secuencia de las dos órdenes. */
  def componer(o1: Orden, o2: Orden): Orden = ???

  /** Currificada: fijados los pasos y el giro, queda una función de las veces. */
  def rutina(pasos: Int)(giro: Sentido)(veces: Int): Orden = ???

  /** La misma orden ejecutada sobre cada uno de los estados. */
  def aplicarA(estados: List[Estado], o: Orden): List[Estado] = ???

  /** Reemplaza cada orden básica por f(orden), respetando la estructura de las compuestas. */
  def transformar(o: Orden, f: Orden => Orden): Orden = ???

  // Punto 5 -------------------------------------------------------------------

  /** Una orden equivalente más corta, con las reglas del enunciado. */
  def simplificar(o: Orden): Orden = ???
}
