import cats.effect.Sync
import cats.implicits._
import io.circe.Json
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

object ZioQuickstartRoutes {

  def helloWorldRoutes[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._

    /*
    object ClientIdQueryParamMatcher extends QueryParamDecoderMatcher[String]("client_id")
    object RedirectUriQueryParamMatcher extends QueryParamDecoderMatcher[Uri]("redirect_uri")
    object StateQueryParamMatcher extends QueryParamDecoderMatcher[String]("state")
     */

    HttpRoutes.of[F] {
      /*
      case GET -> Root / "auth" :? ClientIdQueryParamMatcher(clientId) +& RedirectUriQueryParamMatcher(redirectUri) +& StateQueryParamMatcher(state) =>
        val code = "foobar"
        val uri = redirectUri
          .withQueryParam("code", code)
          .withQueryParam("state", state)
        SeeOther(Location(uri))

      case req @ POST -> Root / "token" =>
        Ok (
          Json.obj (
            ("token_type", Json.fromString("Bearer")),
            ("access_token", Json.fromString("ACCESS_TOKEN")),
            ("refresh_token", Json.fromString("REFRESH_TOKEN")),
            ("expires_in", Json.fromInt(Int.MaxValue))
          )
        )

      case req @ POST -> Root / "home" =>
        for {
          json <- req.as[Json]
          requestId = json.hcursor.get[String]("requestId")
          _ = println(requestId)
          res <- Ok(json)
        } yield res

       */

      case req @ POST -> Root / "hook" =>
        for {
          json <- req.as[Json]
          maybeLanguage = json.hcursor.downField("queryResult").downField("parameters").get[String]("language").toOption.filter(_.nonEmpty)
          response = maybeLanguage.fold("What is your favorite language") { language =>
            if (language.equalsIgnoreCase("scala")) "Correct!" else "Wrong."
          }
          res <- Ok(
            Json.obj(
              "fulfillmentText" -> Json.fromString(response)
            )
          )
        } yield res
    }
  }
}
