package sutd.compiler

import scala.util.matching.Regex
import sutd.compiler.JsonToken.*

object JsonLexerWithRegex {

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
    val string = raw"([^']*)(.*)".r
    val squote = raw"(')(.*)".r
    val lbracket = raw"(\[)(.*)".r
    val rbracket = raw"(\])(.*)".r
    val lbrace = raw"(\{)(.*)".r
    val rbrace = raw"(\})(.*)".r
    val colon = raw"(:)(.*)".r
    val comma = raw"(,)(.*)".r

    import LToken.*
    def lex_one(src:String):Either[(LToken, String), Error] = src match {
        // TODO: Exercise 1
        // more cases here.
        case _ => Right(s"lexer error: unexpected token at ${src}")
    }

    val jsonstr = "{'k1':1,'k2':[]}"
}
