# Mosckito

```
                     ,-.
         `._        /  |        ,
            `--._  ,   '    _,-'
     _       __  `.|  / ,--'
      `-._,-'  `-. \ : /
           ,--.-.-`'.'.-.,_-
         _ `--'-'-;.'.'-'`--
     _,-' `-.__,-' / : \
                _,'|  \ `--._
  jrei     _,--'   '   .     `-.
         ,'         \  |        ` [1]
```

_Mo(s)ckito_ is a wrapper for _Mockito_ written, hopefully, for idiomatic use in _(S)cala_.

# Purpose

_Scala_ is interoperable with _Java_, so any _Java_ mocking frameworks are available in _Scala_, but that doesn't make them pleasant to use. Scala has a lot of 'nice' features that result in much more succinct code, and has excellent test support from the likes of _ScalaTest_ and _Specs2_. Both of these frameworks provide test layouts that are easy to dive into from another language. The mocking space, however, doesn't seem as well supported. [ScalaMock](http://scalamock.org) has excellent support for mocking, but has very specific requirements for setup ordering when used with _ScalaTest_, and feels a bit verbose at times.

_Mosckito_ is meant to expose the power and simplicity of _Mockito_ in _Scala_ with the language features we developers appreciate. And that's all it is meant to do.

# Usage

_Mosckito_ is meant to be a thin layer of _Scala_ goodness on top of _Mockito_, adding some type-safety and support for lambdas.

## Building

The project uses _SBT_, so if you have that installed, it should be as simple as:

```sh
> sbt package
```

This will produce a JAR file in `./target/scala-2.10/`.

## Dependencies

The only dependency is `mockito-all` version 1.9.5.

## In Code

Usage from _Scala_ is the whole point, so things are kept as simple as possible. To add support to a test class, just mix in the `Mosckito` trait:
```scala
class SomeTest extends Suite with Mosckito
```

There are only a handful of useful calls at this point:

* `def mock[T](implicit mockType: ClassTag[T]): T` - Delegates into `Mockito.mock(Class<T>)` with an implicit type tag
* `def ?[T]: T` - Delegates to `org.mockito.Matchers.<T>any()`
* `def eq[T](expected: T): T` - Delegates to `org.mockito.Matchers.eq(expected)`

The real usefulness shows up in the `Mosckito.stub` object, which provides stubbing methods for mock functions with arities between 0 and 22, or in other words, all arity levels supported by _Scala_. These are simply wrappers around `Mockito.when(obj.someMethod(any(), any(), ...)).thenAnswer([lambda])`, but each matches the signature of the function being mocked and takes a lambda with the same signature, handling the conversion to `Answer` and casting values from the arg array for you. This hopefully covers more than 80% of mocking use cases.

Here's an example of using a stub function:

```scala
//trait to mock
trait SomeType{
  def foo(): Int
  def bar(value: Int): Int
}

//builds the mock
val someMock = mock[SomeType]

//the first parameter is a partial application of the function to mock
//the 2nd parameter is a lambda (in this case, a value will suffice) to return
stub(someMock.foo _)(34)

//again, we partially apply the function to stub
//The stubbed function in this case is a lambda which matches the signature of SomeType#bar
stub(someMock.bar _){ value =>
    value * value
}

// -- Alternatively

//we can just as clearly express that we want to throw an exception for the call
stub(someMock.bar _){ v => throw new IllegalStateException("This shouldn't happen") }


... test code
```

Because the signatures are so simple, we can fit behavior for `OngoingStubbing[T]`'s `thenReturn(value: T)`, `thenAnswer(answer: Answer[T])` and `thenThrow(ex: Exception)` with the same stub method.

#### Acks

* [1] - Found @ www.retrojunkie.com/asciiart/animals/mosquito.htm
