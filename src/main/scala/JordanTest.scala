
import scalaz._
import Scalaz._
import scalaz.stream._

object JordanTest extends App {


  type Rule = String => List[String]

  val parseA: Rule = line => {
    val regex = "(.*)@(.*)".r
    line match {
      case regex(label, price) => List(label, price)
      case _                   => List("", "")
    }
  }

  val parseB: Rule = line => {
    val regex = "(.*) [xX] (.*)".r
    line match {
      case regex(quantity, code) => List(quantity, code)
      case _                     => List("", "")
    }
  }

  val parseC: Rule = line => {
    val regex = ".*(http.*)".r
    line match {
      case regex(url) => List(url, "END OF RECORD")
      case _          => List("", "END OF RECORD")
    }
  }

  val rules: scala.collection.immutable.Map[Rule, Rule] = scala.collection.immutable.Map(
    parseA -> parseB,
    parseB -> parseC,
    parseC -> parseA
  )

  var latestRule = parseA

  def getRecord(line: String): Process0[String] = {
    val record: List[String] = latestRule(line)
    latestRule = rules(latestRule)

    record match {
      case h :: t if h.isEmpty => Process.emitAll(record) ++ getRecord(line)
      case _                   => Process.emitAll(record)
    }
  }

  def toCsvRow(word: String): String = {
    word match {
      case "END OF RECORD" => "\n"
      case _ => word.trim + ","
    }
  }

  println(io.linesR("pigsback.txt")
    .filter(line => !line.isEmpty)
    .flatMap(getRecord)
    .map(toCsvRow)
    .pipe(text.utf8Encode)
    .to(io.fileChunkW("pigsback.csv"))
    .run
    .run)
}
