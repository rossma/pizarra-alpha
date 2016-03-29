package controllers

import javax.inject._

import models.Phrase
import models.Cat
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import daos.PhraseDao
import daos.CatDAO
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (webJarAssets: WebJarAssets, phraseDao: PhraseDao, catDao: CatDAO) extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action.async {
    phraseDao.insert(Phrase(Some(10L), "eeeeeeeeeee", "dffffdddddd"))
    // phraseDao.create("dsdsdsdsds", "dffffdddddd")
    // Logger.info(s"ssssssssssssssssss:${phraseDao.count.value}.")
    //phraseDao.all().map { case (phrases) => Ok(views.html.index(webJarAssets, phrases)) }
    catDao.insert(Cat("dsdsdsdsds", "dffffdddddd"))
    // catDao.all().map {case (cats) => Ok(views.html.index(webJarAssets, cats)) }
    catDao.all().zip(phraseDao.all()).map {case (cats, dogs) => Ok(views.html.index(webJarAssets, cats, dogs)) }

  }

}
