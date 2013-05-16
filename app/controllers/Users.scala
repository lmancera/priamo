package controllers

import play.api._
import play.api.mvc._

import models.User

object Users extends Controller {
  
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
        error("Error viewing user")
      }
    }
  }

  def photo(uuid:String) = Action {
    try {
      var photo = User.getPhoto(uuid)
      Ok(views.html.user.photo(photo))
    } catch {
      case e: Exception => {
        error("Error viewing photo")
      }
    }
  }

  def create = TODO

  def update(uuid:String) = TODO

  def delete(uuid:String) = TODO
 
}