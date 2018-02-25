package com.samehadar.exchange

import com.samehadar.exchange.data._

import scala.util.parsing.combinator._

object Main extends App {
  import com.samehadar.exchange.util.Parser._

  val clients = parseFile("clients.txt", objClient).map{
    cl => cl.name -> cl
  }.toMap
  val bids: List[Bid] = parseFile("orders.txt", objBid) //todo:: change to order.txt
  println(clients.size)
  println(bids.size)


  val result = BidMatcher.simpleMatch(bids, clients).values
  println("Result clients set")
  println(result.mkString("\n"))

  printToFile(result.toList, "updatedClients.txt")
}


//todo:: remove all

class BidParser extends JavaTokenParsers {

  private val clientNameR = """C-?\d+""".r
  private val exchangeR = "b|s".r
  private val paperR = "A|B|C|D".r

  def obj: Parser[Bid] =
    clientNameR ~
      '\t' ~ exchangeR ~
      '\t' ~ paperR ~
      '\t' ~ wholeNumber ~
      '\t' ~ wholeNumber ^^ {
      case name ~ '\t' ~ ex ~ '\t' ~ paper ~ '\t' ~ price ~ '\t' ~ count =>
        data.Bid(name, toExchange(ex), toPaper(paper), price.toInt, count.toInt)
  }

  private[BidParser] def toExchange(exch: String): Exchange = exch match {
    case "s" => Sell
    case "b" => Buy
  }

  private[BidParser] def toPaper(paper: String): Paper = paper match {
    case "A" => A
    case "B" => B
    case "C" => C
    case "D" => D
  }

}

class JSON extends JavaTokenParsers {

  def obj: Parser[Map[String, Any]] = "{" ~> repsep(member, ",") <~ "}" ^^ (Map() ++ _)

  def arr: Parser[List[Any]] = "[" ~> repsep(value, ",") <~ "]"

  def member: Parser[(String, Any)] = stringLiteral ~ ":" ~ value ^^
    { case name ~ ":" ~ value => (name, value) }

  def value: Parser[Any] = (
      obj
    | arr
    | stringLiteral
    | floatingPointNumber ^^ (_.toDouble)
    | "null" ^^ (_ => null)
    | "true" ^^ (_ => true)
    | "false" ^^ (_ => false)
    )

}


class Arith extends JavaTokenParsers {
  def expr: Parser[Any] = term ~ rep("+" ~ term | "-" ~ term)

  def term: Parser[Any] = factor ~ rep("*" ~ factor | "/" ~ factor)

  def factor: Parser[Any] = floatingPointNumber | "(" ~ expr ~ ")"
}

