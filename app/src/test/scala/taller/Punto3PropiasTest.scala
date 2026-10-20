package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class Punto3PropiasTest extends AnyFunSuite {

  val r = new RobotCuadricula()
  import r._

  test("cuantasBasicas: las repeticiones anidadas se multiplican") {
    assert(cuantasBasicas(Repetir(3, Repetir(2, Avanzar(1)))) == 6)
  }

  test("cuantasBasicas: una secuencia dentro de otra suma sus básicas") {
    val o = Secuencia(List(
      Avanzar(1),
      Secuencia(List(Girar(Izquierda), Avanzar(2))),
      Girar(Derecha)))
    assert(cuantasBasicas(o) == 4)
  }

  test("cuantasBasicas: repetir una secuencia vacía no ejecuta nada") {
    assert(cuantasBasicas(Repetir(5, Secuencia(Nil))) == 0)
  }

  test("profundidad: tres repeticiones anidadas dan profundidad 4") {
    assert(profundidad(Repetir(2, Repetir(3, Repetir(4, Avanzar(1))))) == 4)
  }

  test("profundidad: la rama más honda puede ir al principio de la secuencia") {
    val o = Secuencia(List(
      Repetir(2, Secuencia(List(Avanzar(1)))),
      Avanzar(1),
      Girar(Derecha)))
    assert(profundidad(o) == 4)
  }
}