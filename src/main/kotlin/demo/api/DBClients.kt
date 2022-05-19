package demo.api

import javax.persistence.Id
import javax.persistence.Entity
import javax.persistence.Table
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository

@Entity
@Table(name = "sample")
data class SampleMysql(@Id val uuid: String, val content: String)

@Document(collection = "sample")
data class SampleMongo(val uuid: String, val content: String)

@Repository
interface SampleMysqlRepo: JpaRepository<SampleMysql, String> {
    fun findByUuid(uuid: String): SampleMysql?
}

interface SampleMongoRepo: MongoRepository<SampleMongo, String> {
    fun findByUuid(uuid: String): SampleMongo?
}

data class OpResponse(val value: String?, val errors: String?)
