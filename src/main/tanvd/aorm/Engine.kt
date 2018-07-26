package tanvd.aorm

import tanvd.aorm.expression.Column
import java.util.*

sealed class Engine {
    abstract fun toSqlDef(): String

    abstract fun toReplicatedSqlDef(index: Int): String

    class MergeTree(val dateColumn: Column<Date, DbPrimitiveType<Date>>, val primaryKey: List<Column<*, DbPrimitiveType<*>>>,
                    val indexGranularity: Long = 8192, val zookeeperPath: String? = null)
        : Engine() {
        override fun toSqlDef() = "MergeTree(${dateColumn.name}, (${primaryKey.joinToString { it.name }}), $indexGranularity)"

        override fun toReplicatedSqlDef(index: Int): String = zookeeperPath?.let { zookeeperPath ->
            "ReplicatedMergeTree($zookeeperPath, $index, ${dateColumn.name}, (${primaryKey.joinToString { it.name }}), $indexGranularity)"
        } ?: throw NotImplementedError("Zookeeper path not stated. Replicated table can not be created.")

    }

    class ReplacingMergeTree(val dateColumn: Column<Date, DbPrimitiveType<Date>>, val primaryKey: List<Column<*, DbPrimitiveType<*>>>,
                             val versionColumn: Column<Long, DbUInt64>,
                             val indexGranularity: Long = 8192,
                             val zookeeperPath: String? = null)
        : Engine() {
        override fun toSqlDef() = "ReplacingMergeTree(${dateColumn.name}, (${primaryKey.joinToString { it.name }})," +
                " $indexGranularity, ${versionColumn.name})"

        override fun toReplicatedSqlDef(index: Int): String = zookeeperPath?.let { zookeeperPath ->
            "ReplicatedReplacingMergeTree($zookeeperPath, $index, ${dateColumn.name}, (${primaryKey.joinToString { it.name }}), $indexGranularity, ${versionColumn.name})"
        } ?: throw NotImplementedError("Zookeeper path not stated. Replicated table can not be created.")
    }

    class AggregatingMergeTree(val dateColumn: Column<Date, DbPrimitiveType<Date>>, val primaryKey: List<Column<*, DbPrimitiveType<*>>>,
                               val indexGranularity: Long = 8192, val zookeeperPath: String? = null)
        : Engine() {
        override fun toSqlDef() = "AggregatingMergeTree(${dateColumn.name}, (${primaryKey.joinToString { it.name }}), $indexGranularity)"

        override fun toReplicatedSqlDef(index: Int): String = zookeeperPath?.let { zookeeperPath ->
            "ReplicatedAggregatingMergeTree($zookeeperPath, $index, ${dateColumn.name}, (${primaryKey.joinToString { it.name }}), $indexGranularity)"
        } ?: throw NotImplementedError("Zookeeper path not stated. Replicated table can not be created.")

    }
}