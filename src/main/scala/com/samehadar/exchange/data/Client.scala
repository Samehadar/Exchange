package com.samehadar.exchange.data

/**
  * @param name Имя клиента
  * @param balance Баланс клиента по долларам
  * @param aCount Баланс клиента по ценной бумаге "A" в штуках
  * @param bCount Баланс по ценной бумаге "B"
  * @param cCount Баланс по ценной бумаге "C"
  * @param dCount Баланс по ценной бумаге "D"
  */
case class Client(name: String, balance: Int, aCount: Int, bCount: Int, cCount: Int, dCount: Int) extends Comparable[Client] {

  def canBuy(bid: Bid): Boolean = {
    balance >= (bid.paperCount * bid.priceForOne)
  }

  def canSell(bid: Bid): Boolean = {
    bid.paper match {
      case A => this.aCount >= bid.paperCount
      case B => this.bCount >= bid.paperCount
      case C => this.cCount >= bid.paperCount
      case D => this.dCount >= bid.paperCount
    }
  }

  override def toString: String = {
    name + '\t' + balance + '\t' + aCount + '\t' + bCount + '\t' + cCount + '\t' + dCount
  }

  override def compareTo(that: Client): Int = name.drop(1).toInt compareTo that.name.drop(1).toInt
}

object Client {

  def dealIsPossible(buyer: Client, seller: Client, bid: Bid): Boolean = {
    buyer.canBuy(bid) && seller.canSell(bid)
  }

  def buyBid(buyer: Client, seller: Client, bid: Bid): (Client, Client) = {

    val buyerBalance = buyer.balance - (bid.paperCount * bid.priceForOne)
    val sellerBalance = seller.balance + (bid.paperCount * bid.priceForOne)
    bid.paper match {
      case A =>
        (buyer.copy(balance = buyerBalance, aCount = buyer.aCount + bid.paperCount),
          seller.copy(balance = sellerBalance, aCount = seller.aCount - bid.paperCount))
      case B =>
        (buyer.copy(balance = buyerBalance, bCount = buyer.bCount + bid.paperCount),
          seller.copy(balance = sellerBalance, bCount = seller.bCount - bid.paperCount))
      case C =>
        (buyer.copy(balance = buyerBalance, cCount = buyer.cCount + bid.paperCount),
          seller.copy(balance = sellerBalance, cCount = seller.cCount - bid.paperCount))
      case D =>
        (buyer.copy(balance = buyerBalance, dCount = buyer.dCount + bid.paperCount),
          seller.copy(balance = sellerBalance, dCount = seller.dCount - bid.paperCount))
    }
  }

}