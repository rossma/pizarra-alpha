package controllers

import javax.inject._

import daos.PhraseDao
import models.Phrase
import org.joda.time.DateTime
import play.api.data.Form
import play.api.data.Forms.{ignored, jodaDate, longNumber, mapping, nonEmptyText, optional}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, _}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (webJarAssets: WebJarAssets, phraseDao: PhraseDao) extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action.async {
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
    phraseDao.insert(phrase).map(_ => Redirect(routes.HomeController.index))
  }

}
