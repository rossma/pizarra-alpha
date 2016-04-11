package controllers

import javax.inject._

import daos.PhraseDao
import models.Phrase
import org.joda.time.DateTime
import play.api.data.Form
import play.api.data.Forms.{ignored, jodaDate, longNumber, mapping, nonEmptyText, optional}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, _}
import views.html

import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (val messagesApi: MessagesApi, webJarAssets: WebJarAssets, phraseDao: PhraseDao) extends Controller with I18nSupport {

  val Home = Redirect(routes.HomeController.index)

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action.async { implicit request =>
    phraseDao.all().map { case (phrases) => Ok(views.html.index(webJarAssets, phrases)) }
  }

  /** Describe the phrase form (used in both edit and create screens).*/
  val phraseForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "spanish" -> nonEmptyText,
      "english" -> nonEmptyText,
      "createdAt" -> ignored[DateTime](DateTime.now()))(Phrase.apply)(Phrase.unapply))

  def add = Action {
    Ok(views.html.phrase.add(webJarAssets))
  }

  def addPhrase = Action.async { implicit request =>
    val phrase: Phrase = phraseForm.bindFromRequest.get
    phraseDao.insert(phrase).map(_ => Home.flashing("success" -> "Phrase has been added"))
  }

  def edit(id: Long) = Action.async { implicit request =>
    val phrase = phraseDao.findById(id)

    phrase.map { case (ph) =>
      ph match {
        case Some(p) => Ok(html.phrase.edit(webJarAssets, id, phraseForm.fill(p)))
        case None => NotFound
      }
    }
  }

  def editPhrase(id: Long) = Action.async { implicit request =>
    phraseForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(html.phrase.edit(webJarAssets, id, formWithErrors))),
      phrase => {
        for {
          _ <- phraseDao.update(id, phrase)
        } yield Home.flashing("success" -> "Phrase has been updated")
      })
  }

  def deletePhrase(id: Long) = Action.async { implicit rs =>
    for {
      _ <- phraseDao.delete(id)
    } yield Home.flashing("success" -> "Phrase has been deleted")
  }

}
