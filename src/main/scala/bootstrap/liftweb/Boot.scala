package bootstrap.liftweb

import java.util.Locale
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.provider.HTTPCookie
import net.liftweb.sitemap.Loc._
import net.liftweb.sitemap._
import net.liftweb.util._
import net.pointsgame.db.DBSetUp
import net.pointsgame.lift.liftvar._
import net.pointsgame.lift.rest.{ RestPages, MyRest }

/** A class that's instantiated early and run.  It allows the application
 *  to modify lift's environment
 */
class Boot extends Loggable {
	def boot() {

		LiftRules.addToPackages("net.pointsgame.lift") // where to search snippet

		LiftRules.dispatch.append(MyRest)
		LiftRules.dispatch.append(RestPages)

		DBSetUp.setUp()
		DBSetUp.createDB()

		{
			def isConsoleRequest(r: Req) = r.path.partPath.headOption == Some("console")
			if (Props.devMode &&
					Props.getBool("insecure.allow.console").openOr(false) &&
					Props.get("jdbc.address", "").startsWith("jdbc:h2")) {
				// rules for h2 database console
				LiftRules.liftRequest.append {
					case r if isConsoleRequest(r) ⇒ false
				}
			}
		}

		LiftRules.localeCalculator = boxReq ⇒
			S.param("lang").map { s ⇒
				S.addCookie(HTTPCookie("lang", s))
				new Locale(s)
			}.or {
				S.cookieValue("lang").map(new Locale(_))
			}.openOr {
				LiftRules.defaultLocaleCalculator(boxReq)
			}

		// set the sitemap.  Note if you don't want access control for
		// each page, just comment this line out.
		LiftRules.setSiteMap(sitemap)

		//Show the spinny image when an Ajax call starts
		LiftRules.ajaxStart =
			Full(() ⇒ LiftRules.jsArtifacts.show("ajax-loader").cmd)

		// Make the spinny image go away when it ends
		LiftRules.ajaxEnd =
			Full(() ⇒ LiftRules.jsArtifacts.hide("ajax-loader").cmd)

		// Force the request to be UTF-8
		LiftRules.early.append(_.setCharacterEncoding("UTF-8"))
	}

	def sitemap = {
		val userMenu = UserSessionVar.is.dmap[List[ConvertableToMenu]](Nil) { user ⇒
			Menu.i("Настройки") / "settings" ::
				Menu(JoinedGamesSessionVar.htmlMenu) / "faq9" ::
				Nil
		}

		def goodRoom = () ⇒ S.uri.split('/').lastOption.exists(_ matches "[0-9]{1,9}")
		val globalMenu: List[ConvertableToMenu] =
			List(
				Menu.i("Home") / "index",
				Menu.i("NN - NN") / "gr" / * >> Hidden >>
					If(goodRoom, "no such room") >> Loc.TemplateBox {
						() ⇒ Templates("gameRoom" :: Nil)
					},
				Menu.i("Общая комната") / "lr" / * >> Hidden >>
					If(goodRoom, "no such room") >> Loc.TemplateBox { () ⇒
						Templates("langRoom" :: Nil)
					},
				Menu.i("Terms") / "tos",
				Menu.i("Faq") / "faq",
				Menu.i("Rules") / "rules"
			)

		SiteMap(globalMenu ::: userMenu: _*)
	}

}
