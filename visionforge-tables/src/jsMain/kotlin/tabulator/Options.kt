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

external interface Options : OptionsGeneral, OptionsMenu, OptionsHistory, OptionsLocale, OptionsDownload,
    OptionsColumns, OptionsRows, OptionsData, OptionsSorting, OptionsFiltering, OptionsRowGrouping, OptionsPagination,
    OptionsPersistentConfiguration, OptionsClipboard, OptionsDataTree, OptionsDebug, OptionsHTML, OptionsSpreadsheet

external interface OptionsDebug {
    var invalidOptionWarning: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var debugInvalidOptions: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var debugInitialization: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var debugEventsExternal: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var debugEventsInternal: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var debugInvalidComponentFuncs: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var debugDeprecation: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OptionsDataTree {
    var dataTree: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var dataTreeElementColumn: dynamic /* Boolean? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var dataTreeBranchElement: dynamic /* Boolean? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var dataTreeChildIndent: Number?
        get() = definedExternally
        set(value) = definedExternally
    var dataTreeChildField: String?
        get() = definedExternally
        set(value) = definedExternally
    var dataTreeCollapseElement: dynamic /* String? | HTMLElement? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var dataTreeExpandElement: dynamic /* String? | HTMLElement? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var dataTreeStartExpanded: dynamic /* Boolean? | Array<Boolean>? | ((row: RowComponent, level: Number) -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var dataTreeSelectPropagate: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var dataTreeFilter: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var dataTreeSort: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var dataTreeChildColumnCalcs: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OptionsClipboard {
    var clipboard: dynamic /* Boolean? | "copy" | "paste" */
        get() = definedExternally
        set(value) = definedExternally
    var clipboardCopyRowRange: String? /* "visible" | "active" | "selected" | "all" | "range" */
        get() = definedExternally
        set(value) = definedExternally
    var clipboardCopyFormatter: dynamic /* "table" | ((type: String /* "plain" | "html" */, output: String) -> String)? */
        get() = definedExternally
        set(value) = definedExternally
    var clipboardCopyHeader: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var clipboardPasteParser: dynamic /* String? | ((clipboard: Any) -> Array<Any>)? */
        get() = definedExternally
        set(value) = definedExternally
    var clipboardPasteAction: String? /* "insert" | "update" | "replace" | "range" */
        get() = definedExternally
        set(value) = definedExternally
    var clipboardCopyStyled: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var clipboardCopyConfig: dynamic /* AdditionalExportOptions? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var groupHeaderClipboard: dynamic /* ((value: Any, count: Number, data: Any, group: GroupComponent) -> String)? | Array<(value: Any, count: Number, data: Any) -> String>? */
        get() = definedExternally
        set(value) = definedExternally
    var groupHeaderHtmlOutput: dynamic /* ((value: Any, count: Number, data: Any, group: GroupComponent) -> String)? | Array<(value: Any, count: Number, data: Any) -> String>? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface OptionsPersistentConfiguration {
    var persistenceID: String?
        get() = definedExternally
        set(value) = definedExternally
    var persistenceMode: dynamic /* "local" | "cookie" | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var persistentLayout: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var persistentSort: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var persistentFilter: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var persistence: dynamic /* Boolean? | PersistenceOptions? */
        get() = definedExternally
        set(value) = definedExternally
    var persistenceWriterFunc: ((id: String, type: String? /* "sort" | "filter" | "group" | "page" | "columns" | "headerFilter" */, data: Any) -> Any)?
        get() = definedExternally
        set(value) = definedExternally
    var persistenceReaderFunc: ((id: String, type: String? /* "sort" | "filter" | "group" | "page" | "columns" | "headerFilter" */) -> Any)?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PersistenceOptions {
    var sort: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var filter: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var group: dynamic /* Boolean? | PersistenceGroupOptions? */
        get() = definedExternally
        set(value) = definedExternally
    var page: dynamic /* Boolean? | PersistencePageOptions? */
        get() = definedExternally
        set(value) = definedExternally
    var columns: dynamic /* Boolean? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally
    var headerFilter: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PersistenceGroupOptions {
    var groupBy: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var groupStartOpen: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var groupHeader: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PersistencePageOptions {
    var size: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var page: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OptionsPagination {
    var pagination: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var paginationMode: String? /* "remote" | "local" */
        get() = definedExternally
        set(value) = definedExternally
    var paginationSize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var paginationSizeSelector: dynamic /* Boolean? | Array<Number>? | Array<Any>? */
        get() = definedExternally
        set(value) = definedExternally
    var paginationElement: dynamic /* HTMLElement? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var dataReceiveParams: Record<String, String>?
        get() = definedExternally
        set(value) = definedExternally
    var dataSendParams: Record<String, String>?
        get() = definedExternally
        set(value) = definedExternally
    var paginationAddRow: String? /* "table" | "page" */
        get() = definedExternally
        set(value) = definedExternally
    var paginationCounter: dynamic /* "rows" | "pages" | ((pageSize: Number, currentRow: Number, currentPage: Number, totalRows: Number, totalPages: Number) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var paginationCounterElement: dynamic /* String? | HTMLElement? */
        get() = definedExternally
        set(value) = definedExternally
    var paginationButtonCount: Number?
        get() = definedExternally
        set(value) = definedExternally
    var paginationInitialPage: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OptionsRowGrouping {
    var groupBy: dynamic /* String? | Array<String>? | ((data: Any) -> Any)? | Array<dynamic /* String | (data: Any) -> Any */>? */
        get() = definedExternally
        set(value) = definedExternally
    var groupValues: GroupValuesArg?
        get() = definedExternally
        set(value) = definedExternally
    var groupHeader: dynamic /* ((value: Any, count: Number, data: Any, group: GroupComponent) -> String)? | Array<(value: Any, count: Number, data: Any) -> String>? */
        get() = definedExternally
        set(value) = definedExternally
    var groupHeaderPrint: dynamic /* ((value: Any, count: Number, data: Any, group: GroupComponent) -> String)? | Array<(value: Any, count: Number, data: Any) -> String>? */
        get() = definedExternally
        set(value) = definedExternally
    var groupStartOpen: dynamic /* Boolean? | Array<Boolean>? | ((value: Any, count: Number, data: Any, group: GroupComponent) -> Boolean)? | Array<dynamic /* Boolean | (value: Any, count: Number, data: Any, group: GroupComponent) -> Boolean */>? */
        get() = definedExternally
        set(value) = definedExternally
    var groupToggleElement: dynamic /* "arrow" | "header" | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var groupClosedShowCalcs: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var groupUpdateOnCellEdit: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Filter {
    var field: String
    var type: String /* "=" | "!=" | "like" | "<" | ">" | "<=" | ">=" | "in" | "regex" | "starts" | "ends" */
    var value: Any
}

external interface FilterParams {
    var separator: String?
        get() = definedExternally
        set(value) = definedExternally
    var matchAll: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OptionsFiltering {
    var initialFilter: Array<Filter>?
        get() = definedExternally
        set(value) = definedExternally
    var initialHeaderFilter: dynamic //Array<Pick<Filter, String? /* "field" | "value" */>>?
        get() = definedExternally
        set(value) = definedExternally
    var headerFilterLiveFilterDelay: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OptionsSorting {
    var initialSort: Array<Sorter>?
        get() = definedExternally
        set(value) = definedExternally
    var sortOrderReverse: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var headerSortClickElement: String? /* "header" | "icon" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface Sorter {
    var column: String
    var dir: String /* "asc" | "desc" */
}

external interface SorterFromTable {
    var column: ColumnComponent
    var field: String
    var dir: String /* "asc" | "desc" */
}

external interface OptionsData {
    var index: dynamic /* Number? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var data: Array<Any>?
        get() = definedExternally
        set(value) = definedExternally
    var importFormat: dynamic /* "array" | "csv" | "json" | ((fileContents: String) -> Array<Any>)? */
        get() = definedExternally
        set(value) = definedExternally
    var importReader: String? /* "binary" | "buffer" | "text" | "url" */
        get() = definedExternally
        set(value) = definedExternally
    var autoTables: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var ajaxURL: String?
        get() = definedExternally
        set(value) = definedExternally
    var ajaxParams: Any?
        get() = definedExternally
        set(value) = definedExternally
    var ajaxConfig: dynamic /* "GET" | "POST" | AjaxConfig? */
        get() = definedExternally
        set(value) = definedExternally
    var ajaxContentType: dynamic /* "form" | "json" | AjaxContentType? */
        get() = definedExternally
        set(value) = definedExternally
    var ajaxURLGenerator: ((url: String, config: Any, params: Any) -> String)?
        get() = definedExternally
        set(value) = definedExternally
    var ajaxRequestFunc: ((url: String, config: Any, params: Any) -> Promise<Any>)?
        get() = definedExternally
        set(value) = definedExternally
    var ajaxFiltering: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var ajaxSorting: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var progressiveLoad: String? /* "load" | "scroll" */
        get() = definedExternally
        set(value) = definedExternally
    var progressiveLoadDelay: Number?
        get() = definedExternally
        set(value) = definedExternally
    var progressiveLoadScrollMargin: Number?
        get() = definedExternally
        set(value) = definedExternally
    var ajaxLoader: dynamic /* Boolean? | (() -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var ajaxLoaderLoading: String?
        get() = definedExternally
        set(value) = definedExternally
    var ajaxLoaderError: String?
        get() = definedExternally
        set(value) = definedExternally
    var ajaxRequesting: ((url: String, params: Any) -> Boolean)?
        get() = definedExternally
        set(value) = definedExternally
    var ajaxResponse: ((url: String, params: Any, response: Any) -> Any)?
        get() = definedExternally
        set(value) = definedExternally
    var dataLoader: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var dataLoaderLoading: dynamic /* String? | HTMLElement? */
        get() = definedExternally
        set(value) = definedExternally
    var dataLoaderError: String?
        get() = definedExternally
        set(value) = definedExternally
    var dataLoaderErrorTimeout: Number?
        get() = definedExternally
        set(value) = definedExternally
    var sortMode: String? /* "remote" | "local" */
        get() = definedExternally
        set(value) = definedExternally
    var filterMode: String? /* "remote" | "local" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface AjaxContentType {
    var headers: JSONRecord
    var body: (url: String, config: Any, params: Any) -> Any
}

external interface AjaxConfig {
    var method: String? /* "GET" | "POST" */
        get() = definedExternally
        set(value) = definedExternally
    var headers: JSONRecord?
        get() = definedExternally
        set(value) = definedExternally
    var mode: String?
        get() = definedExternally
        set(value) = definedExternally
    var credentials: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OptionsRows {
    var rowFormatter: ((row: RowComponent) -> Any)?
        get() = definedExternally
        set(value) = definedExternally
    var rowFormatterPrint: dynamic /* Boolean? | ((row: RowComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var rowFormatterHtmlOutput: dynamic /* Boolean? | ((row: RowComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var rowFormatterClipboard: dynamic /* Boolean? | ((row: RowComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var addRowPos: String? /* "bottom" | "top" */
        get() = definedExternally
        set(value) = definedExternally
    var selectable: dynamic /* Boolean? | Number? | "highlight" */
        get() = definedExternally
        set(value) = definedExternally
    var selectableRows: dynamic /* Boolean? | Number? | "highlight" */
        get() = definedExternally
        set(value) = definedExternally
    var selectableRange: dynamic /* Boolean? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var selectableRangeColumns: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var selectableRangeRows: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var selectableRangeClearCells: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var selectableRangeClearCellsValue: Any?
        get() = definedExternally
        set(value) = definedExternally
    var selectableRangeMode: String? /* "click" */
        get() = definedExternally
        set(value) = definedExternally
    var selectableRowsRangeMode: String? /* "click" */
        get() = definedExternally
        set(value) = definedExternally
    var selectableRollingSelection: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var selectableRowsRollingSelection: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var selectablePersistence: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var selectableRowsPersistence: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var selectableCheck: ((row: RowComponent) -> Boolean)?
        get() = definedExternally
        set(value) = definedExternally
    var movableRows: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var movableRowsConnectedTables: dynamic /* String? | Array<String>? | HTMLElement? | Array<HTMLElement>? */
        get() = definedExternally
        set(value) = definedExternally
    var movableRowsSender: dynamic /* Boolean? | "delete" | ((fromRow: RowComponent, toRow: RowComponent, toTable: Tabulator) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var movableRowsReceiver: dynamic /* "insert" | "add" | "update" | "replace" | ((fromRow: RowComponent, toRow: RowComponent, fromTable: Tabulator) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var movableRowsConnectedElements: dynamic /* String? | HTMLElement? */
        get() = definedExternally
        set(value) = definedExternally
    var resizableRows: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var resizableRowGuide: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var resizableColumnGuide: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var scrollToRowPosition: String? /* "top" | "center" | "bottom" | "nearest" */
        get() = definedExternally
        set(value) = definedExternally
    var scrollToRowIfVisible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var tabEndNewRow: dynamic /* Boolean? | JSONRecord? | ((row: RowComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var frozenRowsField: String?
        get() = definedExternally
        set(value) = definedExternally
    var frozenRows: dynamic /* Number? | Array<String>? | ((row: RowComponent) -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var editTriggerEvent: String? /* "click" | "dblclick" | "focus" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface OptionsColumns {
    var columns: Array<ColumnDefinition>?
        get() = definedExternally
        set(value) = definedExternally
    var autoColumns: dynamic /* Boolean? | "full" */
        get() = definedExternally
        set(value) = definedExternally
    var autoColumnsDefinitions: dynamic /* ((columnDefinitions: Array<ColumnDefinition>) -> Array<ColumnDefinition>)? | Array<ColumnDefinition>? | Record<String, ColumnDefinitionPartial>? */
        get() = definedExternally
        set(value) = definedExternally
    var layout: String? /* "fitData" | "fitColumns" | "fitDataFill" | "fitDataStretch" | "fitDataTable" */
        get() = definedExternally
        set(value) = definedExternally
    var layoutColumnsOnNewData: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var responsiveLayout: dynamic /* Boolean? | "hide" | "collapse" */
        get() = definedExternally
        set(value) = definedExternally
    var responsiveLayoutCollapseStartOpen: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var responsiveLayoutCollapseUseFormatters: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var responsiveLayoutCollapseFormatter: ((data: Array<Any>) -> Any)?
        get() = definedExternally
        set(value) = definedExternally
    var movableColumns: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var columnHeaderVertAlign: String? /* "top" | "middle" | "bottom" */
        get() = definedExternally
        set(value) = definedExternally
    var scrollToColumnPosition: String? /* "left" | "center" | "middle" | "right" */
        get() = definedExternally
        set(value) = definedExternally
    var scrollToColumnIfVisible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var columnCalcs: dynamic /* Boolean? | "both" | "table" | "group" */
        get() = definedExternally
        set(value) = definedExternally
    var nestedFieldSeparator: dynamic /* String? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var columnHeaderSortMulti: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var headerVisible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var headerSort: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var headerSortElement: dynamic /* String? | ((column: ColumnComponent, dir: String /* "asc" | "desc" | "none" */) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var columnDefaults: ColumnDefinitionPartial?
        get() = definedExternally
        set(value) = definedExternally
    var resizableColumnFit: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OptionsGeneral {
    var height: dynamic /* String? | Number? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var maxHeight: dynamic /* String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var minHeight: dynamic /* String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var renderVertical: dynamic /* "virtual" | "basic" | Renderer? */
        get() = definedExternally
        set(value) = definedExternally
    var renderHorizontal: dynamic /* "virtual" | "basic" | Renderer? */
        get() = definedExternally
        set(value) = definedExternally
    var rowHeight: Number?
        get() = definedExternally
        set(value) = definedExternally
    var renderVerticalBuffer: dynamic /* Boolean? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var placeholder: dynamic /* String? | HTMLElement? | ((this: dynamic /* Tabulator | TabulatorFull */) -> String)? */
        get() = definedExternally
        set(value) = definedExternally
    var placeholderHeaderFilter: dynamic /* String? | HTMLElement? | ((this: dynamic /* Tabulator | TabulatorFull */) -> String)? */
        get() = definedExternally
        set(value) = definedExternally
    var footerElement: dynamic /* String? | HTMLElement? */
        get() = definedExternally
        set(value) = definedExternally
    var keybindings: dynamic /* Boolean? | KeyBinding? */
        get() = definedExternally
        set(value) = definedExternally
    var reactiveData: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var autoResize: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var invalidOptionWarnings: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var validationMode: String? /* "blocking" | "highlight" | "manual" */
        get() = definedExternally
        set(value) = definedExternally
    var textDirection: String? /* "auto" | "ltr" | "rtl" */
        get() = definedExternally
        set(value) = definedExternally
    var rowHeader: dynamic /* Boolean? | `T$0`? */
        get() = definedExternally
        set(value) = definedExternally
    var editorEmptyValue: Any?
        get() = definedExternally
        set(value) = definedExternally
    var editorEmptyValueFunc: ((value: Any) -> Boolean)?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$1` {
    var editor: String
    var resizable: String
}

external interface OptionsSpreadsheet {
    var spreadsheet: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var spreadsheetRows: Number?
        get() = definedExternally
        set(value) = definedExternally
    var spreadsheetColumns: Number?
        get() = definedExternally
        set(value) = definedExternally
    var spreadsheetColumnDefinition: `T$1`?
        get() = definedExternally
        set(value) = definedExternally
    var spreadsheetSheets: Array<SpreadsheetSheet>?
        get() = definedExternally
        set(value) = definedExternally
    var spreadsheetSheetTabs: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var spreadsheetOutputFull: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SpreadsheetSheet {
    var title: String
    var key: String
    var rows: Number?
        get() = definedExternally
        set(value) = definedExternally
    var columns: Number?
        get() = definedExternally
        set(value) = definedExternally
    var data: Array<Array<Any>>
}

external interface SpreadsheetComponent {
    fun getTitle(): String
    fun setTitle(title: String)
    fun getKey(): String
    fun getDefinition(): SpreadsheetSheet
    fun setRows(rows: Number)
    fun setColumns(columns: Number)
    fun getData(): Array<Array<Any>>
    fun setData(data: Array<Array<Any>>)
    fun clear()
    fun remove()
    fun active()
}

external interface OptionsMenu {
    var rowContextMenu: dynamic /* Array<dynamic /* MenuObject<RowComponent> | MenuSeparator */>? | ((e: MouseEvent, component: RowComponent) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var rowClickMenu: dynamic /* Array<dynamic /* MenuObject<RowComponent> | MenuSeparator */>? | ((e: MouseEvent, component: RowComponent) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var rowDblClickMenu: dynamic /* Array<dynamic /* MenuObject<RowComponent> | MenuSeparator */>? | ((e: MouseEvent, component: RowComponent) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var groupClickMenu: dynamic /* Array<dynamic /* MenuObject<GroupComponent> | MenuSeparator */>? | ((e: MouseEvent, component: GroupComponent) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var groupDblClickMenu: dynamic /* Array<dynamic /* MenuObject<GroupComponent> | MenuSeparator */>? | ((e: MouseEvent, component: GroupComponent) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var groupContextMenu: Array<MenuObject<GroupComponent>>?
        get() = definedExternally
        set(value) = definedExternally
    var popupContainer: dynamic /* Boolean? | String? | HTMLElement? */
        get() = definedExternally
        set(value) = definedExternally
    var groupClickPopup: String?
        get() = definedExternally
        set(value) = definedExternally
    var groupContextPopup: String?
        get() = definedExternally
        set(value) = definedExternally
    var groupDblPopup: String?
        get() = definedExternally
        set(value) = definedExternally
    var groupDblClickPopup: String?
        get() = definedExternally
        set(value) = definedExternally
    var rowClickPopup: String?
        get() = definedExternally
        set(value) = definedExternally
    var rowContextPopup: String?
        get() = definedExternally
        set(value) = definedExternally
    var rowDblClickPopup: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface MenuObject<T> {
    var label: dynamic /* String | HTMLElement | (component: T) -> dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var action: ((e: Any, component: T) -> Any)?
        get() = definedExternally
        set(value) = definedExternally
    var disabled: dynamic /* Boolean? | ((component: T) -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var menu: Array<MenuObject<T>>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface MenuSeparator {
    var separator: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface DownloadOptions : DownloadCSV, DownloadXLXS, DownloadPDF, DownloadHTML {
    override var documentProcessing: ((input: Any) -> Any)?
        get() = definedExternally
        set(value) = definedExternally
}

external interface DownloadCSV {
    var delimiter: String?
        get() = definedExternally
        set(value) = definedExternally
    var bom: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface DownloadHTML {
    var style: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface DownloadXLXS {
    var sheetName: String?
        get() = definedExternally
        set(value) = definedExternally
    var documentProcessing: ((input: Any) -> Any)?
        get() = definedExternally
        set(value) = definedExternally
    var compress: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var writeOptions: Record<String, Any>?
        get() = definedExternally
        set(value) = definedExternally
    var test: Any?
        get() = definedExternally
        set(value) = definedExternally
}

external interface DownloadPDF {
    var orientation: String? /* "portrait" | "landscape" */
        get() = definedExternally
        set(value) = definedExternally
    var title: String?
        get() = definedExternally
        set(value) = definedExternally
    var rowGroupStyles: Any?
        get() = definedExternally
        set(value) = definedExternally
    var rowCalcStyles: Any?
        get() = definedExternally
        set(value) = definedExternally
    var jsPDF: Any?
        get() = definedExternally
        set(value) = definedExternally
    var autoTable: dynamic /* Any? | ((doc: Any) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var documentProcessing: ((doc: Any) -> Any)?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OptionsDownload {
    var downloadEncoder: ((fileContents: Any, mimeType: String) -> dynamic)?
        get() = definedExternally
        set(value) = definedExternally
    var downloadConfig: AdditionalExportOptions?
        get() = definedExternally
        set(value) = definedExternally
    var downloadRowRange: String? /* "visible" | "active" | "selected" | "all" | "range" */
        get() = definedExternally
        set(value) = definedExternally
    var downloadDataFormatter: ((data: Any) -> Any)?
        get() = definedExternally
        set(value) = definedExternally
    var downloadReady: ((fileContents: Any, blob: Blob) -> dynamic)?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OptionsHTML {
    var htmlOutputConfig: AdditionalExportOptions?
        get() = definedExternally
        set(value) = definedExternally
    var printAsHtml: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var printConfig: AdditionalExportOptions?
        get() = definedExternally
        set(value) = definedExternally
    var printStyled: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var printRowRange: dynamic /* "visible" | "active" | "selected" | "all" | "range" | (() -> Array<RowComponent>)? */
        get() = definedExternally
        set(value) = definedExternally
    var printHeader: dynamic /* String? | HTMLElement? | (() -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var printFooter: dynamic /* String? | HTMLElement? | (() -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var printFormatter: ((tableHolderElement: Any, tableElement: Any) -> Any)?
        get() = definedExternally
        set(value) = definedExternally
    var groupHeaderDownload: dynamic /* ((value: Any, count: Number, data: Any, group: GroupComponent) -> String)? | Array<(value: Any, count: Number, data: Any) -> String>? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface AdditionalExportOptions {
    var columnHeaders: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var columnGroups: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var rowGroups: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var columnCalcs: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var dataTree: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var rowHeaders: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var formatCells: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OptionsLocale {
    var locale: dynamic /* Boolean? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var langs: Any?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OptionsHistory {
    var history: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ColumnLayout {
    var title: String
    var field: String?
        get() = definedExternally
        set(value) = definedExternally
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var width: dynamic /* Number? | String? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface ColumnLayoutPartial {
    var title: String?
        get() = definedExternally
        set(value) = definedExternally
    var field: String?
        get() = definedExternally
        set(value) = definedExternally
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var width: dynamic /* Number? | String? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface ColumnDefinition : ColumnLayout, CellCallbacks {
    var hozAlign: String? /* "left" | "center" | "right" */
        get() = definedExternally
        set(value) = definedExternally
    var headerHozAlign: String? /* "left" | "center" | "right" */
        get() = definedExternally
        set(value) = definedExternally
    var vertAlign: String? /* "top" | "middle" | "bottom" */
        get() = definedExternally
        set(value) = definedExternally
    var minWidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var widthGrow: Number?
        get() = definedExternally
        set(value) = definedExternally
    var widthShrink: Number?
        get() = definedExternally
        set(value) = definedExternally
    var resizable: dynamic /* Boolean? | "header" | "cell" */
        get() = definedExternally
        set(value) = definedExternally
    var frozen: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var responsive: Number?
        get() = definedExternally
        set(value) = definedExternally
    var tooltip: dynamic /* String? | Boolean? | ((event: MouseEvent, cell: CellComponent, onRender: () -> Unit) -> String)? */
        get() = definedExternally
        set(value) = definedExternally
    var cssClass: String?
        get() = definedExternally
        set(value) = definedExternally
    var rowHandle: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var hideInHtml: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var sorter: dynamic /* "string" | "number" | "alphanum" | "boolean" | "exists" | "date" | "time" | "datetime" | "array" | ((a: Any, b: Any, aRow: RowComponent, bRow: RowComponent, column: ColumnComponent, dir: String /* "asc" | "desc" */, sorterParams: Any) -> Number)? */
        get() = definedExternally
        set(value) = definedExternally
    var sorterParams: dynamic /* ColumnDefinitionSorterParams? | ColumnSorterParamLookupFunction? */
        get() = definedExternally
        set(value) = definedExternally
    var formatter: dynamic /* "plaintext" | "textarea" | "html" | "money" | "image" | "datetime" | "datetimediff" | "link" | "tickCross" | "color" | "star" | "traffic" | "progress" | "lookup" | "buttonTick" | "buttonCross" | "rownum" | "handle" | "rowSelection" | "responsiveCollapse" | "toggle" | ((cell: CellComponent, formatterParams: Any, onRendered: EmptyCallback) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var formatterParams: dynamic /* MoneyParams? | ImageParams? | LinkParams? | DateTimeParams? | DateTimeDifferenceParams? | TickCrossParams? | TrafficParams? | ProgressBarParams? | StarRatingParams? | RowSelectionParams? | JSONRecord? | ToggleSwitchParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var variableHeight: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var editable: dynamic /* Boolean? | ((cell: CellComponent) -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var editor: dynamic /* Boolean? | "input" | "textarea" | "number" | "range" | "tickCross" | "star" | "list" | "date" | "time" | "datetime" | ((cell: CellComponent, onRendered: EmptyCallback, success: ValueBooleanCallback, cancel: ValueVoidCallback, editorParams: Any) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var editorParams: dynamic /* NumberParams? | CheckboxParams? | ListEditorParams? | InputParams? | TextAreaParams? | DateParams? | TimeParams? | DateTimeEditorParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var validator: dynamic /* "required" | "unique" | "integer" | "float" | "numeric" | "string" | "alphanumeric" | Array<String /* "required" | "unique" | "integer" | "float" | "numeric" | "string" | "alphanumeric" */>? | Validator? | Array<Validator>? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var mutator: CustomMutator?
        get() = definedExternally
        set(value) = definedExternally
    var mutatorParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "edit" */, cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var mutatorData: CustomMutator?
        get() = definedExternally
        set(value) = definedExternally
    var mutatorDataParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "edit" */, cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var mutatorEdit: CustomMutator?
        get() = definedExternally
        set(value) = definedExternally
    var mutatorEditParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "edit" */, cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var mutatorClipboard: CustomMutator?
        get() = definedExternally
        set(value) = definedExternally
    var mutatorClipboardParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "edit" */, cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var accessor: dynamic /* CustomAccessor? | "rownum" */
        get() = definedExternally
        set(value) = definedExternally
    var accessorParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "download" | "clipboard" */, column: ColumnComponent, row: RowComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var accessorDownload: CustomAccessor?
        get() = definedExternally
        set(value) = definedExternally
    var accessorDownloadParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "download" | "clipboard" */, column: ColumnComponent, row: RowComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var accessorClipboard: CustomAccessor?
        get() = definedExternally
        set(value) = definedExternally
    var accessorClipboardParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "download" | "clipboard" */, column: ColumnComponent, row: RowComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var download: dynamic /* Boolean? | ((column: ColumnComponent) -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var titleDownload: String?
        get() = definedExternally
        set(value) = definedExternally
    var topCalc: dynamic /* "avg" | "max" | "min" | "sum" | "concat" | "count" | "unique" | ((values: Array<Any>, data: Array<Any>, calcParams: Any) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var topCalcParams: dynamic /* `T$3`? | ((values: Any, data: Any) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var topCalcFormatter: dynamic /* "plaintext" | "textarea" | "html" | "money" | "image" | "datetime" | "datetimediff" | "link" | "tickCross" | "color" | "star" | "traffic" | "progress" | "lookup" | "buttonTick" | "buttonCross" | "rownum" | "handle" | "rowSelection" | "responsiveCollapse" | "toggle" | ((cell: CellComponent, formatterParams: Any, onRendered: EmptyCallback) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var topCalcFormatterParams: dynamic /* MoneyParams? | ImageParams? | LinkParams? | DateTimeParams? | DateTimeDifferenceParams? | TickCrossParams? | TrafficParams? | ProgressBarParams? | StarRatingParams? | RowSelectionParams? | JSONRecord? | ToggleSwitchParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var bottomCalc: dynamic /* "avg" | "max" | "min" | "sum" | "concat" | "count" | "unique" | ((values: Array<Any>, data: Array<Any>, calcParams: Any) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var bottomCalcParams: dynamic /* `T$3`? | ((values: Any, data: Any) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var bottomCalcFormatter: dynamic /* "plaintext" | "textarea" | "html" | "money" | "image" | "datetime" | "datetimediff" | "link" | "tickCross" | "color" | "star" | "traffic" | "progress" | "lookup" | "buttonTick" | "buttonCross" | "rownum" | "handle" | "rowSelection" | "responsiveCollapse" | "toggle" | ((cell: CellComponent, formatterParams: Any, onRendered: EmptyCallback) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var bottomCalcFormatterParams: dynamic /* MoneyParams? | ImageParams? | LinkParams? | DateTimeParams? | DateTimeDifferenceParams? | TickCrossParams? | TrafficParams? | ProgressBarParams? | StarRatingParams? | RowSelectionParams? | JSONRecord? | ToggleSwitchParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var headerSort: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var headerSortStartingDir: String? /* "asc" | "desc" */
        get() = definedExternally
        set(value) = definedExternally
    var headerSortTristate: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var headerClick: ColumnEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerDblClick: ColumnEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerMouseDown: ColumnEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerMouseUp: ColumnEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerContext: ColumnEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerTap: ColumnEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerDblTap: ColumnEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerTapHold: ColumnEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerTooltip: dynamic /* Boolean? | String? | ((column: ColumnComponent) -> String)? */
        get() = definedExternally
        set(value) = definedExternally
    var headerVertical: dynamic /* Boolean? | "flip" */
        get() = definedExternally
        set(value) = definedExternally
    var editableTitle: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var titleFormatter: dynamic /* "plaintext" | "textarea" | "html" | "money" | "image" | "datetime" | "datetimediff" | "link" | "tickCross" | "color" | "star" | "traffic" | "progress" | "lookup" | "buttonTick" | "buttonCross" | "rownum" | "handle" | "rowSelection" | "responsiveCollapse" | "toggle" | ((cell: CellComponent, formatterParams: Any, onRendered: EmptyCallback) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var titleFormatterParams: dynamic /* MoneyParams? | ImageParams? | LinkParams? | DateTimeParams? | DateTimeDifferenceParams? | TickCrossParams? | TrafficParams? | ProgressBarParams? | StarRatingParams? | RowSelectionParams? | JSONRecord? | ToggleSwitchParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var headerFilter: dynamic /* Boolean? | "input" | "textarea" | "number" | "range" | "tickCross" | "star" | "list" | "date" | "time" | "datetime" | ((cell: CellComponent, onRendered: EmptyCallback, success: ValueBooleanCallback, cancel: ValueVoidCallback, editorParams: Any) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var headerFilterParams: dynamic /* NumberParams? | CheckboxParams? | ListEditorParams? | InputParams? | TextAreaParams? | DateParams? | TimeParams? | DateTimeEditorParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var headerFilterPlaceholder: String?
        get() = definedExternally
        set(value) = definedExternally
    var headerFilterEmptyCheck: ValueBooleanCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerFilterFunc: dynamic /* "=" | "!=" | "like" | "<" | ">" | "<=" | ">=" | "in" | "regex" | "starts" | "ends" | ((headerValue: Any, rowValue: Any, rowData: Any, filterParams: Any) -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var headerFilterFuncParams: Any?
        get() = definedExternally
        set(value) = definedExternally
    var headerFilterLiveFilter: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var htmlOutput: dynamic /* Boolean? | ((column: ColumnComponent) -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var clipboard: dynamic /* Boolean? | ((column: ColumnComponent) -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var print: dynamic /* Boolean? | ((column: ColumnComponent) -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var columns: Array<ColumnDefinition>?
        get() = definedExternally
        set(value) = definedExternally
    var headerMenu: dynamic /* Array<dynamic /* MenuObject<ColumnComponent> | MenuSeparator */>? | (() -> Array<dynamic /* MenuObject<ColumnComponent> | MenuSeparator */>)? */
        get() = definedExternally
        set(value) = definedExternally
    var headerMenuIcon: dynamic /* String? | HTMLElement? | ((component: ColumnComponent) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var headerContextMenu: Array<dynamic /* MenuObject<ColumnComponent> | MenuSeparator */>?
        get() = definedExternally
        set(value) = definedExternally
    var headerDblClickPopup: String?
        get() = definedExternally
        set(value) = definedExternally
    var dblClickPopup: String?
        get() = definedExternally
        set(value) = definedExternally
    var contextMenu: Array<dynamic /* MenuObject<CellComponent> | MenuSeparator */>?
        get() = definedExternally
        set(value) = definedExternally
    var clickMenu: Array<dynamic /* MenuObject<CellComponent> | MenuSeparator */>?
        get() = definedExternally
        set(value) = definedExternally
    var headerDblClickMenu: Array<dynamic /* MenuObject<CellComponent> | MenuSeparator */>?
        get() = definedExternally
        set(value) = definedExternally
    var dblClickMenu: Array<dynamic /* MenuObject<CellComponent> | MenuSeparator */>?
        get() = definedExternally
        set(value) = definedExternally
    var cellPopup: dynamic /* String? | ((e: MouseEvent, component: dynamic /* RowComponent | CellComponent | ColumnComponent */, onRendered: () -> Any) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var formatterClipboard: dynamic /* "plaintext" | "textarea" | "html" | "money" | "image" | "datetime" | "datetimediff" | "link" | "tickCross" | "color" | "star" | "traffic" | "progress" | "lookup" | "buttonTick" | "buttonCross" | "rownum" | "handle" | "rowSelection" | "responsiveCollapse" | "toggle" | ((cell: CellComponent, formatterParams: Any, onRendered: EmptyCallback) -> dynamic)? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var formatterClipboardParams: dynamic /* MoneyParams? | ImageParams? | LinkParams? | DateTimeParams? | DateTimeDifferenceParams? | TickCrossParams? | TrafficParams? | ProgressBarParams? | StarRatingParams? | RowSelectionParams? | JSONRecord? | ToggleSwitchParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var formatterPrint: dynamic /* "plaintext" | "textarea" | "html" | "money" | "image" | "datetime" | "datetimediff" | "link" | "tickCross" | "color" | "star" | "traffic" | "progress" | "lookup" | "buttonTick" | "buttonCross" | "rownum" | "handle" | "rowSelection" | "responsiveCollapse" | "toggle" | ((cell: CellComponent, formatterParams: Any, onRendered: EmptyCallback) -> dynamic)? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var formatterPrintParams: dynamic /* MoneyParams? | ImageParams? | LinkParams? | DateTimeParams? | DateTimeDifferenceParams? | TickCrossParams? | TrafficParams? | ProgressBarParams? | StarRatingParams? | RowSelectionParams? | JSONRecord? | ToggleSwitchParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var accessorPrint: CustomAccessor?
        get() = definedExternally
        set(value) = definedExternally
    var accessorPrintParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "download" | "clipboard" */, column: ColumnComponent, row: RowComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var accessorHtmlOutput: CustomAccessor?
        get() = definedExternally
        set(value) = definedExternally
    var accessorHtmlOutputParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "download" | "clipboard" */, column: ColumnComponent, row: RowComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var formatterHtmlOutput: dynamic /* "plaintext" | "textarea" | "html" | "money" | "image" | "datetime" | "datetimediff" | "link" | "tickCross" | "color" | "star" | "traffic" | "progress" | "lookup" | "buttonTick" | "buttonCross" | "rownum" | "handle" | "rowSelection" | "responsiveCollapse" | "toggle" | ((cell: CellComponent, formatterParams: Any, onRendered: EmptyCallback) -> dynamic)? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var formatterHtmlOutputParams: dynamic /* MoneyParams? | ImageParams? | LinkParams? | DateTimeParams? | DateTimeDifferenceParams? | TickCrossParams? | TrafficParams? | ProgressBarParams? | StarRatingParams? | RowSelectionParams? | JSONRecord? | ToggleSwitchParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var titleClipboard: String?
        get() = definedExternally
        set(value) = definedExternally
    var titleHtmlOutput: String?
        get() = definedExternally
        set(value) = definedExternally
    var titlePrint: String?
        get() = definedExternally
        set(value) = definedExternally
    var maxWidth: dynamic /* Number? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var headerWordWrap: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var editorEmptyValue: Any?
        get() = definedExternally
        set(value) = definedExternally
    var editorEmptyValueFunc: ((value: Any) -> Boolean)?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ColumnDefinitionPartial : ColumnLayoutPartial, CellCallbacksPartial {
    var hozAlign: String? /* "left" | "center" | "right" */
        get() = definedExternally
        set(value) = definedExternally
    var headerHozAlign: String? /* "left" | "center" | "right" */
        get() = definedExternally
        set(value) = definedExternally
    var vertAlign: String? /* "top" | "middle" | "bottom" */
        get() = definedExternally
        set(value) = definedExternally
    var minWidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var widthGrow: Number?
        get() = definedExternally
        set(value) = definedExternally
    var widthShrink: Number?
        get() = definedExternally
        set(value) = definedExternally
    var resizable: dynamic /* Boolean? | "header" | "cell" */
        get() = definedExternally
        set(value) = definedExternally
    var frozen: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var responsive: Number?
        get() = definedExternally
        set(value) = definedExternally
    var tooltip: dynamic /* String? | Boolean? | ((event: MouseEvent, cell: CellComponent, onRender: () -> Unit) -> String)? */
        get() = definedExternally
        set(value) = definedExternally
    var cssClass: String?
        get() = definedExternally
        set(value) = definedExternally
    var rowHandle: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var hideInHtml: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var sorter: dynamic /* "string" | "number" | "alphanum" | "boolean" | "exists" | "date" | "time" | "datetime" | "array" | ((a: Any, b: Any, aRow: RowComponent, bRow: RowComponent, column: ColumnComponent, dir: String /* "asc" | "desc" */, sorterParams: Any) -> Number)? */
        get() = definedExternally
        set(value) = definedExternally
    var sorterParams: dynamic /* ColumnDefinitionSorterParams? | ColumnSorterParamLookupFunction? */
        get() = definedExternally
        set(value) = definedExternally
    var formatter: dynamic /* "plaintext" | "textarea" | "html" | "money" | "image" | "datetime" | "datetimediff" | "link" | "tickCross" | "color" | "star" | "traffic" | "progress" | "lookup" | "buttonTick" | "buttonCross" | "rownum" | "handle" | "rowSelection" | "responsiveCollapse" | "toggle" | ((cell: CellComponent, formatterParams: Any, onRendered: EmptyCallback) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var formatterParams: dynamic /* MoneyParams? | ImageParams? | LinkParams? | DateTimeParams? | DateTimeDifferenceParams? | TickCrossParams? | TrafficParams? | ProgressBarParams? | StarRatingParams? | RowSelectionParams? | JSONRecord? | ToggleSwitchParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var variableHeight: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var editable: dynamic /* Boolean? | ((cell: CellComponent) -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var editor: dynamic /* Boolean? | "input" | "textarea" | "number" | "range" | "tickCross" | "star" | "list" | "date" | "time" | "datetime" | ((cell: CellComponent, onRendered: EmptyCallback, success: ValueBooleanCallback, cancel: ValueVoidCallback, editorParams: Any) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var editorParams: dynamic /* NumberParams? | CheckboxParams? | ListEditorParams? | InputParams? | TextAreaParams? | DateParams? | TimeParams? | DateTimeEditorParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var validator: dynamic /* "required" | "unique" | "integer" | "float" | "numeric" | "string" | "alphanumeric" | Array<String /* "required" | "unique" | "integer" | "float" | "numeric" | "string" | "alphanumeric" */>? | Validator? | Array<Validator>? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var mutator: CustomMutator?
        get() = definedExternally
        set(value) = definedExternally
    var mutatorParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "edit" */, cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var mutatorData: CustomMutator?
        get() = definedExternally
        set(value) = definedExternally
    var mutatorDataParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "edit" */, cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var mutatorEdit: CustomMutator?
        get() = definedExternally
        set(value) = definedExternally
    var mutatorEditParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "edit" */, cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var mutatorClipboard: CustomMutator?
        get() = definedExternally
        set(value) = definedExternally
    var mutatorClipboardParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "edit" */, cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var accessor: dynamic /* CustomAccessor? | "rownum" */
        get() = definedExternally
        set(value) = definedExternally
    var accessorParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "download" | "clipboard" */, column: ColumnComponent, row: RowComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var accessorDownload: CustomAccessor?
        get() = definedExternally
        set(value) = definedExternally
    var accessorDownloadParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "download" | "clipboard" */, column: ColumnComponent, row: RowComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var accessorClipboard: CustomAccessor?
        get() = definedExternally
        set(value) = definedExternally
    var accessorClipboardParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "download" | "clipboard" */, column: ColumnComponent, row: RowComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var download: dynamic /* Boolean? | ((column: ColumnComponent) -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var titleDownload: String?
        get() = definedExternally
        set(value) = definedExternally
    var topCalc: dynamic /* "avg" | "max" | "min" | "sum" | "concat" | "count" | "unique" | ((values: Array<Any>, data: Array<Any>, calcParams: Any) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var topCalcParams: dynamic /* `T$3`? | ((values: Any, data: Any) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var topCalcFormatter: dynamic /* "plaintext" | "textarea" | "html" | "money" | "image" | "datetime" | "datetimediff" | "link" | "tickCross" | "color" | "star" | "traffic" | "progress" | "lookup" | "buttonTick" | "buttonCross" | "rownum" | "handle" | "rowSelection" | "responsiveCollapse" | "toggle" | ((cell: CellComponent, formatterParams: Any, onRendered: EmptyCallback) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var topCalcFormatterParams: dynamic /* MoneyParams? | ImageParams? | LinkParams? | DateTimeParams? | DateTimeDifferenceParams? | TickCrossParams? | TrafficParams? | ProgressBarParams? | StarRatingParams? | RowSelectionParams? | JSONRecord? | ToggleSwitchParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var bottomCalc: dynamic /* "avg" | "max" | "min" | "sum" | "concat" | "count" | "unique" | ((values: Array<Any>, data: Array<Any>, calcParams: Any) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var bottomCalcParams: dynamic /* `T$3`? | ((values: Any, data: Any) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var bottomCalcFormatter: dynamic /* "plaintext" | "textarea" | "html" | "money" | "image" | "datetime" | "datetimediff" | "link" | "tickCross" | "color" | "star" | "traffic" | "progress" | "lookup" | "buttonTick" | "buttonCross" | "rownum" | "handle" | "rowSelection" | "responsiveCollapse" | "toggle" | ((cell: CellComponent, formatterParams: Any, onRendered: EmptyCallback) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var bottomCalcFormatterParams: dynamic /* MoneyParams? | ImageParams? | LinkParams? | DateTimeParams? | DateTimeDifferenceParams? | TickCrossParams? | TrafficParams? | ProgressBarParams? | StarRatingParams? | RowSelectionParams? | JSONRecord? | ToggleSwitchParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var headerSort: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var headerSortStartingDir: String? /* "asc" | "desc" */
        get() = definedExternally
        set(value) = definedExternally
    var headerSortTristate: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var headerClick: ColumnEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerDblClick: ColumnEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerMouseDown: ColumnEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerMouseUp: ColumnEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerContext: ColumnEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerTap: ColumnEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerDblTap: ColumnEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerTapHold: ColumnEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerTooltip: dynamic /* Boolean? | String? | ((column: ColumnComponent) -> String)? */
        get() = definedExternally
        set(value) = definedExternally
    var headerVertical: dynamic /* Boolean? | "flip" */
        get() = definedExternally
        set(value) = definedExternally
    var editableTitle: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var titleFormatter: dynamic /* "plaintext" | "textarea" | "html" | "money" | "image" | "datetime" | "datetimediff" | "link" | "tickCross" | "color" | "star" | "traffic" | "progress" | "lookup" | "buttonTick" | "buttonCross" | "rownum" | "handle" | "rowSelection" | "responsiveCollapse" | "toggle" | ((cell: CellComponent, formatterParams: Any, onRendered: EmptyCallback) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var titleFormatterParams: dynamic /* MoneyParams? | ImageParams? | LinkParams? | DateTimeParams? | DateTimeDifferenceParams? | TickCrossParams? | TrafficParams? | ProgressBarParams? | StarRatingParams? | RowSelectionParams? | JSONRecord? | ToggleSwitchParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var headerFilter: dynamic /* Boolean? | "input" | "textarea" | "number" | "range" | "tickCross" | "star" | "list" | "date" | "time" | "datetime" | ((cell: CellComponent, onRendered: EmptyCallback, success: ValueBooleanCallback, cancel: ValueVoidCallback, editorParams: Any) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var headerFilterParams: dynamic /* NumberParams? | CheckboxParams? | ListEditorParams? | InputParams? | TextAreaParams? | DateParams? | TimeParams? | DateTimeEditorParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var headerFilterPlaceholder: String?
        get() = definedExternally
        set(value) = definedExternally
    var headerFilterEmptyCheck: ValueBooleanCallback?
        get() = definedExternally
        set(value) = definedExternally
    var headerFilterFunc: dynamic /* "=" | "!=" | "like" | "<" | ">" | "<=" | ">=" | "in" | "regex" | "starts" | "ends" | ((headerValue: Any, rowValue: Any, rowData: Any, filterParams: Any) -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var headerFilterFuncParams: Any?
        get() = definedExternally
        set(value) = definedExternally
    var headerFilterLiveFilter: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var htmlOutput: dynamic /* Boolean? | ((column: ColumnComponent) -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var clipboard: dynamic /* Boolean? | ((column: ColumnComponent) -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var print: dynamic /* Boolean? | ((column: ColumnComponent) -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var columns: Array<ColumnDefinition>?
        get() = definedExternally
        set(value) = definedExternally
    var headerMenu: dynamic /* Array<dynamic /* MenuObject<ColumnComponent> | MenuSeparator */>? | (() -> Array<dynamic /* MenuObject<ColumnComponent> | MenuSeparator */>)? */
        get() = definedExternally
        set(value) = definedExternally
    var headerMenuIcon: dynamic /* String? | HTMLElement? | ((component: ColumnComponent) -> dynamic)? */
        get() = definedExternally
        set(value) = definedExternally
    var headerContextMenu: Array<dynamic /* MenuObject<ColumnComponent> | MenuSeparator */>?
        get() = definedExternally
        set(value) = definedExternally
    var headerDblClickPopup: String?
        get() = definedExternally
        set(value) = definedExternally
    var dblClickPopup: String?
        get() = definedExternally
        set(value) = definedExternally
    var contextMenu: Array<dynamic /* MenuObject<CellComponent> | MenuSeparator */>?
        get() = definedExternally
        set(value) = definedExternally
    var clickMenu: Array<dynamic /* MenuObject<CellComponent> | MenuSeparator */>?
        get() = definedExternally
        set(value) = definedExternally
    var headerDblClickMenu: Array<dynamic /* MenuObject<CellComponent> | MenuSeparator */>?
        get() = definedExternally
        set(value) = definedExternally
    var dblClickMenu: Array<dynamic /* MenuObject<CellComponent> | MenuSeparator */>?
        get() = definedExternally
        set(value) = definedExternally
    var cellPopup: dynamic /* String? | ((e: MouseEvent, component: dynamic /* RowComponent | CellComponent | ColumnComponent */, onRendered: () -> Any) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var formatterClipboard: dynamic /* "plaintext" | "textarea" | "html" | "money" | "image" | "datetime" | "datetimediff" | "link" | "tickCross" | "color" | "star" | "traffic" | "progress" | "lookup" | "buttonTick" | "buttonCross" | "rownum" | "handle" | "rowSelection" | "responsiveCollapse" | "toggle" | ((cell: CellComponent, formatterParams: Any, onRendered: EmptyCallback) -> dynamic)? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var formatterClipboardParams: dynamic /* MoneyParams? | ImageParams? | LinkParams? | DateTimeParams? | DateTimeDifferenceParams? | TickCrossParams? | TrafficParams? | ProgressBarParams? | StarRatingParams? | RowSelectionParams? | JSONRecord? | ToggleSwitchParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var formatterPrint: dynamic /* "plaintext" | "textarea" | "html" | "money" | "image" | "datetime" | "datetimediff" | "link" | "tickCross" | "color" | "star" | "traffic" | "progress" | "lookup" | "buttonTick" | "buttonCross" | "rownum" | "handle" | "rowSelection" | "responsiveCollapse" | "toggle" | ((cell: CellComponent, formatterParams: Any, onRendered: EmptyCallback) -> dynamic)? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var formatterPrintParams: dynamic /* MoneyParams? | ImageParams? | LinkParams? | DateTimeParams? | DateTimeDifferenceParams? | TickCrossParams? | TrafficParams? | ProgressBarParams? | StarRatingParams? | RowSelectionParams? | JSONRecord? | ToggleSwitchParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var accessorPrint: CustomAccessor?
        get() = definedExternally
        set(value) = definedExternally
    var accessorPrintParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "download" | "clipboard" */, column: ColumnComponent, row: RowComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var accessorHtmlOutput: CustomAccessor?
        get() = definedExternally
        set(value) = definedExternally
    var accessorHtmlOutputParams: dynamic /* Any? | ((value: Any, data: Any, type: String /* "data" | "download" | "clipboard" */, column: ColumnComponent, row: RowComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var formatterHtmlOutput: dynamic /* "plaintext" | "textarea" | "html" | "money" | "image" | "datetime" | "datetimediff" | "link" | "tickCross" | "color" | "star" | "traffic" | "progress" | "lookup" | "buttonTick" | "buttonCross" | "rownum" | "handle" | "rowSelection" | "responsiveCollapse" | "toggle" | ((cell: CellComponent, formatterParams: Any, onRendered: EmptyCallback) -> dynamic)? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var formatterHtmlOutputParams: dynamic /* MoneyParams? | ImageParams? | LinkParams? | DateTimeParams? | DateTimeDifferenceParams? | TickCrossParams? | TrafficParams? | ProgressBarParams? | StarRatingParams? | RowSelectionParams? | JSONRecord? | ToggleSwitchParams? | ((cell: CellComponent) -> Any)? */
        get() = definedExternally
        set(value) = definedExternally
    var titleClipboard: String?
        get() = definedExternally
        set(value) = definedExternally
    var titleHtmlOutput: String?
        get() = definedExternally
        set(value) = definedExternally
    var titlePrint: String?
        get() = definedExternally
        set(value) = definedExternally
    var maxWidth: dynamic /* Number? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var headerWordWrap: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var editorEmptyValue: Any?
        get() = definedExternally
        set(value) = definedExternally
    var editorEmptyValueFunc: ((value: Any) -> Boolean)?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CellCallbacks {
    var cellClick: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellDblClick: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellContext: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellTap: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellDblTap: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellTapHold: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellMouseEnter: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellMouseLeave: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellMouseOver: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellMouseOut: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellMouseMove: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellEditing: CellEditEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellEdited: CellEditEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellEditCancelled: CellEditEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellMouseDown: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellMouseUp: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CellCallbacksPartial {
    var cellClick: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellDblClick: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellContext: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellTap: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellDblTap: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellTapHold: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellMouseEnter: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellMouseLeave: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellMouseOver: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellMouseOut: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellMouseMove: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellEditing: CellEditEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellEdited: CellEditEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellEditCancelled: CellEditEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellMouseDown: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
    var cellMouseUp: CellEventCallback?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ColumnDefinitionSorterParams {
    var format: String?
        get() = definedExternally
        set(value) = definedExternally
    var locale: dynamic /* String? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var alignEmptyValues: String? /* "top" | "bottom" */
        get() = definedExternally
        set(value) = definedExternally
    var type: String? /* "length" | "sum" | "max" | "min" | "avg" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface MoneyParams {
    var decimal: String?
        get() = definedExternally
        set(value) = definedExternally
    var thousand: String?
        get() = definedExternally
        set(value) = definedExternally
    var symbol: String?
        get() = definedExternally
        set(value) = definedExternally
    var symbolAfter: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var precision: dynamic /* Boolean? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var negativeSign: dynamic /* String? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface ImageParams {
    var height: String?
        get() = definedExternally
        set(value) = definedExternally
    var width: String?
        get() = definedExternally
        set(value) = definedExternally
    var urlPrefix: String?
        get() = definedExternally
        set(value) = definedExternally
    var urlSuffix: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface LinkParams {
    var labelField: String?
        get() = definedExternally
        set(value) = definedExternally
    var label: dynamic /* String? | ((cell: CellComponent) -> String)? */
        get() = definedExternally
        set(value) = definedExternally
    var urlPrefix: String?
        get() = definedExternally
        set(value) = definedExternally
    var urlField: String?
        get() = definedExternally
        set(value) = definedExternally
    var url: dynamic /* String? | ((cell: CellComponent) -> String)? */
        get() = definedExternally
        set(value) = definedExternally
    var target: String?
        get() = definedExternally
        set(value) = definedExternally
    var download: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface DateTimeParams {
    var inputFormat: String?
        get() = definedExternally
        set(value) = definedExternally
    var outputFormat: String?
        get() = definedExternally
        set(value) = definedExternally
    var invalidPlaceholder: dynamic /* Boolean? | String? | Number? | ValueStringCallback? */
        get() = definedExternally
        set(value) = definedExternally
    var timezone: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface DateTimeDifferenceParams : DateTimeParams {
    var date: Any?
        get() = definedExternally
        set(value) = definedExternally
    var humanize: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var unit: String? /* "years" | "months" | "weeks" | "days" | "hours" | "minutes" | "seconds" */
        get() = definedExternally
        set(value) = definedExternally
    var suffix: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TickCrossParams {
    var allowEmpty: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var allowTruthy: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var tickElement: dynamic /* Boolean? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var crossElement: dynamic /* Boolean? | String? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface TrafficParams {
    var min: Number?
        get() = definedExternally
        set(value) = definedExternally
    var max: Number?
        get() = definedExternally
        set(value) = definedExternally
    var color: dynamic /* String? | Array<Any>? | ValueStringCallback? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface ProgressBarParams : TrafficParams {
    var legend: dynamic /* String? | Boolean? | ValueStringCallback? */
        get() = definedExternally
        set(value) = definedExternally
    var legendColor: dynamic /* String? | Array<Any>? | ValueStringCallback? */
        get() = definedExternally
        set(value) = definedExternally
    var legendAlign: String? /* "center" | "left" | "right" | "justify" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface StarRatingParams {
    var stars: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface RowSelectionParams {
    var rowRange: String? /* "visible" | "active" | "selected" | "all" | "range" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface ToggleSwitchParams {
    var size: Number?
        get() = definedExternally
        set(value) = definedExternally
    var max: Number?
        get() = definedExternally
        set(value) = definedExternally
    var onValue: dynamic /* String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var offValue: dynamic /* String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var onTruthy: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var onColor: String?
        get() = definedExternally
        set(value) = definedExternally
    var offColor: String?
        get() = definedExternally
        set(value) = definedExternally
    var clickable: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SharedEditorParams {
    var elementAttributes: JSONRecord?
        get() = definedExternally
        set(value) = definedExternally
    var mask: String?
        get() = definedExternally
        set(value) = definedExternally
    var maskAutoFill: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var maskLetterChar: String?
        get() = definedExternally
        set(value) = definedExternally
    var maskNumberChar: String?
        get() = definedExternally
        set(value) = definedExternally
    var maskWildcardChar: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface NumberParams : SharedEditorParams {
    var min: Number?
        get() = definedExternally
        set(value) = definedExternally
    var max: Number?
        get() = definedExternally
        set(value) = definedExternally
    var step: Number?
        get() = definedExternally
        set(value) = definedExternally
    var verticalNavigation: String? /* "editor" | "table" */
        get() = definedExternally
        set(value) = definedExternally
    var selectContents: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface InputParams : SharedEditorParams {
    var search: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var selectContents: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TextAreaParams : SharedEditorParams {
    var verticalNavigation: String? /* "editor" | "table" | "hybrid" */
        get() = definedExternally
        set(value) = definedExternally
    var shiftEnterSubmit: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var selectContents: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CheckboxParams : SharedEditorParams {
    var tristate: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var indeterminateValue: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SharedSelectAutoCompleteEditorParams {
    var defaultValue: String?
        get() = definedExternally
        set(value) = definedExternally
    var sortValuesList: String? /* "asc" | "desc" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface DateParams : SharedEditorParams {
    var min: String?
        get() = definedExternally
        set(value) = definedExternally
    var max: String?
        get() = definedExternally
        set(value) = definedExternally
    var format: String?
        get() = definedExternally
        set(value) = definedExternally
    var verticalNavigation: String? /* "editor" | "table" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface TimeParams : SharedEditorParams {
    var format: String?
        get() = definedExternally
        set(value) = definedExternally
    var verticalNavigation: String? /* "editor" | "table" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface DateTimeEditorParams : SharedEditorParams {
    var format: String?
        get() = definedExternally
        set(value) = definedExternally
    var verticalNavigation: String? /* "editor" | "table" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface LabelValue {
    var label: String
    var value: dynamic /* String | Number | Boolean */
        get() = definedExternally
        set(value) = definedExternally
}

external interface ListEditorParams : SharedEditorParams, SharedSelectAutoCompleteEditorParams {
    var values: dynamic /* Boolean? | Array<String>? | JSONRecord? | String? | Array<Any>? | Array<LabelValue>? */
        get() = definedExternally
        set(value) = definedExternally
    var valuesURL: String?
        get() = definedExternally
        set(value) = definedExternally
    var valuesLookup: String? /* "visible" | "active" | "selected" | "all" | "range" */
        get() = definedExternally
        set(value) = definedExternally
    var valuesLookupField: String?
        get() = definedExternally
        set(value) = definedExternally
    var clearable: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var itemFormatter: ((label: String, value: String, item: Any, element: HTMLElement) -> String)?
        get() = definedExternally
        set(value) = definedExternally
    var sort: String? /* "asc" | "desc" */
        get() = definedExternally
        set(value) = definedExternally
    var emptyValue: Any?
        get() = definedExternally
        set(value) = definedExternally
    var maxWidth: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var placeholderLoading: String?
        get() = definedExternally
        set(value) = definedExternally
    var placeholderEmpty: String?
        get() = definedExternally
        set(value) = definedExternally
    var multiselect: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var autocomplete: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var filterFunc: ((term: String, label: String, value: Array<String>, item: Any) -> Any)?
        get() = definedExternally
        set(value) = definedExternally
    var filterRemote: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var filterDelay: Number?
        get() = definedExternally
        set(value) = definedExternally
    var allowEmpty: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var listOnEmpty: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var freetext: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showListOnEmpty: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var verticalNavigation: String? /* "editor" | "table" | "hybrid" */
        get() = definedExternally
        set(value) = definedExternally
}


external interface Validator {
    var type: dynamic /* "required" | "unique" | "integer" | "float" | "numeric" | "string" | "alphanumeric" | (cell: CellComponent, value: Any, parameters: Any) -> Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var parameters: Any?
        get() = definedExternally
        set(value) = definedExternally
}

external interface KeyBinding {
    var navPrev: dynamic /* String? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var navNext: dynamic /* String? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var navLeft: dynamic /* String? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var navRight: dynamic /* String? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var navUp: dynamic /* String? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var navDown: dynamic /* String? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var undo: dynamic /* String? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var redo: dynamic /* String? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var scrollPageUp: dynamic /* String? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var scrollPageDown: dynamic /* String? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var scrollToStart: dynamic /* String? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var scrollToEnd: dynamic /* String? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var copyToClipboard: dynamic /* String? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface CalculationComponent {
    var getData: () -> Json
    var getElement: () -> HTMLElement
    var getTable: () -> Tabulator
    var getCells: () -> Array<CellComponent>
    var getCell: (column: dynamic /* ColumnComponent | HTMLElement | String */) -> CellComponent
}

external interface RowComponent : CalculationComponent {
    var getNextRow: () -> dynamic
    var getPrevRow: () -> dynamic
    var getIndex: () -> Any
    var getPosition: (filteredPosition: Boolean) -> dynamic
    var getGroup: () -> GroupComponent
    var delete: () -> Promise<Unit>
    var scrollTo: (position: String /* "top" | "center" | "bottom" | "nearest" */, scrollIfVisible: Boolean) -> Promise<Unit>
    var pageTo: () -> Promise<Unit>
    var move: (lookup: dynamic /* RowComponent | HTMLElement | Number */, belowTarget: Boolean) -> Unit
    var update: (data: Any) -> Promise<Unit>
    var select: () -> Unit
    var deselect: () -> Unit
    var toggleSelect: () -> Unit
    var isSelected: () -> Boolean
    var normalizeHeight: () -> Unit
    var reformat: () -> Unit
    var freeze: () -> Unit
    var unfreeze: () -> Unit
    var treeExpand: () -> Unit
    var treeCollapse: () -> Unit
    var treeToggle: () -> Unit
    var getTreeParent: () -> dynamic
    var getTreeChildren: () -> Array<RowComponent>
    var addTreeChild: (rowData: Any, position: Boolean, existingRow: RowComponent) -> Unit
    var isTreeExpanded: () -> Boolean
    var validate: () -> dynamic
    var isFrozen: () -> Boolean
}

external interface GroupComponent {
    var getElement: () -> HTMLElement
    var getTable: () -> Tabulator
    var getKey: () -> Any
    var getField: () -> String
    var getRows: () -> Array<RowComponent>
    var getSubGroups: () -> Array<GroupComponent>
    var getParentGroup: () -> dynamic
    var isVisible: () -> Boolean
    var show: () -> Unit
    var hide: () -> Unit
    var toggle: () -> Unit
    var popup: (contents: String, position: String /* "left" | "center" | "right" | "top" | "bottom" */) -> Unit
    var scrollTo: (position: String /* "top" | "center" | "bottom" | "nearest" */, scrollIfVisible: Boolean) -> Promise<Unit>
}

external interface ColumnComponent {
    var getElement: () -> HTMLElement
    var getTable: () -> Tabulator
    var getDefinition: () -> ColumnDefinition
    var getField: () -> String
    var getCells: () -> Array<CellComponent>
    var getNextColumn: () -> dynamic
    var getPrevColumn: () -> dynamic
    var move: (toColumn: dynamic /* ColumnComponent | ColumnDefinition | HTMLElement | String */, after: Boolean) -> Unit
    var isVisible: () -> Boolean
    var show: () -> Unit
    var hide: () -> Unit
    var toggle: () -> Unit
    var delete: () -> Promise<Unit>
    var scrollTo: (position: String /* "left" | "middle" | "right" */, scrollIfVisible: Boolean) -> Promise<Unit>
    var getSubColumns: () -> Array<ColumnComponent>
    var getParentColumn: () -> dynamic
    var headerFilterFocus: () -> Unit
    var setHeaderFilterValue: (value: Any) -> Unit
    var reloadHeaderFilter: () -> Unit
    var getHeaderFilterValue: () -> Any
    var updateDefinition: (definition: ColumnDefinition) -> Promise<ColumnComponent>
    var getWidth: () -> Number
    var setWidth: (width: dynamic /* Number | Boolean */) -> Unit
    var validate: () -> dynamic
    var popup: (contents: String, position: String /* "left" | "center" | "right" | "top" | "bottom" */) -> Unit
}

external interface CellComponent {
    var getValue: () -> Any
    var getOldValue: () -> Any
    var restoreOldValue: () -> Any
    var getInitialValue: () -> Any
    var restoreInitialValue: () -> Any
    var getElement: () -> HTMLElement
    var getTable: () -> Tabulator
    var getRow: () -> RowComponent
    var getColumn: () -> ColumnComponent
    var getData: (transformType: String /* "data" | "download" | "clipboard" */) -> Json
    var getField: () -> String
    var getType: () -> String
    var setValue: (value: Any, mutate: Boolean) -> Unit
    var checkHeight: () -> Unit
    var edit: (ignoreEditable: Boolean) -> Unit
    var cancelEdit: () -> Unit
    var navigatePrev: () -> Boolean
    var navigateNext: () -> Boolean
    var navigateLeft: () -> Boolean
    var navigateRight: () -> Boolean
    var navigateUp: () -> Unit
    var navigateDown: () -> Unit
    var isEdited: () -> Boolean
    var clearEdited: () -> Unit
    var isValid: () -> dynamic
    var clearValidation: () -> Unit
    var validate: () -> dynamic
    var popup: (contents: String, position: String /* "left" | "center" | "right" | "top" | "bottom" */) -> Unit
    fun getRanges(): Array<RangeComponent>
}

external interface `T$2` {
    var start: CellComponent
    var end: CellComponent
}

external interface RangeComponent {
    var setBounds: (topLeft: CellComponent, bottomRight: CellComponent) -> Unit
    var setStartBound: (cell: CellComponent) -> Unit
    var setEndBound: (cell: CellComponent) -> Unit
    fun remove()
    fun getElement(): Any
    fun getData(): Any
    fun clearValues()
    fun getCells(): Array<CellComponent>
    fun getStructuredCells(): Array<Array<CellComponent>>
    fun getRows(): Array<RowComponent>
    fun getColumns(): Array<ColumnComponent>
    fun getBounds(): `T$2`
    fun getTopEdge(): Number
    fun getBottomEdge(): Number
    fun getLeftEdge(): Number
    fun getRightEdge(): Number
}

external interface EventCallBackMethods {
    var validationFailed: (cell: CellComponent, value: Any, validators: Array<Validator>) -> Unit
    var scrollHorizontal: (left: Number, leftDir: Boolean) -> Unit
    var scrollVertical: (top: Number, topDir: Boolean) -> Unit
    var rowAdded: (row: RowComponent) -> Unit
    var rowDeleted: (row: RowComponent) -> Unit
    var rowMoving: (row: RowComponent) -> Unit
    var rowMoved: (row: RowComponent) -> Unit
    var rowMoveCancelled: (row: RowComponent) -> Unit
    var rowUpdated: (row: RowComponent) -> Unit
    var rowSelectionChanged: (data: Array<Any>, rows: Array<RowComponent>, selectedRows: Array<RowComponent>, deselectedRows: Array<RowComponent>) -> Unit
    var rowSelected: (row: RowComponent) -> Unit
    var rowDeselected: (row: RowComponent) -> Unit
    var rowResized: (row: RowComponent) -> Unit
    var rowClick: (event: UIEvent, row: RowComponent) -> Unit
    var rowDblClick: (event: UIEvent, row: RowComponent) -> Unit
    var rowContext: (event: UIEvent, row: RowComponent) -> Unit
    var rowTap: (event: UIEvent, row: RowComponent) -> Unit
    var rowDblTap: (event: UIEvent, row: RowComponent) -> Unit
    var rowTapHold: (event: UIEvent, row: RowComponent) -> Unit
    var rowMouseEnter: (event: UIEvent, row: RowComponent) -> Unit
    var rowMouseLeave: (event: UIEvent, row: RowComponent) -> Unit
    var rowMouseOver: (event: UIEvent, row: RowComponent) -> Unit
    var rowMouseDown: (event: UIEvent, row: RowComponent) -> Unit
    var rowMouseUp: (event: UIEvent, row: RowComponent) -> Unit
    var rowMouseOut: (event: UIEvent, row: RowComponent) -> Unit
    var rowMouseMove: (event: UIEvent, row: RowComponent) -> Unit
    var htmlImporting: () -> Unit
    var htmlImported: () -> Unit
    var ajaxError: () -> Unit
    var clipboardCopied: (clipboard: String) -> Unit
    var clipboardPasted: (clipboard: String, rowData: Array<Any>, rows: Array<RowComponent>) -> Unit
    var clipboardPasteError: (clipboard: String) -> Unit
    var downloadComplete: () -> Unit
    var dataTreeRowExpanded: (row: RowComponent, level: Number) -> Unit
    var dataTreeRowCollapsed: (row: RowComponent, level: Number) -> Unit
    var pageLoaded: (pageNo: Number) -> Unit
    var pageSizeChanged: (pageSize: Number) -> Unit
    var headerClick: (event: UIEvent, column: ColumnComponent) -> Unit
    var headerDblClick: (event: UIEvent, column: ColumnComponent) -> Unit
    var headerContext: (event: UIEvent, column: ColumnComponent) -> Unit
    var headerTap: (event: UIEvent, column: ColumnComponent) -> Unit
    var headerDblTap: (event: UIEvent, column: ColumnComponent) -> Unit
    var headerTapHold: (event: UIEvent, column: ColumnComponent) -> Unit
    var headerMouseUp: (event: UIEvent, column: ColumnComponent) -> Unit
    var headerMouseDown: (event: UIEvent, column: ColumnComponent) -> Unit
    var groupClick: (event: UIEvent, group: GroupComponent) -> Unit
    var groupDblClick: (event: UIEvent, group: GroupComponent) -> Unit
    var groupContext: (event: UIEvent, group: GroupComponent) -> Unit
    var groupTap: (event: UIEvent, group: GroupComponent) -> Unit
    var groupDblTap: (event: UIEvent, group: GroupComponent) -> Unit
    var groupTapHold: (event: UIEvent, group: GroupComponent) -> Unit
    var groupMouseDown: (event: UIEvent, group: GroupComponent) -> Unit
    var groupMouseUp: (event: UIEvent, group: GroupComponent) -> Unit
    var tableBuilding: () -> Unit
    var tableBuilt: () -> Unit
    var tableDestroyed: () -> Unit
    var dataChanged: (data: Array<Any>) -> Unit
    var dataLoading: (data: Array<Any>) -> Unit
    var dataLoaded: (data: Array<Any>) -> Unit
    var dataLoadError: (error: Error) -> Unit
    var dataProcessing: (data: Array<Any>) -> Unit
    var dataProcessed: (data: Array<Any>) -> Unit
    var dataFiltering: (filters: Array<Filter>) -> Unit
    var dataFiltered: (filters: Array<Filter>, rows: Array<RowComponent>) -> Unit
    var dataSorting: (sorters: Array<SorterFromTable>) -> Unit
    var dataSorted: (sorters: Array<SorterFromTable>, rows: Array<RowComponent>) -> Unit
    var movableRowsSendingStart: (toTables: Array<Tabulator>) -> Unit
    var movableRowsSent: (fromRow: RowComponent, toRow: RowComponent, toTable: Tabulator) -> Unit
    var movableRowsSentFailed: (fromRow: RowComponent, toRow: RowComponent, toTable: Tabulator) -> Unit
    var movableRowsSendingStop: (toTables: Array<Tabulator>) -> Unit
    var movableRowsReceivingStart: (fromRow: RowComponent, fromTable: Tabulator) -> Unit
    var movableRowsReceived: (fromRow: RowComponent, toRow: RowComponent, fromTable: Tabulator) -> Unit
    var movableRowsReceivedFailed: (fromRow: RowComponent, toRow: RowComponent, fromTable: Tabulator) -> Unit
    var movableRowsReceivingStop: (fromTable: Tabulator) -> Unit
    var movableRowsElementDrop: (event: UIEvent, element: Element, row: RowComponent) -> Unit
    var dataGrouping: () -> Unit
    var dataGrouped: (groups: Array<GroupComponent>) -> Unit
    var groupVisibilityChanged: (group: GroupComponent, visible: Boolean) -> Unit
    var localized: (locale: String, lang: Any) -> Unit
    var renderStarted: () -> Unit
    var renderComplete: () -> Unit
    var columnMoved: (column: ColumnComponent, columns: Array<ColumnComponent>) -> Unit
    var columnResized: (column: ColumnComponent) -> Unit
    var columnTitleChanged: (column: ColumnComponent) -> Unit
    var columnVisibilityChanged: (column: ColumnComponent, visible: Boolean) -> Unit
    var historyUndo: (action: String /* "cellEdit" | "rowAdd" | "rowDelete" | "rowMoved" */, component: Any, data: Array<Any>) -> Unit
    var historyRedo: (action: String /* "cellEdit" | "rowAdd" | "rowDelete" | "rowMoved" */, component: Any, data: Array<Any>) -> Unit
    var cellEditing: (cell: CellComponent) -> Unit
    var cellEdited: (cell: CellComponent) -> Unit
    var cellEditCancelled: (cell: CellComponent) -> Unit
    var cellClick: (event: UIEvent, cell: CellComponent) -> Unit
    var cellDblClick: (event: UIEvent, cell: CellComponent) -> Unit
    var cellContext: (event: UIEvent, cell: CellComponent) -> Unit
    var cellMouseDown: (event: UIEvent, cell: CellComponent) -> Unit
    var cellMouseUp: (event: UIEvent, cell: CellComponent) -> Unit
    var cellTap: (event: UIEvent, cell: CellComponent) -> Unit
    var cellDblTap: (event: UIEvent, cell: CellComponent) -> Unit
    var cellTapHold: (event: UIEvent, cell: CellComponent) -> Unit
    var cellMouseEnter: (event: UIEvent, cell: CellComponent) -> Unit
    var cellMouseLeave: (event: UIEvent, cell: CellComponent) -> Unit
    var cellMouseOver: (event: UIEvent, cell: CellComponent) -> Unit
    var cellMouseOut: (event: UIEvent, cell: CellComponent) -> Unit
    var cellMouseMove: (event: UIEvent, cell: CellComponent) -> Unit
    var popupOpen: (cell: CellComponent) -> Unit
    var popupClosed: (cell: CellComponent) -> Unit
    var menuClosed: (cell: CellComponent) -> Unit
    var menuOpened: (cell: CellComponent) -> Unit
    var TooltipClosed: (cell: CellComponent) -> Unit
    var TooltipOpened: (cell: CellComponent) -> Unit
    var rangeAdded: (range: RangeComponent) -> Unit
    var rangeChanged: (range: RangeComponent) -> Unit
    var rangeRemoved: (range: RangeComponent) -> Unit
    var rowHeight: (row: RowComponent) -> Unit
    var rowResizing: (row: RowComponent) -> Unit
    var columnWidth: (column: ColumnComponent) -> Unit
    var columnResizing: (column: ColumnComponent) -> Unit
    var sheetAdded: (sheet: SpreadsheetComponent) -> Unit
    var sheetLoaded: (sheet: SpreadsheetComponent) -> Unit
    var sheetUpdated: (sheet: SpreadsheetComponent) -> Unit
    var sheetRemoved: (sheet: SpreadsheetComponent) -> Unit
    var columnsLoaded: (columns: Array<ColumnComponent>) -> Unit
    var importChoose: () -> Unit
    var importImporting: (files: Array<File>) -> Unit
    var importError: (err: Any) -> Unit
    var importImported: (data: Any) -> Unit
}