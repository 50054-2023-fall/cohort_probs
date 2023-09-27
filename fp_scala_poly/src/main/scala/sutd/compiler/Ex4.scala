package sutd.compiler
import Ex3.*

object Ex4 {
    trait Foldable[T[_]]{
        def foldLeft[A,B](t:T[B])(acc:A)(f:(A,B)=>A):A
        // TODO: Add foldRight
    }

    given listFoldable:Foldable[List] = new Foldable[List] {
        def foldLeft[A,B](t:List[B])(acc:A)(f:(A,B)=>A):A = t.foldLeft(acc)(f)
        // TODO: Add foldRight
    }

    given bstFoldable: Foldable[BST] = 0 // TODO: Fixme
}