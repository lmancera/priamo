package models

import me.prettyprint.hector.api.beans.Row

import com.iactiveit.priamo.db.dao.UserDao
import com.iactiveit.priamo.db.wrapper.Cassandra

// TODO: Refactor here: too many arguments: See play form samples to see how to reduce this
// The idea is to group properties in UserLogin, UserProfile... 

case class User(var uuid: String, name: String, surname: String, email: String, 
                city: Option[String], state: Option[String], country: Option[String], postal_code: Option[String], 
                phone: Option[String], gender: String, birth_date: Option[String])

object User {

  def numberOfColumns = 11

  def from(rows:List[Row[String,String,String]]): Seq[User] = {
    def users = rows.map(row => this from row)
    users
  }

  def from(row:Row[String,String,String]): User = {
    def uuid = Cassandra.getKey(row)
    def name = Cassandra.getColumnValueByName(row,"name")
    def surname = Cassandra.getColumnValueByName(row,"surname")
    def email = Cassandra.getColumnValueByName(row,"email")
    def city = Option(Cassandra.getColumnValueByName(row,"city"))
    def state = Option(Cassandra.getColumnValueByName(row,"state"))
    def country = Option(Cassandra.getColumnValueByName(row,"country"))
    def postal_code = Option(Cassandra.getColumnValueByName(row,"postal_code"))
    def phone = Option(Cassandra.getColumnValueByName(row,"phone"))
    def gender = Cassandra.getColumnValueByName(row,"gender")
    def birth_date = Option(Cassandra.getColumnValueByName(row,"birth_date"))
    User(uuid,name,surname,email,city,state,country,postal_code,phone,gender,birth_date)
  }  

  def findAll: Seq[User] = {
    UserDao.findAllUsers
  }

  def get(uuid: String): User = {
    UserDao.getUser(uuid)
  }

  def upsert(user:User): User = {
    UserDao.upsert(user)
  }

  def delete(uuid:String): User = {
    UserDao.delete(uuid)
  }

  def empty: User = {
    User("","","","",Option(""),Option(""),Option(""),Option(""),Option(""),"",Option(""))
  }
    
}