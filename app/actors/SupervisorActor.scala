package actors

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Await

case class Count()
case class CountResponse(count: Int)

class SupervisorActor extends Actor {

  val hello : ActorRef = context.actorOf(HelloActor.props, "hello")

  override def receive : Receive = {
    case x: Hello => hello ! x
    case response: HelloResponse =>  println( response.response )
    case c: Count => {
      implicit val timeout = new Timeout(1 seconds)
      // ask the greeter for count
      println(s"First: $hello")

      val future = hello ? c
      // synchronously wait for a response
      val result = Await.result(future,timeout.duration).asInstanceOf[CountResponse] // synchronously wait for a response
      println(s"response is ${result}")
    }
    case _  =>
  }

}

object SupervisorActor {
  def props = Props(classOf[SupervisorActor])
}
