package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models.User
import models.UserPhoto

// TODO: Change output to JSON/XML
// TODO: Instructions about how to call the WS

object Users extends Controller {

  val userForm: Form[User] = Form(
    mapping(
      "uuid" -> nonEmptyText,
      "name" -> text(minLength = 2),
      "surname" -> text(minLength = 2),
      "email" -> email,
      "city" -> optional(text),
      "state" -> optional(text),
      "country" -> optional(text),
      "postal_code" -> optional(text),
      "phone" -> optional(text),
      "gender" -> nonEmptyText,
      "birth_date" -> optional(text)
    )(User.apply)(User.unapply)
  )
  
  def all = Action{
    try {
  	   var users = User.findAll
  	   Ok(views.html.user.all(users,""))
    } catch {
      case e: Exception => {
        error("Error listing users")
      }
    }
  }

  def get(uuid:String) = Action {
    try {
      var user = User.get(uuid)
      Ok(views.html.user.view(user,""))
    } catch {
      case e: Exception => {
        Ok(views.html.user.view(User.empty,"Error viewing user: " + e.getMessage))
      }
    }
  }

  def photo(uuid:String) = Action {
    try {
      var photo = UserPhoto.getPhoto(uuid)
      Ok(views.html.user.photo(photo))
    } catch {
      case e: Exception => {
        error("Error viewing photo")
      }
    }
  }

  def addnew = Action {
    try {
        var user = User.empty
        Ok(views.html.user.form(userForm.fill(user), "Add user"))
      } catch {
        case e: Exception => {
          error(e.getMessage)
        }
      }
  }

  def edit(uuid:String) = Action {
    try {
        var user = User.get(uuid)
        Ok(views.html.user.form(userForm.fill(user), "Edit user"))
      } catch {
        case e: Exception => {
          error(e.getMessage)
        }
      }
  }

  def upsert = Action { implicit request =>
    userForm.bindFromRequest.fold(
      errors => {
        BadRequest(views.html.user.form(errors,"Please check errors"))
      },
      user => {
        User.upsert(user)
        Ok(views.html.user.form(userForm.fill(user),"User submitted"))
      }
    )
  }

  def delete(uuid:String) = Action {
    try {
        var user = User.delete(uuid)
        Redirect("/user/all")
      } catch {
        case e: Exception => {
          error(e.getMessage)
        }
      }    
  }
 
}