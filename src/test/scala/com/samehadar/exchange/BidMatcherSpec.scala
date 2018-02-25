package com.samehadar.exchange

import org.scalatest.FlatSpec
import com.samehadar.exchange.BidMatcher._
import com.samehadar.exchange.data.{A, Bid, Client, Sell}
import com.samehadar.exchange.util.Parser
import org.scalatest.BeforeAndAfter

class BidMatcherSpec extends FlatSpec with BeforeAndAfter {

  import BidMatcher._
  var bids: List[Bid] = Parser.parseFile("one_match_case.txt", Parser.objBid)
  var clients: Map[String, Client] = Parser.parseFile("clients.txt", Parser.objClient).map{
    cl => cl.name -> cl
  }.toMap
  val bid = Bid("C0", Sell, A, 12, 4)

  "Empty bid set" should "do nothing" in {
    val clientsWithNoChanges = simpleMatch(Nil, clients)
    assert(clientsWithNoChanges === clients)
  }

  s"Bids list (${List(bid)}) with only one bid" should "change not clients state" in {
    val clientsWithNoChanges = simpleMatch(List(bid), clients)
    assert(clientsWithNoChanges === clients)
  }

  "Bids list with one matchable bids" should "correct change clients state" in {
    val oldClient1 = clients("C4") // buyer
    val newClient1 = oldClient1.copy(
      balance = oldClient1.balance - (bids.head.paperCount * bids.head.priceForOne),
      dCount = oldClient1.dCount + bids.head.paperCount
    )
    val oldClient2 = clients("C9") // seller
    val newClient2 = oldClient2.copy(
      balance = oldClient2.balance + (bids.head.paperCount * bids.head.priceForOne),
      dCount = oldClient2.dCount - bids.head.paperCount
    )
    val expectedClientsStates = clients + (oldClient1.name -> newClient1) + (oldClient2.name -> newClient2)
    val clientsWithNewStates = simpleMatch(bids, clients)
    assert(clientsWithNewStates === expectedClientsStates)
  }

}
