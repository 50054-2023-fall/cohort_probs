package sutd.compiler

import sutd.compiler.MathExpToken.*
import sutd.compiler.BacktrackParsec.*

object MathExpParser {



    /* grammar 4 before left recursion elimination 
    E::= T + E
    E::= T
    T::= T * F 
    T::= F
    F::= i
    */

    enum Exp {
        case TermExp(t:Term)
        case PlusExp(t:Term, e:Exp)
    }

    enum Term {
        case FactorTerm(f:Factor)
        case MultTerm(t:Term, f:Factor)
    }

    case class Factor(v:Int)

    /* after left recursion elimination
    E  ::= T + E
    E  ::= T
    T  ::= FT'
    T' ::= *FT'
    T' ::= epsilon
    F  ::=i
    */

    enum ExpLE {
        case TermExpLE(t:TermLE)
        case PlusExpLE(t:TermLE, e:ExpLE) 
    }
    
    case class TermLE(f:Factor, tp:TermLEP)

    enum TermLEP {
        case MultTermLEP(f:Factor, tp:TermLEP)
        case Eps
    }

    import Exp.*
    import ExpLE.*
    import Term.*
    import TermLEP.*
    import LToken.*

    def parseExpLE:Parser[LToken, ExpLE] = 
        choice(parsePlusExpLE)(parseTermExp)

    def parsePlusExpLE:Parser[LToken, ExpLE] = for {
        t <- parseTerm
        plus <- parsePlusTok
        e <- parseExpLE
    } yield PlusExpLE(t, e)

    def parseTermExp:Parser[LToken, ExpLE] = for {
        t <- parseTerm
    } yield TermExpLE(t)

    def parseTerm:Parser[LToken, TermLE] = for {
        f <- parseFactor
        tp <- parseTermP 
    } yield TermLE(f, tp)

    def parseTermP:Parser[LToken, TermLEP] = for {
        omt <- optional(parseMultTermP)
    } yield { omt match {
        case Right(_) => Eps
        case Left(t) => t
    }}
        

    def parseMultTermP:Parser[LToken, TermLEP] = for {
        asterix <- parseAsterixTok
        f <- parseFactor
        tp <- parseTermP
    } yield MultTermLEP(f, tp)

    def parseFactor:Parser[LToken, Factor] = for {
        i <- parseIntTok
        f <- someOrFail(i)( itok => itok match {
            case IntTok(v) => Some(Factor(v))
            case _         => None
        })("parseFactor() fail: expect to parse an integer token but it is not an integer.")
    } yield f

    def parsePlusTok:Parser[LToken, LToken] = sat ((x:LToken) => x match {
        case PlusTok => true
        case _       => false
    })


    def parseAsterixTok:Parser[LToken, LToken] = sat ((x:LToken) => x match {
        case AsterixTok => true
        case _          => false
    })

    def parseIntTok:Parser[LToken, LToken] = sat ((x:LToken) => x match {
        case IntTok(v) => true
        case _         => false
    })

    // converting from ExpLE tro Exp
    def fromExpLE(e:ExpLE):Exp = e match {
        case TermExpLE(t) => TermExp(fromTermLE(t))
        case PlusExpLE(t,e) => PlusExp(fromTermLE(t), fromExpLE(e))
    }

    /* 
    parse tree with left recursion
        T
       / \
      T   f
     / \
     f  f 

     parse tree with left recursion eliminated
      Tp
     / \
     f  Tp
       / \
       f  Tp
          / \
          f  eps
          
    */
    def fromTermLE(t:TermLE):Term = t match {
        case TermLE(f, tep) => fromTermLEP(FactorTerm(f))(tep)
    } 
    def fromTermLEP(t1:Term)(tp1:TermLEP):Term = tp1 match {
        case Eps => t1 
        case MultTermLEP(f2, tp2) => {
            val t2 = MultTerm(t1, f2)
            fromTermLEP(t2)(tp2)
        }
    }



    
}