package com.samehadar.exchange

import com.samehadar.exchange.data.{Bid, Buy, Client, Sell}
import org.slf4j.LoggerFactory

import scala.annotation.tailrec

object BidMatcher {

  private val log = LoggerFactory.getLogger("MATCHER")

  def simpleMatch(bids: List[Bid], clients: Map[String, Client]): Map[String, Client] = {

    var updatableClients = clients

    @tailrec def insideMatch(bids: List[Bid]): Unit = bids match {
      case Nil => ()
      case bid :: tail =>
//        println(s"sMatch from: $bid")
//        println(s"sMatch and : ${tail.mkString("\t")}")
//        println()

        val res = tail.find(bid.simpleMatch).map{
          case matched @ Bid(buyer, Buy, _, _, _) =>
            updatableClients = updateClients(updatableClients, buyer, bid.clientName, bid)
            tail.filterNot(_ eq matched)
          case matched @ Bid(seller, Sell, _, _, _) =>
            updatableClients = updateClients(updatableClients, bid.clientName, seller, bid)
            tail.filterNot(_ eq matched)
        }
        insideMatch(res getOrElse tail)
    }

    insideMatch(bids)

    updatableClients
  }

  def aboveZeroMatch(bids: List[Bid], clients: Map[String, Client]): Map[String, Client] = {

    var updatableClients = clients

    @tailrec def insideMatch(bids: List[Bid]): Unit = bids match {
      case Nil => ()
      case bid :: tail =>
        val res = tail.find{ matched =>
          bid.simpleMatch(matched) && checkDeal(bid, matched, updatableClients)
        }.map{
          case matched @ Bid(buyer, Buy, _, _, _) =>
            updatableClients = updateClients(updatableClients, buyer, bid.clientName, bid)
            tail.filterNot(_ eq matched)
          case matched @ Bid(seller, Sell, _, _, _) =>
            updatableClients = updateClients(updatableClients, bid.clientName, seller, bid)
            tail.filterNot(_ eq matched)
        }
        insideMatch(res getOrElse tail)
    }

    insideMatch(bids)

    updatableClients
  }

  private def checkDeal(bid: Bid, matched: Bid, clients: Map[String, Client]): Boolean = matched match {
    case Bid(buyer, Buy, _, _, _) if Client.dealIsPossible(clients(buyer), clients(bid.clientName), bid) => true
    case Bid(seller, Sell, _, _, _) if Client.dealIsPossible(clients(bid.clientName), clients(seller), bid)  => true
    case _ => false
  }

  def updateClients(clients: Map[String, Client], buyerName: String, sellerName: String, bid: Bid): Map[String, Client] = {
    log.debug(s"DEAL START BID($bid)")
    log.debug(s"BUYER(${clients(buyerName)}) :: SELLER(${clients(sellerName)})")
    val (buyer, seller) = Client.buyBid(clients(buyerName), clients(sellerName), bid)
    log.debug(s"BUYER($buyer) :: SELLER($seller)")
    log.debug("DEAL END")
    clients + (buyerName -> buyer) + (sellerName -> seller)
  }

}
