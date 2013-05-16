package models

import me.prettyprint.hector.api.beans.Row

import com.iactiveit.priamo.db.dao.UserDao
import com.iactiveit.priamo.db.wrapper.Cassandra

// TODO: Refactor here: too many arguments

case class User(uuid: String, name: String, surname: String, email: String, 
                city: String, state: String, country: String, postal_code: String, 
                phone: String, gender: String, birth_date: String, photo: String)

object User {

  def numberOfColumns = 10

  def from(rows:List[Row[String,String,String]]): Seq[User] = {
    def users = rows.map(row => this from row)
    users
  }

  def from(row:Row[String,String,String]): User = {
    def uuid = Cassandra.getKey(row)
    def name = Cassandra.getColumnValueByName(row,"name")
    def surname = Cassandra.getColumnValueByName(row,"surname")
    def email = Cassandra.getColumnValueByName(row,"email")
    def city = Cassandra.getColumnValueByName(row,"city")
    def state = Cassandra.getColumnValueByName(row,"state")
    def country = Cassandra.getColumnValueByName(row,"country")
    def postal_code = Cassandra.getColumnValueByName(row,"postal_code")
    def phone = Cassandra.getColumnValueByName(row,"phone")
    def gender = Cassandra.getColumnValueByName(row,"gender")
    def birth_date = Cassandra.getColumnValueByName(row,"birth_date")
    User(uuid,name,surname,email,city,state,country,postal_code,phone,gender,birth_date,"")
  }  

  def findAll: Seq[User] = {
    UserDao.findAllUsers
  }

  def get(uuid: String): User = {
    UserDao.getUser(uuid)
  }

  def getPhoto(uuid: String): String = {
    UserDao.getUserPhoto(uuid)
  }
    
}