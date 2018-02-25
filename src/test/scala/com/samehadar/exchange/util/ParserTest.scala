package com.samehadar.exchange.util

import com.samehadar.exchange.data._
import org.scalatest.FlatSpec

class ParserTest extends FlatSpec {

  import Parser._

  val testBid = Bid("C6", Sell, B,	5,	2)
  val testBidsList = List(
    Bid("C2", Sell,	C,	14,	5),
    Bid("C2",	Sell,	C,	13,	2),
    Bid("C9",	Sell,	D,	5,	4),
    Bid("C4",	Buy,	D,	5,	4),
    Bid("C6",	Sell,	B,	5,	2)
  )
  val testClient = Client("C0",	1000,	130,	240,	760,	320)
  val testClientsList = List(
    Client("C1", 1000, 130, 240, 760, 320),
    Client("C2", 4350, 370, 120, 950, 560),
    Client("C3", 2760, 0, 0, 0, 0)
  )

  it should s"parse equals bid ($testBid)" in {
    val bids: List[Bid] = parseFile("solo_bid.txt", objBid)
    assert(bids === List(testBid))
  }

  it should "parse equals list of bids" in {
    val bids: List[Bid] = parseFile("few_bids.txt", objBid)
    assert(bids === testBidsList)
  }

  it should s"parse equals client ($testClient)" in {
    val clients: List[Client] = parseFile("solo_client.txt", objClient)
    assert(clients === List(testClient))
  }

  it should "parse equals list of clients" in {
    val clients: List[Client] = parseFile("few_clients.txt", objClient)
    assert(clients === testClientsList)
  }

}
