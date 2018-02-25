package com.samehadar.exchange

import com.samehadar.exchange.data._

object Main extends App {
  import com.samehadar.exchange.util.Parser._
  import com.samehadar.exchange.util.FileUtils._

  val clients = parseFile("clients.txt", objClient).map{
    cl => cl.name -> cl
  }.toMap
  val bids: List[Bid] = parseFile("orders.txt", objBid)

  val result = BidMatcher.simpleMatch(bids, clients).values.toList.sorted
  val result2 = BidMatcher.aboveZeroMatch(bids, clients).values.toList.sorted

  saveToWorkingDirectory(result, "result.txt")
  saveToTargetDirectory(result, "result.txt")

  saveToWorkingDirectory(result2, "result2.txt")
  saveToTargetDirectory(result2, "result2.txt")
}