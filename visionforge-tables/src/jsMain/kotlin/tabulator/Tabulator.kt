@file:JsModule("tabulator-tables")
@file:Suppress("NO_EXPLICIT_VISIBILITY_IN_API_MODE_WARNING")
package tabulator

import js.objects.Record
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.UIEvent
import org.w3c.files.Blob
import org.w3c.files.File
import kotlin.js.Json
import kotlin.js.Promise


external open class Tabulator {
    constructor(selector: String, options: Options = definedExternally)
    constructor(selector: String)
    constructor(selector: HTMLElement, options: Options = definedExternally)
    constructor(selector: HTMLElement)

    open var columnManager: Any
    open var rowManager: Any
    open var footerManager: Any
    open var browser: String
    open var browserSlow: Boolean
    open var modules: Any
    open var options: Options
    open var element: HTMLElement
    open var download: (downloadType: dynamic /* "csv" | "json" | "xlsx" | "pdf" | "html" | (columns: Array<ColumnDefinition>, data: Any, options: Any, setFileContents: Any) -> Any */, fileName: String, params: DownloadOptions, filter: String /* "visible" | "active" | "selected" | "all" | "range" */) -> Unit
    open var downloadToTab: (downloadType: String /* "csv" | "json" | "xlsx" | "pdf" | "html" */, fileName: String, params: DownloadOptions) -> Unit
    open var import: (data: Any, extension: dynamic /* String | Array<String> */, format: String /* "buffer" | "binary" | "url" | "text" */) -> Any
    open var copyToClipboard: (rowRangeLookup: String /* "visible" | "active" | "selected" | "all" | "range" */) -> Unit
    open var undo: () -> Boolean
    open var getHistoryUndoSize: () -> dynamic
    open var redo: () -> Boolean
    open var getHistoryRedoSize: () -> dynamic
    open var getEditedCells: () -> Array<CellComponent>
    open var clearCellEdited: (clear: dynamic /* CellComponent | Array<CellComponent> */) -> Unit
    open var alert: (message: String) -> Unit
    open var clearAlert: () -> Unit
    open var destroy: () -> Unit
    open var setData: (data: Any, params: Any, config: Any) -> Promise<Unit>
    open var clearData: () -> Unit
    open var getData: (activeOnly: String /* "visible" | "active" | "selected" | "all" | "range" */) -> Array<Any>
    open var getDataCount: (activeOnly: String /* "visible" | "active" | "selected" | "all" | "range" */) -> Number
    open var searchRows: (field: String, type: String /* "=" | "!=" | "like" | "<" | ">" | "<=" | ">=" | "in" | "regex" | "starts" | "ends" */, value: Any) -> Array<RowComponent>
    open var searchData: (field: String, type: String /* "=" | "!=" | "like" | "<" | ">" | "<=" | ">=" | "in" | "regex" | "starts" | "ends" */, value: Any) -> Array<Any>
    open var getHtml: (rowRangeLookup: String /* "visible" | "active" | "selected" | "all" | "range" */, style: Boolean, config: AdditionalExportOptions) -> Any
    open var print: (rowRangeLookup: String /* "visible" | "active" | "selected" | "all" | "range" */, style: Boolean, config: AdditionalExportOptions) -> Any
    open var getAjaxUrl: () -> String
    open var replaceData: (data: dynamic /* Array<Any> | String */, params: Any, config: Any) -> Promise<Unit>
    open var updateData: (data: Array<Any>) -> Promise<Unit>
    open var addData: (data: Array<Any>, addToTop: Boolean, positionTarget: dynamic /* RowComponent | HTMLElement | String | Number */) -> Promise<Array<RowComponent>>
    open var updateOrAddData: (data: Array<Any>) -> Promise<Array<RowComponent>>
    open var getRow: (row: dynamic /* RowComponent | HTMLElement | String | Number */) -> RowComponent
    open var getRowFromPosition: (position: Number, activeOnly: Boolean) -> RowComponent
    open var deleteRow: (index: dynamic /* RowComponent | HTMLElement | String | Number | Array<dynamic /* RowComponent | HTMLElement | String | Number */> */) -> Unit
    open var addRow: (data: Any, addToTop: Boolean, positionTarget: dynamic /* RowComponent | HTMLElement | String | Number */) -> Promise<RowComponent>
    open var updateOrAddRow: (row: dynamic /* RowComponent | HTMLElement | String | Number */, data: Any) -> Promise<RowComponent>
    open var updateRow: (row: dynamic /* RowComponent | HTMLElement | String | Number */, data: Any) -> Boolean
    open var scrollToRow: (row: dynamic /* RowComponent | HTMLElement | String | Number */, position: String /* "top" | "center" | "bottom" | "nearest" */, ifVisible: Boolean) -> Promise<Unit>
    open var moveRow: (fromRow: dynamic /* RowComponent | HTMLElement | String | Number */, toRow: dynamic /* RowComponent | HTMLElement | String | Number */, placeAboveTarget: Boolean) -> Unit
    open var getRows: (activeOnly: String /* "visible" | "active" | "selected" | "all" | "range" */) -> Array<RowComponent>
    open var getRowPosition: (row: dynamic /* RowComponent | HTMLElement | String | Number */, activeOnly: Boolean) -> dynamic
    open var setColumns: (definitions: Array<ColumnDefinition>) -> Unit
    open var getColumns: (includeColumnGroups: Boolean) -> Array<ColumnComponent>
    open var getColumn: (column: dynamic /* ColumnComponent | ColumnDefinition | HTMLElement | String */) -> ColumnComponent
    open var getColumnDefinitions: () -> Array<ColumnDefinition>
    open var getColumnLayout: () -> Array<ColumnLayout>
    open var setColumnLayout: (layout: Array<ColumnLayout>) -> Unit
    open var showColumn: (column: dynamic /* ColumnComponent | ColumnDefinition | HTMLElement | String */) -> Unit
    open var hideColumn: (column: dynamic /* ColumnComponent | ColumnDefinition | HTMLElement | String */) -> Unit
    open var toggleColumn: (column: dynamic /* ColumnComponent | ColumnDefinition | HTMLElement | String */) -> Unit
    open var addColumn: (definition: ColumnDefinition, insertRightOfTarget: Boolean, positionTarget: dynamic /* ColumnComponent | ColumnDefinition | HTMLElement | String */) -> Promise<Unit>
    open var deleteColumn: (column: dynamic /* ColumnComponent | ColumnDefinition | HTMLElement | String */) -> Promise<Unit>
    open var moveColumn: (fromColumn: dynamic /* ColumnComponent | ColumnDefinition | HTMLElement | String */, toColumn: dynamic /* ColumnComponent | ColumnDefinition | HTMLElement | String */, after: Boolean) -> Unit
    open var scrollToColumn: (column: dynamic /* ColumnComponent | ColumnDefinition | HTMLElement | String */, position: String /* "left" | "center" | "middle" | "right" */, ifVisible: Boolean) -> Promise<Unit>
    open var updateColumnDefinition: (column: dynamic /* ColumnComponent | ColumnDefinition | HTMLElement | String */, definition: ColumnDefinition) -> Promise<Unit>
    open var setLocale: (locale: dynamic /* String | Boolean */) -> Unit
    open var getLocale: () -> String
    open var getLang: (locale: String) -> Any
    open var redraw: (force: Boolean) -> Unit
    open var blockRedraw: () -> Unit
    open var restoreRedraw: () -> Unit
    open var setHeight: (height: dynamic /* Number | String */) -> Unit
    open var setSort: (sortList: dynamic /* String | Array<Sorter> */, dir: String /* "asc" | "desc" */) -> Unit
    open var getSorters: () -> Array<SorterFromTable>
    open var clearSort: () -> Unit
    open var setFilter: (p1: dynamic /* String | Array<Filter> | Array<Any> | (data: Any, filterParams: Any) -> Boolean */, p2: dynamic /* "=" | "!=" | "like" | "<" | ">" | "<=" | ">=" | "in" | "regex" | "starts" | "ends" | Any */, value: Any, filterParams: FilterParams) -> Unit
    open var addFilter: FilterFunction
    open var getFilters: (includeHeaderFilters: Boolean) -> Array<Filter>
    open var setHeaderFilterValue: (column: dynamic /* ColumnComponent | ColumnDefinition | HTMLElement | String */, value: String) -> Unit
    open var setHeaderFilterFocus: (column: dynamic /* ColumnComponent | ColumnDefinition | HTMLElement | String */) -> Unit
    open var getHeaderFilters: () -> Array<Filter>
    open var getHeaderFilterValue: (column: dynamic /* ColumnComponent | ColumnDefinition | HTMLElement | String */) -> String
    open var removeFilter: FilterFunction
    open var clearFilter: (includeHeaderFilters: Boolean) -> Unit
    open var clearHeaderFilter: () -> Unit
    open var selectRow: (lookup: dynamic /* Array<dynamic /* RowComponent | HTMLElement | String | Number */> | RowComponent | HTMLElement | String | Number | "visible" | "active" | "selected" | "all" | "range" | Boolean */) -> Unit
    open var deselectRow: (row: dynamic /* Array<dynamic /* RowComponent | HTMLElement | String | Number */> | RowComponent | HTMLElement | String | Number */) -> Unit
    open var toggleSelectRow: (row: dynamic /* RowComponent | HTMLElement | String | Number */) -> Unit
    open var getSelectedRows: () -> Array<RowComponent>
    open var getSelectedData: () -> Array<Any>
    open var setMaxPage: (max: Number) -> Unit
    open var setPage: (page: dynamic /* Number | "first" | "prev" | "next" | "last" */) -> Promise<Unit>
    open var setPageToRow: (row: dynamic /* RowComponent | HTMLElement | String | Number */) -> Promise<Unit>
    open var setPageSize: (size: Number) -> Unit
    open var getPageSize: () -> Number
    open var previousPage: () -> Promise<Unit>
    open var nextPage: () -> Promise<Unit>
    open var getPage: () -> dynamic
    open var getPageMax: () -> dynamic
    open var setGroupBy: (groups: dynamic /* String | Array<String> | (data: Any) -> Any | Array<dynamic /* String | (data: Any) -> Any */> */) -> Unit
    open var setGroupStartOpen: (values: dynamic /* Boolean | Array<Boolean> | (value: Any, count: Number, data: Any, group: GroupComponent) -> Boolean | Array<dynamic /* Boolean | (value: Any, count: Number, data: Any, group: GroupComponent) -> Boolean */> */) -> Unit
    open var setGroupHeader: (values: dynamic /* (value: Any, count: Number, data: Any, group: GroupComponent) -> String | Array<(value: Any, count: Number, data: Any) -> String> */) -> Unit
    open var getGroups: () -> Array<GroupComponent>
    open var getGroupedData: (activeOnly: Boolean) -> Any
    open var getCalcResults: () -> Any
    open var recalc: () -> Unit
    open var navigatePrev: () -> Unit
    open var navigateNext: () -> Unit
    open var navigateLeft: () -> Unit
    open var navigateRight: () -> Unit
    open var navigateUp: () -> Unit
    open var navigateDown: () -> Unit
    open var getInvalidCells: () -> Array<CellComponent>
    open var clearCellValidation: (clearType: dynamic /* CellComponent | Array<CellComponent> */) -> Unit
    open var validate: () -> dynamic
    open var setGroupValues: (data: GroupValuesArg) -> Unit
    open var refreshFilter: () -> Unit
    open var clearHistory: () -> Unit
    open var addRange: (topLeft: CellComponent, bottomRight: CellComponent) -> RangeComponent
    open var getRanges: () -> Array<RangeComponent>
    open var getRangeData: () -> Array<Array<Any>>
    open var setSheets: (data: Array<SpreadsheetSheet>) -> Unit
    open var addSheet: (data: SpreadsheetSheet) -> Unit
    open var getSheetDefinitions: () -> Array<SpreadsheetSheet>
    open var getSheets: () -> Array<SpreadsheetComponent>
    open var getSheet: (lookup: dynamic /* String | SpreadsheetComponent */) -> SpreadsheetComponent
    open var setSheetData: (lookup: dynamic /* String | SpreadsheetComponent */, data: Array<Array<Any>>) -> Unit
    open var getSheetData: (lookup: dynamic /* String | SpreadsheetComponent */) -> Array<Array<Any>>
    open var clearSheet: (lookup: dynamic /* String | SpreadsheetComponent */) -> Unit
    open var activeSheet: (lookup: dynamic /* String | SpreadsheetComponent */) -> Unit
    open var removeSheet: (lookup: dynamic /* String | SpreadsheetComponent */) -> Unit
//    open var on: (event: K, callback: Any) -> Unit
//    open var off: (event: K, callback: Any) -> Unit

    companion object {
        var defaultOptions: Options
        var extendModule: (name: String, property: String, values: Any) -> Unit
        var findTable: (query: String) -> Array<Tabulator>
        var registerModule: (modules: dynamic /* Any | Array<Any> */) -> Unit
        var bindModules: (__0: Any) -> Unit
    }
}

