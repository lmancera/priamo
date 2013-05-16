package test.com.iactiveit.priamo.db.wrapper

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import me.prettyprint.hector.api.factory.HFactory

import com.iactiveit.priamo.db.wrapper.Cassandra

class CassandraSpec extends Specification {
  
  "Cassandra" should {
    
    "connect a valid keyspace" in {
      val testcf = "testcf"
      val keyspace = Cassandra.getKeyspace
      keyspace.getKeyspaceName() must equalTo(Cassandra.keyspaceName)
    }

    "create and drop a column family" in {
      Cassandra.addColumnFamily("testcf")

      
    }
    
  }
}