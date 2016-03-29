package daos

import javax.inject.Inject
import models.Phrase
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future

class PhraseDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  private val Phrases = TableQuery[PhrasesTable]

  def all(): Future[Seq[Phrase]] = db.run(Phrases.result)

  def insert(phrase: Phrase): Future[Unit] = db.run(Phrases += phrase).map { _ => () }

 // def insert(phrases: Seq[Phrase]): Future[Unit] = db.run(this.Phrases ++= phrases).map(_ => ())
  


  // val insertQuery = phrases returning phrases.map(_.id) into ((phrase, id) => phrase.copy(id = id))

  // def create(name: String, price: String) : Future[Phrase] = {
  //   val action = insertQuery += Phrase(Some(0L), name, price)
  //   db.run(action)
  // }

  // val insertActions = DBIO.seq(
  //   phrases += (Option(1L), "dd", "dd"),

  //   coffees ++= Seq(
  //     ("French_Roast", 49, 8.99, 0, 0),
  //     ("Espresso",    150, 9.99, 0, 0)
  //   ),

  //   // "sales" and "total" will use the default value 0:
  //   coffees.map(c => (c.name, c.supID, c.price)) += ("Colombian_Decaf", 101, 8.99)
  // )

  // Get the statement without having to specify a value to insert:
  // val sql = coffees.insertStatement


  def count(): Future[Int] = {
    // this should be changed to
    // db.run(computers.length.result)
    // when https://github.com/slick/slick/issues/1237 is fixed
    db.run(Phrases.map(_.id).length.result)
  }

  // private class PhrasesTable(tag: Tag) extends Table[Phrase](tag, "PHRASE") {

  //   def id = column[Long]("ID", O.PrimaryKey)
  //   def spanish = column[String]("SPANISH")
  //   def english = column[String]("ENGLISH")

  //   def * = (id, spanish, english) <> (Phrase.tupled, Phrase.unapply _)
  // }

  private class PhrasesTable(tag: Tag) extends Table[Phrase](tag, "PHRASE") {

    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def spanish = column[String]("SPANISH")
    def english = column[String]("ENGLISH")

    def * = (id.?, spanish, english) <> (Phrase.tupled, Phrase.unapply _)
  }


}