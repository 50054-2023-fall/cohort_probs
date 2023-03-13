% Applicative and Monad


# Learning Outcomes



1. Describe and define derived type class
2. Describe and define Applicative Functors
3. Describe and define Monads
4. Apply Monad to in design and develop highly modular and resusable software.


# Exercise 1 


Define the type class instance `Functor[Option]`. Then verify that it satisfies the Functor Laws.




# Exercise 2

Given the following type lambda of a pair type.
```scala
type PP = [B] =>> [A] =>> (A,B)
```

Define the functor of instance of `Functor[PP[C]]`.





# Exercise 3

Show that for all Applicative instance $a$ satisfying the Applicative Laws implies that $a$ satisfies the Functor Laws.


# Exercise 4

Consider the following data type, complete the implementation of `map` and `flatMap`.

```scala
case class Mk[S,A]( f : (S =>(S, Option[A]) )) {
    def map[B](f:A=>B):Mk[S,B] = // TODO
    def flatMap[B](f:A=>Mk[S,B]):Mk[S,B] = // TODO
}
```



# Exercise 5

Continue from the previouse quesiton, 

```scala
type MkM = [S] =>> [A] =>> Mk[S,A]
```

Define a derived type class `MkMonad[S]` that extends `Monad[MkM[S]]`

```scala
trait MkMonad[S] extends Monad[MkM[S]] {
    // TODO
}
```



# Exercise 6


Consider the following code

```scala
enum BTree[+A] {
    case Empty
    case Node(v:A, lft:BTree[A], rght:BTree[A])
}


```

TODO: use reader monad to print the tree?

```
    4
  2
1   5
  3

```