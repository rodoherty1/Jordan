
import scalaz._
import Scalaz._
import scalaz.stream._

object PiggyPrototype extends App {

  type Rule = String => List[String]

  val endOfRecord = "END OF RECORD"
  val emptyField  = ""

  val parseA: Rule = line => {
    val regex = "(.*)@(.*)".r
    line match {
      case regex(label, price) => List(label, price)
      case _                   => List(emptyField, emptyField)
    }
  }

  val parseB: Rule = line => {
    val regex = "(.*) [xX] (.*)".r
    line match {
      case regex(quantity, code) => List(quantity, code)
      case _                     => List(emptyField, emptyField)
    }
  }

  val parseC: Rule = line => {
    val regex = ".*(http.*)".r
    line match {
      case regex(url) => List(url, endOfRecord)
      case _          => List(emptyField, endOfRecord)
    }
  }

  val rules: scala.collection.immutable.Map[Rule, Rule] = scala.collection.immutable.Map(
    parseA -> parseB,
    parseB -> parseC,
    parseC -> parseA
  )

  /*
   TODO: Need to replace this mutable variable with an immutable value
   */
  var latestRule = parseA

  def getRecord(line: String): Process0[String] = {
    val record: List[String] = latestRule(line)
    latestRule = rules(latestRule)

    record match {
      case "" :: t => Process.emitAll(record) ++ getRecord(line)
      case _       => Process.emitAll(record)
    }
  }

  def toCsvRow(word: String): String = {
    word match {
      case "END OF RECORD" => "\n"
      case _               => word.trim + ","
    }
  }

  io.linesR("piggy.txt")
    .filter(line => !line.isEmpty)
    .flatMap(getRecord)
    .map(toCsvRow)
    .pipe(text.utf8Encode)
    .to(io.fileChunkW("piggy.csv"))
    .run
    .run
}
