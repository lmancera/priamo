package test.controllers

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class UsersSpec extends Specification {
  
  "Users" should {
    
    "render list of users" in {
      running(FakeApplication()) {
        val page = route(FakeRequest(GET, "/user/all")).get
        
        status(page) must equalTo(OK)
        contentType(page) must beSome.which(_ == "text/html")
        contentAsString(page) must contain ("<body>")
      }
    }

    "render user view" in {
      running(FakeApplication()) {
        val page = route(FakeRequest(GET, "/user/1/get")).get
        
        status(page) must equalTo(OK)
        contentType(page) must beSome.which(_ == "text/html")
        contentAsString(page) must contain ("<body>")
      }
    }

    "render user photo" in {
      running(FakeApplication()) {
        val page = route(FakeRequest(GET, "/user/1/photo")).get
        
        status(page) must equalTo(OK)
        contentType(page) must beSome.which(_ == "text/html")
        contentAsString(page) must contain ("<body>")
      }
    }

  }
}