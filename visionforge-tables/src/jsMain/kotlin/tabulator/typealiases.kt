@file:Suppress("NO_EXPLICIT_VISIBILITY_IN_API_MODE_WARNING")

package tabulator

import js.objects.Record
import org.w3c.dom.events.UIEvent


typealias ValueStringCallback = (value: Any) -> String

typealias ValueBooleanCallback = (value: Any) -> Boolean

typealias ValueVoidCallback = (value: Any) -> Unit

typealias EmptyCallback = (callback: () -> Unit) -> Unit

typealias CellEventCallback = (e: UIEvent, cell: CellComponent) -> Unit

typealias CellEditEventCallback = (cell: CellComponent) -> Unit

typealias ColumnEventCallback = (e: UIEvent, column: ColumnComponent) -> Unit

typealias RowEventCallback = (e: UIEvent, row: RowComponent) -> Unit

typealias RowChangedCallback = (row: RowComponent) -> Unit

typealias GroupEventCallback = (e: UIEvent, group: GroupComponent) -> Unit

typealias JSONRecord = Record<String, dynamic /* String | Number | Boolean */>

typealias FilterFunction = (field: String, type: String /* "=" | "!=" | "like" | "<" | ">" | "<=" | ">=" | "in" | "regex" | "starts" | "ends" */, value: Any, filterParams: FilterParams) -> Unit

typealias GroupValuesArg = Array<Array<Any>>

typealias CustomMutator = (value: Any, data: Any, type: String /* "data" | "edit" */, mutatorParams: Any, cell: CellComponent) -> Any

typealias CustomAccessor = (value: Any, data: Any, type: String /* "data" | "download" | "clipboard" */, AccessorParams: Any, column: ColumnComponent, row: RowComponent) -> Any

typealias ColumnSorterParamLookupFunction = (column: ColumnComponent, dir: String /* "asc" | "desc" */) -> Any
