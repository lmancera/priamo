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
package com.iactiveit.priamo.db.wrapper

import scala.collection.JavaConversions._

import me.prettyprint.cassandra.serializers.StringSerializer
import me.prettyprint.hector.api.Cluster
import me.prettyprint.hector.api.Keyspace
import me.prettyprint.hector.api.beans.Row
import me.prettyprint.hector.api.factory.HFactory

// TODO: Make this a HectorWrapper for the atomic operations
// as in https://github.com/zcourts/cassandra-hector-wrapper
// but for Scala

// TODO: The queries should return generic types for columns, not only string
// TODO: Achieve that instead of Row[String, String, String] we use Row

object Cassandra {

    val se = StringSerializer.get

    var clusterName = "Localhost"
    var host = "localhost"
    var port = "9160"
    val keyspaceName = "priamo"

    // TODO: create a config doc

    // TODO: Make tests when creation is done

    // TODO: Make the test for creation and drop and improve the rest of functions to deal ok with exceptions
    // (non existing items and so on...)

    // TODO: cluster and keyspace are treated as singletons here?

    val cluster: Cluster = {
		HFactory.getOrCreateCluster(clusterName, host + ":" + port)
    }

    val keyspace: Keyspace = {
        HFactory.createKeyspace(keyspaceName, cluster)
    }

    def getRows(cf: String, from:String, to: String, options:Map[String,Any]=Map.empty): List[Row[String,String,String]] = {
    	var columns = (options getOrElse ("columns", 10)).asInstanceOf[Int]
    	var max = (options getOrElse ("max", 100)).asInstanceOf[Int]
    	var reverse = (options getOrElse ("reverse", false)).asInstanceOf[Boolean]

        val rangeSlicesQuery = HFactory.createRangeSlicesQuery(keyspace, se, se, se)
        rangeSlicesQuery.setColumnFamily(cf)
        rangeSlicesQuery.setKeys(from, to)
        rangeSlicesQuery.setRange("", "", reverse, columns)
        rangeSlicesQuery.setRowCount(max)
        val result = rangeSlicesQuery.execute()
        val orderedRows = result.get.getList().toList
        orderedRows
    }

    def getKey(row: Row[String,String,String]): String = {
    	row.getKey
    }

    def getColumnValueByName(row: Row[String,String,String], name: String) = {
    	row.getColumnSlice.getColumnByName(name).getValue
    }

    def addColumnFamily(name: String): Unit = {
    	// TODO: Check if exists
		val cfDef = HFactory.createColumnFamilyDefinition(keyspaceName, name)
    	cluster.addColumnFamily(cfDef, true)
    }

    def dropColumnFamily(name: String): String = {
    	val result = cluster.dropColumnFamily(keyspaceName, name, true)
    	result
    }

}