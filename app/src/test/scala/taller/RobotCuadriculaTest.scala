package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RobotCuadriculaTest extends AnyFunSuite {

  val r = new RobotCuadricula()
  import r._

  val cuadrado = Repetir(4, Secuencia(List(Avanzar(1), Girar(Derecha))))

  // Punto 1 -------------------------------------------------------------------

  test("girar: Norte a la derecha da Este") { assert(girar(Norte, Derecha) == Este) }
  test("girar: Norte a la izquierda da Oeste") { assert(girar(Norte, Izquierda) == Oeste) }
  test("girar: Oeste a la derecha da Norte") { assert(girar(Oeste, Derecha) == Norte) }
  test("girar: Sur a la izquierda da Este") { assert(girar(Sur, Izquierda) == Este) }

  test("girar: cuatro giros al mismo lado vuelven al principio") {
    val cuatro = List(Derecha, Derecha, Derecha, Derecha)
      .foldLeft(Norte: Direccion)((d, s) => girar(d, s))
    assert(cuatro == Norte)
  }

  test("girar: un giro y su contrario se cancelan") {
    assert(girar(girar(Este, Derecha), Izquierda) == Este)
  }
  // casos de prueba propios:

  test("girar: Sur a la derecha da Oeste"){
    assert(girar(Sur,Derecha)== Oeste)
  }
  test("girar: Este a la izquierda da Norte"){
    assert(girar(Este, Izquierda)== Norte)
  }
  test ("girar: tres veces a la izquierda de Norte da Este"){
    val tres = List(Izquierda, Izquierda, Izquierda)
      .foldLeft(Norte: Direccion)((d, s) => girar(d, s))
    assert(tres == Este)
  }
  test ("girar: Este a la derecha da sur"){
    assert(girar(Este, Derecha)== Sur)
  }
  test ("girar: Oeste a la Izquierda da Sur"){
    assert(girar(Oeste,Izquierda) == Sur)
  }

  // Punto 2 -------------------------------------------------------------------

  test("ejecutar: avanzar tres al norte") {
    assert(ejecutar(inicial, Avanzar(3)) == Estado(0, 3, Norte))
  }

  test("ejecutar: una secuencia de avance, giro y avance") {
    assert(ejecutar(inicial, Secuencia(List(Avanzar(2), Girar(Derecha), Avanzar(4)))) ==
      Estado(4, 2, Este))
  }

  test("ejecutar: el cuadrado vuelve al punto de partida") {
    assert(ejecutar(inicial, cuadrado) == Estado(0, 0, Norte))
  }

  test("ejecutar: avanzar con paso negativo retrocede") {
    assert(ejecutar(inicial, Avanzar(-2)) == Estado(0, -2, Norte))
  }

  test("ejecutar: repetir cero veces no hace nada") {
    assert(ejecutar(inicial, Repetir(0, Avanzar(5))) == inicial)
  }

  test("ejecutar: la secuencia vacía no hace nada") {
    assert(ejecutar(inicial, Secuencia(Nil)) == inicial)
  }

  test("ejecutar: avanzar al sur disminuye y") {
    assert(ejecutar(Estado(5, 5, Sur), Avanzar(2)) == Estado(5, 3, Sur))
  }

  test("ejecutar: avanzar al oeste disminuye x") {
    assert(ejecutar(Estado(5, 5, Oeste), Avanzar(2)) == Estado(3, 5, Oeste))
  }

  // Punto 2 > Pruebas Adicionales

  test("ejecutar: Avance negativo al oeste aumenta x"){
    assert(ejecutar(Estado(-2, 5, Oeste), Avanzar(-3)) == Estado(1,5,Oeste))
  }

  test("ejecutar: Rectangulo vuelve al punto de partida"){
    val rectangulo = Repetir(2,Secuencia(List(Avanzar(2),Girar(Derecha),Avanzar(1),Girar(Derecha))))
    assert(ejecutar(inicial,rectangulo) == inicial)
  }

  test("ejecutar: Secuencia de Repetir(Giro, Avance, Giro, Avance) y termina con Avance(2), Giro"){
    assert(ejecutar(inicial,Secuencia( List(Repetir (3,Secuencia(List(Girar(Izquierda),Avanzar(1),Girar(Derecha),Avanzar(1))) ),Avanzar(2), Girar(Derecha)) )) == Estado(-3,5,Este))
  }

  test("ejecutar: Secuencia de Avance, Cuadrado y Avance"){
    assert(ejecutar(inicial, Secuencia(List(Avanzar(2),cuadrado,Avanzar(2)))) == Estado(0,4,Norte))
  }

  test("ejecutar: Repetir secuencia que tiene repeticion"){
    assert(ejecutar(Estado(2,2,Norte), Repetir(2, Secuencia(List(Repetir(3,Secuencia(List(Avanzar(1),Girar(Derecha)))),Avanzar(2))) )) == Estado(1,1,Sur))
  }

  // Punto 3 -------------------------------------------------------------------

  test("cuantasBasicas: una orden básica cuenta uno") {
    assert(cuantasBasicas(Avanzar(3)) == 1)
  }

  test("cuantasBasicas: el cuadrado ejecuta ocho básicas") {
    assert(cuantasBasicas(cuadrado) == 8)
  }

  test("cuantasBasicas: repetir cero veces no ejecuta nada") {
    assert(cuantasBasicas(Repetir(0, cuadrado)) == 0)
  }

  test("cuantasBasicas: la secuencia vacía no ejecuta nada") {
    assert(cuantasBasicas(Secuencia(Nil)) == 0)
  }

  test("profundidad: una orden básica tiene profundidad 1") {
    assert(profundidad(Avanzar(3)) == 1)
  }

  test("profundidad: una secuencia de básicas tiene profundidad 2") {
    assert(profundidad(Secuencia(List(Avanzar(1), Girar(Derecha)))) == 2)
  }

  test("profundidad: el cuadrado tiene profundidad 3") {
    assert(profundidad(cuadrado) == 3)
  }

  test("profundidad: la secuencia vacía tiene profundidad 1") {
    assert(profundidad(Secuencia(Nil)) == 1)
  }

  test("profundidad: manda la rama más honda") {
    assert(profundidad(Secuencia(List(Avanzar(1), cuadrado))) == 4)
  }

  // Punto 4 -------------------------------------------------------------------

  test("componer: deja las dos órdenes en secuencia") {
    assert(componer(Avanzar(1), Girar(Derecha)) ==
      Secuencia(List(Avanzar(1), Girar(Derecha))))
  }

  test("componer: ejecutar la compuesta es ejecutar una y después la otra") {
    assert(ejecutar(inicial, componer(Avanzar(2), Girar(Derecha))) ==
      ejecutar(ejecutar(inicial, Avanzar(2)), Girar(Derecha)))
  }

  test("rutina: currificada, el cuadrado de cuatro vuelve al origen") {
    val cuadradoDe = rutina(1)(Derecha) _
    assert(ejecutar(inicial, cuadradoDe(4)) == Estado(0, 0, Norte))
  }

  test("rutina: con los mismos argumentos construye el cuadrado del enunciado") {
    assert(rutina(1)(Derecha)(4) == cuadrado)
  }

  test("aplicarA: ejecuta la misma orden sobre varios estados") {
    assert(aplicarA(List(inicial, Estado(5, 5, Sur)), Avanzar(2)) ==
      List(Estado(0, 2, Norte), Estado(5, 3, Sur)))
  }

  test("aplicarA: sobre la lista vacía no hay nada que hacer") {
    assert(aplicarA(Nil, Avanzar(2)) == Nil)
  }

  test("transformar: duplicar los avances deja los giros como estaban") {
    val doble = transformar(cuadrado, {
      case Avanzar(n) => Avanzar(2 * n)
      case otra => otra
    })
    assert(doble == Repetir(4, Secuencia(List(Avanzar(2), Girar(Derecha)))))
  }

  test("transformar: con la identidad no cambia nada") {
    assert(transformar(cuadrado, (o: Orden) => o) == cuadrado)
  }

  // Punto 5 -------------------------------------------------------------------

  test("simplificar: dos avances seguidos se juntan") {
    assert(simplificar(Secuencia(List(Avanzar(2), Avanzar(3)))) == Avanzar(5))
  }

  test("simplificar: repetir una vez es la orden misma") {
    assert(simplificar(Repetir(1, Avanzar(4))) == Avanzar(4))
  }

  test("simplificar: avanzar cero desaparece de la secuencia") {
    assert(simplificar(Secuencia(List(Avanzar(0), Girar(Derecha)))) == Girar(Derecha))
  }

  test("simplificar: junta solo los avances contiguos") {
    assert(simplificar(Secuencia(List(Avanzar(1), Avanzar(2), Girar(Derecha), Avanzar(3)))) ==
      Secuencia(List(Avanzar(3), Girar(Derecha), Avanzar(3))))
  }

  test("simplificar: repetir cero veces no hace nada") {
    assert(simplificar(Repetir(0, Avanzar(9))) == Secuencia(Nil))
  }

  test("simplificar: dos avances que se cancelan desaparecen") {
    assert(simplificar(Secuencia(List(Avanzar(2), Avanzar(-2)))) == Secuencia(Nil))
  }

  test("simplificar: es idempotente") {
    val ordenes = List(cuadrado,
      Secuencia(List(Avanzar(1), Avanzar(2), Girar(Derecha), Avanzar(3))),
      Repetir(1, Secuencia(List(Avanzar(0), Avanzar(4)))),
      Secuencia(List(Secuencia(Nil), Repetir(0, Avanzar(3)))))
    assert(ordenes.forall(o => simplificar(simplificar(o)) == simplificar(o)))
  }

  test("simplificar: no cambia el resultado de ejecutar") {
    val estados = List(inicial, Estado(3, -2, Este), Estado(-1, 4, Sur))
    val ordenes = List(cuadrado,
      Secuencia(List(Avanzar(2), Avanzar(3))),
      Secuencia(List(Avanzar(1), Avanzar(2), Girar(Derecha), Avanzar(3))),
      Repetir(1, Avanzar(4)),
      Repetir(0, Avanzar(9)),
      Secuencia(List(Avanzar(0), Girar(Derecha))),
      Secuencia(List(Avanzar(2), Avanzar(-2))),
      Repetir(3, Secuencia(List(Repetir(1, Avanzar(2)), Girar(Izquierda)))))
    assert(ordenes.forall(o =>
      estados.forall(e => ejecutar(e, o) == ejecutar(e, simplificar(o)))))
  }

  test("simplificar: el anidamiento interior también se reduce") {
    assert(simplificar(Repetir(3, Repetir(1, Avanzar(2)))) == Repetir(3, Avanzar(2)))
  }

  // Pruebas adicionales - Punto 4 ---------------------------------------------

  test("componer: dos giros a la izquierda orienta al robot en sentido opuesto") {
    val mediaVuelta = componer(Girar(Izquierda), Girar(Izquierda))
    assert(mediaVuelta == Secuencia(List(Girar(Izquierda), Girar(Izquierda))))
    assert(ejecutar(Estado(0, 0, Norte), mediaVuelta) == Estado(0, 0, Sur))
  }

  test("rutina: trazo rectangular con giros hacia la izquierda") {
    val dosPasos = rutina(3)(Izquierda)(2)
    assert(dosPasos == Repetir(2, Secuencia(List(Avanzar(3), Girar(Izquierda)))))
  }

  test("rutina con cero repeticiones deja al robot inmóvil en su casilla") {
    val rutinaCero = rutina(5)(Derecha)(0)
    assert(rutinaCero == Repetir(0, Secuencia(List(Avanzar(5), Girar(Derecha)))))
    assert(ejecutar(Estado(2, 4, Este), rutinaCero) == Estado(2, 4, Este))
  }

  test("aplicarA rota en simultáneo varios robots en cuadrantes distintos") {
    val flota = List(Estado(0, 0, Norte), Estado(3, 3, Este), Estado(-2, 5, Sur))
    val esperados = List(Estado(0, 0, Oeste), Estado(3, 3, Norte), Estado(-2, 5, Este))
    assert(aplicarA(flota, Girar(Izquierda)) == esperados)
  }

  test("transformar genera una ruta espejo invirtiendo el sentido de los giros") {
    val ordenOriginal = Secuencia(List(Avanzar(4), Girar(Derecha)))
    val reflejada = transformar(ordenOriginal, {
      case Girar(Derecha) => Girar(Izquierda)
      case otra           => otra
    })
    assert(reflejada == Secuencia(List(Avanzar(4), Girar(Izquierda))))
  }

  // Pruebas adicionales - Punto 5 ---------------------------------------------

  test("simplificar: fusiona múltiples avances consecutivos en una única orden") {
    val variosAvances = Secuencia(List(Avanzar(1), Avanzar(2), Avanzar(3)))
    assert(simplificar(variosAvances) == Avanzar(6))
  }

  test("simplificar: avanzar cero pasos de forma directa se convierte en secuencia vacía") {
    assert(simplificar(Avanzar(0)) == Secuencia(Nil))
  }

  test("simplificar: cancela avance y retroceso contiguos preservando el resto de la trayectoria") {
    val orden = Secuencia(List(Avanzar(5), Girar(Izquierda), Avanzar(3), Avanzar(-3)))
    assert(simplificar(orden) == Secuencia(List(Avanzar(5), Girar(Izquierda))))
  }

  test("simplificar: colapsa en cascada repeticiones unitarias con secuencias redundantes") {
    val cascada = Repetir(1, Secuencia(List(Avanzar(4), Avanzar(0))))
    assert(simplificar(cascada) == Avanzar(4))
  }

  test("simplificar: orden compuesta optimizada produce la misma posición final") {
    val estadoInicial = Estado(-3, 7, Oeste)
    val rutaCompleja = Secuencia(List(
      Repetir(1, Avanzar(2)),
      Avanzar(3),
      Girar(Derecha),
      Repetir(0, Girar(Izquierda)),
      Avanzar(4),
      Avanzar(-1)
    ))
    val optimizada = simplificar(rutaCompleja)
    assert(ejecutar(estadoInicial, rutaCompleja) == ejecutar(estadoInicial, optimizada))
  }
}
