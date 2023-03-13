package sutd.compiler

import scala.util.matching.Regex
import sutd.compiler.MathExpToken.* 
object MathExpLexerWithRegex {
    import LToken.*
    type Error = String

    def lex(src:String):Either[List[LToken], Error] = {
        def go(src:String, acc:List[LToken]):Either[List[LToken], Error] = {
            if (src.length == 0)  
            {
                Left(acc)
            } 
            else 
            {
                lex_one(src) match {
                    case Right(error) => Right(error)
                    case Left((ltoken, rest)) => go(rest, acc++List(ltoken))
                }
            }
        }
        go(src, List())
    }

    val integer = raw"(\d+)(.*)".r
    val plus = raw"(\+)(.*)".r
    val asterix = raw"(\*)(.*)".r

    import LToken.*
    def lex_one(src:String):Either[(LToken, String), Error] = src match {
        case integer(s, rest) => Left((IntTok(s.toInt), rest))
        case plus(_, rest) => Left((PlusTok, rest))
        case asterix(_, rest) => Left((AsterixTok, rest))
        case _ => Right(s"lexer error: unexpected token at ${src}")
    }

    val mathexpstr = "1+2*3"
}