external class TabulatorFull : Tabulator {
    constructor(selector: String, options: Options = definedExternally)
    constructor(selector: String)
    constructor(selector: HTMLElement, options: Options = definedExternally)
    constructor(selector: HTMLElement)
}

external open class Module(table: Tabulator) {
    open var table: Tabulator
    open fun registerTableOption(propName: String, defaultValue: Any = definedExternally)
    open fun registerTableFunction(functionName: String, callback: (args: Any) -> Any)
    open fun registerColumnOption(propName: String, defaultValue: Any = definedExternally)
    open fun subscribe(eventName: String, callback: (args: Any) -> Any, order: Number = definedExternally)
    open fun unsubscribe(eventName: String, callback: (args: Any) -> Any)
    open fun setOption(key: Nothing?, value: Any)
    open fun reloadData(data: Array<Any>, silent: Boolean, columnsChanged: Boolean): Promise<Unit>
    open fun reloadData(data: String, silent: Boolean, columnsChanged: Boolean): Promise<Unit>
    open fun dispatchExternal(eventName: String, vararg args: Any)
    open fun initialize()

    companion object {
        var moduleName: String
        var moduleInitOrder: Number
    }
}

external open class AccessorModule(table: Tabulator) : Module

external open class AjaxModule(table: Tabulator) : Module

