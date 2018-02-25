package com.samehadar.exchange.data

sealed trait Paper
case object A extends Paper
case object B extends Paper
case object C extends Paper
case object D extends Paper

sealed trait Exchange {
  override def toString: String = this match {
    case Buy => "b"
    case Sell => "s"
  }
}
case object Buy extends Exchange
case object Sell extends Exchange

/**
  * @param clientName   Имя клиента выставившего заявку
  * @param exchange     Символ операции: "s" - продажа или "b" - покупка.
  * @param paper        Наименование ценной бумаги
  * @param priceForOne  Цена заявки (целое число за одну штуку ценной бумаги)
  * @param paperCount   Количество продаваемых или покупаемых ценных бумаг
  */
case class Bid(clientName: String, exchange: Exchange, paper: Paper, priceForOne: Int, paperCount: Int) {
  def simpleMatch(that: Bid): Boolean =
    (clientName != that.clientName) &&
      (exchange != that.exchange) &&
      (paper == that.paper) &&
      (paperCount == that.paperCount)

  override def toString: String = {
    clientName + '\t' + exchange + '\t' + paper + '\t' + priceForOne + '\t' + paperCount
  }
}