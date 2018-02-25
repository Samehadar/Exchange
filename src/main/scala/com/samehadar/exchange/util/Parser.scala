package com.samehadar.exchange.util

import java.io.{File, PrintWriter}

import com.samehadar.exchange.data
import com.samehadar.exchange.data._

import scala.io.Source
import scala.util.parsing.combinator.JavaTokenParsers

object Parser extends JavaTokenParsers {

  private val clientNameR = """C-?\d+""".r
  private val exchangeR = "b|s".r
  private val paperR = "A|B|C|D".r

  def parseFile[T](resourceFile: String, objParser: Parser[T]): List[T] = {
    val src = Source.fromResource(resourceFile)

    src.getLines().flatMap { line =>
      parse(objParser, line) match {
        case Success(r, _) => println(r); Some(r)
        case Failure(msg, _) => println(msg); None
        case Error(msg, _) => println(msg); None
      }
    }.toList
  }

  def printToFile[T](objects: List[T], resourceFile: String): Unit = {
    //have no time to war with separator
    val writer = new PrintWriter(new File(getClass.getResource("/" + resourceFile).toURI))

    objects.foreach(obj => writer.println(obj.toString))

    writer.close()
  }

  def objClient: Parser[Client] =
    clientNameR ~
      '\t' ~ wholeNumber ~
      '\t' ~ wholeNumber ~
      '\t' ~ wholeNumber ~
      '\t' ~ wholeNumber ~
      '\t' ~ wholeNumber ^^ {
      case name ~ '\t' ~ balance ~ '\t' ~ aCount ~ '\t' ~ bCount ~ '\t' ~ cCount ~ '\t' ~ dCount =>
        Client(name, balance.toInt, aCount.toInt, bCount.toInt, cCount.toInt, dCount.toInt)
    }

  def objBid: Parser[Bid] =
    clientNameR ~
      '\t' ~ exchangeR ~
      '\t' ~ paperR ~
      '\t' ~ wholeNumber ~
      '\t' ~ wholeNumber ^^ {
      case name ~ '\t' ~ ex ~ '\t' ~ paper ~ '\t' ~ price ~ '\t' ~ count =>
        data.Bid(name, toExchange(ex), toPaper(paper), price.toInt, count.toInt)
    }

  def toExchange(exch: String): Exchange = exch match {
    case "s" => Sell
    case "b" => Buy
  }

  def toPaper(paper: String): Paper = paper match {
    case "A" => A
    case "B" => B
    case "C" => C
    case "D" => D
  }

}
