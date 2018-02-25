package com.samehadar.exchange.data

import org.scalatest.FlatSpec

class BidTest extends FlatSpec {

  val testBid = Bid("C6", Sell, B,	5,	2)
  val testBid2 = Bid("C6", Sell, B,	5,	2)
  val testBid3 = Bid("C1", Buy, A,	4,	7)
  val testBid4 = Bid("C5", Buy, B,	5,	2)

  it should "correct convert Bid to string" in {
    assert(testBid.toString === "C6\ts\tB\t5\t2")
  }


  s"Bid $testBid" should s"not match with himself" in {
    assert((testBid simpleMatch testBid) === false)
  }

  s"Bid $testBid" should s"not match with equals bid $testBid2" in {
    assert((testBid simpleMatch testBid2) === false)
  }

  s"Bid $testBid" should s"not match with another $testBid3" in {
    assert((testBid simpleMatch testBid3) === false)
  }

  s"Bid $testBid" should s"match with correct bid from another client $testBid4" in {
    assert((testBid simpleMatch testBid4) === true)
  }


  s"$Sell" should "correct convert to 's'" in {
    assert(Sell.toString === "s")
  }

  s"$Buy" should "correct convert to 'b'" in {
    assert(Buy.toString === "b")
  }

}
