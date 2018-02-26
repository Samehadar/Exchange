package com.samehadar.exchange.data

import org.scalatest.FlatSpec

class ClientTest extends FlatSpec {

  val testBid = Bid("C7", Sell, B,	5,	2)

  val buyer = Client("C6", 100, 10, 10, 10, 10)
  val seller = Client("C7", 100, 10, 10, 10, 10)

  s"client($buyer)" should s"be available to buy $testBid" in {
    assert(buyer.canBuy(testBid))
  }

  s"client($seller)" should s"be available to sell $testBid" in {
    assert(seller.canSell(testBid))
  }

  s"deal $testBid from C7 to C6" should "be available" in {
    assert(Client.dealIsPossible(buyer, seller, testBid))
  }

  it should "be correct deal" in {
    val (upd1, upd2) = Client.buyBid(buyer, seller, testBid)
    val exp1 = buyer.copy(balance = 90, bCount = 12)
    val exp2 = seller.copy(balance = 110, bCount = 8)
    assert(upd1 === exp1)
    assert(upd2 === exp2)
  }
}