external open class ClipboardModule(table: Tabulator) : Module

external open class ColumnCalcsModule(table: Tabulator) : Module

external open class DataTreeModule(table: Tabulator) : Module

external open class DownloadModule(table: Tabulator) : Module

external open class EditModule(table: Tabulator) : Module

external open class ExportModule(table: Tabulator) : Module

external open class FilterModule(table: Tabulator) : Module

external open class FormatModule(table: Tabulator) : Module

external open class FrozenColumnsModule(table: Tabulator) : Module

external open class FrozenRowsModule(table: Tabulator) : Module

external open class GroupRowsModule(table: Tabulator) : Module

external open class HistoryModule(table: Tabulator) : Module

external open class HtmlTableImportModule(table: Tabulator) : Module

external open class InteractionModule(table: Tabulator) : Module

external open class KeybindingsModule(table: Tabulator) : Module

external open class MenuModule(table: Tabulator) : Module

external open class MoveColumnsModule(table: Tabulator) : Module

external open class MoveRowsModule(table: Tabulator) : Module

external open class MutatorModule(table: Tabulator) : Module

external open class PageModule(table: Tabulator) : Module

external open class PersistenceModule(table: Tabulator) : Module

external open class PopupModule(table: Tabulator) : Module

external open class PrintModule(table: Tabulator) : Module

external open class PseudoRow

external open class ReactiveDataModule(table: Tabulator) : Module

external open class Renderer

external open class ResizeColumnsModule(table: Tabulator) : Module

external open class ResizeRowsModule(table: Tabulator) : Module

external open class ResizeTableModule(table: Tabulator) : Module

external open class ResponsiveLayoutModule(table: Tabulator) : Module

external open class SelectRowModule(table: Tabulator) : Module

external open class SelectRangeModule(table: Tabulator) : Module

external open class SortModule(table: Tabulator) : Module

external open class SpreadsheetModule(table: Tabulator) : Module

external open class TooltipModule(table: Tabulator) : Module

external open class ValidateModule(table: Tabulator) : Module
