// Copyright 2013 IActive IT

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.iactiveit.priamo.db.dao
import com.iactiveit.priamo.db.wrapper.Cassandra
import models.User

object UserDao {

    // TODO: Take this to an Application general mapping class
    var cfUsers = "users"
    var cfUserPhoto = "user_photo"
    var cnPhoto = "photo"

    var numColumnsUsers = 10
    var numColumnsUserPhoto = 1

    def findAllUsers: Seq[User] = {
        def users = User from Cassandra.getRows(cfUsers,"","",Map("columns" -> numColumnsUsers))
        users
    }

    def getUser(uuid: String): User = {
        def users = User from Cassandra.getRows(cfUsers,uuid,uuid,Map("columns" -> numColumnsUsers))
        if (users.size > 0) users.last
        else User.empty
    }

    def getPhoto(uuid: String): String = {
        def rows = Cassandra.getRows(cfUserPhoto,uuid,uuid,Map("columns" -> numColumnsUserPhoto))
        def photo = Cassandra.getColumnValueByName(rows.last, cnPhoto)
        photo
    }

    // TODO insertRow
    def updatePhoto(uuid: String, photo: String): String = {
        Cassandra.dropRow(cfUserPhoto,uuid)
        Cassandra.insertRow(cfUserPhoto,uuid)
        getPhoto(uuid)
    }

    def upsert(user: User): User = {
        if (getUser(user.uuid) != User.empty){
            update(user)
        } else {
            create(user)
        }
    }

    // TODO insertRow
    def create(user: User): User = {
        if (getUser(user.uuid) != User.empty){
            if (Cassandra.insertRow(cfUsers,user.uuid)) user 
            else User.empty
        } else {
            User.empty
        }
        user
    }

    def update(user: User): User = {
        delete(user.uuid)
        create(user)
        user
    }

    def delete(uuid: String): User = {
        def user = getUser(uuid)
        if (Cassandra.dropRow(cfUsers,user.uuid)) user
        else User.empty
    }

}