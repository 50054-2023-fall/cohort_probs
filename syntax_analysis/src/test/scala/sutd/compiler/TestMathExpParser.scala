package sutd.compiler

import org.scalatest.funsuite 
import org.scalatest.matchers
import sutd.compiler.MathExpToken.*
import sutd.compiler.MathExpParser.*
import sutd.compiler.BacktrackParsec.*



class TestMathExpParser extends funsuite.AnyFunSuite {
    import LToken.*
    import Result.*
    import ExpLE.*
    import TermLEP.*
    
    test("test_parse") {
        // val s = "1+2*3"
        val toks = List(IntTok(1), PlusTok, IntTok(2), AsterixTok, IntTok(3))
        val result = BacktrackParsec.run(parseExpLE)(toks)
        val expected = PlusExpLE(TermLE(Factor(1),Eps),TermExpLE(TermLE(Factor(2),MultTermLEP(Factor(3),Eps))))
        result match {
            case Ok((t, Nil)) =>  assert(t == expected)
            case _ => assert(false)
        }
  }
}