package com.samehadar.exchange

import com.samehadar.exchange.data._

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