package ex1.simonxikun

import com.twitter.finagle.exp.mysql.{ Client, Transactions }

package object db {
  type TrxClient = Client with Transactions
}