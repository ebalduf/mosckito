import org.mockito.stubbing.OngoingStubbing

import scala.reflect.ClassTag

package object mosckito {
  implicit def untag[T](tag: ClassTag[T]): Class[T] = tag.runtimeClass.asInstanceOf[Class[T]]

  implicit def unwrapStubber[T](stub: ScalaStubbing[T]): OngoingStubbing[T] = stub.ongoingStubbing


  /**
   * This is used to generate the 22 arity levels for function signatures for stubbing purposes
   * @param arity The function arity level to generate a stubbing helper for
   * @return String containing the stubbing helper function body with the specified arity level
   */
  private[this] def buildStub(arity: Int) = {
    def enlist(items: Stream[String]) = items take (arity) mkString (", ")
    def repeat[A](a: A) = Stream.iterate(a)(i => i)
    def increment[A](fn: Int => A) = Stream from (0) map (fn)
    val types = enlist(increment(i => s"T${i + 1}"))
    val args = enlist(increment(i => s"a($i)"))
    val matchers = enlist(repeat("?"))

    s"""def stub[$types, R](call: ($types) => R)(handler: ($types) => R) = safeStub(call($matchers), a => handler($args))"""
  }
}

/**
 *
 */
package mosckito {

import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.{Answer, OngoingStubbing}


/**
 *
 * @param values
 */
case class Args(values: Array[AnyRef]) {
  def apply[T](index: Int) = values(index).asInstanceOf[T]

  def get[T](index: Int) = apply(index)
}

/**
 *
 * @param stub
 * @tparam R
 */
class ScalaAnswer[R](stub: Args => R) extends Answer[R] {
  def arg[T](index: Int)(implicit invocation: InvocationOnMock) = invocation.getArguments.apply(index).asInstanceOf[T]

  override def answer(invocation: InvocationOnMock): R = apply(Args(invocation.getArguments))

  def apply(args: Args) = stub(args)
}

/**
 *
 * @param ongoingStubbing
 * @tparam T
 */
class ScalaStubbing[T](val ongoingStubbing: OngoingStubbing[T]) {
  type Result = this.type


}

/**
 *
 */
trait Mosckito {

  import org.mockito.{Matchers => Match, Mockito => Mock}

  def mock[T](implicit mockType: ClassTag[T]): T = Mock.mock(mockType)

  //when
  def when[T](call: T): OngoingStubbing[T] = new ScalaStubbing(Mock.when(call))

  def ?[T]: T = Match.any()

  def eq[T](expected: T): T = Match.eq(expected)

  //Stubbing helpers with arity levels 0-22
  object stub {
    def apply[R](call: () => R)(handler: => R) = safeStub(call(), a => handler)

    def apply[T1, R](call: (T1) => R)(handler: (T1) => R) = safeStub(call(?), a => handler(a(0)))

    def apply[T1, T2, R](call: (T1, T2) => R)(handler: (T1, T2) => R) = safeStub(call(?, ?), a => handler(a(0), a(1)))

    def apply[T1, T2, T3, R](call: (T1, T2, T3) => R)(handler: (T1, T2, T3) => R) = safeStub(call(?, ?, ?), a => handler(a(0), a(1), a(2)))

    def apply[T1, T2, T3, T4, R](call: (T1, T2, T3, T4) => R)(handler: (T1, T2, T3, T4) => R) = safeStub(call(?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3)))

    def apply[T1, T2, T3, T4, T5, R](call: (T1, T2, T3, T4, T5) => R)(handler: (T1, T2, T3, T4, T5) => R) = safeStub(call(?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4)))

    def apply[T1, T2, T3, T4, T5, T6, R](call: (T1, T2, T3, T4, T5, T6) => R)(handler: (T1, T2, T3, T4, T5, T6) => R) = safeStub(call(?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5)))

    def apply[T1, T2, T3, T4, T5, T6, T7, R](call: (T1, T2, T3, T4, T5, T6, T7) => R)(handler: (T1, T2, T3, T4, T5, T6, T7) => R) = safeStub(call(?, ?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5), a(6)))

    def apply[T1, T2, T3, T4, T5, T6, T7, T8, R](call: (T1, T2, T3, T4, T5, T6, T7, T8) => R)(handler: (T1, T2, T3, T4, T5, T6, T7, T8) => R) = safeStub(call(?, ?, ?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5), a(6), a(7)))

    def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](call: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => R)(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => R) = safeStub(call(?, ?, ?, ?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5), a(6), a(7), a(8)))

    def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](call: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R)(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R) = safeStub(call(?, ?, ?, ?, ?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5), a(6), a(7), a(8), a(9)))

    def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](call: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => R)(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => R) = safeStub(call(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5), a(6), a(7), a(8), a(9), a(10)))

    def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](call: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => R)(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => R) = safeStub(call(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5), a(6), a(7), a(8), a(9), a(10), a(11)))

    def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](call: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => R)(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => R) = safeStub(call(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5), a(6), a(7), a(8), a(9), a(10), a(11), a(12)))

    def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](call: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => R)(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => R) = safeStub(call(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5), a(6), a(7), a(8), a(9), a(10), a(11), a(12), a(13)))

    def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](call: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => R)(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => R) = safeStub(call(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5), a(6), a(7), a(8), a(9), a(10), a(11), a(12), a(13), a(14)))

    def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](call: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => R)(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => R) = safeStub(call(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5), a(6), a(7), a(8), a(9), a(10), a(11), a(12), a(13), a(14), a(15)))

    def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](call: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => R)(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => R) = safeStub(call(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5), a(6), a(7), a(8), a(9), a(10), a(11), a(12), a(13), a(14), a(15), a(16)))

    def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](call: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => R)(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => R) = safeStub(call(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5), a(6), a(7), a(8), a(9), a(10), a(11), a(12), a(13), a(14), a(15), a(16), a(17)))

    def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](call: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => R)(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => R) = safeStub(call(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5), a(6), a(7), a(8), a(9), a(10), a(11), a(12), a(13), a(14), a(15), a(16), a(17), a(18)))

    def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](call: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => R)(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => R) = safeStub(call(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5), a(6), a(7), a(8), a(9), a(10), a(11), a(12), a(13), a(14), a(15), a(16), a(17), a(18), a(19)))

    def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](call: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => R)(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => R) = safeStub(call(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5), a(6), a(7), a(8), a(9), a(10), a(11), a(12), a(13), a(14), a(15), a(16), a(17), a(18), a(19), a(20)))

    def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](call: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => R)(handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => R) = safeStub(call(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?), a => handler(a(0), a(1), a(2), a(3), a(4), a(5), a(6), a(7), a(8), a(9), a(10), a(11), a(12), a(13), a(14), a(15), a(16), a(17), a(18), a(19), a(20), a(21)))

    private[this] def safeStub[R](call: => R, stub: Args => R) = Mockito.when(call).thenAnswer(new ScalaAnswer(stub))
  }

}

}
