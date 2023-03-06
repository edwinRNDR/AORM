package tanvd.aorm


import tanvd.aorm.query.Query
import tanvd.aorm.utils.ClickHouseUtil

abstract class View(name: String) {
    var name: String = ClickHouseUtil.escape(name)!!

    abstract val query: Query
}

abstract class MaterializedView(name: String) {
    var name: String = ClickHouseUtil.escape(name)!!

    abstract val query: Query
    abstract val engine: Engine
}